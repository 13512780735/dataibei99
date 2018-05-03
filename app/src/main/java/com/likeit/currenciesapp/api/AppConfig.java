package com.likeit.currenciesapp.api;

/**
 * Created by Administrator on 2017/11/2.
 */

public class AppConfig {
    // public static final String IMAGE_URL_HOST = "http://2467.us/api.php/";//测试
    public static final String IMAGE_URL_HOST = "http://2467.cc/api.php/";//正式
    //图片拼接
    public static final String LIKEIT_LOGO1 = "http://2467.cc";//正式
    // public static final String LIKEIT_LOGO1 = "http://2467.us";//测试
    /**
     * 用戶
     */
    //登录接口
    public static final String LIKEIT_LOGIN = IMAGE_URL_HOST + "?m=login&a=login";
    //注册接口
    public static final String LIKEIT_REGISTER = IMAGE_URL_HOST + "?m=login&a=reg";
    //手机验证码接口
    public static final String LIKEIT_SEND_SMS = IMAGE_URL_HOST + "?m=login&a=send_sms";
    //获取注册来源列表
    public static final String LIKEIT_GET_REGFROM = IMAGE_URL_HOST + "?m=login&a=get_regfrom";
    //用户信息接口
    public static final String LIKEIT_GET_INFO = IMAGE_URL_HOST + "?m=user&a=get_info";
    //上传头像
    public static final String LIKEIT_UP_HEADIMG_64 = IMAGE_URL_HOST + "?m=user&a=up_headimg_base64";
    //修改资料接口
    public static final String LIKEIT_USER_EDIT = IMAGE_URL_HOST + "?m=user&a=edit";
    //获取融云用户信息
    public static final String LIKEIT_GET_RONGCLOUDID = IMAGE_URL_HOST + "?m=user&a=get_info_by_rongcloudid";
    //客服搜索用户接口
    public static final String LIKEIT_GET_SEARCH_USER = IMAGE_URL_HOST + "?m=user&a=search_user";
    //客服列表
    public static final String LIKEIT_GET_KEFU = IMAGE_URL_HOST + "?m=user&a=kefu";
    //找回密码
    public static final String LIKEIT_GET_PWd = IMAGE_URL_HOST + "?m=login&a=get_pwd";

    /**
     * 首页
     */
    //首页接口
    public static final String LIKEIT_HOME_ADL = IMAGE_URL_HOST + "?m=index&a=index";
    //获取汇率详情
    public static final String LIKEIT_HUILV = IMAGE_URL_HOST + "?m=index&a=get_huilv";
    //订单详情
    public static final String LIKEIT_ORDER_DETAILS = IMAGE_URL_HOST + "?m=order&a=order_detail";
    //转入账户接口
    public static final String LIKEIT_ORDER_BEFORE = IMAGE_URL_HOST + "?m=order&a=order_before";
    //下单接口
    public static final String LIKEIT_DO_ORDER = IMAGE_URL_HOST + "?m=order&a=do_order";
    //获取用户银行信息
    public static final String LIKEIT_GET_USERBANK = IMAGE_URL_HOST + "?m=order&a=get_userbank";
    //获取好友支付宝账户
    public static final String LIKEIT_GET_ZFB = IMAGE_URL_HOST + "?m=order&a=get_zfb";
    //预购天数接口
    public static final String LIKEIT_GET_YUGOU = IMAGE_URL_HOST + "?m=order&a=get_yugou";
    //最新公告
    public static final String LIKEIT_GET_NOTICE = IMAGE_URL_HOST + "?m=index&a=get_notice";
    /**
     * 赚赚宝
     */
    //获取点数
    public static final String LIKEIT_GET_DIAN = IMAGE_URL_HOST + "?m=order&a=get_dian";
    //点数兑换率(仅人民币)
    public static final String LIKEIT_GET_DIAN_LV = IMAGE_URL_HOST + "?m=order&a=get_dian_lv";
    //点数记录 & 转账记录
    public static final String LIKEIT_GET_DIAN_LOG = IMAGE_URL_HOST + "?m=user&a=dian_log";
    /**
     * 订单
     */
    //交易记录
    public static final String LIKEIT_GET_LIST = IMAGE_URL_HOST + "?m=order&a=get_list";
    //订单详情
    public static final String LIKEIT_ORDER_DETAIL = IMAGE_URL_HOST + "?m=order&a=order_detail";
    //删除订单
    public static final String LIKEIT_DEL_ORDER = IMAGE_URL_HOST + "?m=order&a=del_order";
    //通知汇款
    public static final String LIKEIT_DO_HUIKUAN = IMAGE_URL_HOST + "?m=order&a=do_huikuan";
    //修改订单
    public static final String LIKEIT_EDIT_ORDER = IMAGE_URL_HOST + "?m=order&a=edit_order";
    /**
     * 获取融云用户信息
     */
    public static final String LIKEIT_USER_ID = "http://friend.wbteam.cn" + "/user/";

    /**
     * 用户实名认证
     */
    public static final String LIKEIT_UP_USER_REAL = IMAGE_URL_HOST + "?m=user&a=up_user_real";
    /**
     * 上传身份证照片
     */
    public static final String LIKEIT_UP_HANDHELD_IDCARD_BASE64 = IMAGE_URL_HOST + "?m=user&a=up_real_pic_base64";
    /**
     * 发送预留手机验证码
     */
    public static final String LIKEIT_CHECK_BY_REALPHONE = IMAGE_URL_HOST + "?m=user&a=check_by_realphone";

    //用户银行账号管理
    /**
     * 银行账号列表
     */
    public static final String LIKEIT_GET_BANK = IMAGE_URL_HOST + "?m=user&a=get_bank";
    /**
     * 添加银行账号
     */
    public static final String LIKEIT_ADD_BANK = IMAGE_URL_HOST + "?m=user&a=add_bank";
    /**
     * 修改银行账号
     */
    public static final String LIKEIT_EDIT_BANK = IMAGE_URL_HOST + "?m=user&a=edit_bank";
    /**
     * 删除银行账号
     */
    public static final String LIKEIT_EEL_BANK = IMAGE_URL_HOST + "?m=user&a=del_bank";
    /**
     * 获取转账用户信息
     */
    public static final String LIKEIT_TRANSFER_USER_INFO = IMAGE_URL_HOST + "?m=user&a=transfer_user_info";
    //转账点数
    public static final String LIKEIT_TRANSFER_USER_DIAN = IMAGE_URL_HOST + "?m=user&a=transfer_dian";
    /**
     * 设置支付密码
     */
    public static final String LIKEIT_SET_PAYPWD = IMAGE_URL_HOST + "?m=user&a=set_paypwd";
    /**
     * 检测支付密码
     */
    public static final String LIKEIT_CHECK_PAYPWD = IMAGE_URL_HOST + "?m=user&a=check_paypwd";
    /**
     * 修改支付密码
     */
    public static final String LIKEIT_EDIT_PAYPWD = IMAGE_URL_HOST + "?m=user&a=edit_paypwd";
    /**
     * 找回支付密码
     */
    public static final String LIKEIT_FORGET_PAYPWD = IMAGE_URL_HOST + "?m=user&a=forget_paypwd";
    /**
     * 接收红包
     */
    public static final String LIKEIT_Get_RedBao = IMAGE_URL_HOST + "?m=user&a=get_redbao";
    /**
     * 发送红包
     */
    public static final String LIKEIT_Send_RedBao = IMAGE_URL_HOST + "?m=user&a=send_redbao";
    /**
     * 检测红包状态
     */
    public static final String LIKEIT_Check_RedBao = IMAGE_URL_HOST + "?m=user&a=check_redbao";
    /**
     * 发送群红包
     */
    public static final String LIKEIT_Send_Group_RedBao = IMAGE_URL_HOST + "?m=red&a=send_group_redbao";
    /**
     * 获取群红包
     */
    public static final String LIKEIT_Get_Group_RedBao = IMAGE_URL_HOST + "?m=red&a=get_group_redbao";
    /**
     * 检测群红包
     */
    public static final String LIKEIT_check_Group_RedBao = IMAGE_URL_HOST + "?m=red&a=check_group_redbao";
}
