package com.likeit.currenciesapp.model;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/11/7.
 */

public class LoginUserInfoEntity implements Serializable {

    /**
     * user_id : 8512
     * truename : ss
     * getuicid : 605aa616ea81b6a627cb1731e1395c98
     * idcard :
     * tel : 13512780735
     * is_kefu : 0
     * address :
     * job :
     * blanklist : 0
     * audit : 1
     * work : 0
     * del : 0
     * did : 0
     * up : 0.020       //优惠汇率：买进
     * down : 0.000       //优惠汇率：卖出
     * s1 : 0.020       //优惠汇率：充值
     * s2 : 0.160        //优惠汇率：代付
     * s3 : 0.002        //优惠汇率：预购
     * s4 : 0.200        //优惠汇率：代购
     * s5 : 0.000        //支付寶手續費設定，不满 s5 元,需收 s6 元
     * s6 : 0.000         //支付寶手續費設定，不满 s5 元,需收 s6 元
     * user_name : 13512780735
     * addtime : 2017-05-16 17:23:10
     * logintime : 2017-11-07 14:51:51
     * dian : 0.0000
     * pic : /pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg,/pub/images/hytx.jpg
     * is_rongcloud : 1
     * rongcloud_token : GzyStPgmxPY8Gwr/AbyrYamdRvU1nH3CRDOVuKN8IvsqLj6A36AfuwXfPBVFwx7dLG82dyj0Uy6ov7yzktkSPQ==
     * logins : 28
     * rongcloud_id：RLktZAQHB
     * rongcloud_cookie
     */

    private String user_id;
    private String truename;
    private String getuicid;
    private String idcard;
    private String tel;
    private int is_kefu;
    private String address;
    private String job;
    private String blanklist;
    private String audit;
    private String work;
    private String del;
    private String did;
    private String up;
    private String down;
    private String s1;
    private String s2;
    private String s3;
    private String s4;
    private String s5;
    private String s6;
    private String user_name;
    private String addtime;
    private String logintime;
    private String dian;
    private String pic;
    private String is_rongcloud;
    private String rongcloud_token;
    private String logins;
    private String rongcloud_id;
    private String rongcloud_cookie;

    public String getRongcloud_cookie() {
        return rongcloud_cookie;
    }

    public void setRongcloud_cookie(String rongcloud_cookie) {
        this.rongcloud_cookie = rongcloud_cookie;
    }

    public String getRongcloud_id() {
        return rongcloud_id;
    }

    public void setRongcloud_id(String rongcloud_id) {
        this.rongcloud_id = rongcloud_id;
    }

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

    public String getGetuicid() {
        return getuicid;
    }

    public void setGetuicid(String getuicid) {
        this.getuicid = getuicid;
    }

    public String getIdcard() {
        return idcard;
    }

    public void setIdcard(String idcard) {
        this.idcard = idcard;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public int getIs_kefu() {
        return is_kefu;
    }

    public void setIs_kefu(int is_kefu) {
        this.is_kefu = is_kefu;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getJob() {
        return job;
    }

    public void setJob(String job) {
        this.job = job;
    }

    public String getBlanklist() {
        return blanklist;
    }

    public void setBlanklist(String blanklist) {
        this.blanklist = blanklist;
    }

    public String getAudit() {
        return audit;
    }

    public void setAudit(String audit) {
        this.audit = audit;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getDel() {
        return del;
    }

    public void setDel(String del) {
        this.del = del;
    }

    public String getDid() {
        return did;
    }

    public void setDid(String did) {
        this.did = did;
    }

    public String getUp() {
        return up;
    }

    public void setUp(String up) {
        this.up = up;
    }

    public String getDown() {
        return down;
    }

    public void setDown(String down) {
        this.down = down;
    }

    public String getS1() {
        return s1;
    }

    public void setS1(String s1) {
        this.s1 = s1;
    }

    public String getS2() {
        return s2;
    }

    public void setS2(String s2) {
        this.s2 = s2;
    }

    public String getS3() {
        return s3;
    }

    public void setS3(String s3) {
        this.s3 = s3;
    }

    public String getS4() {
        return s4;
    }

    public void setS4(String s4) {
        this.s4 = s4;
    }

    public String getS5() {
        return s5;
    }

    public void setS5(String s5) {
        this.s5 = s5;
    }

    public String getS6() {
        return s6;
    }

    public void setS6(String s6) {
        this.s6 = s6;
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

    public String getIs_rongcloud() {
        return is_rongcloud;
    }

    public void setIs_rongcloud(String is_rongcloud) {
        this.is_rongcloud = is_rongcloud;
    }

    public String getRongcloud_token() {
        return rongcloud_token;
    }

    public void setRongcloud_token(String rongcloud_token) {
        this.rongcloud_token = rongcloud_token;
    }

    public String getLogins() {
        return logins;
    }

    public void setLogins(String logins) {
        this.logins = logins;
    }
}
