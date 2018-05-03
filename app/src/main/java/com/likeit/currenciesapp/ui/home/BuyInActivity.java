package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.pk4pk.baseappmoudle.utils.ArithTool;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BuyInActivity extends Container implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
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

    private CoinTypes coin_type = CoinTypes.RMB;
    private OperateTypes operateType = OperateTypes.SELL;
    private RateInfoEntity rateInfoEntity;
    private String nowCoinId;
    private double nowRate = 0;

    private double inputNum = 0d;
    long tmp_hand_money_Vale = 0;
    long result_value = 0;
    private double nowBl;
    private int alipay_give = 0;


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
        nowRate = rateInfoEntity.getBuy();
        Log.d("TAG", "nowCoinId :" + nowCoinId);
        setContentView(R.layout.activity_buy_in);
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
        tvHeader.setText(this.getResources().getString(R.string.currency_name20));
        tvNowRate.setText(String.valueOf(nowRate));
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
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
                    initBuySellValue(inputNum);

                } catch (Exception e) {
                    showToast("請輸入合法數字");
                    initBuySellValue(0d);
                    s.clear();
                }

            }
        });
    }


    private void initBuySellValue(Double inputNum) {
        this.inputNum = inputNum;
        nowBl = ArithTool.div(rateInfoEntity.getSxf().getBl(), 100d);
        if (ArithTool.sub(inputNum, rateInfoEntity.getSxf().getNomore()) >= 0) {
            tmp_hand_money_Vale = 0;
            handMoneyTv.setText("0手續費");
            double tmp_a = ArithTool.mul(inputNum, nowRate);
            long tmp_b = (long) tmp_a;
            result_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
//            result_value = (long) ArithTool.mul(inputNum, nowRate);
            realGetTv.setText(inputNum + "*" + nowRate + "=" + result_value);
        } else {
            tmp_hand_money_Vale = (long) ArithTool.mul(inputNum, nowBl);


            if ((ArithTool.mul(inputNum, nowBl) - tmp_hand_money_Vale) > 0) {
                tmp_hand_money_Vale++;
            }

            if (ArithTool.sub(String.valueOf(tmp_hand_money_Vale), rateInfoEntity.getSxf().getMax()) >= 0) {
                tmp_hand_money_Vale = Long.valueOf(rateInfoEntity.getSxf().getMax());
            }

            handMoneyTv.setText(tmp_hand_money_Vale + "(" + this.getResources().getString(R.string.exchange_name02) + ")");

            double tmp_a = ArithTool.mul(ArithTool.add(inputNum, tmp_hand_money_Vale), nowRate);
            long tmp_b = (long) tmp_a;
            result_value = tmp_a == tmp_b ? tmp_b : (long) (tmp_a + 1);
//                result_value = (long) ArithTool.mul(ArithTool.add(inputNum, tmp_hand_money_Vale), nowRate);
            realGetTv.setText(("(" + inputNum + "+" + tmp_hand_money_Vale + ")" + "*" + nowRate + "=" + result_value));
        }

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

    @OnClick({R.id.backBtn, R.id.go_btn})
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
                Log.d("TAG", bundle.toString());
                toActivity(BuyResultActivity.class, bundle);
                break;
        }
    }
}
