package com.teamsoftware.materialmusic;

import android.app.ProgressDialog;

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
    String[] listItems;
    ArrayList<File> songList;
    SongManager songManager;
    ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);

        init(rootview);

        //songList updated again
        songList = songManager.getSongsList();
        listItems = new String[songList.size()];

        //Remove the file extension '.mp3' from the name
        for (int i = 0; i < songList.size(); i++) {
            listItems[i] = songList.get(i).getName().replace(".mp3", "");
        }

        //ArrayAdapter for setting up the ListView
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.row_layout, R.id.text_view, listItems);

        if (listView != null) {
            listView.setAdapter(arrayAdapter);

            //Start MediaPlayer
            //Send song position and songList
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    //Intent intent = new Intent(getContext(), /*Player class*/);
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

        progressDialog = new ProgressDialog(getContext());
        progressDialog.setMessage("Loading songs...");
        //The dialog is cancelable with BACK key
        progressDialog.setCancelable(true);

        //Display the progress dialog, until all songs have been fetched
        while (!songManager.getFetchStatus()) {
            songList = songManager.findSongList(new File(baseDirectory));
        }
        if (songList != null) {
            progressDialog.dismiss();
        }
    }
}