package com.likeit.currenciesapp.ui.me;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.UtilPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class BankFragment extends DialogFragment implements View.OnClickListener {

    private TextView rlBank01, rlBank02, rlZfb, rlWeixin;
    private RelativeLayout rl01, rl02, rl03, rl04;
    private String flag, bankflag;
    private Bundle bundle;
    protected String order_pty = "1";
    protected String alipayGive = "0";
    private String balance;
    private String inputValue01;
    protected double nowRate = 0;
    protected double nowBl = 0;
    long hand_ = 0;
    String coin_Name = "";
    String inputValue = "";
    long realGet = 0;
    String formula = "";
    protected String nowCoinId = "";
    protected String orderBeforeId = "";
    protected String orderId = "0";
    private String taibi;

    public BankFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.fragment_bank, container, false);
        flag = getArguments().getString("flag");
        bankflag = getArguments().getString("bankflag");
        taibi =  getArguments().getString("taibi");
        nowBl =  getArguments().getDouble(HomeFragment.NOW_Bl);
        nowRate =  getArguments().getDouble(HomeFragment.NOW_RATE);
        coin_Name =  getArguments().getString(HomeFragment.COIN_NAME);
        inputValue =  getArguments().getString(HomeFragment.INPUT_VALUE);
        formula =  getArguments().getString(HomeFragment.FORMULA);
        realGet =  getArguments().getLong(HomeFragment.REAL_GET);
        formula =  getArguments().getString(HomeFragment.FORMULA);
        hand_ =  getArguments().getLong(HomeFragment.HAND);
        nowCoinId =  getArguments().getString(HomeFragment.COIN_ID);
        orderId =  getArguments().getString(HomeFragment.ORDER_ID, "0");
        alipayGive =  getArguments().getString(HomeFragment.AlipayGive, "0");
        balance = getArguments().getString(HomeFragment.Balance);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rlBank01 = (TextView) view.findViewById(R.id.tv_daluBank);//大陆
        rlBank02 = (TextView) view.findViewById(R.id.tv_taiwanBank);//台湾
        rlZfb = (TextView) view.findViewById(R.id.tv_zhifubao);
        rlWeixin = (TextView) view.findViewById(R.id.tv_weixin);
        rl01 = (RelativeLayout) view.findViewById(R.id.rl_daluBank);
        rl02 = (RelativeLayout) view.findViewById(R.id.rl_taiwanBank);
        rl03 = (RelativeLayout) view.findViewById(R.id.rl_zhifubao);
        rl04 = (RelativeLayout) view.findViewById(R.id.rl_weixin);
        rl01.setOnClickListener(this);
        rl02.setOnClickListener(this);
        rl03.setOnClickListener(this);
        rl04.setOnClickListener(this);

//        rlBank01.setOnClickListener(this);
//        rlBank02.setOnClickListener(this);
//        rlZfb.setOnClickListener(this);
//        rlWeixin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_daluBank:
                Intent intent = new Intent(getActivity(), AddBankActivity.class);
                bundle = new Bundle();
                bundle.putString("keys", "1");
                bundle.putString("type", "1");
                bundle.putString("flag", flag);
                bundle.putString("bankflag", bankflag);
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent.putExtras(bundle);
                startActivity(intent);
                getActivity().finish();
                break;
            case R.id.rl_taiwanBank:
                Intent intent01 = new Intent(getActivity(), AddTaiwanBankActivity.class);
                bundle = new Bundle();
                bundle.putString("keys", "1");
                bundle.putString("type", "2");
                bundle.putString("flag", flag);
                bundle.putString("bankflag", bankflag);
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent01.putExtras(bundle);
                startActivity(intent01);
                getActivity().finish();
                break;
            case R.id.rl_zhifubao:
                Intent intent03 = new Intent(getActivity(), AddWeixinZFBActivity.class);
                bundle = new Bundle();
                bundle.putString("keys", "1");
                bundle.putString("type", "3");
                bundle.putString("flag", flag);
                bundle.putString("bankflag", bankflag);
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent03.putExtras(bundle);
                startActivity(intent03);
                getActivity().finish();
                break;
            case R.id.rl_weixin:
                Intent intent04 = new Intent(getActivity(), AddWeixinZFBActivity.class);
                bundle = new Bundle();
                bundle.putString("keys", "1");
                bundle.putString("type", "4");
                bundle.putString("flag", flag);
                bundle.putString("bankflag", bankflag);
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                intent04.putExtras(bundle);
                startActivity(intent04);
                getActivity().finish();
                break;
        }
    }

//    @Override
//    public void onDestroy() {
//        super.onDestroy();
//        getDialog().dismiss();
//    }
}
