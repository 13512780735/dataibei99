package com.likeit.currenciesapp.ui.chat.redMessage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/5.
 */

public class RedMemberModel implements Serializable {


    /**
     * id : 79
     * red_id : 502
     * rongcloud_id : zldCrJx2g
     * user_id : 9600
     * money : 2.20
     * addtime : 2018-02-06 11:12:43
     * luck : 0
     * "pic": "http://2467.us/img/pic/9600/9600_headimg_1513646007.png"
     * user_name : 13512780735
     * truename : 高德
     */

    private String id;
    private String red_id;
    private String rongcloud_id;
    private String user_id;
    private String money;
    private String addtime;
    private String luck;
    private String user_name;
    private String truename;
    private String pic;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }

    public String getRongcloud_id() {
        return rongcloud_id;
    }

    public void setRongcloud_id(String rongcloud_id) {
        this.rongcloud_id = rongcloud_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getLuck() {
        return luck;
    }

    public void setLuck(String luck) {
        this.luck = luck;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getTruename() {
        return truename;
    }

    public void setTruename(String truename) {
        this.truename = truename;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }
}
