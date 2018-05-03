package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/19.
 */

public class GreenteahyInfoEntity implements Serializable{

    /**
     * id : 20
     * user_id : 9578
     * type : 1
     * dian : 100
     * other_user_id : 9601
     * remark :
     * scene : app
     * addtime : 1513501383
     * status : 0
     * orderid :
     * uptime :
     * other_user : {"user_id":"9601","truename":"测试员2","is_kefu":0,"did":"0","user_name":"13726041227","addtime":"2017-12-05 01:09:58","logintime":"2017-12-05 01:09:58","dian":"3100.0328","pic":"http://2467.us/pub/images/hytx.jpg","logins":"0"}
    *type_name
     */

    private String id;
    private String user_id;
    private String type;
    private String dian;
    private String other_user_id;
    private String remark;
    private String scene;
    private String addtime;
    private String status;
    private String orderid;
    private String uptime;
    private String type_name;
    private OtherUserBean other_user;

    public String getType_name() {
        return type_name;
    }

    public void setType_name(String type_name) {
        this.type_name = type_name;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDian() {
        return dian;
    }

    public void setDian(String dian) {
        this.dian = dian;
    }

    public String getOther_user_id() {
        return other_user_id;
    }

    public void setOther_user_id(String other_user_id) {
        this.other_user_id = other_user_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getScene() {
        return scene;
    }

    public void setScene(String scene) {
        this.scene = scene;
    }

    public String getAddtime() {
        return addtime;
    }

    public void setAddtime(String addtime) {
        this.addtime = addtime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOrderid() {
        return orderid;
    }

    public void setOrderid(String orderid) {
        this.orderid = orderid;
    }

    public String getUptime() {
        return uptime;
    }

    public void setUptime(String uptime) {
        this.uptime = uptime;
    }

    public OtherUserBean getOther_user() {
        return other_user;
    }

    public void setOther_user(OtherUserBean other_user) {
        this.other_user = other_user;
    }

    public static class OtherUserBean {
        /**
         * user_id : 9601
         * truename : 测试员2
         * is_kefu : 0
         * did : 0
         * user_name : 13726041227
         * addtime : 2017-12-05 01:09:58
         * logintime : 2017-12-05 01:09:58
         * dian : 3100.0328
         * pic : http://2467.us/pub/images/hytx.jpg
         * logins : 0
         */

        private String user_id;
        private String truename;
        private int is_kefu;
        private String did;
        private String user_name;
        private String addtime;
        private String logintime;
        private String dian;
        private String pic;
        private String logins;

        public String getUser_id() {
            return user_id;
        }

        public void setUser_id(String user_id) {
            this.user_id = user_id;
        }

        public String getTruename() {
            return truename;
        }

        public void setTruename(String truename) {
            this.truename = truename;
        }

        public int getIs_kefu() {
            return is_kefu;
        }

        public void setIs_kefu(int is_kefu) {
            this.is_kefu = is_kefu;
        }

        public String getDid() {
            return did;
        }

        public void setDid(String did) {
            this.did = did;
        }

        public String getUser_name() {
            return user_name;
        }

        public void setUser_name(String user_name) {
            this.user_name = user_name;
        }

        public String getAddtime() {
            return addtime;
        }

        public void setAddtime(String addtime) {
            this.addtime = addtime;
        }

        public String getLogintime() {
            return logintime;
        }

        public void setLogintime(String logintime) {
            this.logintime = logintime;
        }

        public String getDian() {
            return dian;
        }

        public void setDian(String dian) {
            this.dian = dian;
        }

        public String getPic() {
            return pic;
        }

        public void setPic(String pic) {
            this.pic = pic;
        }

        public String getLogins() {
            return logins;
        }

        public void setLogins(String logins) {
            this.logins = logins;
        }
    }
}
