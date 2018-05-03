package com.likeit.currenciesapp.ui.chat.transferMessage;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.chat.redMessage.RedPacketActivity01;
import com.likeit.currenciesapp.ui.chat.redMessage.ZNRedPackageMessage;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongExtension;
import io.rong.imkit.RongIM;
import io.rong.imkit.plugin.IPluginModule;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

/**
 * Created by Administrator on 2018/1/9.
 */

public class TransferPlugin implements IPluginModule {
    HandlerThread mWorkThread;
    Handler mUploadHandler;
    private int REQUEST_CONTACT = 20;
    private Context mContext;
    Conversation.ConversationType conversationType;
    String targetId;
    private String message;
    private String money;
    private String extra;
    private String type;
    private String userid;

    public TransferPlugin(RongContext context) {
        //super(context);
        this.mContext = context;
        mWorkThread = new HandlerThread("RongDemo");
        mWorkThread.start();
        mUploadHandler = new Handler(mWorkThread.getLooper());
    }

    @Override
    public Drawable obtainDrawable(Context context) {
        return ContextCompat.getDrawable(context, R.drawable.selector_zhuan);
    }

    @Override
    public String obtainTitle(Context context) {
        return "转账";
    }

    @Override
    public void onClick(final Fragment currentFragment, RongExtension extension) {
        conversationType = extension.getConversationType();
        targetId = extension.getTargetId();
        Log.d("TAG", "targetId:" + targetId);
        Intent intent = new Intent(mContext, TransferActivity.class);
        intent.putExtra("targetId", targetId);
        extension.startActivityForPluginResult(intent, 101, this);


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case 100:
                message = data.getStringExtra("message");
                money = data.getStringExtra("money");
                extra = data.getStringExtra("extra");
                userid = data.getStringExtra("userid");
                Log.e("TAG", "message-->" + message + "money-->" + money + "extra-->" + extra + "userid-->" + userid);
                ZNTransferMessage rongRedPacketMessage = ZNTransferMessage.obtain(userid, message, money, extra);
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
