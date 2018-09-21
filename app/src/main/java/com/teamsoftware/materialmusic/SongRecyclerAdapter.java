package com.teamsoftware.materialmusic;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

public class SongRecyclerAdapter extends RecyclerView.Adapter<SongRecyclerAdapter.SongViewHolder> {
    private Context mContext;
    private List<File> songsList;
    Fragment frag;
    MetadataCacher cache;
    //private Mp3File song;
    private static ClickListener clickListener;


    public SongRecyclerAdapter(Context context, ArrayList<File> list, Fragment frag, MetadataCacher cache) {
        mContext = context;
        songsList = list;
        this.frag = frag;
        this.cache = cache;

    }

    @Override
    public SongRecyclerAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(view, frag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SongRecyclerAdapter.SongViewHolder holder, int position) {

        holder.bindSong(cache.getMetadataAll(cache.getSongCache().get(position)).get("Title"), cache.getAlbumCache().get(position));
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener  {
        private ImageView image;
        private TextView name;
        private Context mContext;
        private Fragment frag;

        public SongViewHolder(View itemView, Fragment frag) {
            super(itemView);
            mContext = itemView.getContext();
            image = (ImageView) itemView.findViewById(R.id.image_view);
            name = (TextView) itemView.findViewById(R.id.text_view);
            this.frag = frag;
        }
        public void bindSong(String title, Bitmap art){
            image.setImageBitmap(art);
            name.setText(title);

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }



    public void setOnItemClickListener(ClickListener clickListener) {
        SongRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
