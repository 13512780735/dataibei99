package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by admin on 2018/5/24.
 */

public class AdsModel implements Serializable{
    String id;
    String cntitle;
    String img;
    String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCntitle() {
        return cntitle;
    }

    public void setCntitle(String cntitle) {
        this.cntitle = cntitle;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
