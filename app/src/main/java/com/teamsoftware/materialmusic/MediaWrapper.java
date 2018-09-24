package com.teamsoftware.materialmusic;

import android.media.MediaPlayer;

import java.io.File;
import java.util.ArrayList;

public class MediaWrapper {
    MetadataCacher cache;
    private ArrayList<File> songList;
    private File currSong;
    private boolean looping;
    private int currPos, maxSongs;
    private MediaPlayer mediaPlayer;

    public MediaWrapper(MetadataCacher cache, ArrayList<File> songList){
        this.cache = cache;
        this.songList = songList;
        maxSongs = this.songList.size()-1;
        mediaPlayer = new MediaPlayer();

    }
    public void setLoopingList(boolean x){
        looping = x;
    }

    public void playSong(int position){
        currPos = position;
        if(looping && position > maxSongs){
            position = 0;
        }
        else{
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
    public void toggleCurrentSong(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
        }
        else{
            mediaPlayer.start();
        }

    }
    public void nextSong(){
        playSong(currPos+1);
    }
    public void prevSong(){
        playSong(currPos-1);


    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;

    }
}
