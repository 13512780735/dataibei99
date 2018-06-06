package com.likeit.currenciesapp.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.FileProvider;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.hwangjr.rxbus.RxBus;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.HomeViewPagerAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.rxbus.RefreshEvent;
import com.likeit.currenciesapp.ui.chat.SealAppContext;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.server.SealAction;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.ui.chat.ui.activity.NewFriendListActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.NotificationsUtils;
import com.likeit.currenciesapp.utils.PhotoUtils;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.ToastUtils;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.BadgeView;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.NoScrollViewPager;
import com.loopj.android.http.RequestParams;
import com.pk4pk.baseappmoudle.dialog.LoaddingDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.manager.IUnReadMessageObserver;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.UserInfo;
import io.rong.message.ContactNotificationMessage;

public class MainActivity extends FragmentActivity implements ViewPager.OnPageChangeListener, RadioGroup.OnCheckedChangeListener, IUnReadMessageObserver {
    private static final int REQUEST_CODE_ACCESS_COARSE_LOCATION = 1;
    public static NoScrollViewPager mViewPager;
    @BindView(R.id.rbHome)
    RadioButton mRbHome;
    @BindView(R.id.rbGreenteahy)
    RadioButton mRbGreenteahy;
    @BindView(R.id.rbChat)
    RadioButton mRbCohat;
    @BindView(R.id.rbMe)
    RadioButton mRbMe;
    @BindView(R.id.rgTools)
    RadioGroup mRgTools;
    private HomeViewPagerAdapter adapter;

    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    private static final int CAMERA_PERMISSIONS_REQUEST_CODE = 0x03;
    private static final int STORAGE_PERMISSIONS_REQUEST_CODE = 0x04;
    private File fileUri = new File(Environment.getExternalStorageDirectory().getPath() + "/photo.jpg");
    private File fileCropUri = new File(Environment.getExternalStorageDirectory().getPath() + "/crop_photo.jpg");
    private Uri imageUri;
    private Uri cropImageUri;
    private LoginUserInfoEntity mLoginUserInfoEntity;
    private String connectResultId;
    private UserInfo userInfo;
    private JSONObject obj;
    private String id;
    private SharedPreferences sp;
    private SharedPreferences.Editor editor;
    private SealAction action;
    private BadgeView badgeView;
    private String page;
    private MainActivity mContext;

    //SADASDASDASDASD
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ButterKnife.bind(this);
        RxBus.get().register(this);
        mContext = this;

        sp = getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cacheToken = sp.getString("loginToken", "");
        Log.d("TAG", cacheToken);
        if (!TextUtils.isEmpty(cacheToken)) {
            RongIM.connect(cacheToken, SealAppContext.getInstance().getConnectCallback());
        }
        Intent intent = getIntent();
        sp = getSharedPreferences("config", MODE_PRIVATE);
        page = intent.getStringExtra("page");
        editor = sp.edit();
        mLoginUserInfoEntity = (LoginUserInfoEntity) intent.getSerializableExtra("userInfo");
        //  connectResultId = intent.getStringExtra("connectResultId");
        Log.d("TAG44", mLoginUserInfoEntity.getRongcloud_id());
        id = mLoginUserInfoEntity.getRongcloud_id();
        initData(id);//获取用户头像等信息
        initView();
        NotificationsUtils.isNotificationEnabled(this);
        if ("0".equals(page)) {
            mViewPager.setCurrentItem(0);
        } else {
            mViewPager.setCurrentItem(2);
        }
        mViewPager.setOffscreenPageLimit(2);
        action = new SealAction(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        RxBus.get().unregister(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        if (intent.getBooleanExtra("systemconversation", false)) {
            mViewPager.setCurrentItem(2, false);
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void getImEvent(final RefreshEvent event) {
//        FriendDao friendDao = DBManager.getInstance()
//                .getDaoSession().getFriendDao();
//        //获取数据库中我所有好友的bean对象
//        List<Friend> userIdList = DBManager.getInstance().getDaoSession().getFriendDao().loadAll();
//        for (Friend i : userIdList) {
//            RongIM.getInstance().setCurrentUserInfo(new UserInfo(i.getUserId(), i.getName()
//                    , Uri.parse(i.getPortraitUri().toString())));
//            RongIM.getInstance().setMessageAttachedUserInfo(true);
//        }
//        Log.d("TAG", "全部用户：" + userIdList.get(0).getName());
        SealUserInfoManager.getInstance().getAllUserInfo();
        refresh();

    }
//    @Subscribe(thread = EventThread.MAIN_THREAD)
//    public void getImEvent(final RefreshEvent event) {
//        FriendDao friendDao = DBManager.getInstance()
//                .getDaoSession().getFriendDao();
//        //获取数据库中我所有好友的bean对象
//        List<Friend> userIdList = DBManager.getInstance().getDaoSession().getFriendDao().loadAll();
//        for (Friend i : userIdList) {
//            RongIM.getInstance().setCurrentUserInfo(new UserInfo(i.getUserId(), i.getName()
//                    , Uri.parse(i.getPortraitUri().toString())));
//            RongIM.getInstance().setMessageAttachedUserInfo(true);
//        }
//        Log.d("TAG", "全部用户：" + userIdList.get(0).getName());
//        refresh();
//
//    }
//    @Subscribe(thread = EventThread.MAIN_THREAD)
//    public void getImEvent(RefreshEvent event) {
//        if (event.getType() == RefreshEvent.GET_NEW_MSG) {
//           Log.d()
//        }
//    }
//    @Subscribe(thread = EventThread.MAIN_THREAD)
//    public void handlerMessage(final MessageEvent event) {
//        switch (event.getType()) {
//            case "401":
//                Toast.makeText(MainActivity.this, "賬號已經在其他設備登陸", Toast.LENGTH_SHORT).show();
//                RongIM.getInstance().disconnect();
//
//
//                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                intent.putExtra("ukeys", "2");
//                startActivity(intent);
//                finish();
//                break;
//        }
//    }

    @Override
    protected void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {


    }

    @Override
    public void onCountChanged(int count) {
        Log.d("TAG", count + "");
        if (count == 0) {
            badgeView.setVisibility(View.INVISIBLE);
        } else if (count > 0 && count < 100) {
            // mUnreadNumView.setVisibility(View.VISIBLE);
            // mUnreadNumView.setText(String.valueOf(count));
            badgeView.setBadgeCount(count);
        } else {
            // mUnreadNumView.setVisibility(View.VISIBLE);
            //mUnreadNumView.setText(R.string.no_read_message);
            badgeView.setBadgeCount(R.string.no_read_message);
        }
    }

    private void initData(String id) {
        String url = AppConfig.LIKEIT_GET_RONGCLOUDID;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(this, "ukey"));
        params.put("rongcloud_id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG23232", response);
                Log.d("TAG85858", UtilPreference.getStringValue(MainActivity.this, "ukey") + ":" + mLoginUserInfoEntity.getRongcloud_id());
                try {
                    obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        userInfo = new UserInfo(
                                obj.optJSONObject("info").optString("rongcloud_id")
                                , obj.optJSONObject("info").optString("truename")
                                , Uri.parse(AppConfig.LIKEIT_LOGO1 + obj.optJSONObject("info").optString("pic")));
                        // String portraitUri = AppConfig.LIKEIT_LOGO1 + obj.optJSONObject("info").optString("pic");
                        RongIM.getInstance().setCurrentUserInfo(userInfo);
                        RongIM.getInstance().refreshUserInfoCache(userInfo);
//                       Log.d("TAG89898", "1122-->"+portraitUri);
//                        if (RongIM.getInstance() != null) {
//                            RongIM.getInstance().setCurrentUserInfo(new UserInfo(sp.getString(SealConst.SEALTALK_LOGIN_ID, ""), sp.getString(SealConst.SEALTALK_LOGIN_NAME, ""),Uri.parse(portraitUri)));
//                        }
//                        BroadcastManager.getInstance(MainActivity.this).sendBroadcast(SealConst.CHANGEINFO);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }

    private void initView() {
        mViewPager = (NoScrollViewPager) findViewById(R.id.home_viewpager);
        adapter = new HomeViewPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.setOnPageChangeListener(this);
        mViewPager.setOffscreenPageLimit(2);
        mRgTools.setOnCheckedChangeListener(this);
        badgeView = new BadgeView(this);
        badgeView.setTargetView(findViewById(R.id.bt));
        badgeView.setBadgeGravity(Gravity.RIGHT | Gravity.TOP);
        badgeView.setBadgeMargin(0, 0, 10, 0);
        initData1();

    }

    private void initData1() {
        final Conversation.ConversationType[] conversationTypes = {
                Conversation.ConversationType.PRIVATE,
                Conversation.ConversationType.GROUP, Conversation.ConversationType.SYSTEM,
                Conversation.ConversationType.PUBLIC_SERVICE, Conversation.ConversationType.APP_PUBLIC_SERVICE
        };
        RongIM.getInstance().addUnReadMessageCountChangedObserver(this, conversationTypes);
        getConversationPush();// 获取 push 的 id 和 target
        getPushMessage();
    }

    private void getConversationPush() {
        if (getIntent() != null && getIntent().hasExtra("PUSH_CONVERSATIONTYPE") && getIntent().hasExtra("PUSH_TARGETID")) {

            final String conversationType = getIntent().getStringExtra("PUSH_CONVERSATIONTYPE");
            final String targetId = getIntent().getStringExtra("PUSH_TARGETID");


            RongIM.getInstance().getConversation(Conversation.ConversationType.valueOf(conversationType), targetId, new RongIMClient.ResultCallback<Conversation>() {
                @Override
                public void onSuccess(Conversation conversation) {

                    if (conversation != null) {

                        if (conversation.getLatestMessage() instanceof ContactNotificationMessage) { //好友消息的push
                            startActivity(new Intent(MainActivity.this, NewFriendListActivity.class));
                        } else {
                            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon().appendPath("conversation")
                                    .appendPath(conversationType).appendQueryParameter("targetId", targetId).build();
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(uri);
                            startActivity(intent);
                        }
                    }
                }

                @Override
                public void onError(RongIMClient.ErrorCode e) {

                }
            });
        }
    }


    /**
     * 得到不落地 push 消息
     */
    private void getPushMessage() {
        Intent intent = getIntent();
        if (intent != null && intent.getData() != null && intent.getData().getScheme().equals("rong")) {
            String path = intent.getData().getPath();
            if (path.contains("push_message")) {
                SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                String cacheToken = sharedPreferences.getString("loginToken", "");
                if (TextUtils.isEmpty(cacheToken)) {
                    // startActivity(new Intent(MainActivity.this, LoginActivity.class));
                } else {
                    if (!RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED)) {
                        LoadDialog.show(mContext);
                        RongIM.connect(cacheToken, new RongIMClient.ConnectCallback() {
                            @Override
                            public void onTokenIncorrect() {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onSuccess(String s) {
                                LoadDialog.dismiss(mContext);
                            }

                            @Override
                            public void onError(RongIMClient.ErrorCode e) {
                                LoadDialog.dismiss(mContext);
                            }
                        });
                    }
                }
            }
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mRgTools.check(mRgTools.getChildAt(position).getId());
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, @IdRes int checkedId) {
        //  mViewPager.setCurrentItem(radioGroup.indexOfChild(radioGroup.findViewById(checkedId)), false);
        switch (checkedId) {
            case R.id.rbHome:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rbGreenteahy:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.rbChat:
                mViewPager.setCurrentItem(2);
                badgeView.setVisibility(View.GONE);
                break;
            case R.id.rbMe:
                mViewPager.setCurrentItem(3);
                break;
        }
    }

    //记录用户首次点击返回键的时间
    private long firstTime = 0;

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
//        switch (keyCode) {
//            case KeyEvent.KEYCODE_BACK:
//                long secondTime = System.currentTimeMillis();
//                if (secondTime - firstTime > 2000) {
//                    Toast.makeText(MainActivity.this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
//                    firstTime = secondTime;
//                    return true;
//                } else {
//                    //MyActivityManager.getInstance().moveTaskToBack(mContext);// 不退出，后台运行
//                }
//                break;
//        }
//        return super.onKeyUp(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - firstTime) > 2000) {
                Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();

                firstTime = System.currentTimeMillis();
            } else {
                // RongIM.getInstance().disconnect();
                // MyActivityManager.getInstance().appExit(this);
                moveTaskToBack(false);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //    @Override
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_BACK) {
//            moveTaskToBack(false);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }
    private void showImages(Bitmap bitmap) {
        CircleImageView ivAvatar = (CircleImageView) findViewById(R.id.iv_avatar);
        ivAvatar.setImageBitmap(bitmap);
    }

    private void upLoad(String base64Token) {
        //  String base64Token = Base64.encodeToString(FileUtil.getFileToByte(file), Base64.DEFAULT);//  编码后
        String ukey = UtilPreference.getStringValue(MainActivity.this, "ukey");
        String url = AppConfig.LIKEIT_UP_HEADIMG_64;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("pic", base64Token);
        params.put("rongcloud_cookie", mLoginUserInfoEntity.getRongcloud_cookie());
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        ToastUtil.showS(MainActivity.this, "上傳成功");
                        // initData();
                        initData(id);
                    } else {
                        ToastUtil.showS(MainActivity.this, msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });

    }

    private static final int output_X = 480;
    private static final int output_Y = 480;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CODE_CAMERA_REQUEST://拍照完成回调
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.likeit.currenciesapp.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.cropImageUri(this, imageUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(MainActivity.this, "设备没有SD卡！");
                    }

                    break;
                case CODE_GALLERY_REQUEST://访问相册完成回调
                    if (hasSdcard()) {
                        cropImageUri = Uri.fromFile(fileCropUri);
                        Uri newUri = Uri.parse(PhotoUtils.getPath(MainActivity.this, data.getData()));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            newUri = FileProvider.getUriForFile(this, "com.likeit.currenciesapp.fileprovider", new File(newUri.getPath()));
                        PhotoUtils.cropImageUri(this, newUri, cropImageUri, 1, 1, output_X, output_Y, CODE_RESULT_REQUEST);
                    } else {
                        ToastUtils.showShort(MainActivity.this, "设备没有SD卡！");
                    }
                    break;
                case CODE_RESULT_REQUEST:
                    Bitmap bitmap = PhotoUtils.getBitmapFromUri(cropImageUri, MainActivity.this);
                    if (bitmap != null) {
                        showImages(bitmap);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                        byte[] bytes = baos.toByteArray();
                        String base64Token = Base64.encodeToString(bytes, Base64.DEFAULT);//  编码后
                        upLoad(base64Token);
                    }
                    break;
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CAMERA_PERMISSIONS_REQUEST_CODE: {//调用系统相机申请拍照权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (hasSdcard()) {
                        imageUri = Uri.fromFile(fileUri);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                            imageUri = FileProvider.getUriForFile(this, "com.likeit.currenciesapp.fileprovider", fileUri);//通过FileProvider创建一个content类型的Uri
                        PhotoUtils.takePicture(this, imageUri, CODE_CAMERA_REQUEST);
                    } else {
                        ToastUtils.showShort(this, "设备没有SD卡！");
                    }
                } else {

                    ToastUtils.showShort(this, "请允许打开相机！！");
                }
                break;


            }
            case STORAGE_PERMISSIONS_REQUEST_CODE://调用系统相册申请Sdcard权限回调
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    PhotoUtils.openPic(this, CODE_GALLERY_REQUEST);
                } else {

                    ToastUtils.showShort(this, "请允许打操作SDCard！！");
                }
                break;
        }
    }

    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        return state.equals(Environment.MEDIA_MOUNTED);
    }

}
