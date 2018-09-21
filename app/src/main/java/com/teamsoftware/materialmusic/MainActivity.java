package com.teamsoftware.materialmusic;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    BottomNavigationView navbar;
    AppBarLayout appBar;
    FrameLayout container;

    private View viewLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setReference();
        navigate();
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

        if (position == 0) {
            newFragment = new MusicFragment();
        } else if (position == 1) {
            newFragment = new AlbumFragment();
        } else if (position == 2) {
            newFragment = new ArtistFragment();
        } else {
            newFragment = new PlaylistFragment();
        }

        getSupportFragmentManager().beginTransaction().replace(container.getId(), newFragment).commit();
    }
}
