package com.example.homewallpaper.model;

public class Wallpapermodel {
    public Wallpapermodel() {
    }

    public String categoryid,imagelink;

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getImagelink() {
        return imagelink;
    }

    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public Wallpapermodel(String categoryid, String imagelink) {
        this.categoryid = categoryid;
        this.imagelink = imagelink;
    }
}
