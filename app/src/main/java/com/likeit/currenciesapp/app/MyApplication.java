package com.likeit.currenciesapp.app;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;
import android.view.View;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.dumpapp.DumperPlugin;
import com.facebook.stetho.inspector.database.DefaultDatabaseConnectionProvider;
import com.facebook.stetho.inspector.protocol.ChromeDevtoolsDomain;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.im.MyExtensionModule;
import com.likeit.currenciesapp.ui.chat.SealAppContext;
import com.likeit.currenciesapp.ui.chat.SealUserInfoManager;
import com.likeit.currenciesapp.ui.chat.db.Friend;
import com.likeit.currenciesapp.ui.chat.message.TestMessage;
import com.likeit.currenciesapp.ui.chat.message.provider.ContactNotificationMessageProvider;
import com.likeit.currenciesapp.ui.chat.message.provider.TestMessageProvider;
import com.likeit.currenciesapp.ui.chat.redMessage.RedPackageItemProvider;
import com.likeit.currenciesapp.ui.chat.redMessage.ZNRedPackageMessage;
import com.likeit.currenciesapp.ui.chat.server.pinyin.CharacterParser;
import com.likeit.currenciesapp.ui.chat.server.utils.NLog;
import com.likeit.currenciesapp.ui.chat.server.utils.RongGenerate;
import com.likeit.currenciesapp.ui.chat.stetho.RongDatabaseDriver;
import com.likeit.currenciesapp.ui.chat.stetho.RongDatabaseFilesProvider;
import com.likeit.currenciesapp.ui.chat.stetho.RongDbFilesDumperPlugin;
import com.likeit.currenciesapp.ui.chat.transferMessage.TransferItemProvider;
import com.likeit.currenciesapp.ui.chat.transferMessage.ZNTransferMessage;
import com.likeit.currenciesapp.ui.chat.ui.activity.UserDetailActivity;
import com.likeit.currenciesapp.ui.chat.utils.SharedPreferencesContext;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.rong.imageloader.core.DisplayImageOptions;
import io.rong.imageloader.core.display.FadeInBitmapDisplayer;
import io.rong.imkit.DefaultExtensionModule;
import io.rong.imkit.IExtensionModule;
import io.rong.imkit.RongExtensionManager;
import io.rong.imkit.RongIM;
import io.rong.imkit.widget.provider.RealTimeLocationMessageProvider;
import io.rong.imlib.ipc.RongExceptionHandler;
import io.rong.imlib.model.UserInfo;
import io.rong.push.RongPushClient;
import io.rong.recognizer.RecognizeExtensionModule;

public class MyApplication extends MultiDexApplication {

    public static MyApplication mContext;
    private static MyApplication instance;
    //  private UserInfo userInfo = null;
    // 记录是否已经初始化
    public static boolean isNotice = true;
    private boolean isInit = false;
    public final String PREF_USERNAME = "username";
    public static String currentUserNick = "";
    private static DisplayImageOptions options;
    private static long mMainThreadId;//主线程id
    private static Thread mMainThread;//主线程、
    private static Handler mHandler;//主线程Handler

    public static MyApplication getInstance() {
        if (mContext == null) {
            return new MyApplication();
        } else {
            return mContext;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        instance = this;
        //initGePushService();
        mMainThreadId = android.os.Process.myTid();
        mMainThread = Thread.currentThread();
        mHandler = new Handler();
        // 初始化融云
//        //初始化
        /**
         * Bugly更新
         */
        Beta.autoCheckUpgrade = true;//设置自动检查
        Bugly.init(mContext, "5744a5eb46", false);
        initUpdate();//腾讯bugly更新
        Stetho.initialize(new Stetho.Initializer(this) {
            @Override
            protected Iterable<DumperPlugin> getDumperPlugins() {
                return new Stetho.DefaultDumperPluginsBuilder(MyApplication.this)
                        .provide(new RongDbFilesDumperPlugin(MyApplication.this, new RongDatabaseFilesProvider(MyApplication.this)))
                        .finish();
            }

            @Override
            protected Iterable<ChromeDevtoolsDomain> getInspectorModules() {
                Stetho.DefaultInspectorModulesBuilder defaultInspectorModulesBuilder = new Stetho.DefaultInspectorModulesBuilder(MyApplication.this);
                defaultInspectorModulesBuilder.provideDatabaseDriver(new RongDatabaseDriver(MyApplication.this, new RongDatabaseFilesProvider(MyApplication.this), new DefaultDatabaseConnectionProvider()));
                return defaultInspectorModulesBuilder.finish();
            }
        });

        if (getApplicationInfo().packageName.equals(getCurProcessName(getApplicationContext()))) {

//            LeakCanary.install(this);//内存泄露检测
            RongPushClient.registerHWPush(this);
            RongPushClient.registerMiPush(this, "2882303761517577129", "5261757731129");
//            try {
//                RongPushClient.registerGCM(this);
//            } catch (RongException e) {
//                e.printStackTrace();
            //           }

            /**
             * 注意：
             *
             * IMKit SDK调用第一步 初始化
             *
             * context上下文
             *
             * 只有两个进程需要初始化，主进程和 push 进程
             */
            RongIM.setServerInfo("nav.cn.ronghub.com", "up.qbox.me");

            RongPushClient.registerHWPush(this);
            RongPushClient.registerMiPush(this, "2882303761517577129", "5261757731129");
            RongIM.init(this);
            //红包消息模型
            RongIM.registerMessageType(ZNRedPackageMessage.class);
            RongIM.registerMessageTemplate(new RedPackageItemProvider());
            RongIM.registerMessageType(ZNTransferMessage.class);
            RongIM.registerMessageTemplate(new TransferItemProvider());

            // RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
            // setMyExtensionModule();
            NLog.setDebug(true);//Seal Module Log 开关
            SealAppContext.init(this);
            SharedPreferencesContext.init(this);
            Thread.setDefaultUncaughtExceptionHandler(new RongExceptionHandler(this));

            try {
                RongIM.registerMessageTemplate(new ContactNotificationMessageProvider());
                RongIM.registerMessageTemplate(new RealTimeLocationMessageProvider());
                RongIM.registerMessageType(TestMessage.class);
                RongIM.registerMessageTemplate(new TestMessageProvider());
                // RongIM.registerMessageTemplate(new RedPackageItemProvider());
//                RongIM.setUserInfoProvider(new MyUserInfoProvider(), true);
//                RongIM.setConnectionStatusListener(new MyConnectionStatusListener());
//                RongIM.setOnReceiveMessageListener(new MyReceiveMessageListener());
//                RongIM.setConversationListBehaviorListener(new MyConversationListBehaviorListener());
                setMyExtensionModule();
            } catch (Exception e) {
                e.printStackTrace();
            }

            openSealDBIfHasCachedToken();

            options = new DisplayImageOptions.Builder()
                    .showImageForEmptyUri(R.drawable.de_default_portrait)
                    .showImageOnFail(R.drawable.de_default_portrait)
                    .showImageOnLoading(R.drawable.de_default_portrait)
                    .displayer(new FadeInBitmapDisplayer(300))
                    .cacheInMemory(true)
                    .cacheOnDisk(true)
                    .build();
            RongExtensionManager.getInstance().registerExtensionModule(new RecognizeExtensionModule());
        }
    }

    public static DisplayImageOptions getOptions() {
        return options;
    }

    private void openSealDBIfHasCachedToken() {
        SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
        String cachedToken = sp.getString("loginToken", "");
        if (!TextUtils.isEmpty(cachedToken)) {
            String current = getCurProcessName(this);
            String mainProcessName = getPackageName();
            if (mainProcessName.equals(current)) {
                SealUserInfoManager.getInstance().openDB();
            }
        }
    }

    public static String getCurProcessName(Context context) {
        int pid = android.os.Process.myPid();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningAppProcessInfo appProcess : activityManager.getRunningAppProcesses()) {
            if (appProcess.pid == pid) {
                return appProcess.processName;
            }
        }
        return null;
    }


    //
    public void setMyExtensionModule() {
        List<IExtensionModule> moduleList = RongExtensionManager.getInstance().getExtensionModules();
        IExtensionModule defaultModule = null;
        if (moduleList != null) {
            for (IExtensionModule module : moduleList) {
                if (module instanceof DefaultExtensionModule) {
                    defaultModule = module;
                    break;
                }
            }
            if (defaultModule != null) {
                RongExtensionManager.getInstance().unregisterExtensionModule(defaultModule);
                RongExtensionManager.getInstance().registerExtensionModule(new MyExtensionModule());
            }
        }
    }


    private void initUpdate() {
        Bugly.init(getApplicationContext(), "5744a5eb46", false);
        Beta.autoCheckUpgrade = true;
        Beta.upgradeCheckPeriod = 60 * 60 * 1000;
        Beta.largeIconId = R.mipmap.ic_launcher;

    }

    /**
     * 根据Pid获取当前进程的名字，一般就是当前app的包名
     *
     * @param pid 进程的id
     * @return 返回进程的名字
     */
    private String getAppName(int pid) {
        String processName = null;
        ActivityManager activityManager = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List list = activityManager.getRunningAppProcesses();
        Iterator i = list.iterator();
        while (i.hasNext()) {
            ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // 根据进程的信息获取当前进程的名字
                    processName = info.processName;
                    // 返回当前进程名
                    return processName;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        // 没有匹配的项，返回为null
        return null;
    }

    public static Context getContext() {
        return mContext;
    }

    public static void setMainThreadId(long mMainThreadId) {
        MyApplication.mMainThreadId = mMainThreadId;
    }

    public static Thread getMainThread() {
        return mMainThread;
    }

    public static long getMainThreadId() {
        return mMainThreadId;
    }

    public static void setMainThread(Thread mMainThread) {
        MyApplication.mMainThread = mMainThread;
    }

    public static Handler getMainHandler() {
        return mHandler;
    }

    /**
     * 获取用户信息
     *
     * @return
     */
//    public UserInfo getUserInfo() {
//        if (userInfo == null)
//            init();
//        return userInfo;
//    }
}
//    private void init() {
//        userInfo = Storage.getObject(AppConfig.USER_INFO, UserInfo.class);
//
//    }

/**
 * 登录信息的保存
 *
 * @param user
 */
//    public static void doLogin(UserInfo user) {
//        Storage.saveObject(AppConfig.USER_INFO, user);
//        Storage.saveObject(AppConfig.USER_INFO, user);
//        Preferences
//                .saveString(AppConfig.USER_ID, user.getUkey(), getInstance());
//        MyApplication.getInstance().init();
//    }

//    /**
//     * 清除登录信息(退出账号)
//     */
//    public static void doLogout() {
//        Storage.clearObject(AppConfig.USER_INFO);
//        MyApplication.getInstance().init();
//    }
//}