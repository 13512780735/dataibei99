package com.likeit.currenciesapp.ui.Greenteahy;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
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

public class DianBuyResultActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.input_bank_type_et)
    EditText inputBankTypeEt;
    @BindView(R.id.input_bank_dai_et)
    EditText inputBankDaiEt;
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

    @BindView(R.id.input_ps_et)
    EditText inputPsEt;
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


    private UserBankInfo mUserBankInfo;
    private String bankType, bankAccount, bankName, bz, zhi, city;


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
    private String pty = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dian_buy_result);
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

            initData();
            initInfo();
            showProgress("Loading...");
        }
        initView();
    }

    private void initInfo() {
        String url = AppConfig.LIKEIT_GET_USERBANK;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("type", nowCoinId);
        params.put("pty", "");
        params.put("type2", "6");
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

    private void initData() {
        String url = AppConfig.LIKEIT_ORDER_BEFORE;
        RequestParams params = new RequestParams();
        params.put("type", nowCoinId);
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
                        Log.d("TAG111", mUserBankInfo.toString());
                        orderBeforeId = mUserBankInfo.getId();
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
        tvHeader.setText("賺賺寶");
        tx11.setText("買進幣別:" + "人民幣");
        tx12.setText("支付幣別:台幣");
        tx13.setText("類型:賺賺寶");
        tx21.setText("金額:" + inputValue);
        tx22.setText("比數:" + nowRate);
        tx23.setText("兌換率:" + "1=" + (long) nowBl);

        tx31.setText("實付新台幣金額:" + realGet + "   " + formula);
    }

    @OnClick({R.id.go_btn, R.id.backBtn})
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
                            // HttpMethods.getInstance().modify_order(myRateInfoSubscriber,uKey,orderId,"", bankType, bankName, bankAccount, city, zhi, "", "","",bz);
                        } else {
                            //HttpMethods.getInstance().do_order(myRateInfoSubscriber, uKey, "6", nowCoinId, "1", inputValue, "", bz, orderBeforeId, "", bankType, bankName, bankAccount, city, zhi, "", "");
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
        params.put("type", "6");
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
