package com.teamsoftware.materialmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class SongAdapter extends ArrayAdapter<File> {

    private Context mContext;
    private List<File> songsList;
    private SongManager songManager;
    private Mp3File song;

    public SongAdapter(@NonNull Context context, @LayoutRes ArrayList<File> list) {
        super(context, 0 , list);
        mContext = context;
        songsList = list;
        songManager = new SongManager();
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_layout,parent,false);
        Log.e("Song Path", songsList.get(position).getAbsolutePath());

        try {
            song = new Mp3File(songsList.get(position).getAbsolutePath());
        }
        catch (Exception e){
            e.printStackTrace();
        }

        ImageView image = listItem.findViewById(R.id.image_view);
        //image.setImageBitmap(getAlbumArt(song));

        Glide.with(mContext)
                .load(getAlbumArt(song))
                .into(image);

        TextView name = listItem.findViewById(R.id.text_view);
        name.setText(getMetadataAll(song).get("Title"));

        return listItem;
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
        return null;
    }

}
