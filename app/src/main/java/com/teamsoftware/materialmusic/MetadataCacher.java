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

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import com.mpatric.mp3agic.Mp3File;
import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class MetadataCacher implements Serializable{
    private ArrayList<Mp3File> songCache;
    private ArrayList<File> songList;
    private ArrayList<Bitmap> albumCache;
    private Context mContext;

    public ArrayList<Mp3File> getSongCache() {
        return songCache;
    }

    public ArrayList<Bitmap> getAlbumCache() {
        return albumCache;
    }

    public ArrayList<File> getSongList() {
        return songList;
    }

    MetadataCacher(ArrayList<File> songs){
        songList = songs;
        songCache = new ArrayList<>();
        albumCache = new ArrayList<>();
        Thread thread = new Thread() {
            @Override
            public void run() {
                for(File file : songs){
                    try {
                        songCache.add(new Mp3File(file.getAbsolutePath()));
                        albumCache.add(getAlbumArt(songCache.get(songCache.size()-1)));
                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }
                }

            }
        };

        thread.start();


    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public HashMap<String, String> getMetadataAll(Mp3File file){
        HashMap<String, String> data = new HashMap<>();
//        if(file.hasId3v1Tag()){
//            data.put("Title", file.getId3v1Tag().getTitle());
//            data.put("Album", file.getId3v1Tag().getAlbum());
//            data.put("Artist", file.getId3v1Tag().getArtist());
//        }

        if(file.hasId3v2Tag()){
            data.put("Title", file.getId3v2Tag().getTitle());
            data.put("Album", file.getId3v2Tag().getAlbum());
            data.put("Artist", file.getId3v2Tag().getArtist());

        }
        return data;

    }
    public Bitmap getAlbumArt(Mp3File file){

        byte[] imageData = file.getId3v2Tag().getAlbumImage();
        if (imageData != null) {
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.preload);

    }
}