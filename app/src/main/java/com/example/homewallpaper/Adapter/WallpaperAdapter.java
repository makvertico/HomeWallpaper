package com.example.homewallpaper.Adapter;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.homewallpaper.R;

public class WallpaperAdapter extends RecyclerView.ViewHolder {

    public ImageView wall_imageview;
    public WallpaperAdapter(@NonNull View itemView) {
        super(itemView);

        wall_imageview = itemView.findViewById(R.id.wallpaper_id);

    }
}
