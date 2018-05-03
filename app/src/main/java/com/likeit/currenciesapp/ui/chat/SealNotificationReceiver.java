package com.likeit.currenciesapp.ui.chat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.likeit.currenciesapp.ui.MainActivity;

import io.rong.push.notification.PushMessageReceiver;
import io.rong.push.notification.PushNotificationMessage;

import static android.R.attr.type;


public class SealNotificationReceiver extends PushMessageReceiver {
    private Uri uri;

    @Override
    public boolean onNotificationMessageArrived(Context context, PushNotificationMessage message) {
        return false;
    }

    @Override
    public boolean onNotificationMessageClicked(Context context, PushNotificationMessage message) {
        Intent intent = new Intent(context, MainActivity.class);
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
