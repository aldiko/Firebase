package com.example.firebasechatapp;

public class MessageModel {
    String text;
    String name;
    String imageurl;

    public MessageModel() {
    }

    public MessageModel(String text, String name, String imageurl) {
        this.text = text;
        this.name = name;
        this.imageurl = imageurl;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageurl() {
        return imageurl;
    }

    public void setImageurl(String imageurl) {
        this.imageurl = imageurl;
    }
}
