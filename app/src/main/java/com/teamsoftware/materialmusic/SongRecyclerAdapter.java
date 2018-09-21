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
    //private Mp3File song;
    private ArrayList<Bitmap> albumArts;
    private ArrayList<Mp3File> songPreload;
    private static ClickListener clickListener;


    public SongRecyclerAdapter(Context context, ArrayList<File> list, Fragment frag, ArrayList<Bitmap> arts, ArrayList<Mp3File> pre) {
        mContext = context;
        songsList = list;
        this.frag = frag;
        songPreload = pre;
        albumArts = arts;
    }

    @Override
    public SongRecyclerAdapter.SongViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_layout, parent, false);
        SongViewHolder viewHolder = new SongViewHolder(view, frag);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SongRecyclerAdapter.SongViewHolder holder, int position) {
//        try {
//            song = new Mp3File(songsList.get(position).getAbsolutePath());
//        }
//        catch (Exception e){
//            e.printStackTrace();
//        }
        holder.bindSong(songPreload.get(position), albumArts.get(position));
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
        public void bindSong(Mp3File file, Bitmap art){
            image.setImageBitmap(art);
            name.setText(getMetadataAll(file).get("Title"));

        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
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

    public void setOnItemClickListener(ClickListener clickListener) {
        SongRecyclerAdapter.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }

}
