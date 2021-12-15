package com.example.homewallpaper.Helper;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.widget.Toast;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.lang.ref.WeakReference;

public class SaveHelper implements Target {
    public SaveHelper(Context mcontext,AlertDialog alertDialog, ContentResolver contentResolver, String name , String desc) {
        this.mcontext = mcontext;
        alertDialogWeakReference = new WeakReference<AlertDialog>(alertDialog);
        contentResolverWeakReference = new WeakReference<ContentResolver>(contentResolver);
        this.name = name;
        this.desc = desc;
    }

    Context mcontext;
    WeakReference<AlertDialog> alertDialogWeakReference;
    WeakReference<ContentResolver> contentResolverWeakReference;
    String name,desc;
    @Override
    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
        ContentResolver c = contentResolverWeakReference.get();
        AlertDialog aD = alertDialogWeakReference.get();

        if (c!=null){

            MediaStore.Images.Media.insertImage(c,bitmap,name,desc);
            aD.dismiss();
            Toast.makeText(mcontext, "Image Downloaded ", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBitmapFailed(Exception e, Drawable errorDrawable) {
        Toast.makeText(mcontext,e.getMessage(),Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPrepareLoad(Drawable placeHolderDrawable) {

    }
}
