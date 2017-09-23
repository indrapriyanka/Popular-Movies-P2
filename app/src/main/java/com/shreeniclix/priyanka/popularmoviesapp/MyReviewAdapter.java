package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by priyanka on 8/23/2017.
 */

public class MyReviewAdapter extends RecyclerView.Adapter<MyReviewAdapter.ViewHolder> {
    private List<String> values;
    private Context c;

    public class ViewHolder extends RecyclerView.ViewHolder{
        private View layout;
        private TextView tv_review;

        public ViewHolder(View v) {
            super(v);
            this.layout = v;
            tv_review = (TextView) v.findViewById(R.id.tv_review);
        }

    }
    public MyReviewAdapter(Context c, List<String> values) {
        this.c = c;
        this.values = values;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(c);
        View v = inflater.inflate(R.layout.review_item,parent,false);
        ViewHolder vh = new MyReviewAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String review = values.get(position);
        Log.d("onbindviewholder",review);
        holder.tv_review.setText(review);
    }

    @Override
    public int getItemCount() {
        Log.d("ITEM_COUNT:",String.valueOf(values.size()));
        return values.size();
    }
}
