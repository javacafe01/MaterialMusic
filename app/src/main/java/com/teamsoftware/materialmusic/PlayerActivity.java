package com.teamsoftware.materialmusic;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

import androidx.appcompat.app.AppCompatActivity;
import rm.com.audiowave.AudioWaveView;

public class PlayerActivity extends AppCompatActivity {
    private Intent thisIntent;
    private int position;
    private MediaWrapper medWrap;
    private ImageSwitcher cover;
    private TextView song, artistAlbum;
    private AudioWaveView wave;
    private ImageView prev, play, next;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        getData();
        initView();
    }

    private void getData() {
        thisIntent = getIntent();
        medWrap = (MediaWrapper) thisIntent.getSerializableExtra("mediawrapper");
        position = (int) thisIntent.getIntExtra("position", 0);
    }

    private void setSongData() {
        String songSt = "", artistAlbumSt = "";
        try {
            songSt = medWrap.getCurrSongData().get("Title");
            artistAlbumSt = medWrap.getCurrSongData().get("Artist") + " | " + medWrap.getCurrSongData().get("Album");
            cover.setImageDrawable(new BitmapDrawable(medWrap.getArt()));
        } catch (UnsupportedTagException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidDataException e) {
            e.printStackTrace();
        }

        song.setText(songSt);
        artistAlbum.setText(artistAlbumSt);
    }

    private void initView() {
        isPlaying = medWrap.isPlaying();

        Animation in = AnimationUtils.loadAnimation(this, R.anim.left_to_right_in);
        Animation out = AnimationUtils.loadAnimation(this, R.anim.left_to_right_out);

        cover = (ImageSwitcher) findViewById(R.id.imageswitcher);
        cover.setInAnimation(in);
        cover.setOutAnimation(out);

        song = (TextView) findViewById(R.id.song);
        artistAlbum = (TextView) findViewById(R.id.artist_album);
        wave = (AudioWaveView) findViewById(R.id.wave);
        prev = (ImageView) findViewById(R.id.prev);
        play = (ImageView) findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim));
                pauseResumeSong();
            }
        });
        next = (ImageView) findViewById(R.id.next);

        if (isPlaying) {
            setSongData();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

    private void pauseResumeSong()
    {
        if(isPlaying){
            isPlaying = false;
            medWrap.pause();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        }
        else
        {
            isPlaying = true;
            medWrap.resume();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

}
