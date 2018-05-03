package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/6.
 */

public class BankInfoEntity implements Serializable {

    /**
     * id : 5
     * user_id : 9578
     * bankname : 微信
     * bankid : 123456789
     * province :
     * bankcode;
     * zhi :
     * huming : 林
     * type : 4
     * status : 0
     * addtime : 1511886830
     * uptime :
     * del : 0
     */


    private String id;
    private String user_id;
    private String bankname;
    private String bankid;
    private String province;
    private String zhi;
    private String huming;
    private String type;
    private String status;
    private String addtime;
    private String uptime;
    private String del;
    private String bankcode;

    public String getBankcode() {
        return bankcode;
    }

    public void setBankcode(String bankcode) {
        this.bankcode = bankcode;
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

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }

    public String getBankid() {
        return bankid;
    }

    public void setBankid(String bankid) {
        this.bankid = bankid;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getZhi() {
        return zhi;
    }

    public void setZhi(String zhi) {
        this.zhi = zhi;
    }

    public String getHuming() {
        return huming;
    }

    public void setHuming(String huming) {
        this.huming = huming;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }
}
