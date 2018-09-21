package com.teamsoftware.materialmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MetadataCacher {
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

    public MetadataCacher(ArrayList<File> songs){
        songList = songs;
        songCache = new ArrayList<>();
        albumCache = new ArrayList<>();
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
        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_audiotrack_black_24dp);

    }
}
