package com.duanqu.Idea.bean;

/**
 * Created by Me on 2017/4/7.
 */

public class BaiSiBean {
    private String maxtime;
    private String text;
    private String name;
    private String headurl;
    private String image;
    private String videourl;
    private String width;
    private String height;
    private int radio;

    public int getRadio() {
        return radio;
    }

    public void setRadio(int radio) {
        this.radio = radio;
    }

    public String getHeight() {
        return height;
    }

    public String getWidth() {
        return width;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getMaxtime() {
        return maxtime;
    }

    public void setMaxtime(String maxtime) {
        this.maxtime = maxtime;
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

    public String getHeadurl() {
        return headurl;
    }

    public void setHeadurl(String headurl) {
        this.headurl = headurl;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getVideourl() {
        return videourl;
    }

    public void setVideourl(String videourl) {
        this.videourl = videourl;
    }

    @Override
    public String toString() {
        return "BaiSiBean{" +
                "maxtime='" + maxtime + '\'' +
                ", text='" + text + '\'' +
                ", name='" + name + '\'' +
                ", headurl='" + headurl + '\'' +
                ", image='" + image + '\'' +
                ", videourl='" + videourl + '\'' +
                ", width='" + width + '\'' +
                ", height='" + height + '\'' +
                '}';
    }
}
