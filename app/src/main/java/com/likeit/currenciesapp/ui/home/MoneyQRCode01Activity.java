package com.likeit.currenciesapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.ui.ScanActivity;
import com.likeit.currenciesapp.ui.me.QRCodeCardActivity;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UIUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MoneyQRCode01Activity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tvTip)
    TextView mTvTip;
    @BindView(R.id.ivCard)
    ImageView mIvCard;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private DianInfoEntity mDianInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_money_qrcode);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        mLoginUserInfoEntity = (LoginUserInfoEntity) bundle.getSerializable("userInfo");
        mDianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        initUI();

    }

    private void initUI() {
        tvHeader.setText("收款碼");
        mTvTip.setText("出示二維碼向他人收款");
        setQRCode(mLoginUserInfoEntity.getRongcloud_id());
    }

    private void setQRCode(String content) {
        Observable.just(QRCodeEncoder.syncEncodeQRCode(content, UIUtils.dip2Px(100)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> mIvCard.setImageBitmap(bitmap), this::loadQRCardError);
    }

    private void loadQRCardError(Throwable throwable) {
        ToastUtil.showS(MoneyQRCode01Activity.this, throwable.getLocalizedMessage());
    }

    @OnClick({R.id.backBtn, R.id.go_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.go_btn:
                Bundle bundle=new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                toActivity(Scan01Activity.class, bundle);
                break;
        }
    }
}
