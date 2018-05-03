package com.likeit.currenciesapp.ui.chat.redMessage;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.model.RedModel;
import com.likeit.currenciesapp.ui.chat.ui.widget.LoadingDialog;
import com.likeit.currenciesapp.ui.me.RedPacketActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.MyYAnimation;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/1/11.
 */

public class CustomPop extends PopupWindow implements View.OnClickListener {
    private final TextView tv_money;
    private final CircleImageView iv_header;
    private final TextView tv_name;
    private final TextView tv_look_others, tv_send_rp, tv_no_rp;
    private final ImageView iv_open_rp, iv_close;
    private final Activity mContext;
    private final String money;
    private final String red_id;
    private final String message;
    private final String group_id;
    private final LinearLayout ll_red_number;
    private final TextView tv_red_number, tv_red_total;
    private final String rongcloud_id;
    private final LoadingDialog loading;
    private final String tag;
    private View conentView;
    private String rongcloud_id01;
    private List<RedMemberModel> list;
    private Intent intent;
    private JSONArray jsonArr;
    private String jsonArr01;

    public CustomPop(RedModel redModel, final Activity context) {
        mContext = context;
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        conentView = inflater.inflate(R.layout.fragment_red_message01, null);
        int h = context.getWindowManager().getDefaultDisplay().getHeight();
        int w = context.getWindowManager().getDefaultDisplay().getWidth();
        // 设置SelectPicPopupWindow的View
        this.setContentView(conentView);
        // 设置SelectPicPopupWindow弹出窗体的宽
        this.setWidth(w - 100);
        // 设置SelectPicPopupWindow弹出窗体的高
        //this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        // 设置SelectPicPopupWindow弹出窗体可点击
        this.setFocusable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(Color.TRANSPARENT);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        // mPopupWindow.setAnimationStyle(android.R.style.Animation_Dialog);
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);
        money = redModel.getMoney();
        red_id = redModel.getRed_id();
        message = redModel.getMessage();
        group_id = redModel.getGroup_id();
        rongcloud_id = redModel.getRongcloud_id();
        tag = redModel.getTag();
        tv_money = (TextView) conentView.findViewById(R.id.tv_tip);
        tv_send_rp = (TextView) conentView.findViewById(R.id.tv_send_rp);//是否领取
        tv_no_rp = (TextView) conentView.findViewById(R.id.tv_no_rp);//祝福语
        iv_header = (CircleImageView) conentView.findViewById(R.id.iv_header);
        tv_name = (TextView) conentView.findViewById(R.id.tv_name);
        ll_red_number = (LinearLayout) conentView.findViewById(R.id.ll_red_number);//红包个数
        tv_red_number = (TextView) conentView.findViewById(R.id.tv_red_number);//已被领的个数
        tv_red_total = (TextView) conentView.findViewById(R.id.tv_red_total);//红包总数
        tv_look_others = (TextView) conentView.findViewById(R.id.tv_look_others);
        iv_open_rp = (ImageView) conentView.findViewById(R.id.iv_open_rp);
        iv_close = (ImageView) conentView.findViewById(R.id.iv_close);
        loading = new LoadingDialog(context);
        io.rong.imageloader.core.ImageLoader.getInstance().displayImage(AppConfig.LIKEIT_LOGO1 + redModel.getAvatar(), iv_header, MyApplication.getOptions());
        if ("2".equals(redModel.getTag())) {
            if (group_id == null) {
                checkRed();//私人红包检测
                loading.show();
            } else {
                checkRed1();//群红包检测
                loading.show();
            }

        } else {
            tv_money.setVisibility(View.VISIBLE);
            tv_money.setText(money + "元");
        }
        tv_name.setText(redModel.getName());
        iv_close.setOnClickListener(this);
        tv_look_others.setOnClickListener(this);
        iv_open_rp.setOnClickListener(this);
    }

    private void checkRed1() {
        Log.d("TAG981000", "red_id:" + red_id + "rongcloud_id:" + rongcloud_id + "group_id:" + group_id);
        //  Log.d("TAGMessage", message);
        Log.d("TAG981000", "rongcloud_id111:" + rongcloud_id + "group_id:" + group_id);
        String url = AppConfig.LIKEIT_check_Group_RedBao;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("red_id", red_id);
        params.put("rongcloud_id", UtilPreference.getStringValue(mContext, "rongcloud_id"));
        params.put("group_id", group_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loading.dismiss();
                Log.d("TAG858", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        if (obj.optJSONObject("info").optJSONObject("get_groupred_user").optInt("is_get") == 0) {
                            iv_open_rp.setVisibility(View.VISIBLE);
                            ll_red_number.setVisibility(View.VISIBLE);
                            int count = Integer.valueOf(obj.optJSONObject("info").optString("count"));
                            int count_rest = Integer.valueOf(obj.optJSONObject("info").optString("count_rest"));
                            int remain = Integer.valueOf(count - count_rest);
                            tv_red_number.setText("红包已领取" + remain + "/");
                            tv_red_total.setText(count + "個");
                        } else {
                            iv_open_rp.setVisibility(View.INVISIBLE);
                            ll_red_number.setVisibility(View.VISIBLE);
                            tv_send_rp.setVisibility(View.VISIBLE);
                            tv_money.setVisibility(View.VISIBLE);
                            tv_no_rp.setVisibility(View.VISIBLE);
                            tv_money.setText(obj.optJSONObject("info").optJSONObject("get_groupred_user").optJSONObject("red_data").optString("money"));
                            int count = Integer.valueOf(obj.optJSONObject("info").optString("count"));
                            int count_rest = Integer.valueOf(obj.optJSONObject("info").optString("count_rest"));
                            int remain = Integer.valueOf(count - count_rest);
                            tv_red_number.setText("红包已领取" + remain + "/");
                            tv_red_total.setText(count + "個");
                            tv_send_rp.setText("該紅包已領取");
                            tv_no_rp.setText(message);
                            list = new ArrayList<>();

                            Log.d("TAG755", obj.optJSONObject("info").optJSONArray("get_groupred_user_all").toString());
                            jsonArr = obj.optJSONObject("info").optJSONArray("get_groupred_user_all");
                            jsonArr01 = obj.optJSONObject("info").optJSONArray("get_groupred_user_all").toString();

//                            Log.d("TAG232",jsonArr.toString());
//                            for (int i = 0; i < jsonArr.length(); i++) {
//                                JSONObject object = jsonArr.optJSONObject(i);
//                             RedMemberModel redMemberModel=new RedMemberModel();
//                                redMemberModel.setId(object.optString("id"));
//                                redMemberModel.setRed_id(object.optString("red_id"));
//                                redMemberModel.setRongcloud_id(object.optString("rongcloud_id"));
//                                redMemberModel.setUser_id(object.optString("user_id"));
//                                redMemberModel.setMoney(object.optString("money"));
//                                redMemberModel.setAddtime(object.optString("addtime"));
//                                redMemberModel.setLuck(object.optString("luck"));
//                                redMemberModel.setPic(object.optString("pic"));
//                                redMemberModel.setUser_name(object.optString("user_name"));
//                                redMemberModel.setTruename(object.optString("truename"));
//                                list.add(redMemberModel);
//                            }
//                            Log.d("TAG747", list.toString());
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loading.dismiss();
            }
        });
    }

    private void checkRed() {
        String url = AppConfig.LIKEIT_Check_RedBao;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("red_id", red_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loading.dismiss();
                Log.d("TAG191", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        iv_open_rp.setVisibility(View.VISIBLE);
                    } else {
                        tv_money.setVisibility(View.VISIBLE);
                        tv_send_rp.setVisibility(View.VISIBLE);
                        tv_no_rp.setVisibility(View.VISIBLE);
                        tv_money.setText(money + "元");
                        tv_send_rp.setText(msg);
                        tv_no_rp.setText(message);
                        iv_open_rp.setVisibility(View.INVISIBLE);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loading.dismiss();
            }
        });
    }

    /**
     * 显示popupWindow
     *
     * @param parent
     * @param i
     * @param i1
     * @param i2
     */
    public void showPopupWindow(View parent, int i, int i1, int i2) {
        if (!this.isShowing()) {
            // 以下拉方式显示popupwindow
            this.showAtLocation(parent, i, i1, i2);
        } else {
            this.dismiss();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_close:
                this.dismiss();
                break;
            case R.id.iv_open_rp:
                MyYAnimation myYAnimation = new MyYAnimation();
                myYAnimation.setRepeatCount(1); //旋转的次数（无数次）
                iv_open_rp.startAnimation(myYAnimation);
                myYAnimation.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        if (group_id == null) {
                            tv_money.setVisibility(View.VISIBLE);
                            tv_no_rp.setVisibility(View.VISIBLE);
                            tv_money.setText(money + "元");
                            tv_no_rp.setText(message);
                            getRed();
                        } else {
                            tv_money.setVisibility(View.VISIBLE);
                            tv_no_rp.setVisibility(View.VISIBLE);
                            tv_money.setText(money + "元");
                            tv_no_rp.setText(message);
                            getRed1();
                        }

                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                break;
            case R.id.tv_look_others:
                this.dismiss();

//                intent = new Intent(mContext, RedPacketActivity.class);
//                mContext.startActivity(intent);
                if (group_id == null) {
                    intent = new Intent(mContext, RedPacketActivity.class);
                    mContext.startActivity(intent);
                } else {
                    if ("2".equals(tag)) {
                        if (list != null) {
                            //  Log.d("TAG554", list.toString());
                            Intent intent1 = new Intent(mContext, GroupRedListActivity.class);
                            Bundle bundle = new Bundle();
                            //bundle.putSerializable("RedList", (Serializable) list);
                            bundle.putString("RedList01", jsonArr01);
                            intent1.putExtras(bundle);
                            mContext.startActivity(intent1);
                        } else {
                            tv_look_others.setClickable(false);
                        }

                    } else {
                        intent = new Intent(mContext, RedPacketActivity.class);
                        mContext.startActivity(intent);
                    }
                }

                break;
        }
    }

    private void getRed1() {
        String url = AppConfig.LIKEIT_Get_Group_RedBao;
        Log.d("TAG9898", "red_id:" + red_id + "rongcloud_id:" + rongcloud_id + "group_id:" + group_id);
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("red_id", red_id);
        params.put("rongcloud_id", UtilPreference.getStringValue(mContext, "rongcloud_id"));
        params.put("group_id", group_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG999", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        iv_open_rp.setVisibility(View.INVISIBLE);
                        tv_money.setText(obj.optJSONObject("info").optString("money"));
                        UtilPreference.saveString(mContext, "flag", "1");
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

    private void getRed() {
        String url = AppConfig.LIKEIT_Get_RedBao;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(mContext, "ukey"));
        params.put("red_id", red_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG6969", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        iv_open_rp.setVisibility(View.INVISIBLE);
                        UtilPreference.saveString(mContext, "flag", "1");
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
