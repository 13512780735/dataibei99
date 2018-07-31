package com.likeit.currenciesapp.ui.me;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.base.Container;
import com.tencent.bugly.beta.Beta;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends Container {


    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_rlpwd_setting)
    RelativeLayout tv_rlpwd_setting;
    @BindView(R.id.tv_rlFace_information)
    RelativeLayout tv_rlFace_information;
    @BindView(R.id.tv_rlpersonage_information)
    RelativeLayout tv_rlpersonage_information;
    @BindView(R.id.tv_rlchange_pwd)
    RelativeLayout tv_rlchange_pwd;
    @BindView(R.id.tv_rlupdate_version)
    RelativeLayout tv_rlupdate_version;
    @BindView(R.id.version_tv)
    TextView versionTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.me_name14));
        versionTv.setText(getVersion());
    }
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return "V" + version;
        } catch (Exception e) {
            return "";
        }
    }
    @OnClick({R.id.backBtn, R.id.tv_rlpwd_setting, R.id.tv_rlFace_information, R.id.tv_rlpersonage_information, R.id.tv_rlchange_pwd, R.id.tv_rlupdate_version})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_rlpwd_setting:
                toActivity(PwdSettingActivity.class);
                break;
            case R.id.tv_rlFace_information:
                showToast("待开发");
                break;
            case R.id.tv_rlpersonage_information:
                showToast("待开发");
                break;
            case R.id.tv_rlchange_pwd:
                toActivity(ModifyPasswdActivity.class);
                break;
            case R.id.tv_rlupdate_version:  //showToast("待开发");

                Beta.checkUpgrade();
                break;
        }
    }
}
