package com.shreeniclix.priyanka.popularmoviesapp;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by priyanka on 7/11/2017.
 */

public class CustomAdapter extends ArrayAdapter<CustomGrid> {
    private int resource;
    private Context context;
    private ArrayList<CustomGrid> products;

    public CustomAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull ArrayList<CustomGrid> products) {
        super(context, resource, products);
        this.products=products;
        this.context = context;
        this.resource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = layoutInflater.inflate(R.layout.grid_single,null,true);
        //convertView = layoutInflater.inflate(R.layout.grid_single,parent);
            CustomGrid customgrid = getItem(position);

        ImageView imageView = (ImageView) convertView.findViewById(R.id.im_grid_img);
        Picasso.with(context).load(customgrid.getPosterName()).into(imageView);

        return convertView;
    }
}
