package com.likeit.currenciesapp.ui.login;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ForgetPwdActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.login_et_phone)
    EditText edphone;
    @BindView(R.id.send_code_btn)//点击发送
            TextView tvCode;
    @BindView(R.id.login_et_code)
    EditText etCode;
    @BindView(R.id.login_et_new_pwd)
    EditText etPwd;

    private int time_tatol = 60;
    private final static int TIME = 101;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_pwd);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.forgetpwd_name01));
    }

    @OnClick({R.id.backBtn, R.id.login_tv_get_pwd, R.id.send_code_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                getCheckPhone();
                break;
            case R.id.login_tv_get_pwd:
                String phoneCheck = etCode.getText().toString().trim();
                final String phone = edphone.getText().toString().trim();
                final String passwd = etPwd.getText().toString().trim();
                if (TextUtils.isEmpty(phone) || TextUtils.isEmpty(phoneCheck) || TextUtils.isEmpty(passwd)) {
                    showToast("請填寫完整信息");
                    return;
                }
                LoadDialog.show(mContext);
                String url = AppConfig.LIKEIT_GET_PWd;
                RequestParams params = new RequestParams();
                params.put("user_name", phone);
                params.put("pwd", passwd);
                params.put("code", phoneCheck);
                HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                    @Override
                    public void success(String response) {
                        try {
                            JSONObject object=new JSONObject(response);
                            String status=object.optString("status");
                            String msg=object.optString("msg");
                            if("true".equals(status)){
                                showToast("密碼重置成功");
                                onBackPressed();
                            }else{
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
                break;
        }
    }
    private void getCheckPhone() {
        String phoneNum = edphone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("請輸入手機號碼");
            return;
        }
//        if (!StringUtil.isCellPhone(phoneNum)) {
//            ToastUtil.showS(mContext, "請輸入正確的手機號");
//            return;
//        }
        LoadDialog.show(mContext);
        String url = AppConfig.LIKEIT_SEND_SMS;
        RequestParams params = new RequestParams();
        params.put("mobile", phoneNum);
        params.put("type", "2");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(mContext);
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
}
