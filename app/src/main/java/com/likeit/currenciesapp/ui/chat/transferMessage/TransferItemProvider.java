package com.likeit.currenciesapp.ui.chat.transferMessage;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import com.likeit.currenciesapp.configs.PreferConfigs;
import com.likeit.currenciesapp.model.RedModel;
import com.likeit.currenciesapp.ui.Greenteahy.IncomeExpendActivity;
import com.likeit.currenciesapp.ui.chat.redMessage.CustomPop;
import com.likeit.currenciesapp.ui.chat.redMessage.ZNRedPackageMessage;
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
@ProviderTag(messageContent = ZNTransferMessage.class, showPortrait = true, showProgress = true, centerInHorizontal = false, showReadState = true)
// 会话界面自定义UI注解
public class TransferItemProvider extends IContainerItemProvider.MessageProvider<ZNTransferMessage> {
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
    private Bundle bundle;

    /**
     * 初始化View
     */
    @Override
    public View newView(Context context, ViewGroup group) {
        View view = LayoutInflater.from(context).inflate(
                R.layout.de_customize_message_zhuanzhang, null);
        ViewHolder holder = new ViewHolder();
        holder.message = (TextView) view.findViewById(R.id.textView1);
        holder.tv_money = (TextView) view.findViewById(R.id.tv_money);
        holder.view = (View) view.findViewById(R.id.rc_img);
        con = context;
        view.setTag(holder);
        return view;
    }


    @Override
    public void bindView(View v, int position, ZNTransferMessage content, UIMessage message) {
        ViewHolder holder = (ViewHolder) v.getTag();
        v1 = v;
        // 更改气泡样式
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            // 消息方向，自己发送的
            holder.view.setBackgroundResource(R.drawable._bg_to_zhuanzhang2);
            holder.message.setText(content.getContent()); // 设置消息内容
            holder.tv_money.setText(content.getMoney() + "元");
            //Log.d("TAG","getName-->"+message.getUserInfo().getName());
        } else {
            // 消息方向，别人发送的
            holder.view.setBackgroundResource(R.drawable._bg_from_zhuanzhang2);
            holder.message.setText("转账给你");
            holder.tv_money.setText(content.getMoney() + "元");
        }

    }

    @Override
    public Spannable getContentSummary(ZNTransferMessage data) {
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
    public void onItemClick(View view, int position, ZNTransferMessage content, UIMessage message) {
        if (message.getMessageDirection() == Message.MessageDirection.SEND) {
            // 消息方向，自己发送的
            if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
                Log.d("TAG", "getMoney-->" + content.getMoney() + "getMoney-->" + content.getContent() + "getMoney-->" + content.getExtra());
                targetId = UtilPreference.getStringValue(con, "rongcloud_id");
                Log.d("TAG", "targetId-->" + targetId);
                money = content.getMoney();
                content1 = content.getContent();
                extra = content.getExtra();
                rongcloud_id = UtilPreference.getStringValue(con, "rongcloud_id");
                tag = "1";
                //String action = PreferConfigs.RONG_INTENT_ACTION_INCOME_EXPEND;
                Intent intent = new Intent(view.getContext(), IncomeExpendActivity.class);
                //intent.setPackage(view.getContext().getPackageName());
                bundle = new Bundle();
                bundle.putInt("log_type", 100);
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
            }


        } else if (message.getConversationType() == Conversation.ConversationType.PRIVATE) {
            Log.d("TAG", "私人红包点击");
            Log.d("TAG", "targetId-->" + message.getTargetId());
            Log.d("TAG", "getMoney-->" + content.getMoney() + "getMoney-->" + content.getContent() + "Extra-->" + content.getExtra());
            targetId = message.getTargetId();
            //targetId = message.getSenderUserId();
            content1 = content.getContent();
            Log.d("TAG", "targetId-->" + targetId);
            money = content.getMoney();
            rongcloud_id = message.getTargetId();
            extra = content.getExtra();
            tag = "2";
            // String action = PreferConfigs.RONG_INTENT_ACTION_INCOME_EXPEND;
            Intent intent = new Intent(view.getContext(), IncomeExpendActivity.class);
            //intent.setPackage(view.getContext().getPackageName());
            bundle = new Bundle();
            bundle.putInt("log_type", 999);
            intent.putExtras(bundle);
            view.getContext().startActivity(intent);
        }
        // 消息方向，别人发送的
    }


    class ViewHolder {
        TextView message;
        TextView tv_money;
        View view;

    }


}
