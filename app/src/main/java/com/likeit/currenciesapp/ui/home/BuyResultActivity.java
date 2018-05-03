package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.TipsDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyResultActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.input_bank_type_et)
    EditText inputBankTypeEt;
    //    @BindView(R.id.input_bank_dai_et)
//    EditText inputBankDaiEt;
    @BindView(R.id.input_bank_account_et)
    EditText inputBankAccountEt;
    @BindView(R.id.input_bank_name_et)
    EditText inputBankNameEt;
    @BindView(R.id.bank_type_tv)
    TextView bankTypeTv;
    @BindView(R.id.bank_account_tv)
    TextView bankAccountTv;
    @BindView(R.id.bank_name_tv)
    TextView bankNameTv;
    @BindView(R.id.input_bank_city_et)
    EditText inputBankCityEt;
    @BindView(R.id.input_bank_zhi_et)
    EditText inputBankZhiEt;


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


    @BindView(R.id.input_ps_et)
    EditText inputPsEt;
    @BindView(R.id.go_btn)
    TextView goBtn;
    private String bz;
    private String bankType;
    private String bankAccount;
    private String bankName;
    private UserBankInfo mUserBankInfo;
    private String zhi;
    private String city;


    protected OperateTypes operateType = OperateTypes.BUY;
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

    /**
     * "hbank2": "013國泰世華",//匯入公司台灣銀行行别
     * "hbank3": "010506228901",//匯入公司台灣銀行帳號
     * "hbank4": "蔡亞辰",//匯入公司台灣銀行戶名
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy_result);
        ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
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
        if (modifyOrder) {
            Log.d("TAG", "modifyOrder...");
            bankTypeTv.append(hbank_2 + hbank_6);
            bankNameTv.append(hbank_3);
            bankAccountTv.append(hbank_4);


            /**
             "bank2": "买进",
             "bank3": "joyo",
             "bank4": "摸着",
             "bank5": "买进省",
             "bank6": "我",
             */
            inputBankTypeEt.setText(bank_2);
            inputBankCityEt.setText(bank_3);
            inputBankZhiEt.setText(bank_4);
            inputBankAccountEt.setText(bank_5);
            inputBankNameEt.setText(bank_6);

        } else {
            initData();
            showProgress("Loading...");
        }
        initView();
    }

    private void initData() {
        String url = AppConfig.LIKEIT_ORDER_BEFORE;
        RequestParams params = new RequestParams();
        params.put("type", nowCoinId);
        params.put("ukey", ukey);
        params.put("style", "other");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                // Log.d("TAG",response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status) ) {
                        mUserBankInfo = JSON.parseObject(obj.optString("info"), UserBankInfo.class);
                        Log.d("TAG111", mUserBankInfo.toString());
                        bankTypeTv.append(mUserBankInfo.getBankname() + mUserBankInfo.getZhname());
                        bankNameTv.append(mUserBankInfo.getHuming());
                        bankAccountTv.append(mUserBankInfo.getIdcard());
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

    private void initView() {
        tvHeader.setText("買進貨幣");
        tx11.setText("買進貨幣:" + "人民幣");
        tx12.setText("支付幣別:台幣");
        tx13.setText("類型:買進");

        tx21.setText("金額:" + inputValue);
        tx22.setText("比數:" + nowRate);
        tx23.setText("手續費:" + hand_);

        tx31.setText("實付新台幣金額:" + realGet + "   " + formula);

    }

    @OnClick({R.id.backBtn, R.id.go_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.go_btn:
                bankType = inputBankTypeEt.getText().toString().trim();
                bankAccount = inputBankAccountEt.getText().toString().trim();
                bankName = inputBankNameEt.getText().toString().trim();
                bz = inputPsEt.getText().toString().trim();
//                final String bankDai = inputBankDaiEt.getText().toString().trim();
                zhi = inputBankZhiEt.getText().toString().trim();
                city = inputBankCityEt.getText().toString().trim();

                if (TextUtils.isEmpty(bankType) || TextUtils.isEmpty(bankAccount) || TextUtils.isEmpty(bankName)
                        || TextUtils.isEmpty(zhi) || TextUtils.isEmpty(city)) {
                    showToast("請填寫完整信息!");
                    return;
                }

                TipsDialog tipsDialog = new TipsDialog(mContext);
                tipsDialog.setLeftButt("確定");
                tipsDialog.setRightButt("取消");
                tipsDialog.setTips("請再次確認銀行或支付寶正確與否");
                tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
                    @Override
                    public void onLeftClick() {

                        if (modifyOrder) {
                            editOrder();
                            //HttpMethods.getInstance().modify_order(myRateInfoSubscriber,uKey,orderId, "", bankType, bankName, bankAccount, city, zhi,"","","",bz);
                        } else {
                            // HttpMethods.getInstance().do_order(myRateInfoSubscriber, uKey, "2", nowCoinId, "1", inputValue, "", bz, orderBeforeId, "", bankType, bankName, bankAccount, city, zhi,"","");
                            refer();
                        }
                    }

                    @Override
                    public void onRightClick() {
                    }
                });

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
        params.put("bank5", city);
        params.put("bank6", zhi);
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

    private void refer() {
        String url = AppConfig.LIKEIT_DO_ORDER;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("type", "2");
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
        params.put("bank5", city);
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
}
