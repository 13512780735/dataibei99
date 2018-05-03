package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by admin on 2018/3/10.
 */

public class RonguserModel implements Serializable{

    /**
     * rongcloud : 2
     * truename : 黃昌元
     * pic : img/default.jpg
     * rongcloud_token : no6yPZBuR11feSgMYaFoOyBOcjOHB/+X0Pmt+NJ7pAP8FfNhraN9QqVDuyyyxveeKykMvPWdDDdHwFQOq0IX0w==
     */

    private String rongcloud;
    private String truename;
    private String pic;
    private String rongcloud_token;

    public String getRongcloud() {
        return rongcloud;
    }

    public void setRongcloud(String rongcloud) {
        this.rongcloud = rongcloud;
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

    public String getRongcloud_token() {
        return rongcloud_token;
    }

    public void setRongcloud_token(String rongcloud_token) {
        this.rongcloud_token = rongcloud_token;
    }
}
