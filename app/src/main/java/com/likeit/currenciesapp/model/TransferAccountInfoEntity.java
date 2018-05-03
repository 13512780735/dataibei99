package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/9.
 */

public class TransferAccountInfoEntity implements Serializable{

    /**
     * user_name : 13726041226
     * truename : 测试员
     * pic : /pub/images/hytx.jpg
     * id : 3
     * user_id : 9578
     * name : 林
     * cardid : 123456
     * bankid : 123456
     * tel : 13726041226
     * handheld_idcard : /img/pic/9578/9578_handheld_idcard_1511802890.png
     * idcard_front : /img/pic/9578/9578_idcard_front_1511802918.png
     * idcard_reverse : /img/pic/9578/9578_idcard_reverse_1511802932.png
     * real_status : 0
     * addtime : 1511803223
     */

    private String user_name;
    private String truename;
    private String pic;
    private String id;
    private String user_id;
    private String name;
    private String cardid;
    private String bankid;
    private String tel;
    private String handheld_idcard;
    private String idcard_front;
    private String idcard_reverse;
    private String real_status;
    private String addtime;

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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCardid() {
        return cardid;
    }

    public void setCardid(String cardid) {
        this.cardid = cardid;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getHandheld_idcard() {
        return handheld_idcard;
    }

    public void setHandheld_idcard(String handheld_idcard) {
        this.handheld_idcard = handheld_idcard;
    }

    public String getIdcard_front() {
        return idcard_front;
    }

    public void setIdcard_front(String idcard_front) {
        this.idcard_front = idcard_front;
    }

    public String getIdcard_reverse() {
        return idcard_reverse;
    }

    public void setIdcard_reverse(String idcard_reverse) {
        this.idcard_reverse = idcard_reverse;
    }

    public String getReal_status() {
        return real_status;
    }

    public void setReal_status(String real_status) {
        this.real_status = real_status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }
}
