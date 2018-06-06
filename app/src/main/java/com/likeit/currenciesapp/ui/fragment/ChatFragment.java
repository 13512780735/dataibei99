package com.likeit.currenciesapp.ui.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.ui.base.BaseFragment;
import com.likeit.currenciesapp.ui.chat.SealAppContext;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.ui.ContactActivity;
import com.likeit.currenciesapp.ui.chat.ui.KeFuListActivity;
import com.likeit.currenciesapp.ui.chat.ui.KeFuSearchActivity;
import com.likeit.currenciesapp.ui.chat.ui.fragment.ConversationListAdapterEx;
import com.likeit.currenciesapp.ui.chat.ui.widget.MorePopWindow;

import io.rong.imkit.RongContext;
import io.rong.imkit.RongIM;
import io.rong.imkit.fragment.ConversationListFragment;
import io.rong.imlib.model.Conversation;

import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends BaseFragment implements View.OnClickListener {


    private TextView tvHeader;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private ImageView ivRight;
    private LinearLayout ll_contact;
    private RelativeLayout rl_contacts;
    private RelativeLayout rl_online_service;

    /**
     * 会话列表的fragment
     */
    private ConversationListFragment mConversationListFragment = null;
    private boolean isDebug;
    private Context mContext;
    private Conversation.ConversationType[] mConversationsTypes = null;
    private SharedPreferences sp;

    @Override
    protected int setContentView() {
        return R.layout.fragment_chat;
    }

    @Override
    protected void lazyLoad() {
        sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);
        String cacheToken = sp.getString("loginToken", "");
        Log.d("TAG", cacheToken);
        if (!TextUtils.isEmpty(cacheToken)) {
            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
        }
        mLoginUserInfoEntity = (LoginUserInfoEntity) getActivity().getIntent().getSerializableExtra("userInfo");
        initData();
        initView();
        initIm();
    }

    private void initData() {
        SealUserInfoManager.getInstance().getAllUserInfo();

    }

    @Override
    public void onResume() {
        super.onResume();
        initData();

    }

    private void initView() {
        tvHeader = findViewById(R.id.tv_header);
        ivRight = findViewById(R.id.iv_header_right);
        ll_contact = findViewById(R.id.ll_contact);
        rl_contacts = findViewById(R.id.rl_contacts);
        rl_online_service = findViewById(R.id.rl_online_service);
        tvHeader.setText(this.getResources().getString(R.string.chat_name01));
        if (mLoginUserInfoEntity.getIs_kefu() == 0) {
            ivRight.setImageResource(R.mipmap.icon_add);
            rl_contacts.setOnClickListener(this);
            rl_online_service.setOnClickListener(this);

        } else if (mLoginUserInfoEntity.getIs_kefu() == 1) {
            ivRight.setImageResource(R.mipmap.nav_search);
            ll_contact.setVisibility(View.GONE);
        }

        initListener();
    }

    private void initListener() {
        ivRight.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_right:
                if (mLoginUserInfoEntity.getIs_kefu() == 0) {
                    MorePopWindow morePopWindow = new MorePopWindow(getActivity());
                    morePopWindow.showPopupWindow(ivRight);
                } else if (mLoginUserInfoEntity.getIs_kefu() == 1) {
                    toActivity(KeFuSearchActivity.class);
                }
                break;
            case R.id.rl_contacts:
                toActivity(ContactActivity.class);
                break;
            case R.id.rl_online_service:
                toActivity(KeFuListActivity.class);
                break;
        }
    }

    private void initIm() {
//        ConversationListFragment conversationListFragment = new ConversationListFragment();
//        Uri uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversationlist")
//                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
//                .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
//                .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
//                .build();
//        conversationListFragment.setUri(uri);
//        getChildFragmentManager().beginTransaction().add(R.id.container, conversationListFragment).commit();
        ConversationListFragment listFragment = new ConversationListFragment();
        listFragment.setAdapter(new ConversationListAdapterEx(RongContext.getInstance()));
        Uri uri;
        if (isDebug) {
            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "true") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "true")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "true")
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM,
                    Conversation.ConversationType.DISCUSSION
            };

        } else {
            uri = Uri.parse("rong://" + getActivity().getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversationlist")
                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "true")//系统
                    .build();
            mConversationsTypes = new Conversation.ConversationType[]{Conversation.ConversationType.PRIVATE,
                    Conversation.ConversationType.GROUP,
                    Conversation.ConversationType.PUBLIC_SERVICE,
                    Conversation.ConversationType.APP_PUBLIC_SERVICE,
                    Conversation.ConversationType.SYSTEM
            };
        }
        listFragment.setUri(uri);
        mConversationListFragment = listFragment;
        getChildFragmentManager().beginTransaction().add(R.id.container, mConversationListFragment).commit();
    }

}
