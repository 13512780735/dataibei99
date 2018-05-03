package com.likeit.currenciesapp.ui.me;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ModifyPasswdActivity extends BaseActivity {
    @BindView(R.id.now_passwd_et)
    EditText nowPasswdEt;
    @BindView(R.id.new_passwd_et)
    EditText newPasswdEt;
    @BindView(R.id.re_new_passwd_et)
    EditText reNewPasswdEt;
    @BindView(R.id.get_passwd_btn)
    TextView getPasswdBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_passwd);
        ButterKnife.bind(this);
        initTopBar("修改密碼");
    }

    @OnClick({R.id.get_passwd_btn})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.get_passwd_btn:
                getPasswd();
                break;
        }
    }


    private void getPasswd() {
        String nowPasswd = nowPasswdEt.getText().toString().trim();
        String newpasswd = newPasswdEt.getText().toString().trim();
        String renewpasswd = reNewPasswdEt.getText().toString().trim();

        if (TextUtils.isEmpty(newpasswd) || TextUtils.isEmpty(nowPasswd) || TextUtils.isEmpty(renewpasswd)) {
            showToast("請填寫完整信息");
            return;
        }

        if (!TextUtils.equals(newpasswd, renewpasswd)) {
            showToast("兩次密碼不一致!");
            return;
        }
        String url = AppConfig.LIKEIT_USER_EDIT;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("pwd", newpasswd);
        params.put("oldpwd", nowPasswd);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        showToast("修改密碼成功");
                        toFinish();
                    } else {
                        ToastUtil.showS(context, msg);
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
