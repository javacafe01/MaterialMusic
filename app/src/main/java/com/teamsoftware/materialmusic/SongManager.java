package com.teamsoftware.materialmusic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SongManager {

    //Boolean value which determines whether all the songs have been fetched, or the process is ongoing
    boolean fetchStatus = false;

    //ArrayList for holding all the songs
    ArrayList<File> songsList = new ArrayList<>();

    /**
     * Fetches all the songs in the sd card
     * Inner folders are recursively checked for mp3 files
     * Hidden folders are ignored as it may result in a stack-overflow
     */
    public ArrayList<File> findSongList(File root) {
        ArrayList<File> songs = new ArrayList<>();
        File[] files = root.listFiles();

        for (File singleFile : files) {

            if (singleFile.isDirectory() && !singleFile.isHidden()) {
                songs.addAll(findSongList(singleFile));
            } else {
                if (singleFile.getName().endsWith(".mp3")) {
                    songs.add(singleFile);
                }
            }

            //All songs have been fetched
            fetchStatus = true;
            songsList = songs;

        }
        return songs;
    }



    public boolean getFetchStatus() {
        return fetchStatus;
    }

    public ArrayList<File> getSongsList() {
        return songsList;
    }
}