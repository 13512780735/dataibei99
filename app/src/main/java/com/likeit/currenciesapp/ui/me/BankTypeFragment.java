package com.likeit.currenciesapp.ui.me;


import android.app.Activity;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class BankTypeFragment extends DialogFragment implements View.OnClickListener {
    private MyDialogFragment_Listener myDialogFragment_Listener;
    private RelativeLayout rlBank01, rlBank02;
    private String type;

    public BankTypeFragment() {
        // Required empty public constructor
    }

    // 回调接口，用于传递数据给Activity -------
    public interface MyDialogFragment_Listener {
        void getDataFrom_DialogFragment(String Data01);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            myDialogFragment_Listener = (MyDialogFragment_Listener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implementon MyDialogFragment_Listener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View view = inflater.inflate(R.layout.fragment_bank, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        rlBank01 = (RelativeLayout) view.findViewById(R.id.rl_daluBank);//大陆
        rlBank02 = (RelativeLayout) view.findViewById(R.id.rl_taiwanBank);//台湾
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_daluBank:
                type = "1";
                getDialog().dismiss();

                break;
            case R.id.rl_taiwanBank:
                type = "2";
                getDialog().dismiss();
                break;
        }
    }

    // DialogFragment关闭时回传数据给Activity
    @Override
    public void onDestroy() {
        // 通过接口回传数据给activity
        if (myDialogFragment_Listener != null) {
            myDialogFragment_Listener.getDataFrom_DialogFragment(type);
        }
        super.onDestroy();
    }
}
