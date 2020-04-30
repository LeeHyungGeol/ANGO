package com.e.ango.API.Play;

public class PlayObject {

    public String addr1;
    public String addr2;
    public String cat2;
    public String cat3;
    public long contentid;
    public long dist;//
    public String firstimage2;
    public String title;
    public double mapx;
    public double mapy;


    public String categoryName;
    public String preferCategoryId;

    public PlayObject() { }

    public PlayObject(String addr1, String addr2, String cat2, String cat3, long contentid, long dist, String firstimage2, String title, String categoryName) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contentid = contentid;
        this.dist = dist;
        this.firstimage2 = firstimage2;
        this.title = title;
        this.categoryName = categoryName;
    }

    public PlayObject(String addr1, String addr2, String cat2, String cat3, long contentid, String firstimage2, String title, double mapx, double mapy, String categoryName, String preferCategoryId) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contentid = contentid;
        this.firstimage2 = firstimage2;
        this.title = title;
        this.mapx = mapx;
        this.mapy = mapy;
        this.categoryName = categoryName;
        this.preferCategoryId = preferCategoryId;
    }

    public PlayObject(String addr1, String addr2, String cat2, String cat3, long contentid, long dist, String firstimage2, String title, String categoryName, double mapx, double mapy) {
        this.addr1 = addr1;
        this.addr2 = addr2;
        this.cat2 = cat2;
        this.cat3 = cat3;
        this.contentid = contentid;
        this.dist = dist;
        this.firstimage2 = firstimage2;
        this.title = title;
        this.categoryName = categoryName;
        this.mapx = mapx;
        this.mapy = mapy;
    }


    public String getPreferCategoryId() {
        return preferCategoryId;
    }

    public void setPreferCategoryId(String preferCategoryId) {
        this.preferCategoryId = preferCategoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }


    public void setCat2(String cat2) {
        this.cat2 = cat2;
    }

    public void setCat3(String cat3) {
        this.cat3 = cat3;
    }

    public void setContentid(long contentid) {
        this.contentid = contentid;
    }


    public void setDist(long dist) {
        this.dist = dist;
    }

    public void setFirstimage2(String firstimage2) {
        this.firstimage2 = firstimage2;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAddr1() {
        return addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public String getCat2() {
        return cat2;
    }

    public String getCat3() {
        return cat3;
    }

    public long getContentid() {
        return contentid;
    }

    public long getDist() {
        return dist;
    }

    public String getFirstimage2() {
        return firstimage2;
    }

    public String getTitle() {
        return title;
    }

    public double getMapx() {
        return mapx;
    }

    public void setMapx(double mapx) {
        this.mapx = mapx;
    }

    public double getMapy() {
        return mapy;
    }

    public void setMapy(double mapy) {
        this.mapy = mapy;
    }
}