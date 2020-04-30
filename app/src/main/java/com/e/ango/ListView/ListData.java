package com.e.ango.ListView;

import android.graphics.Bitmap;

public class ListData {
    private String title;
    private String summary;
    private String address;
    private Bitmap bitmap;
    private String categoryId;
    private long contentId;

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public long getContentId() {
        return contentId;
    }

    public void setContentId(long contentId) {
        this.contentId = contentId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Bitmap getBitmap() { return bitmap; }

    public void setBitmap(Bitmap bitmap) { this.bitmap = bitmap; }
}
