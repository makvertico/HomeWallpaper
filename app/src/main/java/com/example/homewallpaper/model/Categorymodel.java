package com.example.homewallpaper.model;

public class Categorymodel {

    private String imagelink;
    private String name;

    public Categorymodel() {
    }

    private Categorymodel(String name, String imagelink){
        this.name = name;
        this.imagelink = imagelink;
    }



    public String getImagelink() {
        return imagelink;
    }


    public void setImagelink(String imagelink) {
        this.imagelink = imagelink;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
