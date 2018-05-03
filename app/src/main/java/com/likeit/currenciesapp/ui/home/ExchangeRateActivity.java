package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeRateActivity extends Container implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.ll_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;
    private CurrencyFragment dialog;
    private int huilvid;
    private RateInfoEntity mRateInfoEntity;
    private boolean is_kaipan;
    private DianInfoEntity mDianInfoEntity;
    private String work;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_rate);
        ButterKnife.bind(this);

        Bundle bundle=getIntent().getExtras();
        mRateInfoEntity= (RateInfoEntity) bundle.getSerializable("mRateInfoEntity");
        mDianInfoEntity= (DianInfoEntity) bundle.getSerializable("Dian");
        is_kaipan=bundle.getBoolean("is_kaipan");
        initView();
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.exchange_name01));
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }
    public void showIndentDialog1() {
        dialog = new CurrencyFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("huilvid", huilvid);
        bundle.putSerializable("mRateInfoEntity", mRateInfoEntity);
        bundle.putSerializable("mDianInfoEntity", mDianInfoEntity);
        bundle.putBoolean("is_kaipan", is_kaipan);
        dialog.setArguments(bundle);
        dialog.show(this.getSupportFragmentManager(), "CurrencyFragment");
        dialog.startTime();
    }

    @OnClick({R.id.backBtn, R.id.tv_rl_rmb, R.id.tv_rl_dollar, R.id.tv_rl_krw, R.id.tv_rl_yen, R.id.tv_rl_baht, R.id.tv_rl_hkd})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_rl_rmb:
                huilvid = 2;
                showIndentDialog1();
                break;
            case R.id.tv_rl_dollar:
                huilvid = 3;
                //   showIndentDialog1();
                ToastUtil.showS(mContext, "還沒開盤");
                break;
            case R.id.tv_rl_krw:
                huilvid = 6;
                ToastUtil.showS(mContext, "還沒開盤");
                break;
            case R.id.tv_rl_yen:
                huilvid = 5;
                ToastUtil.showS(mContext, "還沒開盤");
                break;
            case R.id.tv_rl_baht:
                huilvid = 4;
                ToastUtil.showS(mContext, "還沒開盤");
                break;
            case R.id.tv_rl_hkd:
                huilvid = 7;
                ToastUtil.showS(mContext, "還沒開盤");
                break;

        }
    }
}
