package com.example.shoppersbuddy.ui;

public class StoresItem {

    private String brand_image;
    private String brand_name;
    //private String content;
    private String location;

    public StoresItem(){

    }

    public StoresItem(String brand_image, String brand_name, String location) {
        this.brand_image = brand_image;
        this.brand_name = brand_name;
      //  this.content = content;
        this.location = location;
    }

    public String getBrand_image() {
        return brand_image;
    }

    public void setBrand_image(String brand_image) {
        this.brand_image = brand_image;
    }

    public String getBrand_name() {
        return brand_name;
    }

    public void setBrand_name(String brand_name) {
        this.brand_name = brand_name;
    }

//    public String getContent() {
//        return content;
//    }
//
//    public void setContent(String content) {
//        this.content = content;
//    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
