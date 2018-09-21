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

public class MusicFragment extends Fragment implements Serializable, SongRecyclerAdapter.ClickListener {

    RecyclerView recyclerView;
    String baseDirectory;

    ArrayList<File> songList;
    ArrayList<Bitmap> albumArts;
    ArrayList<Mp3File> songPreload;
    LruCache<String, Bitmap> albumCache;

    SongManager songManager;
    ProgressDialog progressDialog;
    private static Context context;
    SongRecyclerAdapter songadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);


        context = getContext();
        init(rootview);

        //songList updated again
        songList = songManager.getSongsList();
        albumArts = new ArrayList<>();
        songPreload = new ArrayList<>();
        for(File file : songList){
            try {
                songPreload.add(new Mp3File(file.getAbsolutePath()));
                albumArts.add(getAlbumArt(songPreload.get(songPreload.size()-1)));
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
        //songadapter = new SongAdapter(context,songList, albumArts, songPreload);

        context = getActivity();

        //songList updated again
        checkPermissions();

        init(rootview);

        songadapter = new SongRecyclerAdapter(context, songList, this, albumArts, songPreload);

        // Create your layout manager.
        LinearLayoutManager layout = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layout);



        recyclerView.setAdapter(songadapter);
        return rootview;
    }
    public Bitmap getAlbumArt(Mp3File file){

        byte[] imageData = file.getId3v2Tag().getAlbumImage();
        if (imageData != null) {
            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
        }
        return BitmapFactory.decodeResource(getContext().getResources(), R.drawable.ic_audiotrack_black_24dp);

    }

    private void checkPermissions() {
        int permission1 = PermissionChecker.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE);
        if (permission1 == PermissionChecker.PERMISSION_GRANTED) {
            //good to go
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    1);
        }
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
    public void onItemClick(int position, View v) {
        Toast.makeText(context, "" + position, Toast.LENGTH_SHORT);
        //Intent intent = new Intent(context, /*Player class*/);
        //intent.putExtra("position", position);
        //startActivity(intent);
    }
}