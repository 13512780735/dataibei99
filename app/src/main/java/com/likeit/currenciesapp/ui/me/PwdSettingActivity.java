package com.likeit.currenciesapp.ui.me;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.base.Container;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PwdSettingActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pwd_setting);
        ButterKnife.bind(this);
        initView();
    }
    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.me_name14));
    }
    @OnClick({R.id.backBtn,R.id.tv_rlpwd_setting,R.id.tv_rlpwd_reset})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_rlpwd_setting:
                Intent intent=new Intent(mContext,PayPwdActivity.class);
                intent.putExtra("keys","1");
                startActivity(intent);
                break;
            case R.id.tv_rlpwd_edit:
                Intent intent01=new Intent(mContext,PayPwdActivity.class);
                intent01.putExtra("keys","2");
                startActivity(intent01);
                break;
            case R.id.tv_rlpwd_reset:
                Intent intent02=new Intent(mContext,ForgetPwd02Activity.class);
                startActivity(intent02);
                break;
        }
    }
}
