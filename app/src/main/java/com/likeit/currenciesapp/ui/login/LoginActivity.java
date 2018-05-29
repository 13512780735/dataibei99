package com.likeit.currenciesapp.ui.login;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.chat.SealConst;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.server.SealAction;
import com.likeit.currenciesapp.ui.chat.server.network.http.HttpException;
import com.likeit.currenciesapp.ui.chat.server.response.GetTokenResponse;
import com.likeit.currenciesapp.ui.chat.server.response.GetUserInfoByIdResponse;
import com.likeit.currenciesapp.ui.chat.server.response.LoginResponse;
import com.likeit.currenciesapp.ui.chat.server.utils.NLog;
import com.likeit.currenciesapp.ui.chat.server.utils.NToast;
import com.likeit.currenciesapp.ui.chat.server.utils.RongGenerate;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.ui.chat.ui.activity.BaseActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;
import me.weyye.hipermission.HiPermission;
import me.weyye.hipermission.PermissionCallback;
import me.weyye.hipermission.PermissionItem;

import static com.likeit.currenciesapp.utils.UIUtils.showToast;
import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class LoginActivity extends BaseActivity {
    private static final int LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int SYNC_USER_INFO = 9;

    @BindView(R.id.login_et_phone)
    EditText edPhone;
    @BindView(R.id.tv_region)
    TextView tv_region;
    @BindView(R.id.login_et_pwd)
    EditText edpwd;
    @BindView(R.id.checkbox_cb)
    CheckBox checkboxCb;
    @BindView(R.id.ll_account)
    LinearLayout ll_account;
    private String phone;
    private String pwd;
    private String url;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private String connectResultId;
    private SharedPreferences sp;
    private SealAction action;
    private final String CONTENT_TYPE = "application/json";
    private final String ENCODING = "utf-8";
    private String loginToken;
    private String ukeys;
    private regionfragment dialogFragment;
    private String sex;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MyActivityManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        setHeadVisibility(View.GONE);
        action = new SealAction(mContext);
        ukeys = getIntent().getStringExtra("ukeys");
        openPermission();
        verifyStoragePermissions(this);
        //   pref = PreferenceManager.getDefaultSharedPreferences(this);
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();

        boolean isRemember = sp.getBoolean("remember_password", true);
        if (isRemember) {
            String phone = UtilPreference.getStringValue(mContext, "phone");
            String pwd = UtilPreference.getStringValue(mContext, "pwd");

            sex = UtilPreference.getStringValue(mContext, "sex");
            if (StringUtil.isBlank(sex)) {
                sex = "886";
            }
            edPhone.setText(phone);
            edpwd.setText(pwd);
            if ("886".equals(sex)) {
                tv_region.setText("台灣");
            } else if ("86".equals(sex)) {
                tv_region.setText("大陸");
            }
            // tv_region.setText(sex);
            checkboxCb.setChecked(true);
        } else {
            tv_region.setText("台灣");
        }
        if (getIntent().getBooleanExtra("kickedByOtherClient", false)) {
            final AlertDialog dlg = new AlertDialog.Builder(LoginActivity.this).create();
            dlg.show();
            Window window = dlg.getWindow();
            window.setContentView(R.layout.other_devices);
            TextView text = (TextView) window.findViewById(R.id.ok);
            text.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dlg.cancel();
                }
            });
        }
    }

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE"};

    private void verifyStoragePermissions(LoginActivity loginActivity) {
        try {
            //检测是否有写的权限
            int permission = ActivityCompat.checkSelfPermission(loginActivity,
                    "android.permission.WRITE_EXTERNAL_STORAGE");
            if (permission != PackageManager.PERMISSION_GRANTED) {
                // 没有写的权限，去申请写的权限，会弹出对话框
                ActivityCompat.requestPermissions(loginActivity, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void openPermission() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE + Manifest.permission.CAMERA + Manifest.permission.WRITE_EXTERNAL_STORAGE
                + Manifest.permission.READ_EXTERNAL_STORAGE + Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(mContext,"请授予下面权限",Toast.LENGTH_SHORT).show();
            List<PermissionItem> permissions = new ArrayList<PermissionItem>();
            permissions.add(new PermissionItem(Manifest.permission.CALL_PHONE, "电话", R.drawable.permission_ic_phone));
            permissions.add(new PermissionItem(Manifest.permission.CAMERA, "照相", R.drawable.permission_ic_camera));
            permissions.add(new PermissionItem(Manifest.permission.WRITE_EXTERNAL_STORAGE, "储存空间", R.drawable.permission_ic_storage));
            permissions.add(new PermissionItem(Manifest.permission.READ_EXTERNAL_STORAGE, "储存空间", R.drawable.permission_ic_storage));
            permissions.add(new PermissionItem(Manifest.permission.RECORD_AUDIO, "录音", R.drawable.permission_ic_micro_phone));
            HiPermission.create(mContext)
                    .permissions(permissions)
                    .msg("为了您正常使用应用，需要以下权限")
                    .animStyle(R.style.PermissionAnimModal)
//                        .style(R.style.CusStyle)
                    .checkMutiPermission(new PermissionCallback() {
                        @Override
                        public void onClose() {
                            Log.i(TAG, "onClose");
                            ToastUtil.showS(mContext, "权限被拒绝");
                        }

                        @Override
                        public void onFinish() {
                            //ToastUtil.showS(mContext,"权限已被开启");
                        }

                        @Override
                        public void onDeny(String permission, int position) {
                            Log.i(TAG, "onDeny");
                        }

                        @Override
                        public void onGuarantee(String permission, int position) {
                            Log.i(TAG, "onGuarantee");
                        }
                    });
            return;
        }
    }

    @OnClick({R.id.login_tv_login, R.id.tv_register, R.id.tv_forgetPwd, R.id.tv_region})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.login_tv_login:
                login();
                break;
            case R.id.tv_register:
                // toActivity(RegisterActivity.class);
                Intent intent01 = new Intent(mContext, RegisterActivity.class);
                startActivity(intent01);
                break;
            case R.id.tv_forgetPwd:
                // toActivity(ForgetPwd02Activity.class);
                Intent intent02 = new Intent(mContext, ForgetPwdActivity.class);
                startActivity(intent02);
                break;
            case R.id.tv_region:
                showIndentDialog();
                break;
        }
    }

    private void showIndentDialog() {
        dialogFragment = new regionfragment();
        dialogFragment.show(this.getSupportFragmentManager(), "android");
        dialogFragment.setOnDialogListener(new regionfragment.OnDialogListener() {
            @Override
            public void onDialogClick(String person) {
                sex = person;
                UtilPreference.saveString(mContext, "sex", sex);
                if ("886".equals(sex)) {
                    tv_region.setText("台灣");
                } else if ("86".equals(sex)) {
                    tv_region.setText("大陸");
                }

            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void login() {
        phone = edPhone.getText().toString().trim();
        pwd = edpwd.getText().toString().trim();
        if (StringUtil.isBlank(phone)) {
            ToastUtil.showS(mContext, "手機號不能為空");
            return;
        }
//        if (!StringUtil.isCellPhone(phone)) {
//            ToastUtil.showS(mContext, "請輸入正確的手機號");
//            return;
//        }
        if (StringUtil.isBlank(pwd)) {
            ToastUtil.showS(mContext, "密碼不能為空");
            return;
        }

        if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(pwd)) {
            // showToast("請輸入賬號或密碼!");
            return;
        }
        LoadDialog.show(mContext);
        //showProgress("Loading...");
        Log.d("TAG", "name_ :" + phone + "  passwd_ :" + pwd);
        url = AppConfig.LIKEIT_LOGIN;
        RequestParams params = new RequestParams();
        params.put("username", phone);
        params.put("password", pwd);
        params.put("getuicid", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                //disShowProgress();
                Log.d("TAG", "Login-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status) && "0".equals(code)) {
                        JSONObject object = obj.optJSONObject("info");
                        String ukey = object.optString("ukey");
                        UtilPreference.saveString(mContext, "ukey", ukey);

                        mLoginUserInfoEntity = JSON.parseObject(object.optString("user"), LoginUserInfoEntity.class);
                        UtilPreference.saveString(mContext, "rongcloud_id", mLoginUserInfoEntity.getRongcloud_id());
                        UtilPreference.saveString(mContext, "work", mLoginUserInfoEntity.getWork());
                        request(LOGIN, true);
                    } else {
                        LoadDialog.dismiss(mContext);
                     showToast(msg);
                    }
                    //toActivityFinish(MainActivity.class);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable e) {
                LoadDialog.dismiss(mContext);
                //disShowProgress();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LoadDialog.dismiss(mContext);
            }
        });

    }

    @Override
    public Object doInBackground(int requestCode, String id) {
        switch (requestCode) {
            case LOGIN:
                // sex = UtilPreference.getStringValue(mContext, "sex");
                Log.d("TAG9998", sex + phone + pwd);
                try {
                    return action.login(sex, phone, pwd);
                } catch (HttpException e) {
                    LoadDialog.dismiss(mContext);
                    e.printStackTrace();
                }
            case GET_TOKEN:
                try {
                    return action.getToken();
                } catch (HttpException e) {
                    LoadDialog.dismiss(mContext);
                    e.printStackTrace();
                }
            case SYNC_USER_INFO:
                try {
                    return action.getUserInfoById(connectResultId);
                } catch (HttpException e) {
                    LoadDialog.dismiss(mContext);
                    e.printStackTrace();
                }
        }
        return null;
    }

    @Override
    public void onSuccess(int requestCode, Object result) {
        if (result != null) {
            switch (requestCode) {
                case LOGIN:
                    LoginResponse loginResponse = (LoginResponse) result;
                    if (loginResponse.getCode() == 200) {
                        loginToken = loginResponse.getResult().getToken();
                        Log.d("TAG", "loginToken-->" + loginToken);
                        if (!TextUtils.isEmpty(loginToken)) {
                            RongIM.connect(loginToken, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    NLog.e("connect", "onTokenIncorrect");
                                    reGetToken();
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                    editor.apply();
                                    Log.d("TAG999", s);
                                    SealUserInfoManager.getInstance().openDB();
                                    request(SYNC_USER_INFO, true);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode errorCode) {
                                    NLog.e("connect", "onError errorcode:" + errorCode.getValue());
                                    LoadDialog.dismiss(mContext);

                                }
                            });
                        }
                    } else {
                        if (loginResponse.getCode() == 100) {
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, R.string.phone_or_psw_error);
                        } else if (loginResponse.getCode() == 1000) {
                            LoadDialog.dismiss(mContext);
                            NToast.shortToast(mContext, R.string.phone_or_psw_error);
                        }
                    }
                    break;
                case SYNC_USER_INFO:
                    GetUserInfoByIdResponse userInfoByIdResponse = (GetUserInfoByIdResponse) result;
                    if (userInfoByIdResponse.getCode() == 200) {
                        if (TextUtils.isEmpty(userInfoByIdResponse.getResult().getPortraitUri())) {
                            userInfoByIdResponse.getResult().setPortraitUri(RongGenerate.generateDefaultAvatar(userInfoByIdResponse.getResult().getNickname(), userInfoByIdResponse.getResult().getId()));
                        }
                        String nickName = userInfoByIdResponse.getResult().getNickname();
                        String portraitUri = userInfoByIdResponse.getResult().getPortraitUri();
                        Log.d("TAG", "portraitUri-->" + portraitUri);
                        editor.putString(SealConst.SEALTALK_LOGIN_NAME, nickName);
                        editor.putString(SealConst.SEALTALK_LOGING_PORTRAIT, portraitUri);
                        editor.apply();
                        RongIM.getInstance().refreshUserInfoCache(new UserInfo(connectResultId, nickName, Uri.parse(portraitUri)));
                    }
                    //不继续在login界面同步好友,群组,群组成员信息
                    SealUserInfoManager.getInstance().getAllUserInfo();


                    goToMain();
                    break;
                case GET_TOKEN:
                    GetTokenResponse tokenResponse = (GetTokenResponse) result;
                    if (tokenResponse.getCode() == 200) {
                        String token = tokenResponse.getResult().getToken();
                        Log.d("TAG", "token-->" + token);
                        if (!TextUtils.isEmpty(token)) {
                            RongIM.connect(token, new RongIMClient.ConnectCallback() {
                                @Override
                                public void onTokenIncorrect() {
                                    Log.e(TAG, "reToken Incorrect");
                                }

                                @Override
                                public void onSuccess(String s) {
                                    connectResultId = s;
                                    NLog.e("connect", "onSuccess userid:" + s);
                                    editor.putString(SealConst.SEALTALK_LOGIN_ID, s);
                                    editor.apply();
                                    SealUserInfoManager.getInstance().openDB();
                                    request(SYNC_USER_INFO, true);
                                }

                                @Override
                                public void onError(RongIMClient.ErrorCode e) {
                                    LoadDialog.dismiss(mContext);
                                }
                            });
                        }
                    }
                    break;
            }
        }
    }

    private void goToMain() {
        if (checkboxCb.isChecked()) {
            editor.putBoolean("remember_password", true);
            editor.putString("phone", phone);
            editor.putString("pwd", pwd);
        } else {
            editor.clear();
        }
        editor.putString("loginToken", loginToken);
        editor.putString(SealConst.SEALTALK_LOGIN_ID, connectResultId);
        editor.putString(SealConst.SEALTALK_LOGING_PHONE, phone);
        editor.putString(SealConst.SEALTALK_LOGING_PASSWORD, pwd);
        UtilPreference.saveString(mContext, "phone", phone);
        UtilPreference.saveString(mContext, "pwd", pwd);
        UtilPreference.saveString(mContext, "sex", sex);
        editor.apply();
        LoadDialog.dismiss(mContext);
        Log.d("TAG858", "loginToken-->" + loginToken);
        Log.d("TAG222", "connectResultId-->" + getSharedPreferences("config", MODE_PRIVATE).getString(SealConst.SEALTALK_LOGIN_ID, ""));/////;TAG
        NToast.shortToast(mContext, R.string.login_success);
        Intent intentMain = new Intent(mContext, MainActivity.class);
        intentMain.putExtra("userInfo", mLoginUserInfoEntity);
        intentMain.putExtra("page", "0");
        //intentMain.putExtra("connectResultId", mLoginUserInfoEntity.getRongcloud_id());
        startActivity(intentMain);
        finish();
    }


    private void reGetToken() {
        request(GET_TOKEN);

    }


}
