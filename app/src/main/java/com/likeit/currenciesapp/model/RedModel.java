package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/1/11.
 */

public class RedModel implements Serializable {
    String name;
    String avatar;
    String money;
    String message;
    String tag;
    Boolean flag;
    String red_id;
    String extra;
    String group_id;
    String rongcloud_id;

    public String getRongcloud_id() {
        return rongcloud_id;
    }

    public void setRongcloud_id(String rongcloud_id) {
        this.rongcloud_id = rongcloud_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getRed_id() {
        return red_id;
    }

    public void setRed_id(String red_id) {
        this.red_id = red_id;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMoney() {
        return money;
    }

    public void setMoney(String money) {
        this.money = money;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public Boolean getFlag() {
        return flag;
    }

    public void setFlag(Boolean flag) {
        this.flag = flag;
    }
}
