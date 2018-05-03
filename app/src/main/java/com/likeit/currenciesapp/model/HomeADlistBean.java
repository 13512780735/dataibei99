package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/13.
 */

public class HomeADlistBean implements Serializable {


    /**
     * id : 23
     * cntitle : app顶部广告
     * img : /pub/sys/ggj/attached/image/20161219/20161219214555_12737.jpg
     * url :
     */

    private String id;
    private String cntitle;
    private String img;
    private String url;

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
