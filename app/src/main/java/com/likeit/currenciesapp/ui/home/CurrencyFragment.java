package com.likeit.currenciesapp.ui.home;


import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.likeit.currenciesapp.Listener.RateShowClickListener;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellInputRateActivity;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * A simple {@link Fragment} subclass.
 */
public class CurrencyFragment extends DialogFragment implements View.OnClickListener {


    private TextView tvCancel;
    private TextView tvName;
    private int data01;
    private TextView tv_sale_, tv_buy_in_, tv_alipay_, tv_negotiation_, tv_Pre_order_;
    private JSONObject object;
    private TextView tv_sale, tv_buy_in, tv_alipay, tv_negotiation, tv_Pre_order;


    private RateShowClickListener rateShowClickListener;
    private final static int TIME = 101;
    private int time_tatol = 180;
    Handler myHanlder = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case TIME:
                    time_tatol--;
                    tvTime.setText(time_tatol + "秒後刷新");
                    if (time_tatol <= 0) {
                        time_tatol = 180;
                        initData(data01);
                    }
                    myHanlder.sendEmptyMessageDelayed(TIME, 1000);
                    break;
            }
        }
    };
    private TextView tvTime;
    private ProgressDialog mDialog;
    private Bundle bundle;
    private RateInfoEntity mRateInfoEntity;
    private Intent intent;
    private boolean is_kaipan;
    private TextView tvWithdraw;
    private TextView tvWithdraw_;
    private DianInfoEntity mDianInfoEntity;
    private String work;

    public CurrencyFragment() {
        // Required empty public constructor
    }

    public void startTime() {
        myHanlder.sendEmptyMessageDelayed(TIME, 1000);
    }

    public void setRateShowClickListener(RateShowClickListener rateShowClickListener) {
        this.rateShowClickListener = rateShowClickListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_currency, container, false);
        Bundle bundle = getArguments();
        work= UtilPreference.getStringValue(getActivity(),"work");
        if (bundle != null) {
            data01 = bundle.getInt("huilvid");
            Log.d("Tag", "Data01: " + data01);
            mRateInfoEntity = (RateInfoEntity) bundle.getSerializable("mRateInfoEntity");
            mDianInfoEntity = (DianInfoEntity) bundle.getSerializable("mDianInfoEntity");
            is_kaipan = bundle.getBoolean("is_kaipan");
        }
        initView(view);
        initData(data01);

        return view;
    }

    private void initData(int data01) {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("Loading...");
        mDialog.show();
        String url = AppConfig.LIKEIT_HUILV;
        String ukey = UtilPreference.getStringValue(getActivity(), "ukey");
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("huilvid", data01);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                mDialog.dismiss();
                Log.d("TAG", "huilv-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status) && "1".equals(code)) {
                        object = obj.optJSONObject("info");
                        Log.d("TAG", object.toString());
                        tv_sale_.setText(String.valueOf(object.optString("sold")));
                        tv_buy_in_.setText(String.valueOf(object.optString("buy")));
                        tv_alipay_.setText(String.valueOf(object.optString("chongzhi")));
                        tv_negotiation_.setText(String.valueOf(object.optString("daifu")));
                        tv_Pre_order_.setText(String.valueOf(object.optString("yugou")));
                        tvWithdraw_.setText(String.valueOf(object.optString("buy")));
                    } else {
                        ToastUtil.showS(getActivity(), msg);
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
                mDialog.dismiss();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        getDialog().getWindow().setLayout(dm.widthPixels, getDialog().getWindow().getAttributes().height);
        Window window = getDialog().getWindow();
        WindowManager.LayoutParams windowParams = window.getAttributes();
        windowParams.dimAmount = 0.5f;

        window.setAttributes(windowParams);
    }

    private void initView(View view) {
        tvCancel = (TextView) view.findViewById(R.id.tv_cancel);
        tvTime = (TextView) view.findViewById(R.id.tv_time);
        tv_sale_ = (TextView) view.findViewById(R.id.tv_sale_);
        tv_sale = (TextView) view.findViewById(R.id.tv_sale);
        tv_buy_in_ = (TextView) view.findViewById(R.id.tv_buy_in_);
        tv_buy_in = (TextView) view.findViewById(R.id.tv_buy_in);
        tv_alipay_ = (TextView) view.findViewById(R.id.tv_alipay_);
        tv_alipay = (TextView) view.findViewById(R.id.tv_alipay);
        tv_negotiation_ = (TextView) view.findViewById(R.id.tv_negotiation_);
        tv_negotiation = (TextView) view.findViewById(R.id.tv_negotiation);
        tv_Pre_order_ = (TextView) view.findViewById(R.id.tv_Pre_order_);
        tv_Pre_order = (TextView) view.findViewById(R.id.tv_Pre_order);
        tvWithdraw = (TextView) view.findViewById(R.id.tvWithdraw);
        tvWithdraw_ = (TextView) view.findViewById(R.id.tvWithdraw_);
        tvName = (TextView) view.findViewById(R.id.tvName);
        if (!is_kaipan) {
            tv_sale.setEnabled(false);
            tv_buy_in.setEnabled(false);
            tv_alipay.setEnabled(false);
            tv_negotiation.setEnabled(false);
            tv_Pre_order.setEnabled(false);
            tvWithdraw.setEnabled(false);

            tv_sale.setBackgroundResource(R.drawable.currency_isclosed);
            tvWithdraw.setBackgroundResource(R.drawable.currency_isclosed);
            tv_buy_in.setBackgroundResource(R.drawable.currency_isclosed);
            tv_alipay.setBackgroundResource(R.drawable.currency_isclosed);
            tv_negotiation.setBackgroundResource(R.drawable.currency_isclosed);
            tv_Pre_order.setBackgroundResource(R.drawable.currency_isclosed);
        } else{ tvCancel.setOnClickListener(this);
        tv_sale.setOnClickListener(this);
        tv_buy_in.setOnClickListener(this);
        tv_alipay.setOnClickListener(this);
        tv_negotiation.setOnClickListener(this);
        tv_Pre_order.setOnClickListener(this);
          tvWithdraw.setOnClickListener(this);}
        if (data01 == 2) {
            tvName.setText(this.getResources().getString(R.string.exchange_name02));
            Drawable left = getResources().getDrawable(R.mipmap.content_rmb);
            tvName.setCompoundDrawablesWithIntrinsicBounds(left, null, null, null);
        } else if (data01 == 3) {
            tvName.setText(this.getResources().getString(R.string.exchange_name02));
            Drawable left = getResources().getDrawable(R.mipmap.icon_dollar);
            tvName.setText(this.getResources().getString(R.string.exchange_name03));
        } else if (data01 == 4) {
            tvName.setText(this.getResources().getString(R.string.exchange_name06));
        } else if (data01 == 5) {
            tvName.setText(this.getResources().getString(R.string.exchange_name05));
        } else if (data01 == 6) {
            tvName.setText(this.getResources().getString(R.string.exchange_name04));
        } else if (data01 == 7) {
            tvName.setText(this.getResources().getString(R.string.exchange_name07));
        }

    }

    private CoinTypes coin_type = CoinTypes.RMB;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                getDialog().dismiss();
                break;
            case R.id.tv_sale://卖出
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                intent = new Intent(getActivity(), SellActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.SELL);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent.putExtras(bundle);
                startActivity(intent);}
                break;
            case R.id.tv_buy_in://买进
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                intent = new Intent(getActivity(), BuyInActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.BUY);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent.putExtras(bundle);
                startActivity(intent);}
                break;
            case R.id.tv_alipay://充值
            //asdasdasd
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                bundle = new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                //  toActivity(DianSellInputRateActivity.class, bundle);
                RechargeFragment dialogFragment01 = new RechargeFragment();
                dialogFragment01.setArguments(bundle);
                dialogFragment01.show(getFragmentManager(), "dialogFragment");}
//                intent = new Intent(getActivity(), AlipayActivity.class);
//                bundle = new Bundle();
//                bundle.putSerializable("Dian", mDianInfoEntity);
//                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
//                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
//                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
//                bundle.putString(HomeFragment.COIN_ID, "2");
//                intent.putExtras(bundle);
//                startActivity(intent);
                break;
            case R.id.tv_negotiation://代付
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                intent = new Intent(getActivity(), NegotiationActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.OTHER);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent.putExtras(bundle);
                startActivity(intent);}
                break;
            case R.id.tv_Pre_order://预购
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                intent = new Intent(getActivity(), PreOrderActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.PRE);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent.putExtras(bundle);
                startActivity(intent);}
                break;
            case R.id.tvWithdraw://提领
//                intent = new Intent(getActivity(), DianSellInputRateActivity.class);
//                 bundle = new Bundle();
//                bundle.putSerializable("Dian", mDianInfoEntity);
//                intent.putExtras(bundle);
//                startActivity(intent);
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                bundle = new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                //  toActivity(DianSellInputRateActivity.class, bundle);
                WithdrawDialogFragment dialogFragment = new WithdrawDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dialogFragment");}
                break;
        }

    }
}
