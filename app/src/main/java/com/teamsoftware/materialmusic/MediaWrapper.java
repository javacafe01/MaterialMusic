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

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import com.mpatric.mp3agic.InvalidDataException;
import com.mpatric.mp3agic.Mp3File;
import com.mpatric.mp3agic.UnsupportedTagException;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;

public class MediaWrapper implements Serializable {
    MetadataCacher cache;
    private ArrayList<File> songList;
    private File currSong;
    private Mp3File currMp3;
    private boolean looping = false;
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

    public void resume() {
        int length;
        length = mediaPlayer.getCurrentPosition();
        mediaPlayer.seekTo(length);
        mediaPlayer.start();
    }

    public void playSong(int position) {
        currPos = position;
        if (position >= 0 && position <= maxSongs) {
            currSong = songList.get(position);
            try {
                mediaPlayer.reset();

                mediaPlayer.setDataSource(currSong.getAbsolutePath());
                mediaPlayer.prepare();
                mediaPlayer.start();
                mediaPlayer.setVolume(100.0f, 100.0f);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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
