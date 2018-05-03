package com.likeit.currenciesapp.ui.Greenteahy;

import android.support.annotation.IdRes;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.UserBankInfo;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.TipsDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DianSellResult01Activity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.btn_radio_group)
    RadioGroup btnRadioGroup;

    @BindView(R.id.input_bank_account_et)
    EditText inputBankAccountEt;
    @BindView(R.id.input_bank_name_et)
    EditText inputBankNameEt;
    @BindView(R.id.input_bank_type_et)
    EditText inputBankTypeEt;
    @BindView(R.id.input_bank_city_et)
    EditText inputBankCityEt;
    @BindView(R.id.input_bank_zhi_et)
    EditText inputBankZhiEt;

    @BindView(R.id.item_comm_other_layout)
    LinearLayout itemCommOtherLayout;
    @BindView(R.id.item_comm_buy_layout)
    LinearLayout itemCommBuyLayout;

    @BindView(R.id.input_alipay_account_et)
    EditText inputAlipayAccountEt;
    @BindView(R.id.input_alipay_name_et)
    EditText inputAlipayNameEt;
    @BindView(R.id.btn_alipay)
    RadioButton btnAlipay;
    @BindView(R.id.btn_taobao)
    RadioButton btnTaobao;
    @BindView(R.id.btn_wechat)
    RadioButton btnWechat;
    @BindView(R.id.btn_bank)
    RadioButton btnBank;
    @BindView(R.id.input_ps_et)
    EditText inputPsEt;
    @BindView(R.id.input_alipay_passwd_et)
    EditText inputAlipayPasswdEt;
    @BindView(R.id.ll_input_alipay_passwd_et)
    LinearLayout ll_input_alipay_passwd_et;
    @BindView(R.id.ll_taobaofriend)
    LinearLayout ll_taobaofriend;
    @BindView(R.id.alipay_accout_tv)
    TextView alipay_accout_tv;
    @BindView(R.id.alipay_name_tv)
    TextView alipay_name_tv;


    @BindView(R.id.tx1_1)
    TextView tx11;
    @BindView(R.id.tx1_2)
    TextView tx12;
    @BindView(R.id.tx1_3)
    TextView tx13;
    @BindView(R.id.tx2_1)
    TextView tx21;
    @BindView(R.id.tx2_2)
    TextView tx22;
    @BindView(R.id.tx2_3)
    TextView tx23;
    @BindView(R.id.tx3_1)
    TextView tx31;
    @BindView(R.id.ll_tx4_1)
    LinearLayout ll_tx4_1;
    @BindView(R.id.ll_tx5_1)
    LinearLayout ll_tx5_1;
    @BindView(R.id.tx4_1)
    TextView tx4_1;
    @BindView(R.id.tx5_1)
    TextView tx5_1;

    private String pty = "1";
    private String bankAccount, bankName, bankType, bankCity, bankZhi, alipayAccount, alipayName, alipayPasswd;
    private String bz;
    private UserBankInfo mUserBankInfo;

    protected OperateTypes operateType = OperateTypes.PRE;
    //    protected RateInfoEntity rateInfoEntity;
    protected double nowRate = 0;
    protected double nowBl = 0;
    long hand_ = 0;
    String coin_Name = "";
    String inputValue = "";
    long realGet = 0;
    String formula = "";
    protected String nowCoinId = "";
    protected String orderBeforeId = "";
    protected boolean modifyOrder = false;
    protected String orderId = "0";
    protected String hbank_1, hbank_2, hbank_3, hbank_4, hbank_5, hbank_6;
    protected String bank_1, bank_2, bank_3, bank_4, bank_5, bank_6, bank_11, bank_12;
    protected String order_pty = "1";
    protected String alipayGive = "0";
    private String balance;
    private String taibi;
    private String df_name = "";
    private String df_tel = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_sell_result01);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        taibi = getIntent().getExtras().getString("taibi");
        operateType = (OperateTypes) getIntent().getExtras().getSerializable(HomeFragment.OPERATE_TYPE);
        nowBl = getIntent().getExtras().getDouble(HomeFragment.NOW_Bl);
        nowRate = getIntent().getExtras().getDouble(HomeFragment.NOW_RATE);
        coin_Name = getIntent().getExtras().getString(HomeFragment.COIN_NAME);
        inputValue = getIntent().getExtras().getString(HomeFragment.INPUT_VALUE);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        realGet = getIntent().getExtras().getLong(HomeFragment.REAL_GET);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        hand_ = getIntent().getExtras().getLong(HomeFragment.HAND);
        nowCoinId = getIntent().getExtras().getString(HomeFragment.COIN_ID);
        modifyOrder = getIntent().getExtras().getBoolean(HomeFragment.MODIFY_ORDER, false);
        orderId = getIntent().getExtras().getString(HomeFragment.ORDER_ID, "0");
        alipayGive = getIntent().getExtras().getString(HomeFragment.AlipayGive, "0");
        balance = getIntent().getExtras().getString(HomeFragment.Balance);
        hbank_1 = getIntent().getExtras().getString(HomeFragment.HBANK_1, "");
        hbank_2 = getIntent().getExtras().getString(HomeFragment.HBANK_2, "");
        hbank_3 = getIntent().getExtras().getString(HomeFragment.HBANK_4, "");
        hbank_4 = getIntent().getExtras().getString(HomeFragment.HBANK_3, "");
        hbank_5 = getIntent().getExtras().getString(HomeFragment.HBANK_5, "");
        hbank_6 = getIntent().getExtras().getString(HomeFragment.HBANK_6, "");

        bank_1 = getIntent().getExtras().getString(HomeFragment.BANK_1, "");
        bank_2 = getIntent().getExtras().getString(HomeFragment.BANK_2, "");
        bank_3 = getIntent().getExtras().getString(HomeFragment.BANK_3, "");
        bank_4 = getIntent().getExtras().getString(HomeFragment.BANK_4, "");
        bank_5 = getIntent().getExtras().getString(HomeFragment.BANK_5, "");
        bank_6 = getIntent().getExtras().getString(HomeFragment.BANK_6, "");
        bank_11 = getIntent().getExtras().getString(HomeFragment.BANK_11, "");
        bank_12 = getIntent().getExtras().getString(HomeFragment.BANK_12, "");
        order_pty = getIntent().getExtras().getString(HomeFragment.ORDER_PTY, "");

        itemCommBuyLayout.setVisibility(View.GONE);
        ll_taobaofriend.setVisibility(View.GONE);
        itemCommOtherLayout.setVisibility(View.VISIBLE);
        if (modifyOrder) {
            pty = order_pty;
            if (TextUtils.equals(order_pty, "0")) {
                btnBank.setChecked(true);
            } else if (TextUtils.equals(order_pty, "1")) {
                btnAlipay.setChecked(true);
            } else if (TextUtils.equals(order_pty, "2")) {
                btnTaobao.setChecked(true);
            } else if (TextUtils.equals(order_pty, "3")) {
                btnWechat.setChecked(true);
            }
        }

        initData();
        initFriendAlipay();
        initBankInfo();
        showProgress("Loading...");
        initView();

    }

    private void initFriendAlipay() {
        String url = AppConfig.LIKEIT_GET_ZFB;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        JSONObject info = obj.optJSONObject("info");
                        df_name = info.optString("idcard");
                        df_tel = info.optString("huming");
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    private void initData() {
        String url = AppConfig.LIKEIT_ORDER_BEFORE;
        RequestParams params = new RequestParams();
        params.put("type", 2);
        params.put("ukey", ukey);
        params.put("style", "other");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                // Log.d("TAG",response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status) && "1".equals(code)) {
                        mUserBankInfo = JSON.parseObject(obj.optString("info"), UserBankInfo.class);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }

    private void initBankInfo() {
        if (modifyOrder) {
            /**
             "bank2": "1",
             "bank3": "unmoderated5",
             "bank4": "咯嘛4",
             "bank5": "2",
             "bank6": "同3",
             */
            inputBankTypeEt.setText(bank_2);
            inputBankCityEt.setText(bank_5);
            inputBankZhiEt.setText(bank_6);
            inputBankAccountEt.setText(bank_4);
            inputBankNameEt.setText(bank_3);
        } else {
            String url = AppConfig.LIKEIT_GET_USERBANK;
            RequestParams params = new RequestParams();
            params.put("ukey", ukey);
            params.put("type", 2);
            params.put("pty", "");
            params.put("type2",8);
            HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                @Override
                public void success(String response) {
                    Log.d("TAG", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.optString("status");
                        String code = obj.optString("code");
                        String msg = obj.optString("msg");
                        if ("true".equals(status)) {
                            JSONObject info = obj.optJSONObject("info");

                            if (!TextUtils.isEmpty(info.optString("bankname"))) {
                                inputBankTypeEt.setText(info.optString("bankname"));
                            }
                            if (!TextUtils.isEmpty(info.optString("city"))) {
                                inputBankCityEt.setText(info.optString("city"));
                            }
                            if (!TextUtils.isEmpty(info.optString("zhname"))) {
                                inputBankZhiEt.setText(info.optString("zhname"));
                            }
                            if (!TextUtils.isEmpty(info.optString("idcard"))) {
                                inputBankAccountEt.setText(info.optString("idcard"));
                            }
                            if (!TextUtils.isEmpty(info.optString("huming"))) {
                                inputBankNameEt.setText(info.optString("huming"));
                            }

                        } else {
                            showToast(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failed(Throwable e) {

                }
            });
        }
    }

    private void initView() {
        ll_tx5_1.setVisibility(View.VISIBLE);
        ll_tx4_1.setVisibility(View.VISIBLE);
        tx5_1.setText("實得台幣:" + taibi);
        tvHeader.setText("提領");
        tx11.setText("幣別:" + coin_Name);
//        tx12.setText("支付幣別:台幣");
        tx13.setText("類型:點數提領");

        tx21.setText("提領點數:" + inputValue);
        tx22.setText("兌換率:" + "1=" + (long) nowBl);
        tx23.setText("手續費:" + hand_);

        tx31.setText("實得人民幣:" + realGet + "   " + formula);
        tx4_1.setText("余額剩余:" + balance);
        btnRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                inputBankTypeEt.setText("");
                inputBankAccountEt.setText("");
                inputBankNameEt.setText("");
                inputBankTypeEt.setText("");
                inputBankCityEt.setText("");
                inputBankZhiEt.setText("");

                inputAlipayPasswdEt.setText("");
                inputAlipayAccountEt.setText("");
                inputAlipayNameEt.setText("");

                itemCommBuyLayout.setVisibility(View.GONE);
                itemCommOtherLayout.setVisibility(View.VISIBLE);
                switch (checkedId) {

                    case R.id.btn_bank:
                        pty = "0";
                        ll_taobaofriend.setVisibility(View.GONE);
                        itemCommBuyLayout.setVisibility(View.VISIBLE);
                        itemCommOtherLayout.setVisibility(View.GONE);
                        initBankInfo();
                        break;
                    case R.id.btn_alipay:
                        pty = "1";
                        ll_taobaofriend.setVisibility(View.GONE);
                        ll_input_alipay_passwd_et.setVisibility(View.VISIBLE);
                        inputAlipayPasswdEt.setHint("請輸入支付寶密碼,不提供密碼請輸入0");
                        inputAlipayAccountEt.setHint("請輸入支付寶電話");
                        initPayInfo();
                        break;
                    case R.id.btn_taobao:
                        pty = "2";
                        ll_taobaofriend.setVisibility(View.VISIBLE);
                        alipay_accout_tv.setText("好友淘寶帳號:" + df_name);
                        alipay_name_tv.setText("姓名:" + df_tel);
                        ll_input_alipay_passwd_et.setVisibility(View.GONE);
                        //inputAlipayPasswdEt.setHint("請輸入淘寶密碼,不提供密碼請輸入0");
                        inputAlipayAccountEt.setHint("請輸入淘寶賬號");
                        initPayInfo();
                        break;
                    case R.id.btn_wechat:
                        pty = "3";
                        ll_taobaofriend.setVisibility(View.GONE);
                        ll_input_alipay_passwd_et.setVisibility(View.GONE);
                        //inputAlipayPasswdEt.setHint("請輸入微信錢包密碼,不提供密碼請輸入0");
                        inputAlipayAccountEt.setHint("請輸入微信錢包ID");
                        initPayInfo();
                        break;
                }
            }
        });
    }

    private void initPayInfo() {
        if (modifyOrder) {
            /**
             "bank1": "充值测试支付宝账号",
             "bank2": "密码",
             "bank3": "名字",
             */


            if (order_pty.equals(pty)) {
                inputAlipayAccountEt.setText("");
                inputAlipayPasswdEt.setText("");
                inputAlipayNameEt.setText("");
            }
        } else {
            String url = AppConfig.LIKEIT_GET_USERBANK;
            RequestParams params = new RequestParams();
            params.put("ukey", ukey);
            params.put("type", 2);
            params.put("pty", pty);
            params.put("type2", 3);
            HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                @Override
                public void success(String response) {
                    Log.d("TAG", response);
                    try {
                        JSONObject obj = new JSONObject(response);
                        String status = obj.optString("status");
                        String code = obj.optString("code");
                        String msg = obj.optString("msg");
                        if ("true".equals(status)) {
                            JSONObject info = obj.optJSONObject("info");
                            if (!TextUtils.isEmpty(info.optString("pay_idcard"))) {
                                inputAlipayAccountEt.setText(info.optString("pay_idcard"));
                            }
                            if (!TextUtils.isEmpty(info.optString("pay_name"))) {
                                inputAlipayNameEt.setText(info.optString("pay_name"));
                            }

                            if (!TextUtils.isEmpty(info.optString("pay_password"))) {
                                inputAlipayPasswdEt.setText(info.optString("pay_password"));
                            }
                        } else {
                            showToast(msg);
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void failed(Throwable e) {

                }
            });
        }
    }

    @OnClick({R.id.backBtn, R.id.go_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;

            case R.id.go_btn:
                bankAccount = inputBankAccountEt.getText().toString().trim();
                bankName = inputBankNameEt.getText().toString().trim();
                bankType = inputBankTypeEt.getText().toString().trim();
                bankCity = inputBankCityEt.getText().toString().trim();
                bankZhi = inputBankZhiEt.getText().toString().trim();
                alipayAccount = inputAlipayAccountEt.getText().toString().trim();
                alipayName = inputAlipayNameEt.getText().toString().trim();
                alipayPasswd = inputAlipayPasswdEt.getText().toString().trim();
                bz = inputPsEt.getText().toString().trim();


                if (!TextUtils.equals("0", pty)) {
                    if (TextUtils.isEmpty(alipayAccount) || TextUtils.isEmpty(alipayName)) {
                        showToast("請填寫完整信息!");
                        return;
                    }
                } else {
                    if (TextUtils.isEmpty(bankType) || TextUtils.isEmpty(bankAccount) || TextUtils.isEmpty(bankName)
                            || TextUtils.isEmpty(bankCity) || TextUtils.isEmpty(bankZhi)) {
                        showToast("請填寫完整信息!");
                        return;
                    }
                }


                TipsDialog tipsDialog = new TipsDialog(mContext);
                tipsDialog.setLeftButt("確定");
                tipsDialog.setRightButt("取消");
                tipsDialog.setTips("請再次確認銀行或支付寶正確與否");

                if (TextUtils.equals("0", pty)) {
                    tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            if (modifyOrder) {
                                // HttpMethods.getInstance().modify_order(myRateInfoSubscriber, uKey, orderId, "", bankType, bankName, bankAccount, bankCity, bankZhi, "", "", "", bz);
                                editOrder();
                            } else {
//                                HttpMethods.getInstance().do_order(myRateInfoSubscriber, uKey, "8", nowCoinId, "1", inputValue, "",
//                                        bz, orderBeforeId, "", bankType, bankName, bankAccount, bankCity, bankZhi, "", "", "");
                                refer();
                            }

                        }

                        @Override
                        public void onRightClick() {
                        }
                    });
                } else {
                    tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
                        @Override
                        public void onLeftClick() {
                            refer2();
                        }

                        @Override
                        public void onRightClick() {

                        }
                    });
                }


                tipsDialog.show();
                break;
        }
    }

    private void editOrder() {
        String url = AppConfig.LIKEIT_EDIT_ORDER;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("id", orderId);
        params.put("bank1", "");
        params.put("bank2", bankType);
        params.put("bank3", bankName);
        params.put("bank4", bankAccount);
        params.put("bank5", bankCity);
        params.put("bank6", bankZhi);
        params.put("bank11", "");
        params.put("bank12", "");
        params.put("df_name", df_name);
        params.put("df_tel", df_tel);
        params.put("pty", "");
        params.put("bz", bz);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        showToast("修改成功");
                        toActivityFinish(MainActivity.class);
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    private void refer2() {
        String url = AppConfig.LIKEIT_DO_ORDER;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("type", "8");
        params.put("htype1", nowCoinId);
        params.put("htype2", "1");
        params.put("money", inputValue);
        params.put("tel", UtilPreference.getStringValue(mContext, "phone"));
        params.put("bz", bz);
        params.put("hbankid", mUserBankInfo.getId());
        params.put("bank1", alipayAccount);
        params.put("bank2", alipayPasswd);
        params.put("bank3", alipayName);
        params.put("bank4", "");
        params.put("bank5", "");
        params.put("bank6", "");
        params.put("bank11", "");
        params.put("bank12", "");
        params.put("pty", pty);
        params.put("dt1", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG88", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        TipsDialog tipsDialog = new TipsDialog(mContext);

                        tipsDialog.setRightButt("確定");
                        tipsDialog.sigleButt();
                        tipsDialog.setTips("提交成功");
                        tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
                            @Override
                            public void onRightClick() {
                                //toFinish();
                                toActivityFinish(MainActivity.class);
                            }

                            @Override
                            public void onLeftClick() {

                            }
                        });
                        tipsDialog.show();

                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    private void refer() {
        String url = AppConfig.LIKEIT_DO_ORDER;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("type", "8");
        params.put("htype1", nowCoinId);
        params.put("htype2", "1");
        params.put("money", inputValue);
        params.put("tel", UtilPreference.getStringValue(mContext, "phone"));
        params.put("bz", bz);
        params.put("hbankid", mUserBankInfo.getId());
        params.put("bank1", "");
        params.put("bank2", bankType);
        params.put("bank3", bankName);
        params.put("bank4", bankAccount);
        params.put("bank5", bankCity);
        params.put("bank6", bankZhi);
        params.put("bank11", "");
        params.put("bank12", "");
        params.put("pty", "");
        params.put("dt1", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG88", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        showToast("送出成功");
                        toActivityFinish(MainActivity.class);
                    } else {
                        showToast(msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }
}
