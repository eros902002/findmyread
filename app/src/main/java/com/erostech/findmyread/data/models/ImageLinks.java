package com.erostech.findmyread.data.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by erosgarciaponte on 15/05/2017.
 */

public class ImageLinks {
    @SerializedName("smallThumbnail")
    @Expose
    private String smallThumbnail;
    @SerializedName("thumbnail")
    @Expose
    private String thumbnail;

    public String getSmallThumbnail() {
        return smallThumbnail;
    }

    public void setSmallThumbnail(String smallThumbnail) {
        this.smallThumbnail = smallThumbnail;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }
}
