 package com.example.homewallpaper;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.AlertDialog;
import android.app.WallpaperManager;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.homewallpaper.Helper.SaveHelper;
import com.example.homewallpaper.utils.Utils;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import com.yalantis.ucrop.UCrop;


import java.io.IOException;
import java.util.UUID;

import static com.example.homewallpaper.MainActivity.setWindowFlag;

 public class WallpaperView extends AppCompatActivity {


     FloatingActionButton fabDownload, fabWallpaper, setlockscreen, sethomescreen;
     ImageView mWallpaper;
     Animation fabopen, fabclose, rotateclockwise, rotateAnticlockwise;
     CoordinatorLayout rootLayout;
     private ScaleGestureDetector mScaleGestureDetector;
     private float mScaleFactor = 1.0f;
     UCrop uCrop;

     boolean isOpen = false;
     private final Target target2 = new Target() {
         @RequiresApi(api = Build.VERSION_CODES.N)
         @Override
         public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
             WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

             try {
                 wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK);
                 Snackbar.make(rootLayout, "Wallpaper set on Lock Screen ", Snackbar.LENGTH_SHORT).show();
             } catch (IOException e) {
                 Snackbar.make(rootLayout, "Unable to set Wallpaper on Lock Screen ", Snackbar.LENGTH_SHORT).show();
                 e.printStackTrace();
             }
         }

         @Override
         public void onBitmapFailed(Exception e, Drawable errorDrawable) {

         }

         @Override
         public void onPrepareLoad(Drawable placeHolderDrawable) {

         }
     };
     private Target target1 = new Target() {
         @RequiresApi(api = Build.VERSION_CODES.N)
         @Override
         public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
             WallpaperManager wallpaperManager = WallpaperManager.getInstance(getApplicationContext());

             try {
                 wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM);
                 Snackbar.make(rootLayout, "Wallpaper was set on Home Screen ", Snackbar.LENGTH_SHORT).show();
             } catch (IOException e) {
                 Snackbar.make(rootLayout, "Unable to set Wallpaper on Home Screen ", Snackbar.LENGTH_SHORT).show();
                 e.printStackTrace();
             }

         }

         @Override
         public void onBitmapFailed(Exception e, Drawable errorDrawable) {

         }

         @Override
         public void onPrepareLoad(Drawable placeHolderDrawable) {

         }
     };


     @Override
     protected void onCreate(Bundle savedInstanceState) {
         super.onCreate(savedInstanceState);
         setContentView(R.layout.activity_wallpaper_view);

         rootLayout = (CoordinatorLayout) findViewById(R.id.rootLayout);
         mScaleGestureDetector = new ScaleGestureDetector(this, new ScaleListener());



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

         Initialize();

         fabWallpaper.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 if (isOpen) {

                     setlockscreen.startAnimation(fabclose);
                     sethomescreen.startAnimation(fabclose);
                     fabWallpaper.startAnimation(rotateclockwise);

                     setlockscreen.setClickable(false);
                     sethomescreen.setClickable(false);

                     isOpen = false;
                 } else {
                     setlockscreen.startAnimation(fabopen);
                     sethomescreen.startAnimation(fabopen);
                     fabWallpaper.startAnimation(rotateAnticlockwise);

                     setlockscreen.setClickable(true);
                     sethomescreen.setClickable(true);

                     isOpen = true;


                 }

                 //Picasso.get().load(Utils.wallpapermodel.getImagelink()).into(target);
             }
         });

         setlockscreen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Picasso.get().load(Utils.wallpapermodel.getImagelink()).into(target2);
                 //Toast.makeText(WallpaperView.this, "u click lockscreen", Toast.LENGTH_SHORT).show();

             }
         });

         sethomescreen.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 Picasso.get().load(Utils.wallpapermodel.getImagelink()).into(target1);
                 //Toast.makeText(WallpaperView.this, "u click homescreen", Toast.LENGTH_SHORT).show();

             }
         });

         Picasso.get().load(Utils.wallpapermodel.getImagelink()).into(mWallpaper);

         fabDownload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {

                 String file = UUID.randomUUID().toString() + ".png";

                 AlertDialog.Builder b = new AlertDialog.Builder(WallpaperView.this);
                 b.setMessage("Downloading...");
                 AlertDialog alertDialog = b.create();
                 alertDialog.show();

                 Picasso.get().load(Utils.wallpapermodel.getImagelink())
                         .into(new SaveHelper(getBaseContext(), alertDialog, getApplicationContext().getContentResolver(), file, "Images"));
             }
         });


     }


     private void Initialize() {

         mWallpaper = findViewById(R.id.WallpaperView);
         fabDownload = findViewById(R.id.Fab_download);
         fabWallpaper = findViewById(R.id.Fab_setWallpaper);
         sethomescreen = findViewById(R.id.set_homescreen);
         setlockscreen = findViewById(R.id.set_lockScreen);

         fabopen = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_open);
         fabclose = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fab_close);
         rotateclockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_clockwise);
         rotateAnticlockwise = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.rotate_anticlockwise);


     }

     public boolean onTouchEvent(MotionEvent motionEvent) {
         mScaleGestureDetector.onTouchEvent(motionEvent);
         return true;
     }
     private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
         @Override
         public boolean onScale(ScaleGestureDetector scaleGestureDetector){
             mScaleFactor *= scaleGestureDetector.getScaleFactor();
             mScaleFactor = Math.max(1.0f,
                     Math.min(mScaleFactor, 5.0f));
             mWallpaper.setScaleX(mScaleFactor);
             mWallpaper.setScaleY(mScaleFactor);
             return true;
         }
     }
 }