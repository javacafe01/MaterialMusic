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
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPermissionChecked = (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED);

        if (isPermissionChecked) {
            setReference();
        }
        checkPermissions();
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

        SlidingUpPanelLayout lay = (SlidingUpPanelLayout) findViewById(R.id.slide);

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
        albumFrag = new AlbumFragment();
        artistFrag = new ArtistFragment();
        changeFragment(songFrag);

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
        Log.d("song", "id " +position);
        mediaWrapper.playSong(position);
    }
}
