package com.teamsoftware.materialmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongAdapter extends ArrayAdapter<File> {

    private Context mContext;
    private List<File> songsList;

    public SongAdapter(@NonNull Context context, @LayoutRes ArrayList<File> list) {
        super(context, 0 , list);
        mContext = context;
        songsList = list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_layout,parent,false);

        File song = songsList.get(position);

        ImageView image = (ImageView)listItem.findViewById(R.id.image_view);
        image.setImageBitmap(getImageFromFile(song));

        TextView name = (TextView) listItem.findViewById(R.id.text_view);
        name.setText(song.getName().replace(".mp3", ""));

        return listItem;
    }

    private Bitmap getImageFromFile(File file) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        byte[] rawArt;
        Bitmap art;
        BitmapFactory.Options bfo = new BitmapFactory.Options();

        mmr.setDataSource(mContext, Uri.fromFile(file));
        rawArt = mmr.getEmbeddedPicture();

        if (null != rawArt)
            art = BitmapFactory.decodeByteArray(rawArt, 0, rawArt.length, bfo);
        else
            art = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.ic_home_white_24dp);

        return art;
    }
}
