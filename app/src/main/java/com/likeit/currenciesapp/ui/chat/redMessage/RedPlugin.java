package com.likeit.currenciesapp.ui.chat.redMessage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.likeit.currenciesapp.R;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/1/9.
 */

public class RedPlugin implements IPluginModule {
    HandlerThread mWorkThread;
    Handler mUploadHandler;
    private int REQUEST_CONTACT = 20;
    private Context mContext;
    Conversation.ConversationType conversationType;
    String targetId;
    private String message;
    private String money;
    private String red_id;
    private String group_id;
    private String extra;
    private String type;
    private String userid;

    public RedPlugin(RongContext context) {
        //super(context);
        this.mContext = context;
        mWorkThread = new HandlerThread("RongDemo");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());
    }

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.selector_hongbao);
    }

    @Override
    public String obtainTitle(Context context) {
        return "红包";
    }

    @Override
    public void onClick(final Fragment currentFragment, RongExtension extension) {
        conversationType = extension.getConversationType();
        if(conversationType==Conversation.ConversationType.GROUP){
            type="2";//群
            targetId = extension.getTargetId();
        }else if(conversationType==Conversation.ConversationType.PRIVATE){
            type="1";//私聊
            targetId = extension.getTargetId();
        }
        Intent intent = new Intent(mContext, RedPacketActivity01.class);
        intent.putExtra("targetId", targetId);
        intent.putExtra("type", type);
        extension.startActivityForPluginResult(intent, 101, this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                message = data.getStringExtra("message");
                money = data.getStringExtra("money");
                red_id = data.getStringExtra("red_id");
                group_id = data.getStringExtra("group_id");
                extra = data.getStringExtra("extra");
                userid = data.getStringExtra("userid");
                Log.e("TAG", "message-->" + message + "money-->" + money + "red_id-->" + red_id + "extra-->" + extra + "group_id-->" + group_id+ "userid-->" + userid);
                ZNRedPackageMessage rongRedPacketMessage = ZNRedPackageMessage.obtain(userid, message, money,extra,group_id, red_id);
                RongIM.getInstance().sendMessage(conversationType, targetId, rongRedPacketMessage, null, null, new RongIMClient.SendMessageCallback() {
                    @Override
                    public void onSuccess(Integer integer) {
                        Log.e("RongRedPacketProvider", "-----onSuccess--" + integer);
                    }

                    @Override
                    public void onError(Integer integer, RongIMClient.ErrorCode errorCode) {

                    }
                });
                break;
        }
    }
}
