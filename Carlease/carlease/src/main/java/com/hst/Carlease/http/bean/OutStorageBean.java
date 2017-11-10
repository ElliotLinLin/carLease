package com.hst.Carlease.http.bean;

/**
 * Author:lsh
 * Version: 1.0
 * Description:
 * Date: 2017/10/19
 */

public class OutStorageBean {


    String imagePath;

    String title;


    public OutStorageBean(String imagePath, String title) {
        this.imagePath = imagePath;
        this.title = title;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
