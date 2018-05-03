package com.likeit.currenciesapp.ui.chat.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.SealAppContext;
import com.likeit.currenciesapp.ui.chat.SealConst;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.db.Friend;
import com.likeit.currenciesapp.ui.chat.server.broadcast.BroadcastManager;
import com.likeit.currenciesapp.ui.chat.server.pinyin.CharacterParser;
import com.likeit.currenciesapp.ui.chat.server.pinyin.PinyinComparator;
import com.likeit.currenciesapp.ui.chat.server.pinyin.SideBar;
import com.likeit.currenciesapp.ui.chat.ui.activity.GroupListActivity;
import com.likeit.currenciesapp.ui.chat.ui.activity.NewFriendListActivity;
import com.likeit.currenciesapp.ui.chat.ui.activity.UserDetailActivity;
import com.likeit.currenciesapp.ui.chat.ui.adapter.FriendListAdapter;
import com.likeit.currenciesapp.ui.chat.ui.widget.MorePopWindow;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;

public class ContactActivity extends Container implements View.OnClickListener {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.iv_header_right)
    ImageView ivRight;
    @BindView(R.id.iv_header_left)
    ImageView ivLeft;
//    private SelectableRoundedImageView mSelectableRoundedImageView;
//    private TextView mNameTextView;
    private TextView mNoFriends;
    private TextView mUnreadTextView;
    private View mHeadView;
    private ListView mListView;
    private PinyinComparator mPinyinComparator;
    private SideBar mSidBar;
    /**
     * 中部展示的字母提示
     */
    private TextView mDialogTextView;

    private List<Friend> mFriendList;
    private List<Friend> mFilteredFriendList;
    /**
     * 好友列表的 mFriendListAdapter
     */
    private FriendListAdapter mFriendListAdapter;
    /**
     * 汉字转换成拼音的类
     */
    private CharacterParser mCharacterParser;
    /**
     * 根据拼音来排列ListView里面的数据类
     */

    private String mId;
    private String mCacheName;

    private static final int CLICK_CONTACT_FRAGMENT_FRIEND = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        ButterKnife.bind(this);
        SealUserInfoManager.getInstance().getAllUserInfo();
        initView();
        initData();
        updateUI();
        refreshUIListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SealUserInfoManager.getInstance().getAllUserInfo();
        initData();
        updateUI();
        refreshUIListener();
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.chat_name02));
        ivLeft.setBackgroundResource(R.drawable.header_back);
        ivRight.setBackgroundResource(R.mipmap.icon_add);
        mListView = (ListView) findViewById(R.id.listview);
        mNoFriends = (TextView) findViewById(R.id.show_no_friend);
        mSidBar = (SideBar) findViewById(R.id.sidrbar);
        mDialogTextView = (TextView) findViewById(R.id.group_dialog);
        mSidBar.setTextView(mDialogTextView);
        LayoutInflater mLayoutInflater = LayoutInflater.from(this);
        mHeadView = mLayoutInflater.inflate(R.layout.item_contact_list_header,
                null);
        mUnreadTextView = (TextView) mHeadView.findViewById(R.id.tv_unread);
        RelativeLayout newFriendsLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_newfriends);
        RelativeLayout groupLayout = (RelativeLayout) mHeadView.findViewById(R.id.re_chatroom);
//        RelativeLayout selfLayout = (RelativeLayout) mHeadView.findViewById(R.id.contact_me_item);
//        mSelectableRoundedImageView = (SelectableRoundedImageView) mHeadView.findViewById(R.id.contact_me_img);
//        mNameTextView = (TextView) mHeadView.findViewById(R.id.contact_me_name);
        updatePersonalUI();
        mListView.addHeaderView(mHeadView);
        mNoFriends.setVisibility(View.VISIBLE);

       // selfLayout.setOnClickListener(this);
        groupLayout.setOnClickListener(this);
        newFriendsLayout.setOnClickListener(this);
        //设置右侧触摸监听
        mSidBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mFriendListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mListView.setSelection(position);
                }

            }
        });
    }

    private void initData() {
        mFriendList = new ArrayList<>();
        FriendListAdapter adapter = new FriendListAdapter(mContext, mFriendList);
        mListView.setAdapter(adapter);
        mFilteredFriendList = new ArrayList<>();
        //实例化汉字转拼音类
        mCharacterParser = CharacterParser.getInstance();
        mPinyinComparator = PinyinComparator.getInstance();
    }


    @OnClick({R.id.iv_header_right, R.id.iv_header_left})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_header_left:
                onBackPressed();
                break;
            case R.id.iv_header_right:
                MorePopWindow morePopWindow = new MorePopWindow(this);
                morePopWindow.showPopupWindow(ivRight);
                break;
            case R.id.re_newfriends:
                mUnreadTextView.setVisibility(View.GONE);
                Intent intent = new Intent(mContext, NewFriendListActivity.class);
                startActivityForResult(intent, 20);
                break;
            case R.id.re_chatroom:
                startActivity(new Intent(mContext, GroupListActivity.class));
                break;
//            case R.id.contact_me_item:
//                RongIM.getInstance().startPrivateChat(mContext, mId, mCacheName);
//                break;
        }
    }

    private void refreshUIListener() {
        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_FRIEND, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    updateUI();
                }
            }
        });

        BroadcastManager.getInstance(this).addAction(SealAppContext.UPDATE_RED_DOT, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String command = intent.getAction();
                if (!TextUtils.isEmpty(command)) {
                    mUnreadTextView.setVisibility(View.INVISIBLE);
                }
            }
        });
        BroadcastManager.getInstance(this).addAction(SealConst.CHANGEINFO, new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                updatePersonalUI();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_FRIEND);
            BroadcastManager.getInstance(this).destroy(SealAppContext.UPDATE_RED_DOT);
            BroadcastManager.getInstance(this).destroy(SealConst.CHANGEINFO);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    private void updateUI() {
        SealUserInfoManager.getInstance().getFriends(new SealUserInfoManager.ResultCallback<List<Friend>>() {
            @Override
            public void onSuccess(List<Friend> friendsList) {
                updateFriendsList(friendsList);
            }

            @Override
            public void onError(String errString) {
                updateFriendsList(null);
            }
        });
    }

    private void updateFriendsList(List<Friend> friendsList) {
        //updateUI fragment初始化和好友信息更新时都会调用,isReloadList表示是否是好友更新时调用
        boolean isReloadList = false;
        if (mFriendList != null && mFriendList.size() > 0) {
            mFriendList.clear();
            isReloadList = true;
        }
        mFriendList = friendsList;
        if (mFriendList != null && mFriendList.size() > 0) {
            handleFriendDataForSort();
            mNoFriends.setVisibility(View.GONE);
        } else {
            mNoFriends.setVisibility(View.VISIBLE);
        }

        // 根据a-z进行排序源数据
        Collections.sort(mFriendList, mPinyinComparator);
        if (isReloadList) {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter.updateListView(mFriendList);
        } else {
            mSidBar.setVisibility(View.VISIBLE);
            mFriendListAdapter = new FriendListAdapter(mContext, mFriendList);

            mListView.setAdapter(mFriendListAdapter);
            mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    if (mListView.getHeaderViewsCount() > 0) {
                        startFriendDetailsPage(mFriendList.get(position - 1));
                    } else {
                        startFriendDetailsPage(mFilteredFriendList.get(position));
                    }
                }
            });


            mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                @Override
                public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                    Friend bean = mFriendList.get(position - 1);
                    startFriendDetailsPage(bean);
                    return true;
                }
            });

        }
    }

    private void startFriendDetailsPage(Friend friend) {
            String displayName = friend.getDisplayName();
            if (!TextUtils.isEmpty(displayName)) {
                RongIM.getInstance().startPrivateChat(mContext, friend.getUserId(), displayName);
            } else {
                RongIM.getInstance().startPrivateChat(mContext, friend.getUserId(), friend.getName());
            }
           // finish();



//
//        Intent intent = new Intent(this, UserDetailActivity.class);
//        intent.putExtra("type", CLICK_CONTACT_FRAGMENT_FRIEND);
//        intent.putExtra("friend", friend);
//        startActivity(intent);
    }

    private void updatePersonalUI() {
        SharedPreferences sp = SealAppContext.getInstance().getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        mId = sp.getString(SealConst.SEALTALK_LOGIN_ID, "");
        mCacheName = sp.getString(SealConst.SEALTALK_LOGIN_NAME, "");
        final String header = sp.getString(SealConst.SEALTALK_LOGING_PORTRAIT, "");
//        mNameTextView.setText(mCacheName);
//        if (!TextUtils.isEmpty(mId)) {
//            UserInfo userInfo = new UserInfo(mId, mCacheName, Uri.parse(header));
//          // String portraitUri =  SealUserInfoManager.getInstance().getPortraitUri(userInfo);
//            String portraitUri=SealUserInfoManager.getInstance().getPortraitUri(userInfo);
//            Log.d("TAG","portraitUri:"+portraitUri);
//            //ImageLoader.getInstance().displayImage(portraitUri, mSelectableRoundedImageView, MyApplication.getOptions());
//            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(portraitUri, mSelectableRoundedImageView, MyApplication.getOptions());
//        }
    }

    private void handleFriendDataForSort() {
        for (Friend friend : mFriendList) {
            if (friend.isExitsDisplayName()) {
                String letters = replaceFirstCharacterWithUppercase(friend.getDisplayNameSpelling());
                friend.setLetters(letters);
            } else {
                String letters = replaceFirstCharacterWithUppercase(friend.getNameSpelling());
                friend.setLetters(letters);
            }
        }
    }

    private String replaceFirstCharacterWithUppercase(String spelling) {
        if (!TextUtils.isEmpty(spelling)) {
            char first = spelling.charAt(0);
            char newFirst = first;
            if (first >= 'a' && first <= 'z') {
                newFirst -= 32;
            }
            return spelling.replaceFirst(String.valueOf(first), String.valueOf(newFirst));
        } else {
            return "#";
        }
    }

}
