package com.example.dos_thesis;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.content.Context;

public class CustomAdapter extends BaseAdapter {
    Context context;
    int photos[];
    LayoutInflater inflater;

    public CustomAdapter(Context applicationContext, int[] photos) {
        this.context = applicationContext;
        this.photos = photos;
        inflater = (LayoutInflater.from(applicationContext));
    }

    @Override
    public int getCount() {
        return photos.length;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = inflater.inflate(R.layout.gv_cell, null); // inflate the layout
        ImageView icon = view.findViewById(R.id.gv_image); // get the reference of ImageView
        icon.setImageResource(photos[i]); // set logo images
        return view;
    }
}