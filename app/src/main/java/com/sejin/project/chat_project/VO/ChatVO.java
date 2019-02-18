package com.sejin.project.chat_project.VO;

public class ChatVO {
    private String userName,message;
    private String date;

    public ChatVO() {}

    public ChatVO(String userName, String message, String date) {
        this.userName = userName;
        this.message = message;
        this.date = date;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
