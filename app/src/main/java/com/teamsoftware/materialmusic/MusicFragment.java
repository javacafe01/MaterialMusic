package com.teamsoftware.materialmusic;

import android.Manifest;
import android.app.ProgressDialog;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.PermissionChecker;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicFragment extends Fragment implements Serializable, SongRecyclerAdapter.ClickInterface{

    RecyclerView recyclerView;
    String baseDirectory;

    ArrayList<File> songList;


    SongManager songManager;
    ProgressDialog progressDialog;
    private static Context context;
    SongRecyclerAdapter songadapter;
    MetadataCacher cache;
    public MusicFragment(MetadataCacher cache){
        this.cache = cache;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);

        context = getContext();
        init(rootview);

        //songList updated again
        songList = cache.getSongList();

        //songadapter = new SongAdapter(context,songList, albumArts, songPreload);

        context = getActivity();

        init(rootview);

        songadapter = new SongRecyclerAdapter(context, songList, this, cache, this);

        // Create your layout manager.
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);



        recyclerView.setAdapter(songadapter);
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

        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
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

    @Override
    public void onSongClick(int position) {
        Log.d("Item", "Item Position =" + position);
    }
}