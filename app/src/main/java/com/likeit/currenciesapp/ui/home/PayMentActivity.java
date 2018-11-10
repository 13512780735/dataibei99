package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.RoundImageView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;

public class PayMentActivity extends Container {
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
    private String ukey;
    private String mUserId;
    private String money;
    private String balance;
    private String truename;
    private String avatar;
    private String account;
    private DianInfoEntity dianInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_ment);
        ButterKnife.bind(this);
        ukey = UtilPreference.getStringValue(this, "ukey");

        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        dianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        mUserId = getIntent().getExtras().getString("userId");
        Log.d("TAG", "mUserId-->" + mUserId);
        initData();
        initView();
    }

    private void initData() {
        String url = AppConfig.LIKEIT_GET_RONGCLOUDID;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("rongcloud_id", mUserId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {

            @Override
            public void success(String response) {
                Log.d("TAG111", response);
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        truename = obj.optJSONObject("info").optString("truename");
                        avatar = obj.optJSONObject("info").optString("pic");
                        account = obj.optJSONObject("info").optString("user_name");
                        Log.d("TAG", truename + avatar);

                    }

                    tv_account.setText(account);
                    tv_name.setText("向" + truename + "付款");
                    Glide.with(PayMentActivity.this).load(AppConfig.LIKEIT_LOGO1 + avatar).centerCrop().into(iv_avatar);
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


    private void initView() {
        tvHeader.setText("付款");
//        String url = transferAccountInfoEntity.getPic();
//        if (StringUtil.isBlank(url)) {
//            iv_avatar.setBackground(this.getResources().getDrawable(R.mipmap.icon_avatar));
//        } else {
//            String portraitUri = AppConfig.LIKEIT_LOGO1 + url;
//            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, MyApplication.getOptions());
//        }
//        tv_name.setText("向" + transferAccountInfoEntity.getTruename() + "轉賬");
//        tv_account.setText(transferAccountInfoEntity.getUser_name());
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
        PayFragment dialog = new PayFragment();
        Bundle bundle = new Bundle();
        //  bundle.putSerializable("transferAccountInfoEntity", transferAccountInfoEntity);
        bundle.putString("money", money);
        bundle.putString("avatar", avatar);
        bundle.putString("truename", truename);
        bundle.putString("account", account);
        Log.d("TAG", "dian-->" + money);
        Log.d("TAG", "account-->" + account);
        Log.d("TAG", "truename-->" + truename);
        dialog.setArguments(bundle);
        dialog.show(this.getSupportFragmentManager(), "PayFragment");
    }
}
