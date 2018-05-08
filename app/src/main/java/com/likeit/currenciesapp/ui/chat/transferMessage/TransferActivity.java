package com.likeit.currenciesapp.ui.chat.transferMessage;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.RonguserModel;
import com.likeit.currenciesapp.model.TransferAccountInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.TransferAccounts02Activity;
import com.likeit.currenciesapp.ui.Greenteahy.TransferDialog01Fragment;
import com.likeit.currenciesapp.ui.Greenteahy.TransferDialog02Fragment;
import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
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
import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

import static android.R.attr.id;

public class TransferActivity extends Container {
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
    private String money;
    private DianInfoEntity mDianInfoEntity;
    private String balance;
    private TransferAccountInfoEntity transferAccountInfoEntity;
    private String targetId;
    private JSONObject obj;
    private RonguserModel ronguserModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transfer);
        ButterKnife.bind(this);
//        if (getIntent() == null || getIntent().getExtras() == null) {
//            toFinish();
//            return;
//        }
        tvHeader.setText("轉賬");
        targetId = getIntent().getStringExtra("targetId");
        initData();
        LoadDialog.show(mContext);
        //initView();
    }

//    private void initTransfer() {
//        String url = AppConfig.LIKEIT_TRANSFER_USER_INFO;
//        RequestParams params = new RequestParams();
//        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
//        params.put("transfer_user", "13823925879");
//        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
//            @Override
//            public void success(String response) {
//                Log.d("TAG222", response);
//                loadingDialog.dismiss();
//                try {
//                    JSONObject object = new JSONObject(response);
//                    String status = object.optString("status");
//                    if ("true".equals(status)) {
//                        transferAccountInfoEntity = JSON.parseObject(object.optString("info"), TransferAccountInfoEntity.class);
//                        initView();
//                    } else {
//                        ToastUtil.showS(mContext, object.optString("msg"));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void failed(Throwable e) {
//                loadingDialog.dismiss();
//            }
//        });
//    }

    private void initTransfer() {
        String url = AppConfig.LIKEIT_GET_RONGCLOUDID;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(this, "ukey"));
        params.put("rongcloud_id", targetId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG23232", response);
                try {
                    obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        ronguserModel = JSON.parseObject(obj.optString("info"), RonguserModel.class);
                        initView();
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


    private void initData() {
        String url = AppConfig.LIKEIT_GET_DIAN;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG333", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        mDianInfoEntity = JSON.parseObject(obj.optString("info"), DianInfoEntity.class);
                        initTransfer();
                    } else {
                        ToastUtil.showS(mContext, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LoadDialog.dismiss(mContext);

            }
        });
    }

    private void initView() {

        String url = ronguserModel.getPic();
        if (StringUtil.isBlank(url)) {
            iv_avatar.setBackground(this.getResources().getDrawable(R.mipmap.icon_avatar));
        } else {
            String portraitUri = AppConfig.LIKEIT_LOGO1 + url;
            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(portraitUri, iv_avatar, MyApplication.getOptions());
        }
        tv_name.setText("向" + ronguserModel.getTruename() + "轉賬");
        // tv_account.setText(ronguserModel.getUser_name());
    }

    @OnClick({R.id.backBtn, R.id.tv_apply})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_apply:
                money = ed_input.getText().toString().trim();
                String dian = mDianInfoEntity.getDianshu();
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
        TransferDialog03Fragment dialog = new TransferDialog03Fragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("ronguserModel", ronguserModel);
        bundle.putString("money", money);
        bundle.putString("targetId", targetId);
        Log.d("TAG", "dian-->" + money);
        dialog.setArguments(bundle);
        dialog.show(this.getSupportFragmentManager(), "TransferDialog01Fragment");
    }
}