package com.shreeniclix.priyanka.popularmoviesapp;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

/**
 * Created by priyanka on 8/27/2017.
 */

public class FavsRecyclerAdapter extends RecyclerView.Adapter<FavsRecyclerAdapter.TaskViewHolder> {
    private Cursor mCursor;
    private Context mContext;
    private final ListItemClickListener mOnClickListener;
    private String title, posterPath, overview, voteAvg, releaseDate;
    private int movieId;
    private int idIndex, titleIndex,posterPathIndex,overviewIndex,voteAverageIndex,releaseDateIndex,movieIdIndex;


    public interface ListItemClickListener {
        void onListItemClick(int clickedItemPosition, String titleItem, String posterPathItem, String overviewItem, String voteAvgItem, String releaseDateItem, int movieIdItem);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private View layout;
        ImageView favimgicon;

        public TaskViewHolder(View itemView) {
            super(itemView);
            layout =itemView;
            Log.d("Reached..", "task view holder variable creation");
            favimgicon = (ImageView) itemView.findViewById(R.id.im_fav_item);
            itemView.setOnClickListener(this);

        }
        @Override
        public void onClick(View view) {
            int clickedPosition = getAdapterPosition();
            Log.d("Clicked_position",clickedPosition+"");

            mCursor.moveToPosition(clickedPosition);
            final int id = mCursor.getInt(idIndex);
            title = mCursor.getString(titleIndex);
            posterPath = mCursor.getString(posterPathIndex);
            overview = mCursor.getString(overviewIndex);
            voteAvg = mCursor.getString(voteAverageIndex);
            releaseDate = mCursor.getString(releaseDateIndex);

            movieId = mCursor.getInt(movieIdIndex);
            Log.d("details:",clickedPosition + " : " + title + " : " + posterPath + " : " + overview + " : " + voteAvg + " : " + releaseDate + " : " + movieId);

            mOnClickListener.onListItemClick(clickedPosition, title, posterPath, overview, voteAvg, releaseDate, movieId);
        }
    }

    public FavsRecyclerAdapter(Context mContext, ListItemClickListener listener) {
        this.mContext = mContext;
        mOnClickListener = listener;
    }


    @Override
    public TaskViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.grid_favorites, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TaskViewHolder holder, int position) {

         idIndex = mCursor.getColumnIndex(TaskContract.TaskEntry._ID);
         titleIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_TITLE);
         posterPathIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_POSTERPATH);
         overviewIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_OVERVIEW);
         voteAverageIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_VOTEAVERAGE);
         releaseDateIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_RELEASEDATE);
         movieIdIndex = mCursor.getColumnIndex(TaskContract.TaskEntry.COLUMN_MOVIEID);

        mCursor.moveToPosition(position);
        final int id = mCursor.getInt(idIndex);
        title = mCursor.getString(titleIndex);
        posterPath = mCursor.getString(posterPathIndex);
        overview = mCursor.getString(overviewIndex);
        voteAvg = mCursor.getString(voteAverageIndex);
        releaseDate = mCursor.getString(releaseDateIndex);

        movieId = mCursor.getInt(movieIdIndex);

        holder.itemView.setTag(id);
        Log.d("Poster_Image", posterPath);
        Picasso.with(mContext).load(posterPath).into(holder.favimgicon);
    }

    @Override
    public int getItemCount() {
        if (mCursor == null) {
            return 0;
        }
        return mCursor.getCount();
    }

    public Cursor swapCursor(Cursor c) {
        if (mCursor == c) {
            return null;
        }
        Cursor temp = mCursor;
        this.mCursor = c;
        if (c != null) {
            this.notifyDataSetChanged();
        }
        return temp;
    }
}
