package com.likeit.currenciesapp.ui.fragment;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.model.UserInfo;
import com.likeit.currenciesapp.ui.base.BaseFragment;
import com.likeit.currenciesapp.ui.chat.SealConst;
import com.likeit.currenciesapp.ui.chat.server.broadcast.BroadcastManager;
import com.likeit.currenciesapp.ui.login.LoginActivity;
import com.likeit.currenciesapp.ui.me.BankcardActivity;
import com.likeit.currenciesapp.ui.me.OrderListActivity;
import com.likeit.currenciesapp.ui.me.QRCodeCardActivity;
import com.likeit.currenciesapp.ui.me.RealActivity;
import com.likeit.currenciesapp.ui.me.RedPacketActivity;
import com.likeit.currenciesapp.ui.me.SettingActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.PhotoUtils;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.ToastUtils;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.NoScrollViewPager;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;

/**
 * A simple {@link Fragment} subclass.
 */
public class MeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {


    private TextView tvHeader;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private RelativeLayout rlSetting;
    private RelativeLayout rlReal;
    private RelativeLayout rlRedPacket;
    private RelativeLayout rlGreenteahy;
    private RelativeLayout rlBankcard;
    private NoScrollViewPager mViewPage;
    private TextView tvLogout;
    private CircleImageView ivAvatar;
    private TextView tvName, tvPhone, tvApplyTime, tvLastlogin;
    private ProgressDialog mDialog;
    private UserInfo mUserInfo;


    private PopupWindow mPopupWindow;
    private View mpopview;
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private RelativeLayout tv_rlOrder_inquiry;
    private RelativeLayout tv_rlRCordCard;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private String imageUris;

    @Override
    protected int setContentView() {
        return R.layout.fragment_me;
    }

    @Override
    protected void lazyLoad() {
        mLoginUserInfoEntity = (LoginUserInfoEntity) getActivity().getIntent().getSerializableExtra("userInfo");
        initData();//获取用户信息
        initView();

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
        String url = AppConfig.LIKEIT_GET_INFO;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            public JSONObject object;

            @Override
            public void success(String response) {
                Log.d("TAG", response);
                //    mDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        object = obj.optJSONObject("info");
                        mUserInfo = JSON.parseObject(obj.optString("info"), UserInfo.class);
                        tvName.setText(mUserInfo.getTruename());
                        tvPhone.setText(mUserInfo.getUser_name());
                        tvApplyTime.setText("申請日期:" + mUserInfo.getAddtime());
                        tvLastlogin.setText("最後登錄:" + mUserInfo.getLogintime());
                        imageUris = mUserInfo.getPic();
                        if (TextUtils.isEmpty(mUserInfo.getPic())) {
                            ivAvatar.setImageResource(R.mipmap.icon_me_avatar);
                        } else {
                            Log.d("TAG", mUserInfo.getPic());

                            //String portraitUri=AppConfig.LIKEIT_LOGO1 + mUserInfo.getPic();
                            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(mUserInfo.getPic(), ivAvatar, MyApplication.getOptions());
                            //ImageLoader.getInstance().displayImage(AppConfig.LIKEIT_LOGO1 + mUserInfo.getPic(), ivAvatar);
                        }

                    } else {
                        ToastUtil.showS(getActivity(), msg);
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
                //    mDialog.dismiss();
            }
        });
    }

    private void initView() {
        tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText(this.getResources().getString(R.string.me_name01));
        ivAvatar = findViewById(R.id.iv_avatar);//图像
        tvName = findViewById(R.id.tv_name);//姓名
        tvPhone = findViewById(R.id.tv_phone);//电话
        tvApplyTime = findViewById(R.id.tv_time);//申请日期
        tvLastlogin = findViewById(R.id.tv_last_login);//最后登录日期


        rlSetting = findViewById(R.id.tv_rlSetting);
        tvLogout = findViewById(R.id.tv_tvLogout);
        rlReal = findViewById(R.id.tv_rlReal_authentication);
        rlRedPacket = findViewById(R.id.tv_rlRed_packet);
        rlGreenteahy = findViewById(R.id.tv_rlGreenteahy);
        rlBankcard = findViewById(R.id.tv_rlBankcard);
        tv_rlRCordCard = findViewById(R.id.tv_rlRCordCard);
        tv_rlOrder_inquiry = findViewById(R.id.tv_rlOrder_inquiry);
        mPullToRefreshScrollView = findViewById(R.id.ll_home_scrollview);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        mPullToRefreshScrollView.setBackgroundColor(this.getResources().getColor(R.color.headerbg));
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        // mPullToRefreshScrollView.getLoadingLayoutProxy().setLoadingDrawable(this.getResources().getColor(R.color.white));

        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        initListener();
    }

    private void initListener() {
        rlSetting.setOnClickListener(this);
        rlReal.setOnClickListener(this);
        rlRedPacket.setOnClickListener(this);
        rlGreenteahy.setOnClickListener(this);
        rlBankcard.setOnClickListener(this);
        tvLogout.setOnClickListener(this);
        ivAvatar.setOnClickListener(this);
        tv_rlOrder_inquiry.setOnClickListener(this);
        tv_rlRCordCard.setOnClickListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        initData();
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_rlSetting:
                toActivity(SettingActivity.class);
                break;
            case R.id.tv_rlRCordCard:
                Bundle bundle = new Bundle();
                bundle.putSerializable("userInfo", mLoginUserInfoEntity);
                bundle.putString("imageUris", imageUris);
                toActivity(QRCodeCardActivity.class, bundle);
                break;
            case R.id.tv_rlRed_packet:
                toActivity(RedPacketActivity.class);
                break;
            case R.id.tv_rlGreenteahy:
                mViewPage = (NoScrollViewPager) getActivity().findViewById(R.id.home_viewpager);
                mViewPage.setCurrentItem(1);
                break;
            case R.id.tv_rlOrder_inquiry://訂單查詢
                toActivity(OrderListActivity.class);
                break;
            case R.id.tv_rlBankcard:
               // UtilPreference.saveString(getActivity(), "bankflag", "1");
                Intent intent01 = new Intent(getContext(), BankcardActivity.class);
                bundle = new Bundle();
                bundle.putString("flag", "1");
                bundle.putString("bankflag", "1");
                intent01.putExtras(bundle);
                startActivity(intent01);
                // toActivity(BankcardActivity.class);
                break;
            case R.id.tv_rlReal_authentication:
                toActivity(RealActivity.class);
                break;
            case R.id.tv_tvLogout:
                // RongIM.getInstance().disconnect();
                BroadcastManager.getInstance(getActivity()).sendBroadcast(SealConst.EXIT);
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.putExtra("ukeys", "2");
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.iv_avatar:
                selectAvatar();
                break;
        }
    }

    private void selectAvatar() {
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        mpopview = inflater.inflate(R.layout.layout_choose_photo, null);
        mPopupWindow = new PopupWindow(mpopview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.mid_filter_bg));

        mPopupWindow.showAtLocation(getActivity().findViewById(R.id.id_content), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOutsideTouchable(false);
        mPopupWindow.setAnimationStyle(R.style.AnimBottom);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
        mPopupWindow.setTouchable(true); // 设置popupwindow可点击
        mPopupWindow.setOutsideTouchable(true); // 设置popupwindow外部可点击
        mPopupWindow.setFocusable(true); // 获取焦点
        mPopupWindow.update();

        Button mbuttonTakePhoto = (Button) mpopview
                .findViewById(R.id.button_take_photo);
        Button mbuttonChoicePhoto = (Button) mpopview
                .findViewById(R.id.button_choice_photo);
        Button mbuttonChoicecannce = (Button) mpopview
                .findViewById(R.id.button_choice_cancer);

        // 相册上传
        mbuttonChoicePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                autoObtainStoragePermission();
            }
        });

        // 拍照上传
        mbuttonTakePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPopupWindow.dismiss();
                autoObtainCameraPermission();
            }
        });

        mbuttonChoicecannce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                mPopupWindow.dismiss();
            }
        });
        mPopupWindow.setTouchInterceptor(new View.OnTouchListener() {

            @SuppressLint("ClickableViewAccessibility")
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // 如果点击了popupwindow的外部，popupwindow也会消失
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    mPopupWindow.dismiss();
                    return true;
                }
                return false;
            }
        });
    }


    /**
     * 自动获取相机权限
     */
    private void autoObtainCameraPermission() {

        if (ContextCompat.checkSelfPermission(MyApplication.mContext, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(MyApplication.mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.CAMERA)) {
                ToastUtils.showShort(getActivity(), "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(getActivity(), "com.likeit.currenciesapp.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(getActivity(), imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(getActivity(), "设备没有SD卡！");
            }
        }
    }


    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(MyApplication.mContext, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(getActivity(), CODE_GALLERY_REQUEST);
        }

    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
