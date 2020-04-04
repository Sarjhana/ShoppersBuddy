package com.example.shoppersbuddy.ui;

public class Stores {
    private String brand_image;
    private String brand_name;
    private String contact;
    private String content;
    private String location;
    private String map_img;
    private String offer;
    private String timings;

    public Stores(){

    }

    public Stores(String brand_image, String brand_name, String contact, String content, String location, String map_img,String offer, String timings) {
        this.brand_image = brand_image;
        this.brand_name = brand_name;
        this.contact = contact;
        this.content = content;
        this.location = location;
        this.map_img = map_img;
        this.offer = offer;
        this.timings = timings;

    }

    public String getOffer() {
        return offer;
    }

    public void setOffer(String offer) {
        this.offer = offer;
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

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    public String getMap_img() {
        return map_img;
    }

    public void setMap_img(String map_img) {
        this.map_img = map_img;
    }

    public String getTimings() {
        return timings;
    }

    public void setTimings(String timings) {
        this.timings = timings;
    }
}
