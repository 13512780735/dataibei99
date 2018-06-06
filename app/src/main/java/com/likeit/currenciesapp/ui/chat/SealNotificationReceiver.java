package com.likeit.currenciesapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import com.likeit.currenciesapp.ui.MainActivity;
import com.likeit.currenciesapp.ui.login.SplashActivity;

import io.rong.imkit.RongIM;
import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static android.R.attr.type;
import static android.content.Context.MODE_PRIVATE;


public class SealNotificationReceiver extends PushMessageReceiver {

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
//        SharedPreferences sp =context.getSharedPreferences("config", MODE_PRIVATE);
//        String cacheToken = sp.getString("loginToken", "");
//        Log.d("TAG", cacheToken);
//        if (!TextUtils.isEmpty(cacheToken)) {
//            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
//        }
        Intent intent = new Intent(context, SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("page","1");
        context.startActivity(intent);


//        Intent intent = new Intent();
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        Uri.Builder builder = Uri.parse("rong://" + this.getPackageName()).buildUpon();
//
//        builder.appendPath("conversation").appendPath(type.getName())
//                .appendQueryParameter("targetId", targetId)
//                .appendQueryParameter("title", targetName);
//        uri = builder.build();
//        intent.setData(uri);
//        context.startActivity(intent);
        return true;
    }

}
