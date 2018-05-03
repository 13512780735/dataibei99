package com.likeit.currenciesapp.ui.me;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.configs.AppConst;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UIUtils;
import com.lqr.ninegridimageview.LQRNineGridImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.zxing.QRCodeEncoder;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class QRCodeCardActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tvTip)
    TextView mTvTip;
    @BindView(R.id.ngiv)
    LQRNineGridImageView mNgiv;
    @BindView(R.id.tvName)
    TextView mTvName;
    @BindView(R.id.ivCard)
    ImageView mIvCard;
    @BindView(R.id.ivHeader)
    ImageView mIvHeader;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private String imageUris;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode_card);
        ButterKnife.bind(this);
        Bundle bundle = getIntent().getExtras();
        mLoginUserInfoEntity = (LoginUserInfoEntity) bundle.getSerializable("userInfo");
        imageUris=bundle.getString("imageUris");
        Log.d("TAG213", mLoginUserInfoEntity.getPic() + mLoginUserInfoEntity.getTruename() + mLoginUserInfoEntity.getRongcloud_id());
        initData();
        initView();
    }

    private void initData() {
        Glide.with(this).load(imageUris).centerCrop().into(mIvHeader);
        mTvName.setText(mLoginUserInfoEntity.getTruename());
        setQRCode( mLoginUserInfoEntity.getRongcloud_id());
}

    private void setQRCode(String content) {
        Observable.just(QRCodeEncoder.syncEncodeQRCode(content, UIUtils.dip2Px(100)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(bitmap -> mIvCard.setImageBitmap(bitmap), this::loadQRCardError);
    }

    private void loadQRCardError(Throwable throwable) {
        ToastUtil.showS(QRCodeCardActivity.this, throwable.getLocalizedMessage());
    }

    private void initView() {
        tvHeader.setText("我的二維碼");
        mTvTip.setText("掃壹掃上面的二維碼圖案，加我賬號");
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
