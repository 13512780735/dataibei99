package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/9.
 */

public class PreRateInfoEntity implements Serializable {

    /**
     * id : 4
     * days : 10
     * zvs : 4.65
     * date : 2017-01-25
     * minmoney : 2000
     */

    private String id;
    private String days;
    private double zvs;
    private String date;
    private String minmoney;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDays() {
        return days;
    }

    public void setDays(String days) {
        this.days = days;
    }

    public double getZvs() {
        return zvs;
    }

    public void setZvs(double zvs) {
        this.zvs = zvs;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getMinmoney() {
        return minmoney;
    }

    public void setMinmoney(String minmoney) {
        this.minmoney = minmoney;
    }
}
