package com.likeit.currenciesapp.ui.me;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.PhotoUtils;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.ToastUtils;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RealActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_id_card_positive)
    ImageView ivCardPositive;
    @BindView(R.id.iv_id_card_contrary)
    ImageView ivIdCardContrary;
    @BindView(R.id.iv_id_hand_card)
    ImageView ivIdHandCard;
    @BindView(R.id.apply_et_name)
    EditText apply_et_name;
    @BindView(R.id.apply_et_id_card)
    EditText apply_et_id_card;
    @BindView(R.id.apply_et_band_card)
    EditText apply_et_band_card;
    @BindView(R.id.apply_et_phone)
    EditText apply_et_phone;
    @BindView(R.id.apply_et_code)
    EditText apply_et_code;
    @BindView(R.id.send_code_btn)
    TextView tvCode;
    @BindView(R.id.apply_scrollView)
    PullToRefreshScrollView apply_scrollView;
    @BindView(R.id.isshow)
    TextView isshow;

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
    private int staus;
    private final static int TIME = 101;
    private int time_tatol = 60;
    Handler myHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME:
                    if (!isFinishing()) {
                        time_tatol--;
                        tvCode.setText(time_tatol + "秒后刷新");
                        if (time_tatol <= 0) {
                            tvCode.setText("獲取驗證碼");
                            tvCode.setEnabled(true);
                            tvCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_round_home_rate_pre_sure));
                        }
                        myHanlder.sendEmptyMessageDelayed(TIME, 1000);
                    }
                    break;
            }
        }
    };
    private Bitmap bitmap1;
    private Bitmap bitmap2;
    private Bitmap bitmap3;
    private JSONObject obj;
    private String real_status;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_real);
        ButterKnife.bind(this);
       // apply_scrollView.setVisibility(View.VISIBLE);
        initView();
      initData();
    }

    private void initData() {
        String url = AppConfig.LIKEIT_check_real_status;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    // String message=object.optString("msg");
                    String status = object.optString("status");
                    String message = object.optString("msg");
                    if ("true".equals(status)) {
                        obj = object.optJSONObject("info");
                        real_status=obj.optString("real_status");
                        if("0".equals(real_status)){
                            apply_scrollView.setVisibility(View.GONE);
                            isshow.setVisibility(View.VISIBLE);
                            isshow.setText("實名認證待審核");
                        }else if("1".equals(real_status)){
                            isshow.setVisibility(View.VISIBLE);
                            isshow.setText("實名認證審核通過");
                        }else{
                            apply_scrollView.setVisibility(View.VISIBLE);
                        }
                    } else {
                        showToast(message);
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

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.me_name13));

    }

    @OnClick({R.id.backBtn, R.id.iv_id_card_positive, R.id.iv_id_card_contrary, R.id.iv_id_hand_card, R.id.tv_apply, R.id.send_code_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.iv_id_card_positive:
                staus = 2;
                selectAvatar(staus);
                break;
            case R.id.iv_id_card_contrary:
                staus = 3;
                selectAvatar(staus);
                break;
            case R.id.iv_id_hand_card:
                staus = 1;
                selectAvatar(staus);
                break;
            case R.id.tv_apply:
                send();
                break;
            case R.id.send_code_btn:
                getCheckPhone();
                break;
        }
    }

    private void getCheckPhone() {
        String phoneNum = apply_et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("請輸入手機號碼");
            return;
        }
        showProgress("Loading...");
        String url = AppConfig.LIKEIT_CHECK_BY_REALPHONE;
        RequestParams params = new RequestParams();
        params.put("tel", phoneNum);
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                // Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    // String message=object.optString("msg");
                    String status = object.optString("status");
                    String message = object.optString("msg");
                    if ("true".equals(status) && "0".equals(code)) {
                        showToast("驗證碼發送成功，稍後請留言你的短信!");
                        time_tatol = 60;
                        tvCode.setText(time_tatol + "秒后刷新");
                        tvCode.setEnabled(false);
                        tvCode.setBackgroundDrawable(getResources().getDrawable(R.drawable.shape_right_round_phone_unable_check_));
                        myHanlder.sendEmptyMessageDelayed(TIME, 1000);
                    } else {
                        showToast(message);
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

    private void send() {
        String name = apply_et_name.getText().toString().trim();
        String idCard = apply_et_id_card.getText().toString().trim();
        final String bandCard = apply_et_band_card.getText().toString().trim();
        final String phone = apply_et_phone.getText().toString().trim();
        String code = apply_et_code.getText().toString().trim();
//        if (bitmap1 == null || bitmap2 == null || bitmap3 == null) {
//            showToast("請填寫身份證明");
//            return;
//        }
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(idCard) || TextUtils.isEmpty(bandCard) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(code)) {
            showToast("請填寫完整信息");
            return;
        }
        showProgress("Loading...");
        String url = AppConfig.LIKEIT_UP_USER_REAL;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("name", name);
        params.put("cardid", idCard);
        params.put("bankid", bandCard);
        params.put("tel", phone);
        params.put("telcode", code);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                try {
                    JSONObject object = new JSONObject(response);
                    String code = object.optString("code");
                    // String message=object.optString("msg");
                    String status = object.optString("status");
                    String message = object.optString("msg");
                    if ("true".equals(status)) {
                        showToast("上传成功");
                        onBackPressed();
                    } else {
                        showToast(message);
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

    private void selectAvatar(int staus) {
        LayoutInflater inflater = LayoutInflater.from(this);
        mpopview = inflater.inflate(R.layout.layout_choose_photo, null);
        mPopupWindow = new PopupWindow(mpopview, ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mPopupWindow.setBackgroundDrawable(getResources().getDrawable(
                R.drawable.mid_filter_bg));

        //   mPopupWindow.showAsDropDown(ll_id, 0, 20, Gravity.CENTER);
        mPopupWindow.showAtLocation(getLayoutInflater().inflate(R.layout.activity_real, null), Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        mPopupWindow.setOutsideTouchable(false);
        //mPopupWindow.setAnimationStyle(R.style.PopupAnimation);
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

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
                ToastUtils.showShort(this, "您已经拒绝过一次");
            }
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE}, CAMERA_PERMISSIONS_REQUEST_CODE);
        } else {//有权限直接调用系统相机拍照
            if (hasSdcard()) {
                imageUri = Uri.fromFile(fileUri);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                    imageUri = FileProvider.getUriForFile(mContext, "com.likeit.currenciesapp.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
            } else {
                ToastUtils.showShort(this, "设备没有SD卡！");
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(mContext, "com.likeit.currenciesapp.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
        }
    }

    private static final int output_X = 720;
    private static final int output_Y = 1280;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    cropImageUri = Uri.fromFile(fileCropUri);
                    Log.d("TAG321", imageUri.getPath());
                    PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.likeit.currenciesapp.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                        Log.d("TAG123", newUri.getPath());
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, this);
                    Log.d("TAG555", cropImageUri.toString());
                    if (bitmap != null) {
                        showImages(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        String base64Token = Base64.encodeToString(bytes, Base64.DEFAULT);//  编码后
                        Log.d("TAG666", base64Token);
                        upLoad(base64Token);
                    }
                    break;
            }
        }
    }

    private void upLoad(String base64Token) {
        String url = AppConfig.LIKEIT_UP_HANDHELD_IDCARD_BASE64;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("pic", base64Token);
        params.put("type", staus);
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
                        showToast("上傳成功");
                        // initData();
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


    /**
     * 自动获取sdk权限
     */

    private void autoObtainStoragePermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, STORAGE_PERMISSIONS_REQUEST_CODE);
        } else {
            PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
        }

    }

    private void showImages(Bitmap bitmap) {

        if (staus == 1) {
            ivIdHandCard.setImageBitmap(bitmap);
            bitmap1=bitmap;
        } else if (staus == 2) {
            ivCardPositive.setImageBitmap(bitmap);
            bitmap2=bitmap;
        } else if (staus == 3) {
            ivIdCardContrary.setImageBitmap(bitmap);
            bitmap3=bitmap;
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }


}
