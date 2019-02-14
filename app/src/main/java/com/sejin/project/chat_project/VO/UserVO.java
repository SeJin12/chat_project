package com.sejin.project.chat_project.VO;

import com.google.gson.annotations.SerializedName;

public class UserVO {

    @SerializedName("uemail")
    public String uemail;
    @SerializedName("upw")
    public String upw;
    @SerializedName("uname")
    public String uname;
    @SerializedName("uphone")
    public String uphone;
    @SerializedName("uregion")
    public String uregion;

    public UserVO() {
    }

    public UserVO(String uemail, String upw) {
        this.uemail = uemail;
        this.upw = upw;
    }

    public UserVO(String uemail, String upw, String uname, String uphone, String uregion) {
        this.uemail = uemail;
        this.upw = upw;
        this.uname = uname;
        this.uphone = uphone;
        this.uregion = uregion;
    }

    public String getUemail() {
        return uemail;
    }

    public void setUemail(String uemail) {
        this.uemail = uemail;
    }

    public String getUpw() {
        return upw;
    }

    public void setUpw(String upw) {
        this.upw = upw;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUregion() {
        return uregion;
    }

    public void setUregion(String uregion) {
        this.uregion = uregion;
    }
}
