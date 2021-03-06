package com.likeit.currenciesapp.ui.chat.redMessage;


import android.app.Activity;
import android.content.Context;
import android.text.Spannable;
import android.text.SpannableString;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.RedModel;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import io.rong.imkit.emoticon.AndroidEmoji;
import io.rong.imkit.model.ProviderTag;
import io.rong.imkit.model.UIMessage;
import io.rong.imkit.widget.provider.IContainerItemProvider;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;


/**
 * 自定义融云IM消息提供者
 *
 * @author lsy
 */
@ProviderTag(messageContent = ZNRedPackageMessage.class, showPortrait = true, showProgress = true, centerInHorizontal = false,showReadState = true)
// 会话界面自定义UI注解
public class RedPackageItemProvider extends IContainerItemProvider.MessageProvider<ZNRedPackageMessage> {
    private Context con;
    private View contentview;
    private View v1;
    private String targetId;
    private String tag;
    private String money;
    private String red_id;
    private String flag = null;
    private String group_id;
    private String extra;
    private String content1;
    private String rongcloud_id;

    /**
     * 初始化View
     */
    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.de_customize_message_red_packet, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.textView1);
        holder.view = (View) view.findViewById(R.id.rc_img);
        con = context;
        view.setTag(holder);
        return view;
    }

    @Override
    public void bindView(View v, int position, ZNRedPackageMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        v1 = v;
        // 更改气泡样式
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            // 消息方向，自己发送的
            holder.view.setBackgroundResource(R.drawable._bg_from_hongbao2);
            //Log.d("TAG","getName-->"+message.getUserInfo().getName());
        } else {
            // 消息方向，别人发送的
            holder.view.setBackgroundResource(R.drawable._bg_to_hongbao2);
        }
        holder.message.setText(content.getContent()); // 设置消息内容
    }

    @Override
    public Spannable getContentSummary(ZNRedPackageMessage data) {
        if (data == null)
            return null;

        String content = data.getContent();
        if (content != null) {
            if (content.length() > 100) {
                content = content.substring(0, 100);
            }
            return new SpannableString(AndroidEmoji.ensure(content));
        }
        return null;
    }

    @Override
    public void onItemClick(View view, int position, ZNRedPackageMessage content, UIMessage message) {
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            // 消息方向，自己发送的
            if (message.getConversationType() == Conversation.ConversationType.GROUP) {
                rongcloud_id = UtilPreference.getStringValue(con, "rongcloud_id");
                Log.d("TAG", "rongcloud_id-->" + rongcloud_id);
                Log.d("TAG", "group_id-->" + message.getTargetId());
                Log.d("TAG", "extra-->" + extra);
                Log.d("TAG", "red_id-->" + content.getExtra());
                Log.d("TAG", "money-->" + money);
                money = content.getMoney();
                content1 = content.getContent();
                red_id = content.getExtra();
                group_id = message.getTargetId();
                extra = content.getExtra();
                tag = "1";
                getUserInfo1(rongcloud_id, tag, money, red_id);
            } else if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
                Log.d("TAG", "getMoney-->" + content.getMoney() + "getMoney-->" + content.getContent() + "getMoney-->" + content.getExtra());
                targetId = UtilPreference.getStringValue(con, "rongcloud_id");
                Log.d("TAG", "targetId-->" + targetId);
                money = content.getMoney();
                content1 = content.getContent();
                red_id = content.getExtra();
                group_id = message.getTargetId();
                extra = content.getExtra();
                rongcloud_id = UtilPreference.getStringValue(con, "rongcloud_id");
                tag = "1";
                getUserInfo(targetId, tag, money, red_id);
            }


        } else {
            if (message.getConversationType() == Conversation.ConversationType.GROUP) {
                Log.d("TAG", "群红包点击");
                Log.d("TAG", "group_id-->" + message.getTargetId() + "rongcloud_id" + message.getMessage().getSenderUserId());
                Log.d("TAG", "red_id-->" + content.getExtra());
                targetId = message.getTargetId();
                red_id = content.getExtra();
                group_id = message.getTargetId();
                money = content.getMoney();
                tag = "2";
                money = content.getMoney();
                rongcloud_id = message.getMessage().getSenderUserId();
                getUserInfo(targetId, tag, money, red_id);
            } else if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
                Log.d("TAG", "私人红包点击");
                Log.d("TAG", "targetId-->" + message.getTargetId());
                Log.d("TAG", "getMoney-->" + content.getMoney() + "getMoney-->" + content.getContent() + "Extra-->" + content.getExtra());
                Log.d("TAG", "group_id-->" + content.getGroup_id() + "red_id-->" + content.getRed_id());
                targetId = message.getTargetId();
                //targetId = message.getSenderUserId();
                content1 = content.getContent();
                Log.d("TAG", "targetId-->" + targetId);
                money = content.getMoney();
                red_id = content.getExtra();
                group_id = content.getGroup_id();
                rongcloud_id = message.getTargetId();
                extra = content.getExtra();
                tag = "2";
                getUserInfo(targetId, tag, money, red_id);
            }
            // 消息方向，别人发送的

        }
    }

    private void getUserInfo1(String targetId, String tag, String money, String red_id) {
        String url = AppConfig.LIKEIT_GET_RONGCLOUDID;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(con, "ukey"));
        params.put("rongcloud_id", rongcloud_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        String truename = obj.optJSONObject("info").optString("truename");
                        String avatar = obj.optJSONObject("info").optString("pic");
                        RedModel mRedModel = new RedModel();
                        mRedModel.setMoney(money);
                        mRedModel.setTag(tag);
                        mRedModel.setRed_id(red_id);
                        mRedModel.setName(truename);
                        mRedModel.setExtra(extra);
                        mRedModel.setMessage(content1);
                        mRedModel.setGroup_id(group_id);
                        mRedModel.setAvatar(avatar);
                        mRedModel.setRongcloud_id(rongcloud_id);
                        //mRedModel.setRed_id(red_id);
                        CustomPop pop = new CustomPop(mRedModel, (Activity) con);
                        pop.showPopupWindow(v1, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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

    private void getUserInfo(String targetId, String tag, String money, String red_id) {
        String url = AppConfig.LIKEIT_GET_RONGCLOUDID;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(con, "ukey"));
        params.put("rongcloud_id", rongcloud_id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        String truename = obj.optJSONObject("info").optString("truename");
                        String avatar = obj.optJSONObject("info").optString("pic");
                        RedModel mRedModel = new RedModel();
                        mRedModel.setMoney(money);
                        mRedModel.setTag(tag);
                        mRedModel.setRed_id(red_id);
                        mRedModel.setName(truename);
                        mRedModel.setExtra(extra);
                        mRedModel.setMessage(content1);
                        mRedModel.setGroup_id(group_id);
                        mRedModel.setAvatar(avatar);
                        mRedModel.setRongcloud_id(rongcloud_id);
                        //mRedModel.setRed_id(red_id);
                        CustomPop pop = new CustomPop(mRedModel, (Activity) con);
                        pop.showPopupWindow(v1, Gravity.CENTER | Gravity.CENTER_HORIZONTAL, 0, 0);
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
    public void onItemLongClick(View view, int position, ZNRedPackageMessage content, UIMessage message) {

    }

    class ViewHolder {
        TextView message;
        View view;

    }


}
