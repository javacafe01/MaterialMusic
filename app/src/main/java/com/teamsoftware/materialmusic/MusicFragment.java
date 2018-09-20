package com.teamsoftware.materialmusic;

import android.app.ProgressDialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class MusicFragment extends Fragment implements Serializable {

    ListView listView;
    String baseDirectory;
    ArrayList<File> songList;
    SongManager songManager;
    ProgressDialog progressDialog;
    private static Context context;
    SongAdapter songadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);

        context = getActivity();
        init(rootview);

        //songList updated again
        songList = songManager.getSongsList();

        songadapter = new SongAdapter(context,songList);

        if (listView != null) {
            listView.setAdapter(songadapter);

            //Start MediaPlayer
            //Send song position and songList
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent = new Intent(context, /*Player class*/);
                    //intent.putExtra("position", position);
                    //startActivity(intent);
                }
            });
        }

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

    public void init(View v) {

        listView = (ListView) v.findViewById(R.id.list_view);
        songManager = new SongManager();
        //Path to external storage directory, in this case SD card
        baseDirectory = Environment.getExternalStorageDirectory().getAbsolutePath();

        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(true);
        progressDialog.setMessage("Loading songs...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

        //Display the progress dialog, until all songs have been fetched
        while (!songManager.getFetchStatus()) {
            progressDialog.show();
            songList = songManager.findSongList(new File(baseDirectory));
        }
        if (songList != null) {
            progressDialog.dismiss();
        }
    }


}