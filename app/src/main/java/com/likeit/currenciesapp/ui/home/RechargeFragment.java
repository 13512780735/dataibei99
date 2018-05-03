package com.likeit.currenciesapp.ui.home;


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

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianBuyInputRateActivity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellInputRateActivity;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.UtilPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class RechargeFragment extends DialogFragment {

    private RelativeLayout rl_tiling;
    private RelativeLayout rl_tixian;
    private DianInfoEntity dianInfoEntity;
    private Bundle bundle;

    private CoinTypes coin_type = CoinTypes.RMB;
    private OperateTypes operateType = OperateTypes.ALIPAY;
    private RateInfoEntity rateInfoEntity;
    private String nowCoinId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.fragment_recharge, container, false);
        dianInfoEntity = (DianInfoEntity) getArguments().getSerializable("Dian");
        coin_type = (CoinTypes) getArguments().getSerializable(HomeFragment.COIN_TYPE);
        operateType = (OperateTypes) getArguments().getSerializable(HomeFragment.OPERATE_TYPE);
        rateInfoEntity = (RateInfoEntity) getArguments().getSerializable(HomeFragment.RATE_INFO);
        nowCoinId = getArguments().getString(HomeFragment.COIN_ID);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rl_tiling = (RelativeLayout) view.findViewById(R.id.rl_daluBank);
        rl_tixian = (RelativeLayout) view.findViewById(R.id.rl_taiwanBank);
        rl_tiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AlipayActivity.class);
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putSerializable(HomeFragment.RATE_INFO, rateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                // toActivity(AlipayActivity.class, bundle);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        rl_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), DianBuyInputRateActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("Dian", dianInfoEntity);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
    }

}
