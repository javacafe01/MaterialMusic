/*
 * MIT License
 *
 * Copyright (c) 2018 Gokul Swaminathan and Neal Chokshi
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.teamsoftware.materialmusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.indatus.smoothseekbar.library.SmoothSeekBar;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import com.mpatric.mp3agic.Mp3File;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SongRecyclerAdapter.ClickInterface {

    private BottomNavigationView navbar;
    private AppBarLayout appBar;
    private FrameLayout container;
    private MetadataCacher cache;
    private View viewLayout;
    private boolean isPermissionChecked;
    private SongManager songManager;
    private ProgressDialog progressDialog;
    private MediaWrapper mediaWrapper;
    private ArrayList<File> allSongs;
    private Fragment songFrag, albumFrag, artistFrag, currentFrag;
    private Intent playIntent;
    private SlidingUpPanelLayout lay;
    private Handler seekHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPermissionChecked = (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED);

        if (isPermissionChecked) {
            setReference();
        }

        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    seekBar.setProgress(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                mediaWrapper.getMediaPlayer().pause();
                seekbar.pauseAnimating();
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                mediaWrapper.getMediaPlayer().seekTo(getSeekToProgress());
                mediaWrapper.getMediaPlayer().start();
                seekbar.beginAnimating();
            }
        });
    }

    private int getSeekToProgress() {
        return (int) ((double) seekbar.getProgress() * (1 / (double) seekbar.getMax()) * (double) mediaWrapper.getMediaPlayer().getDuration());
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setReference();
        }
    }

    private void checkPermissions() {
        if ((PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED)) {
            //good to go
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
    }

    private void setReference() {
        viewLayout = LayoutInflater.from(this).inflate(R.layout.activity_main, container);

        lay = (SlidingUpPanelLayout) findViewById(R.id.slide);

        //navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        container = (FrameLayout) findViewById(R.id.container);
        appBar = (AppBarLayout) findViewById(R.id.my_app_bar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBar, "elevation", 5));
        appBar.setStateListAnimator(stateListAnimator);

        preloadMusic();
        songFrag = new MusicFragment(cache, this);
        mediaWrapper = new MediaWrapper(cache, cache.getSongList());
        changeFragment(songFrag);

        initPlayerView();
    }

    private void changeFragment(Fragment frag) {
        currentFrag = frag;
        getSupportFragmentManager().beginTransaction().replace(container.getId(), currentFrag).commit();
    }

    private void preloadMusic() {

        songManager = new SongManager();

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading songs...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        if (cache == null) {
            while (!songManager.getFetchStatus()) {
                progressDialog.show();
                allSongs = songManager.findSongList(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
            }
            if (allSongs != null) {
                cache = new MetadataCacher(allSongs);
                progressDialog.dismiss();
            }
        }


    }

    @Override
    public void onSongClick(int position) {
        Log.d("song", "id " + position);
        this.position = position;
        mediaWrapper.playSong(position);
        setSongData();
        play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        lay.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
        seekbar.endAnimation();
        seekbar.beginAnimating();
    }


    /*-----------Player----------*/


    private int position;
    private ImageSwitcher cover;
    private TextView song, artistAlbum;
    private ImageView prev, play, next, looper, shuffle;
    private Animation in, out;
    private SmoothSeekBar seekbar;

    private void setSongData() {
        String songSt = "", artistAlbumSt = "";
        byte[] bytes = Utils.readBytesFromFile(cache.getSongList().get(position));

        seekbar.setEndTime(mediaWrapper.getMediaPlayer().getDuration());
        Mp3File file = cache.getSongCache().get(position);

        songSt = cache.getMetadataAll(file).get("Title");
        artistAlbumSt = cache.getMetadataAll(file).get("Artist") + " | " + cache.getMetadataAll(cache.getSongCache().get(position)).get("Album");

        Bitmap img = cache.getAlbumArt(file);
        cover.setImageDrawable(new BitmapDrawable(img));

        song.setText(songSt);
        artistAlbum.setText(artistAlbumSt);
    }

    private void initPlayerView() {
        seekbar = (SmoothSeekBar) findViewById(R.id.seekbar);

        Animation a1 = AnimationUtils.loadAnimation(this, R.anim.in_prev_anim);
        Animation a2 = AnimationUtils.loadAnimation(this, R.anim.out_prev_anim);

        Animation a3 = AnimationUtils.loadAnimation(this, R.anim.in_next_anim);
        Animation a4 = AnimationUtils.loadAnimation(this, R.anim.out_next_anim);

        shuffle = (ImageView) findViewById(R.id.shuffle);
        shuffle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
            }
        });

        prev = (ImageView) findViewById(R.id.prev);
        prev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
                in = a1;
                out = a2;
                resetAnim();
                mediaWrapper.prevSong();
                position--;
                setSongData();
                seekbar.endAnimation();
                seekbar.beginAnimating();
            }
        });

        play = (ImageView) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
                if (mediaWrapper.isPlaying()) {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
                    seekbar.pauseAnimating();
                } else {
                    play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
                    seekbar.beginAnimating();
                }
                Log.d("Play Button", "Button Pressed");
                pauseResumeSong();
            }
        });

        next = (ImageView) findViewById(R.id.next);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
                in = a3;
                out = a4;
                resetAnim();
                mediaWrapper.nextSong();
                position++;
                setSongData();
                seekbar.endAnimation();
                seekbar.beginAnimating();
            }
        });

        looper = (ImageView) findViewById(R.id.loop);
        looper.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
                if (mediaWrapper.isLooping()) {
                    mediaWrapper.setLoopingList(false);
                } else {
                    mediaWrapper.setLoopingList(true);
                }
            }
        });

        cover = (ImageSwitcher) findViewById(R.id.imageswitcher);
        cover.setFactory(new ViewSwitcher.ViewFactory() {
            public View makeView() {
                ImageView myView = new ImageView(getApplicationContext());
                myView.setScaleType(ImageView.ScaleType.FIT_CENTER);
                return myView;
            }
        });

        cover.setImageDrawable(getResources().getDrawable(R.drawable.preload));

        song = (TextView) findViewById(R.id.song);
        artistAlbum = (TextView) findViewById(R.id.artist_album);

        if (mediaWrapper.isPlaying()) {
            setSongData();
        }
    }

    private void resetAnim() {
        cover.setInAnimation(in);
        cover.setOutAnimation(out);
    }

    private void pauseResumeSong() {
        mediaWrapper.toggleCurrentSong();
    }

    @Override
    public void onBackPressed() {
        if (lay.getPanelState() == SlidingUpPanelLayout.PanelState.EXPANDED) {
            lay.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
        }
    }
}