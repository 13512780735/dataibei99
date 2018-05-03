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
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellInputRateActivity;
import com.likeit.currenciesapp.utils.UtilPreference;

/**
 * A simple {@link Fragment} subclass.
 */
public class WithdrawDialogFragment extends DialogFragment {


    private RelativeLayout rl_tiling;
    private RelativeLayout rl_tixian;
    private DianInfoEntity dianInfoEntity;
    private Bundle bundle;

    public WithdrawDialogFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(true);
        View view = inflater.inflate(R.layout.fragment_withdraw_dialog, container, false);
        dianInfoEntity = (DianInfoEntity) getArguments().getSerializable("Dian");
        initView(view);
        return view;

    }

    private void initView(View view) {
        rl_tiling = (RelativeLayout) view.findViewById(R.id.rl_daluBank);
        rl_tixian = (RelativeLayout) view.findViewById(R.id.rl_taiwanBank);
        rl_tiling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilPreference.saveString(getActivity(), "bankskey", "1");
                Intent intent = new Intent(getContext(), DianSellInputRateActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("Dian", dianInfoEntity);
               // bundle.putString("bankskey", "1");
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
        rl_tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UtilPreference.saveString(getActivity(), "bankskey", "2");
                Intent intent = new Intent(getContext(), DianSellInputRateActivity.class);
                bundle = new Bundle();
                bundle.putSerializable("Dian", dianInfoEntity);
                bundle.putString("bankskey", "2");
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                dismiss();
            }
        });
    }

}
