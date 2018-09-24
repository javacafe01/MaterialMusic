package com.teamsoftware.materialmusic;

import android.graphics.Bitmap;
import android.media.MediaPlayer;

import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MediaWrapper implements Serializable {
    MetadataCacher cache;
    private ArrayList<File> songList;
    private File currSong;
    private Mp3File currMp3;
    private boolean looping;
    private int currPos, maxSongs;
    private MediaPlayer mediaPlayer;

    public MediaWrapper(MetadataCacher cache, ArrayList<File> songList) {
        this.cache = cache;
        this.songList = songList;
        maxSongs = this.songList.size() - 1;
        mediaPlayer = new MediaPlayer();

    }

    public void setLoopingList(boolean x) {
        looping = x;
    }

    public boolean isLooping() {
        return looping;
    }

    public void pause() {
        mediaPlayer.pause();
    }

    public void resume() {
        int length;
        length = mediaPlayer.getCurrentPosition();
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
    }

    public void playSong(int position) {
        currPos = position;
        if (looping && position > maxSongs) {
            position = 0;
        } else {
            position = -1;
        }
        if (position >= 0 && position <= maxSongs) {
            currSong = songList.get(position);
            try {
                mediaPlayer.setDataSource(currSong.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public HashMap<String, String> getCurrSongData() throws InvalidDataException, IOException, UnsupportedTagException {
        return cache.getMetadataAll(new Mp3File(currSong.getAbsolutePath()));
    }

    public Bitmap getArt() throws InvalidDataException, IOException, UnsupportedTagException {
        return cache.getAlbumArt(new Mp3File(currSong.getAbsolutePath()));
    }

    public boolean isPlaying() {
        return mediaPlayer.isPlaying();
    }

    public void toggleCurrentSong() {
        if (isPlaying()) {
            mediaPlayer.pause();
        } else {
            mediaPlayer.start();
        }

    }

    public void nextSong() {
        playSong(currPos + 1);
    }

    public void prevSong() {
        playSong(currPos - 1);


    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;

    }
}
