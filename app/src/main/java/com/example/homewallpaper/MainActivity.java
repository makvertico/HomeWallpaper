package com.example.homewallpaper;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.homewallpaper.model.Categorymodel;
import com.example.homewallpaper.utils.Utils;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    ImageButton info_button;
    private static final int STORAGE_PERMISSION_CODE = 101;
    ImageSlider mimageslider;
    RecyclerView mrecyclerview;
    DatabaseReference mDatabase;
    FirebaseRecyclerOptions<Categorymodel> options;
    List<SlideModel> slideModels;
    FirebaseRecyclerAdapter<Categorymodel,CategoryViewHolder> adapter;
    GridLayoutManager gridLayoutManager;

    //slider image


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        info_button = findViewById(R.id.info_button);
        mimageslider = findViewById(R.id.slider_img);
        mrecyclerview = findViewById(R.id.recycler_view);
        mrecyclerview.setHasFixedSize(true);
        mrecyclerview.setDrawingCacheEnabled(true);
        mrecyclerview.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                STORAGE_PERMISSION_CODE);






        //list of elements
        slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/homewallpaper-2c92d.appspot.com/o/imageslider%2Fezgif.com-gif-maker%20(1).webp?alt=media&token=faebce87-23f2-429a-857f-42673c7dd0d3","Iam not very good at giving anyone a clear no."));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/homewallpaper-2c92d.appspot.com/o/imageslider%2Fezgif.com-gif-maker%20(2).webp?alt=media&token=f2903a88-4ddc-4747-960e-93d385966de2","I had always seen myself as a star; I wanted to be a galaxy."));
        slideModels.add(new SlideModel("https://firebasestorage.googleapis.com/v0/b/homewallpaper-2c92d.appspot.com/o/imageslider%2Fezgif.com-gif-maker.webp?alt=media&token=de096438-6cda-49a9-a998-c328baec8a31","Don't let anyone ever dull your sparkle."));
        mimageslider.setImageList(slideModels,true);



        //============================info view ================================
        info_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),Info.class);
                startActivity(intent);
            }
        });

        //make translucent statusBar on kitkat devices
        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }
        if (Build.VERSION.SDK_INT >= 19) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        //make fully Android Transparent Status bar
        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
      getData();










    }

    public void getData(){

        mDatabase = FirebaseDatabase.getInstance().getReference("category");
        mDatabase.keepSynced(true);

        options = new FirebaseRecyclerOptions.Builder<Categorymodel>()
                .setQuery(mDatabase,Categorymodel.class).build();

        adapter = new FirebaseRecyclerAdapter<Categorymodel, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final CategoryViewHolder holder, final int position, @NonNull final Categorymodel model) {
                holder.list_name.setText(model.getName());

                Picasso.get().load(model.getImagelink())
                        .networkPolicy(NetworkPolicy.OFFLINE)
                        .into(holder.list_imagelink, new Callback() {
                            @Override
                            public void onSuccess() {

                            }

                            @Override
                            public void onError(Exception e) {

                                Picasso.get().load(model.getImagelink()).into(holder.list_imagelink);
                            }
                        });


                holder.list_imagelink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Utils.CATEGORY_ID = adapter.getRef(position).getKey();
                        Utils.CATEGORY_SELECTED = model.getName();

                        Intent i = new Intent(MainActivity.this,list_wallpaper.class);
                        startActivity(i);
                    }
                });
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_view_category,parent,false);

                return new CategoryViewHolder(view);
            }
        };

        //===================== Grid View=======================
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        mrecyclerview.setLayoutManager(gridLayoutManager);
        adapter.startListening();
        mrecyclerview.setAdapter(adapter);





    }



    @Override
    protected void onPause() {
        super.onPause();
        adapter.stopListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }


    @Override
    protected void onResume() {
        super.onResume();
        adapter.startListening();
        }


    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }





    //==================Category ViewHolder==============

    private static class CategoryViewHolder extends RecyclerView.ViewHolder {

        private TextView list_name;
        private ImageView list_imagelink;
        private static final int STORAGE_PERMISSION_CODE = 101;


        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);
            list_name= itemView.findViewById(R.id.name_id);
            list_imagelink= itemView.findViewById(R.id.img_id);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(MainActivity.this, permission)
                == PackageManager.PERMISSION_DENIED) {

            // Requesting the permission
            ActivityCompat.requestPermissions(MainActivity.this,
                    new String[] { permission },
                    requestCode);
        }
        else {
            Toast.makeText(MainActivity.this,
                    "Permission already granted",
                    Toast.LENGTH_SHORT)
                    .show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super
                .onRequestPermissionsResult(requestCode,
                        permissions,
                        grantResults);

        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Granted",
                        Toast.LENGTH_SHORT)
                        .show();
            }
            else {
                Toast.makeText(MainActivity.this,
                        "Storage Permission Denied",
                        Toast.LENGTH_SHORT)
                        .show();
            }
        }

    }





}