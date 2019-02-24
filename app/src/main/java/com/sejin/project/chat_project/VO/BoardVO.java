package com.sejin.project.chat_project.VO;


public class BoardVO {

    private int bno;
    private String title,content,writer;
    private String regdate;
    private int viewcnt;
    private String latitude,longitude,address;

    public BoardVO() {}

    public BoardVO(int bno, String title, String content, String writer, String regdate, int viewcnt) {
        this.bno = bno;
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.regdate = regdate;
        this.viewcnt = viewcnt;
    }

    public BoardVO(String title, String content, String writer, String latitude, String longitude, String address) {
        this.title = title;
        this.content = content;
        this.writer = writer;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public int getBno() {
        return bno;
    }

    public void setBno(int bno) {
        this.bno = bno;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getWriter() {
        return writer;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getRegdate() {
        return regdate;
    }

    public void setRegdate(String regdate) {
        this.regdate = regdate;
    }

    public int getViewcnt() {
        return viewcnt;
    }

    public void setViewcnt(int viewcnt) {
        this.viewcnt = viewcnt;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
