package com.likeit.currenciesapp.ui.home;

import android.content.Context;
import android.content.Intent;
import android.os.Vibrator;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.thread.ThreadPoolFactory;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.UIUtils;
import com.lqr.imagepicker.ImagePicker;
import com.lqr.imagepicker.bean.ImageItem;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.QRCodeDecoder;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

public class Scan01Activity extends Container implements QRCodeView.Delegate {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.zxingview)
    ZXingView mZxingview;
    private DianInfoEntity mDianInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan01);
        ButterKnife.bind(this);
        mDianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        initView();
    }

    private void initView() {
        tvHeader.setText("二維碼");
        mZxingview.setDelegate(this);
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mZxingview.startCamera();
        mZxingview.startSpotAndShowRect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mZxingview.stopCamera();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mZxingview.onDestroy();
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        handleResult(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        UIUtils.showToast(UIUtils.getString(R.string.open_camera_error));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {//返回多张照片
            if (data != null) {
                final ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (images != null && images.size() > 0) {
                    //取第一张照片
                    ThreadPoolFactory.getNormalPool().execute(new Runnable() {
                        @Override
                        public void run() {
                            String result = QRCodeDecoder.syncDecodeQRCode(images.get(0).path);
                            if (TextUtils.isEmpty(result)) {
                                UIUtils.showToast(UIUtils.getString(R.string.scan_fail));
                            } else {
                                handleResult(result);
                            }
                        }
                    });
                }
            }
        }
    }

    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }

    private void handleResult(String result) {
        // showToast("扫描结果:" + result);
        Log.d("TAG", "扫描结果:" + result);
        //String account = result.substring(AppConst.QrCodeCommon.ADD.length());
        String account = result;
        Log.d("TAG", "扫描结果account:" + account);
        vibrate();
        mZxingview.startSpot();
        Bundle bundle = new Bundle();
        Intent intent = new Intent(Scan01Activity.this, PayMentActivity.class);
        bundle.putString("userId", account);
        bundle.putSerializable("Dian", mDianInfoEntity);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
        //添加好友
//        if (result.startsWith(AppConst.QrCodeCommon.ADD)) {
//            String account = result.substring(AppConst.QrCodeCommon.ADD.length());
////            if (SealUserInfoManager.getInstance().i(account)) {
////                UIUtils.showToast(UIUtils.getString(R.string.this_account_was_your_friend));
////                return;
////            }
//
//        }
//        //进群
//        else if (result.startsWith(AppConst.QrCodeCommon.JOIN)) {
//            String groupId = result.substring(AppConst.QrCodeCommon.JOIN.length());
//            if (DBManager.getInstance().isInThisGroup(groupId)) {
//                UIUtils.showToast(UIUtils.getString(R.string.you_already_in_this_group));
//                return;
//            } else {
//                ApiRetrofit.getInstance().JoinGroup(groupId)
//                        .subscribeOn(Schedulers.io())
//                        .observeOn(AndroidSchedulers.mainThread())
//                        .filter(joinGroupResponse -> joinGroupResponse != null && joinGroupResponse.getCode() == 200)
//                        .flatMap(new Func1<JoinGroupResponse, Observable<GetGroupInfoResponse>>() {
//                            @Override
//                            public Observable<GetGroupInfoResponse> call(JoinGroupResponse joinGroupResponse) {
//                                return ApiRetrofit.getInstance().getGroupInfo(groupId);//似乎这里会报错，导致下面subscribe中的逻辑没有执行，不知道为啥。。
//                            }
//                        })
//                        .subscribe(getGroupInfoResponse -> {
//                            if (getGroupInfoResponse != null && getGroupInfoResponse.getCode() == 200) {
//                                GetGroupInfoResponse.ResultEntity resultEntity = getGroupInfoResponse.getResult();
//                                DBManager.getInstance().saveOrUpdateGroup(new Groups(resultEntity.getId(), resultEntity.getName(), null, String.valueOf(0)));
//                                Intent intent = new Intent(ScanActivity.this, SessionActivity.class);
//                                intent.putExtra("sessionId", resultEntity.getId());
//                                intent.putExtra("sessionType", SessionActivity.SESSION_TYPE_GROUP);
//                                jumpToActivity(intent);
//                                finish();
//                            } else {
//                                Observable.error(new ServerException(UIUtils.getString(R.string.select_group_info_fail_please_restart_app)));
//                            }
//                        }, this::loadError);
//            }
//        }
    }

//    private void loadError(Throwable throwable) {
//        showToast(throwable.getLocalizedMessage());
//        UIUtils.showToast(throwable.getLocalizedMessage());
//    }
}