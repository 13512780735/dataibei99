package com.likeit.currenciesapp.ui.chat.ui.fragment;


import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.ui.chat.ui.KeFuSearchActivity;

import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyConversationListFragment extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        initTopBar();
        initIm();
    }

    private TextView top_bar_right_tv;

    private void initTopBar() {
        top_bar_back_img = (ImageView) findViewById(R.id.top_bar_back_img);
        top_bar_back_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toFinish();
            }
        });
        top_bar_title = (TextView) findViewById(R.id.top_bar_title);
        top_bar_right_tv = (TextView) findViewById(R.id.top_bar_right_tv);
        top_bar_title.setText("聯繫人");
        top_bar_right_tv.setText("搜索");
        top_bar_right_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toActivity(KeFuSearchActivity.class);
            }
        });
    }

    private void initIm() {
        ConversationListFragment conversationListFragment = new ConversationListFragment();
        //conversationListFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversationlist")
                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话聚合显示
                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
                .build();
        conversationListFragment.setUri(uri);
        getSupportFragmentManager().beginTransaction().add(R.id.container, conversationListFragment).commit();
    }

}
