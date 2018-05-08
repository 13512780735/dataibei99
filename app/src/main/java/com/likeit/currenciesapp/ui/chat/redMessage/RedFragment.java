package com.likeit.currenciesapp.ui.chat.redMessage;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.PswInputView;
import com.likeit.currenciesapp.views.RoundImageView;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imlib.model.Conversation;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedFragment extends DialogFragment implements View.OnClickListener {


    private String money;
    private RoundImageView iv_avatar;
    private ImageView iv_close;
    private TextView tv_name;
    private TextView tv_money;
    private String name;
    private PswInputView psw_input;
    private String targetId;
    private String message, groupNumber, rednumber, redType, type;
    private Conversation.ConversationType conversationType;
    private String group_id;
    private String userid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_red, container, false);
        Bundle bundle = getArguments();
        money = bundle.getString("money");
        message = bundle.getString("message");
        targetId = bundle.getString("targetId");
        groupNumber = bundle.getString("groupNumber");
        rednumber = bundle.getString("rednumber");
        redType = bundle.getString("redType");
        type = bundle.getString("type");
        if ("1".equals(type)) {
            conversationType = Conversation.ConversationType.PRIVATE;
        } else if ("2".equals(type)) {
            conversationType = Conversation.ConversationType.GROUP;
        }
        initView(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    private void initView(final View view) {
        iv_avatar = (RoundImageView) view.findViewById(R.id.iv_avatar);
        iv_close = (ImageView) view.findViewById(R.id.iv_close);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_money = (TextView) view.findViewById(R.id.tv_money);
        psw_input = (PswInputView) view.findViewById(R.id.psw_input);
        iv_close.setOnClickListener(this);
        tv_name.setText("轉轉寳");
        tv_money.setText("¥ " + money);
        psw_input.setInputCallBack(new PswInputView.InputCallBack() {
            @Override
            public void onInputFinish(final String result) {
                psw_input.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // view.clearResult();
                        check_paypwd(result);
                        LoadDialog.show(getActivity());
                    }
                }, 500);

            }
        });
    }

    private void check_paypwd(final String result) {
        String url = AppConfig.LIKEIT_CHECK_PAYPWD;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("paypwd", result);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        if (conversationType == Conversation.ConversationType.GROUP) {
                            refer1(result);//群红包
                            Log.e("TAG", " refer1");
                        } else if (conversationType == Conversation.ConversationType.PRIVATE) {
                            refer(result);//私聊红包
                            Log.e("TAG", " refer");
                        }

                    } else {
                        LoadDialog.dismiss(getActivity());
                        psw_input.clearResult();
                        ToastUtil.showS(getActivity(), obj.optString("msg"));
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
            }
        });
    }

    private void refer1(String result) {
        String url = AppConfig.LIKEIT_Send_Group_RedBao;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("rongcloud_id", UtilPreference.getStringValue(getActivity(), "rongcloud_id"));
        params.put("group_id", targetId);
        params.put("redtype", redType);
        params.put("count", rednumber);
        params.put("money", money);
        params.put("paypwd", result);
        params.put("remark", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(getActivity());
                Log.d("TAG7848", response);
                Log.d("TAG800", UtilPreference.getStringValue(getActivity(), "rongcloud_id"));
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    Log.d("TAG", "message22-->" + message);
                    if ("true".equals(status)) {
                        String red_id = obj.optJSONObject("info").optString("red_id");
                        group_id = obj.optJSONObject("info").optString("group_id");
                        userid = obj.optJSONObject("info").optString("rongcloud_id");
                        Log.e("TAG11", "red_id-->" + red_id + "group_id-->" + group_id + "money-->" + money + "message-->" + message + "extra-->" + red_id);
                        ToastUtil.showS(getActivity(), "发送成功");
                        Intent intent = getActivity().getIntent();
                        intent.putExtra("message", message);
                        intent.putExtra("money", money);
                        intent.putExtra("red_id", red_id);
                        intent.putExtra("group_id", group_id);
                        intent.putExtra("userid", userid);
                        intent.putExtra("extra", red_id);
                        getActivity().setResult(100, intent);
                        getActivity().finish();
                    } else {
                        ToastUtil.showS(getActivity(), obj.optString("msg"));
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
                LoadDialog.dismiss(getActivity());
            }
        });
    }

    private void refer(String result) {
        String url = AppConfig.LIKEIT_Send_RedBao;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(getActivity(), "ukey"));
        params.put("transfer_rongcloudid", targetId);
        params.put("money", money);
        params.put("paypwd", result);
        params.put("remark", "");
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(getActivity());
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    Log.d("TAG", "message22-->" + message);
                    if ("true".equals(status)) {
                        String red_id = obj.optJSONObject("info").optString("red_id");
                        Log.d("TAG", "red_id-->" + red_id);
                        Log.e("TAG22", "red_id-->" + red_id + "group_id-->" + group_id + "money-->" + money + "message-->" + message + "extra-->" + red_id);
                        ToastUtil.showS(getActivity(), "发送成功");
                        Intent intent = getActivity().getIntent();
                        intent.putExtra("message", message);
                        intent.putExtra("money", money);
                        intent.putExtra("red_id", red_id);
                        intent.putExtra("group_id", "");
                        intent.putExtra("extra", red_id);
                        intent.putExtra("userid", targetId);
                        getActivity().setResult(100, intent);
                        getActivity().finish();
                        // getActivity().onBackPressed();
                        //  MyActivityManager.getInstance().backToMain();
                    } else {
                        ToastUtil.showS(getActivity(), obj.optString("msg"));
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
                LoadDialog.dismiss(getActivity());
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                getDialog().dismiss();
                break;
        }
    }
}
