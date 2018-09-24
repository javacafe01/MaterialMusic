package com.teamsoftware.materialmusic;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.IOException;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import rm.com.audiowave.AudioWaveView;

public class PlayerFragment extends Fragment {
    private int position;
    private MediaWrapper medWrap;
    private ImageSwitcher cover;
    private TextView song, artistAlbum;
    private AudioWaveView wave;
    private ImageView prev, play, next;
    private boolean isPlaying;

    public PlayerFragment(MediaWrapper medWrap, int position) {
        this.medWrap = medWrap;
        this.position = position;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.activity_player, container, false);

        initView();

        return rootview;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        // write logic here b'z it is called when fragment is visible to user
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
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

        Animation in = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_in);
        Animation out = AnimationUtils.loadAnimation(getActivity(), R.anim.left_to_right_out);

        cover = (ImageSwitcher) getActivity().findViewById(R.id.imageswitcher);
        cover.setInAnimation(in);
        cover.setOutAnimation(out);

        song = (TextView) getActivity().findViewById(R.id.song);
        artistAlbum = (TextView) getActivity().findViewById(R.id.artist_album);
        wave = (AudioWaveView) getActivity().findViewById(R.id.wave);
        prev = (ImageView) getActivity().findViewById(R.id.prev);
        play = (ImageView) getActivity().findViewById(R.id.play);
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.startAnimation(AnimationUtils.loadAnimation(getActivity(), R.anim.button_anim));
                pauseResumeSong();
            }
        });
        next = (ImageView) getActivity().findViewById(R.id.next);

        if (isPlaying) {
            setSongData();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }

    private void pauseResumeSong() {
        if (isPlaying) {
            isPlaying = false;
            medWrap.pause();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_play));
        } else {
            isPlaying = true;
            medWrap.resume();
            play.setImageDrawable(getResources().getDrawable(R.drawable.ic_pause));
        }
    }
}