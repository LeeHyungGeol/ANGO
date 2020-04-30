package com.e.ango.Review;

public class DetailInfoPlayObject {
    //DetailInfoObject
    private String title;
    private String homepage;
    private String tel;
    private String overview;
    private String addr1;
    private String addr2;

    public DetailInfoPlayObject() {
    }

    public DetailInfoPlayObject(String title, String homepage, String tel, String overview, String addr1, String addr2) {
        this.title = title;
        this.homepage = homepage;
        this.tel = tel;
        this.overview = overview;
        this.addr1 = addr1;
        this.addr2 = addr2;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getHomepage() {
        return homepage;
    }

    public void setHomepage(String homepage) {
        this.homepage = homepage;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public String getAddr1() {
        return addr1;
    }

    public void setAddr1(String addr1) {
        this.addr1 = addr1;
    }

    public String getAddr2() {
        return addr2;
    }

    public void setAddr2(String addr2) {
        this.addr2 = addr2;
    }
}
