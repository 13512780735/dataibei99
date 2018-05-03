package com.likeit.currenciesapp.ui.Greenteahy;

import android.content.Intent;
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
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.UserBankInfo;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.ui.me.BankcardActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.TipsDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DianSellResultActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;

//    @BindView(R.id.btn_radio_group)
//    RadioGroup btnRadioGroup;

//    @BindView(R.id.input_bank_account_et)
//    EditText inputBankAccountEt;
//    @BindView(R.id.input_bank_name_et)
//    EditText inputBankNameEt;
//    @BindView(R.id.input_bank_type_et)
//    EditText inputBankTypeEt;
//    @BindView(R.id.input_bank_city_et)
//    EditText inputBankCityEt;
//    @BindView(R.id.input_bank_zhi_et)
//    EditText inputBankZhiEt;

//  @BindView(R.id.item_comm_other_layout)
//    LinearLayout itemCommOtherLayout;
//    @BindView(R.id.item_comm_buy_layout)
//    LinearLayout itemCommBuyLayout;

    //    @BindView(R.id.input_alipay_account_et)
//    EditText inputAlipayAccountEt;
//    @BindView(R.id.input_alipay_name_et)
//    EditText inputAlipayNameEt;
//    @BindView(R.id.btn_alipay)
//    RadioButton btnAlipay;
//    @BindView(R.id.btn_taobao)
//    RadioButton btnTaobao;
//    @BindView(R.id.btn_wechat)
//    RadioButton btnWechat;
//    @BindView(R.id.btn_bank)
//    RadioButton btnBank;
    @BindView(R.id.input_ps_et)
    EditText inputPsEt;
//    @BindView(R.id.input_alipay_passwd_et)
//    EditText inputAlipayPasswdEt;

    @BindView(R.id.tv_select_bank)
    TextView tvSelectBank;//选择银行

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
    @BindView(R.id.tx4_1)
    TextView tx4_1;
    @BindView(R.id.ll_tx5_1)
    LinearLayout ll_tx5_1;
    @BindView(R.id.tx5_1)
    TextView tx5_1;
    private String pty = "0";
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
    private String inputValue01;
    private String bankcode = "";
    private String bankskey;
    private String bankid01;
    private String taibi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_sell_result);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        operateType = (OperateTypes) getIntent().getExtras().getSerializable(HomeFragment.OPERATE_TYPE);
       // bankskey = getIntent().getExtras().getString("bankskey");
       bankskey = UtilPreference.getStringValue(mContext,"bankskey");
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

        type = getIntent().getExtras().getString("type");
        taibi = getIntent().getExtras().getString("taibi");
        bankid = getIntent().getExtras().getString("bankid");
        huming = getIntent().getExtras().getString("huming");
        province = getIntent().getExtras().getString("province");
        bankcode = getIntent().getExtras().getString("bankcode");
        zhi = getIntent().getExtras().getString("zhi");
        bankname = getIntent().getExtras().getString("bankname");
        banksId = getIntent().getExtras().getString("banksId");
        bankid01 = getIntent().getExtras().getString("bankid01");
        Log.d("TAG76786876", type + bankid + huming + province + zhi + bankname + banksId+bankcode);

        Log.d("TAG555", nowRate + "");
//        itemCommBuyLayout.setVisibility(View.VISIBLE);
//        itemCommOtherLayout.setVisibility(View.GONE);
        if (modifyOrder) {
            pty = order_pty;
//            if (TextUtils.equals(order_pty, "0")) {
//                btnBank.setChecked(true);
//            } else if (TextUtils.equals(order_pty, "1")) {
//                btnAlipay.setChecked(true);
//            } else if (TextUtils.equals(order_pty, "2")) {
//                btnTaobao.setChecked(true);
//            } else if (TextUtils.equals(order_pty, "3")) {
//                btnWechat.setChecked(true);
//            }
        }
        initData();
        // initBankInfo();
        showProgress("Loading...");
        initView();

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
//            inputBankTypeEt.setText(bank_2);
//            inputBankCityEt.setText(bank_5);
//            inputBankZhiEt.setText(bank_6);
//            inputBankAccountEt.setText(bank_4);
//            inputBankNameEt.setText(bank_3);
        } else {
            String url = AppConfig.LIKEIT_GET_USERBANK;
            RequestParams params = new RequestParams();
            params.put("ukey", ukey);
            params.put("type", 2);
            params.put("pty", "");
            params.put("type2", 7);
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
                                //inputBankTypeEt.setText(info.optString("bankname"));
                            }
                            if (!TextUtils.isEmpty(info.optString("city"))) {
                                //  inputBankCityEt.setText(info.optString("city"));
                            }
                            if (!TextUtils.isEmpty(info.optString("zhname"))) {
                                // inputBankZhiEt.setText(info.optString("zhname"));
                            }
                            if (!TextUtils.isEmpty(info.optString("idcard"))) {
                                //  inputBankAccountEt.setText(info.optString("idcard"));
                            }
                            if (!TextUtils.isEmpty(info.optString("huming"))) {
                                //  inputBankNameEt.setText(info.optString("huming"));
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
        ll_tx4_1.setVisibility(View.VISIBLE);
        ll_tx5_1.setVisibility(View.VISIBLE);
        //tvHeader.setText("點數計息");
        if ("1".equals(bankskey)) {
            tvHeader.setText("提領");
        } else {
            tvHeader.setText("提現");
        }
        tx11.setText("幣別:" + coin_Name);
//        tx12.setText("支付幣別:台幣");
        tx13.setText("類型:點數提現");

        tx21.setText("提現點數:" + inputValue);
        tx22.setText("兌換率:" + "1=" + (long) nowBl);
        tx23.setText("手續費:" + hand_);

        tx31.setText("實得人民幣:" + realGet + "   " + formula);
        tx5_1.setText("實得台幣:" +taibi);
        tx4_1.setText("余額剩余:" + balance);
        if (!StringUtil.isBlank(type)) {
            // String bankid01 = bankid.substring(bankid.length() - 4, bankid.length());
            if ("1".equals(type)) {
                tvSelectBank.setText("大陸銀行" );
                ll_bankInformation.setVisibility(View.VISIBLE);
                ll_bankdaihao.setVisibility(View.GONE);
                tv_account.setText("帳號：");
                apply_et_account.setText(bankid);
                tv_name.setText("戶名");
                apply_et_name.setText(huming);
                tv_bankName.setText("銀行名稱");
                apply_et_bankName.setText(bankname);
                tv_bankCity.setText("銀行所屬省份");
                apply_et_bankCity.setText(province);
                tv_bankZH.setText("銀行所屬支行");
                apply_et_bankZH.setText(zhi);
            } else if ("2".equals(type)) {
                tvSelectBank.setText("銀行" );
                ll_bankInformation.setVisibility(View.VISIBLE);
                ll_bankCity.setVisibility(View.GONE);
                ll_bankZH.setVisibility(View.GONE);
                tv_bankdaihao.setText("銀行代號：");
                apply_et_bankdaihao.setText(bankcode);
                tv_account.setText("帳號：");
                apply_et_account.setText(bankid);
                tv_name.setText("戶名");
                apply_et_name.setText(huming);
                tv_bankName.setText("銀行名稱/分行");
                apply_et_bankName.setText(bankname);
            } else if ("3".equals(type)) {
                tvSelectBank.setText("支付寶賬號" + "(" + bankid01 + ")");
                ll_bankInformation.setVisibility(View.VISIBLE);
                ll_bankdaihao.setVisibility(View.GONE);
                ll_bankZH.setVisibility(View.GONE);
                ll_bankCity.setVisibility(View.GONE);
                ll_bankName.setVisibility(View.GONE);
                tv_account.setText("帳號：");
                apply_et_account.setText(bankid);
                tv_name.setText("姓名");
                apply_et_name.setText(huming);
            } else if ("4".equals(type)) {
                tvSelectBank.setText("微信賬號" + "(" + bankid01 + ")");
                ll_bankInformation.setVisibility(View.VISIBLE);
                ll_bankdaihao.setVisibility(View.GONE);
                ll_bankZH.setVisibility(View.GONE);
                ll_bankCity.setVisibility(View.GONE);
                ll_bankName.setVisibility(View.GONE);
                tv_account.setText("帳號：");
                apply_et_account.setText(bankid);
                tv_name.setText("姓名");
                apply_et_name.setText(huming);
            }
        }
    }

    @BindView(R.id.ll_bankInformation)
    LinearLayout ll_bankInformation;//银行信息
    @BindView(R.id.ll_bankdaihao)
    LinearLayout ll_bankdaihao;//代号
    @BindView(R.id.tv_bankdaihao)
    TextView tv_bankdaihao;//代号
    @BindView(R.id.apply_et_bankdaihao)
    EditText apply_et_bankdaihao;//代号
    @BindView(R.id.ll_account)
    LinearLayout ll_account;//帐号
    @BindView(R.id.tv_account)
    TextView tv_account;//帐号
    @BindView(R.id.apply_et_account)
    EditText apply_et_account;//帐号
    @BindView(R.id.ll_name)
    LinearLayout ll_name;//户名
    @BindView(R.id.tv_name)
    TextView tv_name;//户名
    @BindView(R.id.apply_et_name)
    EditText apply_et_name;//户名
    @BindView(R.id.ll_bankName)
    LinearLayout ll_bankName;//银行名称
    @BindView(R.id.tv_bankName)
    TextView tv_bankName;//银行名称
    @BindView(R.id.apply_et_bankName)
    EditText apply_et_bankName;//银行名称
    @BindView(R.id.ll_bankCity)
    LinearLayout ll_bankCity;//省份
    @BindView(R.id.tv_bankCity)
    TextView tv_bankCity;//省份
    @BindView(R.id.apply_et_bankCity)
    EditText apply_et_bankCity;//省份
    @BindView(R.id.ll_bankZH)
    LinearLayout ll_bankZH;//支行
    @BindView(R.id.tv_bankZH)
    TextView tv_bankZH;//支行
    @BindView(R.id.apply_et_bankZH)
    EditText apply_et_bankZH;//支行

    private void initPayInfo() {
        if (modifyOrder) {
            /**
             "bank1": "充值测试支付宝账号",
             "bank2": "密码",
             "bank3": "名字",
             */
            if (order_pty.equals(pty)) {
                // inputBankAccountEt.setText(bank_1);
                //inputAlipayPasswdEt.setText(bank_2);
                // inputBankNameEt.setText(bank_3);
            }
        } else {
            String url = AppConfig.LIKEIT_GET_USERBANK;
            RequestParams params = new RequestParams();
            params.put("ukey", ukey);
            params.put("type", 2);
            params.put("pty", pty);
            params.put("type2", 4);
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
                            if (!TextUtils.isEmpty(info.optString("idcard"))) {
                                //inputBankAccountEt.setText(info.optString("idcard"));
                            }
                            if (!TextUtils.isEmpty(info.optString("pay_name"))) {
                                //  inputBankNameEt.setText(info.optString("pay_name"));
                            }

                            if (!TextUtils.isEmpty(info.optString("pay_password"))) {
                                //  inputAlipayPasswdEt.setText(info.optString("pay_password"));
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

    private static final int REQUEST_REGION_PICK = 1;

    @OnClick({R.id.backBtn, R.id.go_btn, R.id.tv_select_bank})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_select_bank:

               // UtilPreference.saveString(mContext, "bankflag", "2");
              // UtilPreference.saveString(mContext, "bankskey", bankskey);
                //toActivity(BankcardActivity.class);
                Intent intent = new Intent(mContext, BankcardActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);

                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
            //    bundle.putString("bankskey", bankskey);
                bundle.putString("taibi", taibi);
                bundle.putString("flag", "2");
                bundle.putString("bankflag", "2");
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                // intent.putExtra("bankskey", bankskey);
                //startActivityForResult(intent, REQUEST_REGION_PICK);
                // startActivity(intent);
                break;
            case R.id.go_btn:
//                bankAccount = inputBankAccountEt.getText().toString().trim();
//                bankName = inputBankNameEt.getText().toString().trim();
//                bankType = inputBankTypeEt.getText().toString().trim();
//                bankCity = inputBankCityEt.getText().toString().trim();
//                bankZhi = inputBankZhiEt.getText().toString().trim();
//                alipayAccount = inputAlipayAccountEt.getText().toString().trim();
//                alipayName = inputAlipayNameEt.getText().toString().trim();
//                alipayPasswd = inputAlipayPasswdEt.getText().toString().trim();
                bz = inputPsEt.getText().toString().trim();
                refer3();//修改选择之后的提交
//                if (!TextUtils.equals("0", pty)) {
//                    if (TextUtils.isEmpty(alipayPasswd) || TextUtils.isEmpty(alipayAccount) || TextUtils.isEmpty(alipayName)) {
//                        showToast("請填寫完整信息!");
//                        return;
//                    }
//                } else {
//                    if (TextUtils.isEmpty(bankType) || TextUtils.isEmpty(bankAccount) || TextUtils.isEmpty(bankName)
//                            || TextUtils.isEmpty(bankCity) || TextUtils.isEmpty(bankZhi)) {
//                        showToast("請填寫完整信息!");
//                        return;
//                    }
//                }
//
//
//                TipsDialog tipsDialog = new TipsDialog(mContext);
//                tipsDialog.setLeftButt("確定");
//                tipsDialog.setRightButt("取消");
//                tipsDialog.setTips("請再次確認銀行或支付寶正確與否");
//
//                if (TextUtils.equals("0", pty)) {
//                    tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            if (modifyOrder) {
//                                // HttpMethods.getInstance().modify_order(myRateInfoSubscriber, uKey, orderId, "", bankType, bankName, bankAccount, bankCity, bankZhi, "", "", "", bz);
//                                editOrder();
//                            } else {
////                                HttpMethods.getInstance().do_order(myRateInfoSubscriber, uKey, "8", nowCoinId, "1", inputValue, "",
////                                        bz, orderBeforeId, "", bankType, bankName, bankAccount, bankCity, bankZhi, "", "", "");
//                                refer();
//                            }
//
//                        }
//
//                        @Override
//                        public void onRightClick() {
//                        }
//                    });
//                } else {
//                    tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
//                        @Override
//                        public void onLeftClick() {
//                            refer2();
//                        }
//
//                        @Override
//                        public void onRightClick() {
//
//                        }
//                    });
//                }
//
//
//                tipsDialog.show();
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
        params.put("user_bankid", mUserBankInfo.getId());
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
                                toFinish();
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

    String type = "";
    String bankid = "";
    String huming = "";
    String province = "";
    String zhi = "";
    String bankname = "";
    String banksId = "";


    private void refer3() {
        bank_1 = apply_et_account.getText().toString();
        huming = apply_et_name.getText().toString();
        bankname = apply_et_bankName.getText().toString();
        province = apply_et_bankCity.getText().toString();
        zhi = apply_et_bankZH.getText().toString();
        bankcode = apply_et_bankdaihao.getText().toString();

        if (TextUtils.isEmpty(bankid)) {
            showToast("請選擇提領帳號!");
            return;
        }

        if ("2".equals(type)) {
            inputValue01 = String.valueOf((Integer.valueOf(inputValue)) * nowRate);
        } else {
            inputValue01 = inputValue;
        }
        Log.d("TAG", inputValue);
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
        params.put("user_bankid", banksId);
        params.put("bank1", bankcode);
        params.put("bank2", bankname);
        params.put("bank3", huming);
        params.put("bank4", bankid);
        params.put("bank5", province);
        params.put("bank6", zhi);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_REGION_PICK) {
            if (data != null) {
                //1.大陆银行，2.台湾银行，3.支付宝，4.微信
                // type = data.getIntExtra("type", 101);
                bankid = data.getStringExtra("bankid");
                String bankid01 = bankid.substring(bankid.length() - 4, bankid.length());
//                if (type == 1) {
//                    tvSelectBank.setText("大陸銀行" + "(" + bankid01 + ")");
//                } else if (type == 2) {
//                    tvSelectBank.setText("臺灣銀行" + "(" + bankid01 + ")");
//                } else if (type == 3) {
//                    tvSelectBank.setText("支付寶賬號" + "(" + bankid01 + ")");
//                } else if (type == 4) {
//                    tvSelectBank.setText("微信賬號" + "(" + bankid01 + ")");
//                }
//
//                huming = data.getStringExtra("huming");
//                province = data.getStringExtra("province");
//                bankcode = data.getStringExtra("bankcode");
//                zhi = data.getStringExtra("zhi");
//                bankname = data.getStringExtra("bankname");
//                banksId = data.getStringExtra("banksId");
//                Log.d("TAG76786876", type + bankid + huming + province + zhi + bankname + banksId);
            }
        }
    }
}
