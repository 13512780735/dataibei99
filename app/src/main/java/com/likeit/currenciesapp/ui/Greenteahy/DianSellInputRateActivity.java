package com.likeit.currenciesapp.ui.Greenteahy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;
import com.pk4pk.baseappmoudle.utils.ArithTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DianSellInputRateActivity extends Container {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.now_rate_tv)
    TextView nowRateTv;
    @BindView(R.id.hand_money_tv)
    TextView handMoneyTv;
    @BindView(R.id.year_rate_tv)
    TextView yearRateTv;
    @BindView(R.id.bli_tv)
    TextView bliTv;
    @BindView(R.id.input_value_et)
    EditText inputValueEt;
    @BindView(R.id.get_tv)
    TextView getTv;
    @BindView(R.id.real_get_tv)
    TextView realGetTv;
    @BindView(R.id.note_tv)
    TextView noteTv;
    @BindView(R.id.tv_balance)
    TextView tv_balance;
    @BindView(R.id.tv_tai)
    TextView tv_tai;


    private DianInfoEntity dianInfoEntity;
    private double rmb_buy_rate;
    private double nowBl = 0;
    private double dianhl = 0;


    private double inputNum = 0d;
    long result_value = 0;
    private RateInfoEntity rateInfoEntity;
    private String balance;
    private String balance02;
    private String bankskey;
    private String taibi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  setContentView(R.layout.activity_dian_sell_input_rate);
        // ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        rmb_buy_rate = Double.valueOf(UtilPreference.getStringValue(mContext, "sold"));
        dianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        bankskey = UtilPreference.getStringValue(mContext, "bankskey");
        setContentView(R.layout.activity_dian_sell_input_rate);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        if ("1".equals(bankskey)) {
            tvHeader.setText("點數提領");
        } else if ("2".equals(bankskey)) {
            tvHeader.setText("點數提現");
        }

        nowRateTv.setText(rmb_buy_rate + "");
        yearRateTv.setText(dianInfoEntity.getNian());
        inputValueEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    if (s.length() == 0) {
                        realGetTv.setText("");
                        getTv.setText("");
                        tv_tai.setText("");
                        return;
                    }

                    String temp = s.toString();
                    int posDot = temp.indexOf(".");
                    if (posDot > 0) {
                        if (temp.length() - posDot - 1 > 2) {
                            s.delete(posDot + 3, posDot + 4);
                        }
                    }

                    Double inputNum = Double.valueOf(s.toString().trim());
                    String dian = dianInfoEntity.getDianshu();
                    balance = dian.substring(0, dian.length() - 1);
                    if (Double.valueOf(balance) - inputNum < 0) {
                        Log.d("TAG858", balance);
                        ToastUtil.showS(mContext, "賬戶余額不足");
                        // initSellValue(0d);
                        s.clear();
                        return;
                    } else if (Double.valueOf(dian.substring(0, dian.length() - 1)) - inputNum >= 0) {
                        initSellValue(inputNum);
                    }
                } catch (Exception e) {
                    showToast("請輸入合法數字");
                    initSellValue(0d);
                    s.clear();
                }
            }
        });
        String url = AppConfig.LIKEIT_GET_DIAN_LV;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG888", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        JSONObject object = obj.optJSONObject("info");
                        try {
                            dianhl = Double.valueOf(object.optString("lv"));
                            // rmb_buy_rate = Double.valueOf(object.optString("buy_huilv"));
                            nowRateTv.setText(rmb_buy_rate + "");
                        } catch (Exception e) {
                            dianhl = 0;
                        }
                        bliTv.setText("1:" + object.optString("lv"));
                    } else {
                        bliTv.setText("未知");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                bliTv.setText("未知");
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
        initHuili();
    }

    private void initHuili() {
        String url = AppConfig.LIKEIT_HUILV;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("huilvid", 2);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", "huilv-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status) && "1".equals(code)) {
                        rateInfoEntity = JSON.parseObject(obj.optString("info"), RateInfoEntity.class);
                        noteTv.setText(getBuySellNotic());
                    } else {
                        ToastUtil.showS(mContext, msg);
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
            }
        });
    }

    private String getBuySellNotic() {
        StringBuilder notic = new StringBuilder();
//        notic.append("注:人民幣匯款不滿");
//        notic.append(rateInfoEntity.getSxf().getNomore());
//        notic.append("元,");
//        notic.append("需加收比例");
//        notic.append(rateInfoEntity.getSxf().getBl());
//        notic.append("%手續費上限");
//        notic.append(rateInfoEntity.getSxf().getMax());
//        notic.append("元,交易");
//        notic.append(rateInfoEntity.getSxf().getNomore());
//        notic.append("元以上不收手續費");
        notic.append("注:人民幣匯款不滿");
        notic.append("500");
        notic.append("元,");
        notic.append("需扣除4元手续费");


        nowBl = ArithTool.div(4, 100d);
        return notic.toString();
    }

    private long result_get_value = 0;


    long tmp_hand_money_Vale = 0;

    private void initSellValue(Double inputNum) {

        if (ArithTool.sub(inputNum, 500) >= 0) {
            tmp_hand_money_Vale = 0;
            handMoneyTv.setText("0");
            double tmp_a = ArithTool.mul(inputNum, dianhl);
            long tmp_b = (long) tmp_a;
            result_get_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
            realGetTv.setText(inputNum + "*" + dianhl + "=" + result_get_value);
            String balance01 = String.valueOf(Double.valueOf(balance) - inputNum);
            tv_balance.setText(String.valueOf(Double.valueOf(balance) + "-" + inputNum + "=" + balance01));
            balance02 = String.valueOf(Double.valueOf(balance) + "-" + inputNum + "=" + balance01);
        } else {
//            tmp_hand_money_Vale = (long) ArithTool.mul(inputNum, nowBl);
//            if (ArithTool.sub(String.valueOf(tmp_hand_money_Vale), rateInfoEntity.getSxf().getMax()) >= 0) {
//                tmp_hand_money_Vale = Long.valueOf(rateInfoEntity.getSxf().getMax());
//            }

            handMoneyTv.setText("4");
            tmp_hand_money_Vale = 4;
            double tmp_a = ArithTool.mul(ArithTool.sub(inputNum, 0), dianhl);
            long tmp_b = (long) tmp_a;
            result_get_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
            realGetTv.setText(("(" + inputNum + ")" + "*" + dianhl + "=" + result_get_value));
            String balance01 = String.valueOf(Double.valueOf(balance) - inputNum - 4);
            tv_balance.setText(String.valueOf(Double.valueOf(balance) + "-" + inputNum + "-" + 4 + "=" + balance01));
            balance02 = String.valueOf(Double.valueOf(balance) + "-" + inputNum + "-" + 4 + "=" + balance01);
        }
        DecimalFormat df = new DecimalFormat(".00");
        getTv.setText(String.valueOf(result_get_value));
        taibi = ("(" + inputNum + ")" + "*" + dianhl + "*" + rmb_buy_rate + "=" + df.format(result_get_value * rmb_buy_rate));
        tv_tai.setText(taibi);

    }

    @OnClick({R.id.backBtn, R.id.go_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.go_btn:
                if (TextUtils.isEmpty(inputValueEt.getText().toString().trim())) {
                    showToast("請輸入提領點數");
                    return;
                }
                if ("1".equals(bankskey)) {
                    Bundle bundle = new Bundle();
                    RateInfoEntity rateInfoEntity = new RateInfoEntity();
                    bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                    bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);

                    rateInfoEntity.setBuy(rmb_buy_rate);
                    bundle.putSerializable(HomeFragment.RATE_INFO, rateInfoEntity);

                    bundle.putDouble(HomeFragment.NOW_Bl, dianhl);
                    bundle.putDouble(HomeFragment.NOW_RATE, rmb_buy_rate);
                    bundle.putLong(HomeFragment.REAL_GET, result_get_value);
                    bundle.putLong(HomeFragment.HAND, tmp_hand_money_Vale);
                    bundle.putString(HomeFragment.Balance, balance02);

                    bundle.putString(HomeFragment.FORMULA, realGetTv.getText().toString().trim());
                    bundle.putString(HomeFragment.INPUT_VALUE, inputValueEt.getText().toString().trim());
                    bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                    bundle.putString(HomeFragment.COIN_ID, "2");
                    bundle.putString("taibi", taibi);
                    toActivity(DianSellResult01Activity.class, bundle);
                } else {
                    Bundle bundle = new Bundle();
                    RateInfoEntity rateInfoEntity = new RateInfoEntity();
                    bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                    bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);

                    rateInfoEntity.setBuy(rmb_buy_rate);
                    bundle.putSerializable(HomeFragment.RATE_INFO, rateInfoEntity);

                    bundle.putDouble(HomeFragment.NOW_Bl, dianhl);
                    bundle.putDouble(HomeFragment.NOW_RATE, rmb_buy_rate);
                    bundle.putLong(HomeFragment.REAL_GET, result_get_value);
                    bundle.putLong(HomeFragment.HAND, tmp_hand_money_Vale);
                    bundle.putString(HomeFragment.Balance, balance02);

                    bundle.putString(HomeFragment.FORMULA, realGetTv.getText().toString().trim());
                    bundle.putString(HomeFragment.INPUT_VALUE, inputValueEt.getText().toString().trim());
                    bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                    bundle.putString(HomeFragment.COIN_ID, "2");
                    // bundle.putString("bankskey", bankskey);
                    bundle.putString("taibi", taibi);
                    bundle.putString("type", "");
                    bundle.putString("bankid", "");
                    bundle.putString("huming", "");
                    bundle.putString("province", "");
                    bundle.putString("bankcode", "");
                    bundle.putString("zhi", "");
                    bundle.putString("bankname", "");
                    bundle.putString("banksId", "");
                    bundle.putString("bankid01", "");
                    toActivity(DianSellResultActivity.class, bundle);
                }
                //toFinish();
                break;
        }
    }
}
