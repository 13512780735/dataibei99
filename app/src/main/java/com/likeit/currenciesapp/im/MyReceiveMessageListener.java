package com.likeit.currenciesapp.im;

import android.util.Log;

import com.hwangjr.rxbus.RxBus;
import com.likeit.currenciesapp.rxbus.RefreshEvent;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Message;

public class MyReceiveMessageListener implements RongIMClient.OnReceiveMessageListener {

    @Override
    public boolean onReceived(Message message, int i) {
//        Logger.d("收到消息");
        RxBus.get().post(new RefreshEvent(RefreshEvent.GET_NEW_MSG));
        Log.d("TAg", "message22:" + message.getConversationType().getName());
        SealUserInfoManager.getInstance().getUserInfo(message.getSenderUserId());
        Log.d("TAg", "message:" + message);
        Log.d("TAg", "message66:" + message.getSenderUserId());

        return false;
    }


}
