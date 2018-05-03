package com.likeit.currenciesapp.ui.login;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.chat.SealConst;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
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
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Timer;
import java.util.TimerTask;

import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static com.nostra13.universalimageloader.core.ImageLoader.TAG;

public class SplashActivity extends BaseActivity {
    private static final int LOGIN = 5;
    private static final int GET_TOKEN = 6;
    private static final int SYNC_USER_INFO = 9;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SplashActivity mContext;
    private String phone;
    private String pwd;
    private String sex;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private String loginToken;
    private String connectResultId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        MyActivityManager.getInstance().addActivity(this);
        mBtnLeft.setVisibility(View.GONE);
        mContext = this;
        sp = getSharedPreferences("config", MODE_PRIVATE);
        editor = sp.edit();
        boolean isRemember = sp.getBoolean("remember_password", false);
        if (isRemember) {
            phone = UtilPreference.getStringValue(mContext, "phone");
            pwd = UtilPreference.getStringValue(mContext, "pwd");
            sex = UtilPreference.getStringValue(mContext, "sex");
            login();
        } else {
            Timer timer = new Timer();
            TimerTask task = new TimerTask() {
                @Override
                public void run() {
                    Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                    intent.putExtra("ukeys", "1");
                    startActivity(intent);
                    finish();

                }
            };
            timer.schedule(task, 3000);// 此处的Delay可以是3*1000，代表三秒
        }
    }

    private void login() {
        // LoadDialog.show(mContext);
        //showProgress("Loading...");
        Log.d("TAG", "name_ :" + phone + "  passwd_ :" + pwd);
        String url = AppConfig.LIKEIT_LOGIN;
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
                        request(LOGIN, true);
                    } else {
                        //showToast(msg);
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
        });
    }

    @Override
    public Object doInBackground(int requestCode, String id) {
        switch (requestCode) {
            case LOGIN:
                // sex = UtilPreference.getStringValue(mContext, "sex");
                try {
                    return action.login(sex, phone, pwd);
                } catch (HttpException e) {
                    e.printStackTrace();
                }
            case GET_TOKEN:
                try {
                    return action.getToken();
                } catch (HttpException e) {
                    e.printStackTrace();
                }
            case SYNC_USER_INFO:
                try {
                    return action.getUserInfoById(connectResultId);
                } catch (HttpException e) {
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
