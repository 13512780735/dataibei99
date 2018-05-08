package com.likeit.currenciesapp.ui.Greenteahy;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.TransferAccountInfoEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TransferAccountsActivity extends Container {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.apply_et_account)
    EditText edaccount;
    @BindView(R.id.tv_apply)
    TextView tv_apply;
    private TransferAccountInfoEntity transferAccountInfoEntity;
    private TransferAccountsActivity mContext;
    private DianInfoEntity dianInfoEntity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        dianInfoEntity = (DianInfoEntity) getIntent().getExtras().getSerializable("Dian");
        setContentView(R.layout.activity_transfer_accounts);
        MyActivityManager.getInstance().addActivity(this);
        mContext=this;
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvHeader.setText("轉賬");
    }
    @OnClick({R.id.backBtn,R.id.tv_apply})
    public void onClick(View v){
        switch (v.getId()){
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.tv_apply:
                next();
                break;
        }
    }

    private void next() {
        String account=edaccount.getText().toString().trim();
        if(StringUtil.isBlank(account)){
            ToastUtil.showS(mContext,"賬號不能為空");
            return;
        }
        LoadDialog.show(mContext);
        String url= AppConfig.LIKEIT_TRANSFER_USER_INFO;
        RequestParams params=new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext,"ukey"));
        params.put("transfer_user",account);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(mContext);
                try {
                    JSONObject object=new JSONObject(response);
                    String status=object.optString("status");
                    if("true".equals(status)){
                        transferAccountInfoEntity= JSON.parseObject(object.optString("info"),TransferAccountInfoEntity.class);
                        Bundle bundle=new Bundle();
                        bundle.putSerializable("transferAccountInfoEntity",transferAccountInfoEntity);
                        bundle.putSerializable("dianInfoEntity",dianInfoEntity);
                       // toActivity(TransferAccounts02Activity.class,bundle);
                        Intent intent=new Intent(mContext,TransferAccounts02Activity.class);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }else{
                        ToastUtil.showS(mContext,object.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                LoadDialog.dismiss(mContext);
            }
        });
    }

}
