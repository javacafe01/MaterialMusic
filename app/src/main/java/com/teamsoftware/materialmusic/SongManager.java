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

import java.io.File;
import java.util.ArrayList;

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