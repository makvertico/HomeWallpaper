package com.example.homewallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.example.homewallpaper.Adapter.WallpaperAdapter;

import com.example.homewallpaper.model.Wallpapermodel;
import com.example.homewallpaper.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

public class list_wallpaper extends AppCompatActivity {

    RecyclerView wrecyclerview;
    Query query;
    GridLayoutManager gridLayoutManager;
    FirebaseRecyclerOptions<Wallpapermodel> options;
    FirebaseRecyclerAdapter<Wallpapermodel, WallpaperAdapter> adapter;
    Context context;

    private int lastPosition;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_wallpaper);

        wrecyclerview = findViewById(R.id.recycler_view_wallpaper);
        wrecyclerview.setHasFixedSize(true);
        //gridView
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        wrecyclerview.setLayoutManager(gridLayoutManager);

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        lastPosition = getPrefs.getInt("lastPos",0);
        wrecyclerview.scrollToPosition(lastPosition);
        wrecyclerview.setDrawingCacheEnabled(true);
        wrecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);


        wrecyclerview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                lastPosition = gridLayoutManager.findFirstVisibleItemPosition();
            }
        });
        query = FirebaseDatabase.getInstance().getReference("wallpaper")
                .orderByChild("categoryid").equalTo(Utils.CATEGORY_ID);

        options = new FirebaseRecyclerOptions.Builder<Wallpapermodel>()
                .setQuery(query,Wallpapermodel.class).build();

         adapter = new FirebaseRecyclerAdapter<Wallpapermodel, WallpaperAdapter>(options) {
             @Override
             protected void onBindViewHolder(@NonNull final WallpaperAdapter holder, final int position, @NonNull final Wallpapermodel model) {



                 Picasso.get().load(model.getImagelink())
                         .networkPolicy(NetworkPolicy.OFFLINE)
                         .into(holder.wall_imageview, new Callback() {
                             @Override
                             public void onSuccess() {

                             }

                             @Override
                             public void onError(Exception e) {

                                 Picasso.get().load(model.getImagelink())
                                         .error(R.drawable.ic_baseline_terrain_24)
                                         .into(holder.wall_imageview);

                             }
                         });

                 new Handler().postDelayed(new Runnable() {
                     public void run() {

                         holder.wall_imageview.setOnClickListener(new View.OnClickListener() {
                             @Override
                             public void onClick(View v) {

                                 Utils.CATEGORY_ID = adapter.getRef(position).getKey();
                                 Utils.CATEGORY_SELECTED = model.categoryid;
                                 Utils.wallpapermodel = model;

                                 Intent intent = new Intent(getApplicationContext(),WallpaperView.class);
                                 startActivity(intent);
                             }
                         });
                     }
                 }, 100);

             }

             @NonNull
             @Override
             public WallpaperAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_wallpaper,parent,false);

                int height = parent.getMeasuredHeight()/2;
                view.setMinimumHeight(height);



                 return new WallpaperAdapter(view);
             }
         };

         adapter.startListening();
         wrecyclerview.setAdapter(adapter);

    }





    @Override
    protected void onDestroy() {
        super.onDestroy();

        SharedPreferences getPrefs = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
        SharedPreferences.Editor editor = getPrefs.edit();
        editor.putInt("lastPos", lastPosition);
        editor.apply();


    }
}