package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by priyanka on 8/22/2017.
 */

public class MyVideoAdapter extends RecyclerView.Adapter<MyVideoAdapter.ViewHolder> {
    private List<String> values, key;
    private Context c;
    private final ListItemClickListener mOnClickListener;

    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex, String utubeUrl);
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private View layout;
        private VideoView vv_videoView;
        private ImageView iv_thumbnail;

        public ViewHolder(View v) {
            super(v);
            layout = v;
            vv_videoView = (VideoView) v.findViewById(R.id.vv_videoView);
            iv_thumbnail = (ImageView) v.findViewById(R.id.iv_imgthumanail);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            Log.d("MyVideoAdapter", values.get(clickedPosition));
            mOnClickListener.onListItemClick(clickedPosition, values.get(clickedPosition));
        }
    }

    public MyVideoAdapter(Context c, List<String> movieList, List<String> key, ListItemClickListener listener) {
        this.c = c;
        this.values = movieList;
        this.key = key;
        mOnClickListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.video_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String videoUrl = values.get(position);
        String id = key.get(position);
        String thumbnail = "http://img.youtube.com/vi/".concat(id).concat("/hqdefault.jpg");
        Log.d("VIDEO_URL:", videoUrl + " - KEY IS : " + id);
        Picasso.with(c).load(thumbnail).placeholder(R.drawable.placeholder).into(holder.iv_thumbnail);
    }

    @Override
    public int getItemCount() {
        Log.d("ITEMCOUNT_SIZE:", String.valueOf(values.size()));
        return (values!=null) ? values.size():0;
    }
}
