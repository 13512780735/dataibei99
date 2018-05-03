package com.likeit.currenciesapp.model;


import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/11.
 */

public class DianInfoEntity implements Serializable {

    /**
     * nian : 0.001%
     * dianshu : 0點
     * y_dianshu : 0點
     * total : 0點
     */

    private String nian;
    private String dianshu;
    private String y_dianshu;
    private String total;


    public String getNian() {
        return nian;
    }

    public void setNian(String nian) {
        this.nian = nian;
    }

    public String getDianshu() {
        return dianshu;
    }

    public void setDianshu(String dianshu) {
        this.dianshu = dianshu;
    }

    public String getY_dianshu() {
        return y_dianshu;
    }

    public void setY_dianshu(String y_dianshu) {
        this.y_dianshu = y_dianshu;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

}
