package com.likeit.currenciesapp.ui.Greenteahy;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.TransferAccountInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.views.RoundImageView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferAccounts02Activity extends Container {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_avatar)
    RoundImageView iv_avatar;
    @BindView(R.id.tv_name)
    TextView tv_name;
    @BindView(R.id.tv_account)
    TextView tv_account;
    @BindView(R.id.ed_input)
    EditText ed_input;

    TransferAccountInfoEntity transferAccountInfoEntity;
    private LoaddingDialog loadingDialog;
    private DianInfoEntity dianInfoEntity;
    private String balance;
    private String money;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        dianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("dianInfoEntity");
        transferAccountInfoEntity = (TransferAccountInfoEntity) getIntent().getExtras().getSerializable("transferAccountInfoEntity");
        setContentView(R.layout.activity_transfer_accounts02);
        ButterKnife.bind(this);
        loadingDialog = new LoaddingDialog(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("轉賬");
        String url = transferAccountInfoEntity.getPic();
        if (StringUtil.isBlank(url)) {
            iv_avatar.setBackground(this.getResources().getDrawable(R.mipmap.icon_avatar));
        } else {
            String portraitUri = AppConfig.LIKEIT_LOGO1 + url;
            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, MyApplication.getOptions());
        }
        tv_name.setText("向" + transferAccountInfoEntity.getTruename() + "轉賬");
        tv_account.setText(transferAccountInfoEntity.getUser_name());
    }

    @OnClick({R.id.backBtn, R.id.tv_apply})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_apply:
                money = ed_input.getText().toString().trim();
                String dian = dianInfoEntity.getDianshu();
                balance = dian.substring(0, dian.length() - 1);
                if (StringUtil.isBlank(money)) {
                    showToast("轉賬金額不能為空");
                    return;
                }
                if (Double.valueOf(balance) - Double.valueOf(money) < 0) {
                    ToastUtil.showS(mContext, "賬戶余額不足");
                    return;
                } else {
                    transfer();
                }
                break;
        }
    }
    private void transfer() {
        TransferDialog01Fragment dialog = new TransferDialog01Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("transferAccountInfoEntity", transferAccountInfoEntity);
        bundle.putString("money", money);
        Log.d("TAG", "dian-->" + money);
        dialog.setArguments(bundle);
        dialog.show(this.getSupportFragmentManager(), "TransferDialog01Fragment");
    }
}
