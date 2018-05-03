package com.likeit.currenciesapp.ui.Greenteahy;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.loopj.android.http.RequestParams;
import com.pk4pk.baseappmoudle.utils.ArithTool;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DianBuyInputRateActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.type_operate_name)
    TextView typeOperateName;
    @BindView(R.id.type_operate_value)
    TextView typeOperateValue;
    @BindView(R.id.result_type_name)
    TextView resultTypeName;
    @BindView(R.id.now_rate_tv)
    TextView nowRateTv;
    @BindView(R.id.year_rate_tv)
    TextView yearRateTv;
    @BindView(R.id.coin_name_tv)
    TextView coinNameTv;
    @BindView(R.id.bli_tv)
    TextView bliTv;
    @BindView(R.id.input_value_et)
    EditText inputValueEt;
    @BindView(R.id.get_tv)
    TextView getTv;
    @BindView(R.id.real_get_tv)
    TextView realGetTv;



    private DianInfoEntity dianInfoEntity;
    private double rmb_buy_rate = 0;
    private double dianhl = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_dian_buy_input_rate);
       // ButterKnife.bind(this);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        dianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        setContentView(R.layout.activity_dian_buy_input_rate);
        ButterKnife.bind(this);
        initView();
    }


    private void initView() {
        tvHeader.setText("點數計息");
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
                    initBuyValue(inputNum);
                } catch (Exception e) {
                    showToast("請輸入合法數字");
                    initBuyValue(0d);
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
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        JSONObject object = obj.optJSONObject("info");
                        try {
                            dianhl = Double.valueOf(object.optString("lv"));
                            rmb_buy_rate = Double.valueOf(object.optString("buy_huilv"));
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
    }

    private long result_get_value = 0;

    private void initBuyValue(Double inputNum) {
        double tmp_a = ArithTool.mul(ArithTool.mul(inputNum, dianhl), rmb_buy_rate);
        long tmp_b = (long) tmp_a;
        result_get_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
//        result_get_value = (long) ArithTool.mul(ArithTool.mul(inputNum, dianhl), rmb_buy_rate);
        realGetTv.setText(inputNum + "*" + dianhl + "*" + rmb_buy_rate + "=" + result_get_value);
        getTv.setText("" + result_get_value);
    }

    @OnClick({R.id.backBtn, R.id.go_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.go_btn:
                Bundle bundle = new Bundle();
                RateInfoEntity rateInfoEntity = new RateInfoEntity();
//                rateInfoEntity.setBuy(rmb_buy_rate);
//                bundle.putSerializable("rateInfoEntity", rateInfoEntity);
//                bundle.putDouble("dianhl", dianhl);
//                bundle.putDouble("rmb_buy_rate", rmb_buy_rate);
//                bundle.putLong("result_get_value", result_get_value);
//                bundle.putString("realGetTv", realGetTv.getText().toString().trim());
//                bundle.putString("inputValueEt", inputValueEt.getText().toString().trim());


                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);

                rateInfoEntity.setBuy(rmb_buy_rate);
                bundle.putSerializable(HomeFragment.RATE_INFO, rateInfoEntity);

                bundle.putDouble(HomeFragment.NOW_Bl, dianhl);
                bundle.putDouble(HomeFragment.NOW_RATE, rmb_buy_rate);
                bundle.putLong(HomeFragment.REAL_GET, result_get_value);
                bundle.putLong(HomeFragment.HAND, 0);

                bundle.putString(HomeFragment.FORMULA, realGetTv.getText().toString().trim());
                bundle.putString(HomeFragment.INPUT_VALUE, inputValueEt.getText().toString().trim());
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                toActivity(DianBuyResultActivity.class, bundle);
                break;
        }
    }
}
