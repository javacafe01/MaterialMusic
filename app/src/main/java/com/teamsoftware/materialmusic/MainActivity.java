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
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navbar;
    AppBarLayout appBar;
    FrameLayout container;
    MetadataCacher cache;
    private View viewLayout;
    private boolean isPermissionChecked;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isPermissionChecked = (PermissionChecker.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PermissionChecker.PERMISSION_GRANTED);

        if (isPermissionChecked) {
            setReference();
            navigate();
        }
        checkPermissions();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            setReference();
            navigate();
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

    private void navigate() {
        navbar.setOnNavigationItemSelectedListener(
                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.action_song:
                                changeFragment(0);
                                break;
                            case R.id.action_album:
                                changeFragment(1);
                                break;
                            case R.id.action_artist:
                                changeFragment(2);
                                break;
                            case R.id.action_playlist:
                                changeFragment(3);
                                break;
                        }
                        return true;
                    }
                });
    }

    public void setReference() {
        viewLayout = LayoutInflater.from(this).inflate(R.layout.activity_main, container);
        navbar = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        container = (FrameLayout) findViewById(R.id.container);
        appBar = (AppBarLayout) findViewById(R.id.my_app_bar);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        StateListAnimator stateListAnimator = new StateListAnimator();
        stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBar, "elevation", 5));
        appBar.setStateListAnimator(stateListAnimator);

        changeFragment(0);
    }

    private void changeFragment(int position) {

        Fragment newFragment = null;
        String tag = null;

        if (position == 0) {
            preloadMusic();
            newFragment = new MusicFragment(cache);
            tag = "songs";
        } else if (position == 1) {
            newFragment = new AlbumFragment();
            tag = "albums";
        } else if (position == 2) {
            newFragment = new ArtistFragment();
            tag = "artist";
        } else {
            newFragment = new PlaylistFragment();
            tag = "playlist";
        }

        getSupportFragmentManager().beginTransaction().replace(container.getId(), newFragment, tag).addToBackStack(tag).commit();
    }

    public void preloadMusic() {
        if (cache == null) {
            ArrayList<File> allSongs = new SongManager().findSongList(new File(Environment.getExternalStorageDirectory().getAbsolutePath()));
            cache = new MetadataCacher(allSongs);
        }
    }
}
