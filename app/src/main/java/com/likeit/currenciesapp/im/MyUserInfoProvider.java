package com.likeit.currenciesapp.im;


import android.net.Uri;

import com.likeit.currenciesapp.ui.chat.db.DBManager;
import com.likeit.currenciesapp.ui.chat.db.Friend;
import com.likeit.currenciesapp.ui.chat.db.FriendDao;

import java.util.List;

import io.rong.imkit.RongIM;
import io.rong.imlib.model.UserInfo;

public class MyUserInfoProvider implements RongIM.UserInfoProvider {
    @Override
    public UserInfo getUserInfo(String userId) {
        //先从获取数据库操作的实例
        FriendDao friendDao = DBManager.getInstance()
                .getDaoSession().getFriendDao();
        //获取数据库中我所有好友的bean对象
        List<Friend> friends = friendDao.loadAll();
        if (friends != null && friends.size() > 0) {
            //增强for把所有的用户信息 return 给融云
            for (Friend friend : friends) {
                //判断返回的userId
                if (friend.getUserId().equals(userId)) {
                    return new UserInfo(friend.getUserId(), friend.getName(),
                            Uri.parse(friend.getPortraitUri().toString()));
                }
            }
        }
        return null;
    }
}
