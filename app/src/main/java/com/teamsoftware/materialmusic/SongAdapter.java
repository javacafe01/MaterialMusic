package com.teamsoftware.materialmusic;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.mpatric.mp3agic.Mp3File;

import java.io.File;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SongAdapter extends ArrayAdapter<File> {

    private Context mContext;
    private List<File> songsList;
    private ArrayList<Bitmap> albumArts;
    private ArrayList<Mp3File> preloadSongs;
    private Mp3File song;
    public SongAdapter(@NonNull Context context, @LayoutRes ArrayList<File> list, @NonNull ArrayList<Bitmap> arts, @NonNull ArrayList<Mp3File> pre) {
        super(context, 0 , list);
        mContext = context;
        songsList = list;
        albumArts = arts;
        preloadSongs = pre;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if(listItem == null)
            listItem = LayoutInflater.from(mContext).inflate(R.layout.row_layout,parent,false);
        Log.e("Song Path", songsList.get(position).getAbsolutePath());



        ImageView image = listItem.findViewById(R.id.image_view);
        image.setImageBitmap(albumArts.get(position));
//        loadAlbum(position, image, song);
        //new AlbumManager(image).execute(song);
//        new Thread(new Runnable() {
//            public void run() {
//                image.setImageBitmap(getAlbumArt(song));
//            }
//        }).start();
        TextView name = listItem.findViewById(R.id.text_view);
        name.setText(getMetadataAll(preloadSongs.get(position)).get("Title"));

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
//    public Bitmap getAlbumArt(Mp3File file){
//
//        byte[] imageData = file.getId3v2Tag().getAlbumImage();
//        if (imageData != null) {
//            return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//        }
//        return BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_audiotrack_black_24dp);
//
//    }
//    private class AlbumManager extends AsyncTask<Mp3File, Integer, Bitmap> {
//        private final WeakReference<ImageView> imageViewReference;
//        private int data = 0;
//
//        public AlbumManager(ImageView imageView){
//            imageViewReference = new WeakReference<ImageView>(imageView);
//        }
//
//        @Override
//        protected Bitmap doInBackground(Mp3File... mp3Files) {
//
//            byte[] imageData = mp3Files[data].getId3v2Tag().getAlbumImage();
//            if (imageData != null) {
//                return BitmapFactory.decodeByteArray(imageData, 0, imageData.length);
//            }
//            return null;
//
//
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            if (isCancelled()) {
//                bitmap = null;
//            }
//            if (bitmap != null) {
//                final ImageView imageView = imageViewReference.get();
//                final AlbumManager bitmapWorkerTask =
//                        getAlbumManager(imageView);
//                if (this == bitmapWorkerTask  && imageView != null) {
//                    imageView.setImageBitmap(bitmap);
//                }
//            }
//        }
//    }
//    static class AsyncDrawable extends BitmapDrawable {
//        private final WeakReference<AlbumManager> albumManagerWeakReference;
//
//        public AsyncDrawable(Resources res, Bitmap bitmap,
//                             AlbumManager bitmapWorkerTask) {
//            super(res, bitmap);
//            albumManagerWeakReference =
//                    new WeakReference<AlbumManager>(bitmapWorkerTask);
//        }
//
//        public AlbumManager getManager() {
//            return albumManagerWeakReference.get();
//        }
//    }
//    public void loadAlbum(int resId, ImageView imageView, Mp3File mp3File) {
//        if (cancelPotentialWork(resId, imageView)) {
//            final AlbumManager manager = new AlbumManager(imageView);
//            final AsyncDrawable asyncDrawable =
//                    new AsyncDrawable(mContext.getResources(), BitmapFactory.decodeResource(mContext.getResources(), R.drawable.ic_audiotrack_black_24dp), manager);
//            imageView.setImageDrawable(asyncDrawable);
//            manager.execute(mp3File);
//        }
//    }
//    public static boolean cancelPotentialWork(int data, ImageView imageView) {
//        final AlbumManager albumManager = getAlbumManager(imageView);
//
//        if (albumManager != null) {
//            final int bitmapData = albumManager.data;
//            if (bitmapData != data) {
//                // Cancel previous task
//                albumManager.cancel(true);
//            } else {
//                // The same work is already in progress
//                return false;
//            }
//        }
//        // No task associated with the ImageView, or an existing task was cancelled
//        return true;
//    }
//    private static AlbumManager getAlbumManager(ImageView imageView) {
//        if (imageView != null) {
//            final Drawable drawable = imageView.getDrawable();
//            if (drawable instanceof AsyncDrawable) {
//                final AsyncDrawable asyncDrawable = (AsyncDrawable) drawable;
//                return asyncDrawable.getManager();
//            }
//        }
//        return null;
//    }


}
