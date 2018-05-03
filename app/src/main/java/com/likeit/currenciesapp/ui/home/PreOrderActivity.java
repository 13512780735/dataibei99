package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.PreRateInfoEntity;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.views.PreDayDialog;
import com.loopj.android.http.RequestParams;
import com.pk4pk.baseappmoudle.utils.ArithTool;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PreOrderActivity extends Container implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.ll_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;

    @BindView(R.id.now_rate_tv)
    TextView tvNowRate;
    @BindView(R.id.input_value_et)
    EditText inputValueEt;
    @BindView(R.id.get_tv)
    TextView getTv;
    @BindView(R.id.real_get_tv)
    TextView realGetTv;
    @BindView(R.id.hand_money_tv)
    TextView handMoneyTv;
    @BindView(R.id.give_money_tv)
    TextView giveMoneyTv;
    @BindView(R.id.days_tv)
    TextView daysTv;
    @BindView(R.id.pre_rate_layout)
    LinearLayout preRateLayout;

    private CoinTypes coin_type = CoinTypes.RMB;
    private OperateTypes operateType = OperateTypes.SELL;
    private RateInfoEntity rateInfoEntity;
    private String nowCoinId;
    private double nowRate = 0;

    private double inputNum = 0d;
    long tmp_hand_money_Vale = 0;
    long result_value = 0;
    private double nowBl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null && getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        coin_type = (CoinTypes) getIntent().getExtras().getSerializable(HomeFragment.COIN_TYPE);
        operateType = (OperateTypes) getIntent().getExtras().getSerializable(HomeFragment.OPERATE_TYPE);
        rateInfoEntity = (RateInfoEntity) getIntent().getExtras().getSerializable(HomeFragment.RATE_INFO);
        nowCoinId = getIntent().getExtras().getString(HomeFragment.COIN_ID);
        nowRate = rateInfoEntity.getYugou();
        Log.d("TAG", "nowCoinId :" + nowCoinId);
        setContentView(R.layout.activity_pre_order);
        ButterKnife.bind(this);
        initView();

    }

    String coin_Name = "";

    private void initView() {
        switch (coin_type) {
            case YEN://日元
                coin_Name = "日元";
                break;
            case US:
                coin_Name = "美元";
                break;
            case HK:
                coin_Name = "港幣";
                break;
            case RMB:
                coin_Name = "人民幣";
                break;
            case KOREAN://韓幣
                coin_Name = "韓幣";
                break;
            case BAHT://泰銖
                coin_Name = "泰銖";
                break;
        }
        tvHeader.setText(this.getResources().getString(R.string.currency_name05));
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);

        tvNowRate.setText(String.valueOf(nowRate));
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
                        handMoneyTv.setText("");
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
                    initPreValue(inputNum);

                } catch (Exception e) {
                    showToast("請輸入合法數字");
                    initPreValue(0d);
                    s.clear();
                }
            }
        });

    }


    private void initPreValue(Double inputNum) {
        this.inputNum = inputNum;
        handMoneyTv.setText("");
        tmp_hand_money_Vale = 0;
        double tmp_a = ArithTool.mul(inputNum, nowRate);
        long tmp_b = (long) tmp_a;
        result_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
//        result_value = (long) ArithTool.mul(inputNum, nowRate);
        realGetTv.setText(inputNum + "*" + nowRate + "=" + result_value);
        getTv.setText(String.valueOf(result_value));
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @OnClick({R.id.backBtn, R.id.go_btn, R.id.select_pre_day_tv})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.go_btn:
                String input_v = inputValueEt.getText().toString().trim();
                if (TextUtils.isEmpty(input_v)) {
                    showToast("请输入金额!");
                    return;
                }
                if (selectPreInfoEntity == null) {
                    showToast("請選擇存放天數");
                    return;
                }
                Bundle bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putSerializable(HomeFragment.RATE_INFO, rateInfoEntity);

                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, result_value);
                bundle.putLong(HomeFragment.HAND, tmp_hand_money_Vale);

                bundle.putString(HomeFragment.FORMULA, realGetTv.getText().toString().trim());
                bundle.putString(HomeFragment.INPUT_VALUE, input_v);
                bundle.putString(HomeFragment.COIN_NAME, coin_Name);
                bundle.putString(HomeFragment.COIN_ID, nowCoinId);

                bundle.putBoolean(HomeFragment.MODIFY_ORDER, false);
                bundle.putString(HomeFragment.ORDER_ID, "0");
                bundle.putString(HomeFragment.AlipayGive, String.valueOf(rateInfoEntity.getZsds().getZs_money()));


                bundle.putString("PRE_INFO_ID", selectPreInfoEntity.getId());
                Log.d("TAG", bundle.toString());
                toActivity(PreResultActivity.class, bundle);
                break;
            case R.id.select_pre_day_tv:
                preRateInfo();
                break;

        }
    }

    private PreRateInfoEntity selectPreInfoEntity;

    private void preRateInfo() {
        String url = AppConfig.LIKEIT_GET_YUGOU;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
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
                        //  JSONArray array = obj.optJSONArray("info");
                        ArrayList<PreRateInfoEntity> mData = (ArrayList<PreRateInfoEntity>) JSON.parseArray(obj.optString("info"), PreRateInfoEntity.class);
                        Log.d("TAG22", mData.get(0).getId());
                        Log.d("TAG22", mData.toString());
                        PreDayDialog preDayDialog = new PreDayDialog(mContext, mData);
                        preDayDialog.setPreDaySelectCallBackListener(new PreDayDialog.PreDaySelectCallBackListener() {
                            @Override
                            public void onCancle() {

                            }

                            @Override
                            public void onSubmit(PreRateInfoEntity entity) {
                                selectPreInfoEntity = entity;
                                nowRate = selectPreInfoEntity.getZvs();
                                tvNowRate.setText(String.valueOf(nowRate));
                                daysTv.setText(entity.getDays() + "天(不能小於" + entity.getMinmoney() + ")");
                                if (inputNum > 0) {
                                    initPreValue(inputNum);
                                }
                            }
                        });
                        preDayDialog.show();
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
