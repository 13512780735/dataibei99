package com.likeit.currenciesapp.ui.login;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;

import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.RegisterSourceAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.RegisterSourceEntity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class RegisterActivity extends Container implements PullToRefreshBase.OnRefreshListener2<ScrollView> {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.ll_register_scrollview)
    PullToRefreshScrollView mPullToRefreshScrollView;

    @BindView(R.id.login_et_name)
    EditText etName;
    @BindView(R.id.login_et_phone)
    EditText etPhone;
    @BindView(R.id.login_et_code)
    EditText etCode;
    @BindView(R.id.send_code_btn)//点击发送
            TextView tvCode;
    @BindView(R.id.login_et_new_pwd)
    EditText etPwd;
    @BindView(R.id.login_et_sure_pwd)
    EditText etSurePwd;
    @BindView(R.id.ll_resource)//注册来源
            LinearLayout llreSource;
    @BindView(R.id.ll_introducer)//注册来源
            LinearLayout llIntroducer;//註冊人信息
    @BindView(R.id.login_et_referrer_name)
    EditText etReferrerName;
    @BindView(R.id.login_et_referrer_phone)
    EditText etReferrerNPhone;
    @BindView(R.id.checkbox01)
    CheckBox mCheckBox01;
    @BindView(R.id.login_tv_register)
    TextView tvRegister;
    @BindView(R.id.login_et_resource)
    TextView tvResource;


    private final static int TIME = 101;
    private ArrayList<RegisterSourceEntity> dataSource;
    private RegisterSourceAdapter groupAdapter;
    private PopupWindow popupWindow;
    private View view;
    private ListView lv_group;
    private int time_tatol = 60;

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
    private String sourcePhone;
    private String sourceName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
        dataSource = new ArrayList<RegisterSourceEntity>();
        initSource();
        showProgress("Loading...");
        initView();
    }

    private void initSource() {
        String url = AppConfig.LIKEIT_GET_REGFROM;
        RequestParams params = new RequestParams();
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
                //   Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String code = obj.optString("code");
                    String status = obj.optString("status");
                    String message = obj.optString("msg");
                    if ("true".equals(status) && "0".equals(code)) {
                        JSONArray array = obj.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            RegisterSourceEntity mRegisterSourceEntity = new RegisterSourceEntity();
                            JSONObject object = array.optJSONObject(i);
                            mRegisterSourceEntity.setId(object.optString("id"));
                            mRegisterSourceEntity.setAgid(object.optString("agid"));
                            mRegisterSourceEntity.setName(object.optString("name"));
                            dataSource.add(mRegisterSourceEntity);
                        }
                        groupAdapter.notifyDataSetChanged();
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

            @Override
            public void onFinish() {
                super.onFinish();
                disShowProgress();
            }
        });
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.register_name14));
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
    }

    private void showPopup() {
        if (popupWindow == null) {
            LayoutInflater layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            view = layoutInflater.inflate(R.layout.operationinto_popmenulist, null);

            lv_group = (ListView) view.findViewById(R.id.menulist);
            groupAdapter = new RegisterSourceAdapter(this, dataSource);
            lv_group.setAdapter(groupAdapter);
            // 创建一个PopuWidow对象
            popupWindow = new PopupWindow(view, ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT);
            popupWindow.setBackgroundDrawable(getResources().getDrawable(
                    R.drawable.filter_bg));
        }

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(false);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        // 显示的位置为:屏幕的宽度的一半-PopupWindow的高度的一半
        int xPos = windowManager.getDefaultDisplay().getWidth() / 2
                - popupWindow.getWidth() / 2;
        Log.i("coder", "xPos:" + xPos);
        popupWindow.setAnimationStyle(R.style.style_pop_animation);// 动画效果必须放在showAsDropDown()方法上边，否则无效
        backgroundAlpha(0.5f);// 设置背景半透明
        popupWindow.showAsDropDown(tvResource);

        lv_group.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int position, long id) {
                // ToastUtil.showS(RegisterActivity.this, dataSource.get(position).getName());
                tvResource.setText(dataSource.get(position).getName());
                if (position == 0) {
                    llIntroducer.setVisibility(View.VISIBLE);
                } else {
                    llIntroducer.setVisibility(View.GONE);
                }
                if (popupWindow != null) {
                    popupWindow.dismiss();
                    backgroundAlpha(1.0f);// 当点击屏幕时，使半透明效果取消
                }
            }
        });
    }

    // 设置popupWindow背景半透明
    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgAlpha;// 0.0-1.0
        getWindow().setAttributes(lp);
    }


    @OnClick({R.id.backBtn, R.id.send_code_btn, R.id.login_tv_register, R.id.protocol_tv, R.id.ll_resource})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
            case R.id.send_code_btn:
                getCheckPhone();
                break;
            case R.id.login_tv_register:

                String name = etName.getText().toString().trim();
                String phoneCheck = etCode.getText().toString().trim();
                final String phone = etPhone.getText().toString().trim();
                final String passwd = etPwd.getText().toString().trim();
                String passwd1 = etSurePwd.getText().toString().trim();
                String source = tvResource.getText().toString().trim();
                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(phoneCheck) || TextUtils.isEmpty(phone) || TextUtils.isEmpty(passwd) || TextUtils.isEmpty(passwd1)) {
                    showToast("請填寫完整信息");
                    return;
                }
                if (TextUtils.isEmpty(source)) {
                    showToast("請輸入註冊來源");
                    return;
                }

                if (!mCheckBox01.isChecked()) {
                    showToast("請同意條款");
                    return;
                }
                if ("介绍人".equals(source)) {
                    sourceName = etReferrerName.getText().toString().trim();
                    sourcePhone = etReferrerNPhone.getText().toString().trim();
                    if (TextUtils.isEmpty(sourceName) || TextUtils.isEmpty(sourcePhone)) {
                        showToast("請填寫完整信息");
                        return;
                    }
                }
                showProgress("Loading...");
                String url = AppConfig.LIKEIT_REGISTER;
                RequestParams params = new RequestParams();
                params.put("getuicid", "");
                params.put("mobile", phone);
                params.put("password", passwd);
                params.put("telcode", phoneCheck);
                params.put("truename", name);
                params.put("regfrom_type", source);
                params.put("jname", sourceName);
                params.put("jtel", sourcePhone);
                HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                    @Override
                    public void success(String response) {
                        disShowProgress();
                        JSONObject object = null;
                        try {
                            object = new JSONObject(response);
                            String code = object.optString("code");
                            String status = object.optString("status");
                            String message = object.optString("msg");
                            if ("true".equals(status) && "0".equals(code)) {
                                ToastUtil.showS(mContext, "註冊成功");
                                UtilPreference.saveString(mContext, "phone", phone);
                                UtilPreference.saveString(mContext, "pwd", passwd);
                                toActivityFinish(LoginActivity.class);
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

                    @Override
                    public void onFinish() {
                        super.onFinish();
                        disShowProgress();
                    }
                });
                break;
            case R.id.protocol_tv:
                toActivity(WebTermsServiceActivity.class);
                break;
            case R.id.ll_resource:
                showPopup();
                break;
        }
    }

    private void getCheckPhone() {

        String phoneNum = etPhone.getText().toString().trim();
        if (TextUtils.isEmpty(phoneNum)) {
            showToast("請輸入手機號碼");
            return;
        }
//        if (!StringUtil.isCellPhone(phoneNum)) {
//            ToastUtil.showS(mContext, "請輸入正確的手機號");
//            return;
//        }
        showProgress("Loading...");
        String url = AppConfig.LIKEIT_SEND_SMS;
        RequestParams params = new RequestParams();
        params.put("mobile", phoneNum);
        params.put("type", "1");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                disShowProgress();
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

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

}
