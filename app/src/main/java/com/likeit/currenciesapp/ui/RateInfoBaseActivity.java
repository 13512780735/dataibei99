package com.likeit.currenciesapp.ui;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;

import butterknife.BindView;

/**
 * Created by Administrator on 2017/11/14.
 */

public class RateInfoBaseActivity extends BaseActivity {

    protected final static int DATA_LOAD_ING = 0x001;
    protected final static int DATA_LOAD_COMPLETE = 0x002;
    protected final static int DATA_LOAD_FAIL = 0x003;

    public static final Handler handler = new Handler();

    @BindView(R.id.tx1_1)
    TextView tx11;
    @BindView(R.id.tx1_2)
    TextView tx12;
    @BindView(R.id.tx1_3)
    TextView tx13;
    @BindView(R.id.tx2_1)
    TextView tx21;
    @BindView(R.id.tx2_2)
    TextView tx22;
    @BindView(R.id.tx2_3)
    TextView tx23;
    @BindView(R.id.tx3_1)
    TextView tx31;

    @BindView(R.id.input_ps_et)
    EditText inputPsEt;
    @BindView(R.id.go_btn)
    TextView goBtn;

    //    protected CoinTypes coin_type = CoinTypes.RMB;
    protected OperateTypes operateType = OperateTypes.SELL;
    //    protected RateInfoEntity rateInfoEntity;
    protected double nowRate = 0;
    protected double nowBl = 0;
    long hand_ = 0;
    String coin_Name = "";
    String inputValue = "";
    long realGet = 0;
    String formula = "";
    protected String nowCoinId = "";
    protected String orderBeforeId = "";
    protected boolean modifyOrder = false;
    protected String orderId = "0";
    protected String hbank_1, hbank_2, hbank_3, hbank_4, hbank_5, hbank_6;
    protected String bank_1, bank_2, bank_3, bank_4, bank_5, bank_6, bank_11, bank_12;
    protected String order_pty = "1";
    protected String alipayGive = "0";
    private Context mContext;


    /**
     * 加载等待效果
     */
    public ProgressDialog progress;
    public String ukey;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        ukey = UtilPreference.getStringValue(this, "ukey");
//        coin_type = (CoinTypes) getIntent().getExtras().getSerializable(HomeFragment.COIN_TYPE);
        operateType = (OperateTypes) getIntent().getExtras().getSerializable(HomeFragment.OPERATE_TYPE);
//        rateInfoEntity = (RateInfoEntity) getIntent().getExtras().getSerializable(HomeFragment.RATE_INFO);
        nowBl = getIntent().getExtras().getDouble(HomeFragment.NOW_Bl);
        nowRate = getIntent().getExtras().getDouble(HomeFragment.NOW_RATE);
        coin_Name = getIntent().getExtras().getString(HomeFragment.COIN_NAME);
        inputValue = getIntent().getExtras().getString(HomeFragment.INPUT_VALUE);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        realGet = getIntent().getExtras().getLong(HomeFragment.REAL_GET);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        hand_ = getIntent().getExtras().getLong(HomeFragment.HAND);
        nowCoinId = getIntent().getExtras().getString(HomeFragment.COIN_ID);
        modifyOrder = getIntent().getExtras().getBoolean(HomeFragment.MODIFY_ORDER, false);
        orderId = getIntent().getExtras().getString(HomeFragment.ORDER_ID, "0");
        alipayGive = getIntent().getExtras().getString(HomeFragment.AlipayGive, "0");

        hbank_1 = getIntent().getExtras().getString(HomeFragment.HBANK_1, "");
        hbank_2 = getIntent().getExtras().getString(HomeFragment.HBANK_2, "");
        hbank_3 = getIntent().getExtras().getString(HomeFragment.HBANK_4, "");
        hbank_4 = getIntent().getExtras().getString(HomeFragment.HBANK_3, "");
        hbank_5 = getIntent().getExtras().getString(HomeFragment.HBANK_5, "");
        hbank_6 = getIntent().getExtras().getString(HomeFragment.HBANK_6, "");

        bank_1 = getIntent().getExtras().getString(HomeFragment.BANK_1, "");
        bank_2 = getIntent().getExtras().getString(HomeFragment.BANK_2, "");
        bank_3 = getIntent().getExtras().getString(HomeFragment.BANK_3, "");
        bank_4 = getIntent().getExtras().getString(HomeFragment.BANK_4, "");
        bank_5 = getIntent().getExtras().getString(HomeFragment.BANK_5, "");
        bank_6 = getIntent().getExtras().getString(HomeFragment.BANK_6, "");
        bank_11 = getIntent().getExtras().getString(HomeFragment.BANK_11, "");
        bank_12 = getIntent().getExtras().getString(HomeFragment.BANK_12, "");

        order_pty = getIntent().getExtras().getString(HomeFragment.ORDER_PTY, "");

    }
    /**
     * Activity销毁，关闭加载效果
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Activity暂停，关闭加载效果
     */
    @Override
    protected void onPause() {
        super.onPause();
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * Activity停止，关闭加载效果
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (progress != null) {
            progress.dismiss();
        }
    }
    /**
     * 触发手机返回按钮
     */
    @Override
    public void onBackPressed() {
        MyActivityManager.getInstance().removeActivity(this);
    }

    /**
     * 消息队列方式显示进度
     */
    @SuppressLint("HandlerLeak")
    public Handler progressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
            progress = new ProgressDialog(mContext);
            progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            progress.setMessage(msg.obj.toString());
            progress.setCanceledOnTouchOutside(false);
            progress.setCancelable(true);
            progress.show();
        }
    };

    /**
     * 隐藏消息队列进度的显示
     */
    public Handler disProgressHander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            if (progress != null) {
                progress.dismiss();
            }
        }
    };

    /**
     * 显示字符串消息
     *
     * @param message
     */
    public void showProgress(String message) {
        if (progress != null) {
            progress.dismiss();
        }
        progress = new ProgressDialog(mContext);
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.setMessage(message);
        progress.setCanceledOnTouchOutside(false);
        progress.setCancelable(true);
        progress.show();
    }

    /**
     * 隐藏字符串消息
     */
    public void disShowProgress() {
        if (progress != null) {
            progress.dismiss();
        }
    }

    /**
     * 提示信息
     */
    public void showErrorMsg(String message) {
        final String str = message;
        Container.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息号或请求失败信息
     * <p>
     * showErrorRequestFair
     */
    public void showE404() {
        Container.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "手机信号差或服务器维护，请稍候重试。谢谢！", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            }
        });
    }

    /**
     * 提示信息
     */
    public void showMsgAndDisProgress(String message) {
        final String str = message;
        Container.handler.post(new Runnable() {
            @Override
            public void run() {
                Toast toast = Toast.makeText(getApplicationContext(), str,
                        Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                disShowProgress();
            }
        });
    }

    /**
     * 文本View
     */
    public TextView textView(int textview) {
        return (TextView) findViewById(textview);
    }

    /**
     * 文本button
     */
    public Button button(int id) {
        return (Button) findViewById(id);
    }

    /**
     * 文本button
     */
    public ImageView imageView(int id) {
        return (ImageView) findViewById(id);
    }

    /**
     * 文本editText
     */
    public EditText editText(int id) {
        return (EditText) findViewById(id);
    }

    /**
     * 显示数据加载状态
     *
     * @param loading
     * @param datas
     * @param type
     */
    public void viewSwitch(View loading, View datas, int type) {
        switch (type) {
            case DATA_LOAD_ING:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.VISIBLE);
                break;
            case DATA_LOAD_COMPLETE:
                datas.setVisibility(View.VISIBLE);
                loading.setVisibility(View.GONE);
                break;
            case DATA_LOAD_FAIL:
                datas.setVisibility(View.GONE);
                loading.setVisibility(View.GONE);
                break;
        }
    }

    protected void toFinish() {
        finish();
    }

    public void toActivityFinish(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
        toFinish();
    }

    public void toActivity(Class activity) {
        Intent intent = new Intent(mContext, activity);
        startActivity(intent);
    }

    public void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(mContext, activity);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    public void showToast(String msg) {
        ToastUtil.showS(mContext, msg);
    }
}
