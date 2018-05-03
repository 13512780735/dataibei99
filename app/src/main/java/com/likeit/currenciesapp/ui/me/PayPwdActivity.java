package com.likeit.currenciesapp.ui.me;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.StringUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PayPwdActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.get_passwd_btn)
    TextView tvOffer;
    @BindView(R.id.ll_older_pwd)
    LinearLayout ll_older_pwd;
    @BindView(R.id.now_passwd_et)
    EditText edOldPwd;
    @BindView(R.id.new_passwd_et)
    EditText edNewPwd;
    @BindView(R.id.re_new_passwd_et)
    EditText edReNewPwd;


    private String keys;
    private LoaddingDialog loaddingDialog;
    private String oldPwd;
    private String newPwd;
    private String reNewPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_pwd);
        ButterKnife.bind(this);
        keys = getIntent().getStringExtra("keys");
        loaddingDialog = new LoaddingDialog(this);
        initView();
    }

    private void initView() {
        if ("1".equals(keys)) {
            tvHeader.setText("支付密碼設置");
            tvOffer.setText("確定");
        } else if ("2".equals(keys)) {
            ll_older_pwd.setVisibility(View.VISIBLE);
            tvHeader.setText("支付密碼修改");
            tvOffer.setText("確定");
        }
    }
    @OnClick({R.id.backBtn, R.id.get_passwd_btn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.get_passwd_btn:
                oldPwd=edOldPwd.getText().toString().trim();
                newPwd=edNewPwd.getText().toString().trim();
                reNewPwd=edReNewPwd.getText().toString().trim();
                if ("1".equals(keys)) {
                    if (StringUtil.isBlank(newPwd) || StringUtil.isBlank(reNewPwd)) {
                        showToast("密碼不能為空");
                        return;
                    }if(newPwd.length()!=6||reNewPwd.length()!=6){
                        showToast("密碼长度不正确");
                        return;
                    }
                    setPwd();
                } else if ("2".equals(keys)) {
                    if (StringUtil.isBlank(oldPwd) || StringUtil.isBlank(newPwd) || StringUtil.isBlank(reNewPwd)) {
                        showToast("密碼不能為空");
                        return;
                    }
                    if(oldPwd.length()!=6||newPwd.length()!=6||reNewPwd.length()!=6){
                        showToast("密碼长度不正确");
                        return;
                    }
                    editPwd();
                }
                break;
        }
    }


    private void setPwd() {
        loaddingDialog.show();
        String url = AppConfig.LIKEIT_SET_PAYPWD;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("paypwd", newPwd);
        params.put("paypwd_again", reNewPwd);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    String status=obj.optString("status");
                    if("true".equals(status)){
                        showToast("設置成功");
                        onBackPressed();
                    }else{
                        showToast(obj.optString("msg"));
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
                loaddingDialog.dismiss();
            }
        });
    }

    private void editPwd() {
        loaddingDialog.show();
        String url = AppConfig.LIKEIT_EDIT_PAYPWD;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("paypwd", oldPwd);
        params.put("new_paypwd", newPwd);
        params.put("new_paypwd_again", reNewPwd);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj=new JSONObject(response);
                    String status=obj.optString("status");
                    if("true".equals(status)){
                        showToast("修改成功");
                        onBackPressed();
                    }else{
                        showToast(obj.optString("msg"));
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
                loaddingDialog.dismiss();
            }
        });
    }
}
