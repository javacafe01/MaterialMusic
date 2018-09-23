package com.teamsoftware.materialmusic;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.Serializable;
import java.util.ArrayList;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class MusicFragment extends Fragment implements Serializable {

    private RecyclerView recyclerView;
    private ArrayList<File> songList;
    private static Context context;
    private SongRecyclerAdapter songadapter;
    private MetadataCacher cache;
    private SongRecyclerAdapter.ClickInterface clickInterface;

    public MusicFragment(MetadataCacher cache, SongRecyclerAdapter.ClickInterface clickInterface) {
        this.cache = cache;
        this.clickInterface = clickInterface;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.fragment_music, container, false);

        context = getContext();
        init(rootview);

        songList = cache.getSongList();

        songadapter = new SongRecyclerAdapter(context, songList, this, cache, clickInterface);
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
    }
}