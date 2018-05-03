package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/13.
 */

public class CurrencyBean implements Serializable{


    /**
     * id : 3
     * name : 美元
     * pic : null
     * is_kaipan : false
     */

    private String id;
    private String name;
    private String pic;
    private boolean is_kaipan;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public boolean isIs_kaipan() {
        return is_kaipan;
    }

    public void setIs_kaipan(boolean is_kaipan) {
        this.is_kaipan = is_kaipan;
    }
}
