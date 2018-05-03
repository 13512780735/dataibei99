package com.likeit.currenciesapp.ui.chat.redMessage;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.views.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class RedMessage01Fragment extends DialogFragment {


    private LoaddingDialog loaddingDialog;

    private CircleImageView iv_header;
    private TextView tv_name,tv_money,tv_look_others;
    private ImageView iv_open_rp;

    public RedMessage01Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_red_message01, container, false);
        loaddingDialog = new LoaddingDialog(getActivity());
        initView(view);
        return view;
    }

    private void initView(View view) {
        iv_header = (CircleImageView) view.findViewById(R.id.iv_header);
        tv_name = (TextView) view.findViewById(R.id.tv_name);
        tv_money = (TextView) view.findViewById(R.id.tv_tip);
        tv_look_others = (TextView) view.findViewById(R.id.tv_look_others);
        iv_open_rp = (ImageView) view.findViewById(R.id.iv_open_rp);
    }

}
