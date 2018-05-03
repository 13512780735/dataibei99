package com.likeit.currenciesapp.ui.login;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.likeit.currenciesapp.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class regionfragment extends DialogFragment implements View.OnClickListener {
    private TextView tvTaiwan;
    private TextView tvDalu;
    public OnDialogListener mlistener;

//    // 回调接口，用于传递数据给Activity -------
//    public interface MyDialogFragment_Listener {
//        void getDataFrom_DialogFragment(int Data);
//    }

    public interface OnDialogListener {
        void onDialogClick(String person);
    }

    public void setOnDialogListener(OnDialogListener dialogListener) {
        this.mlistener = dialogListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_regionfragment, container, false);
        initView(view);

        return view;
    }

    private void initView(View view) {
        tvTaiwan = (TextView) view.findViewById(R.id.tv_taiwan);
        tvDalu = (TextView) view.findViewById(R.id.tv_dalu);
        tvTaiwan.setOnClickListener(this);
        tvDalu.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_taiwan:
                mlistener.onDialogClick("886");
                getDialog().dismiss();
                break;
            case R.id.tv_dalu:
                mlistener.onDialogClick("86");
                getDialog().dismiss();
                break;
        }
    }


}
