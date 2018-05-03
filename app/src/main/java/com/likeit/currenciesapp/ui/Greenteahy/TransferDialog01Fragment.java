package com.likeit.currenciesapp.ui.Greenteahy;


import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.TransferAccountInfoEntity;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class TransferDialog01Fragment extends DialogFragment implements View.OnClickListener {


    private TransferAccountInfoEntity transferAccountInfoEntity;
    private String money;
    private EditText ed_input_name;
    private TextView tv_cancel;
    private TextView tv_sure;
    private String flag;
    private String targetId;

    public TransferDialog01Fragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().setCanceledOnTouchOutside(false);
        View view = inflater.inflate(R.layout.fragment_transfer_dialog01, container, false);
        Bundle bundle = getArguments();
        transferAccountInfoEntity = (TransferAccountInfoEntity) bundle.getSerializable("transferAccountInfoEntity");
        targetId = bundle.getString("targetId");
        money = bundle.getString("money");
        flag = bundle.getString("flag");
        Log.d("TAG", "money-->" + money);
        initView(view);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        return view;
    }

    private void initView(View view) {
        ed_input_name = (EditText) view.findViewById(R.id.ed_input_name);
        tv_cancel = (TextView) view.findViewById(R.id.tv_cancel);
        tv_sure = (TextView) view.findViewById(R.id.tv_sure);
        tv_cancel.setOnClickListener(this);
        tv_sure.setOnClickListener(this);
        ed_input_name.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_cancel:
                getDialog().dismiss();
                break;
            case R.id.tv_sure:
                String name = ed_input_name.getText().toString().trim();
                if (StringUtil.isBlank(name)) {
                    ToastUtil.showS(getActivity(), "姓名不能為空");
                    return;
                }
                TransferDialog02Fragment dialog = new TransferDialog02Fragment();
                Bundle bundle = new Bundle();
                bundle.putSerializable("transferAccountInfoEntity", transferAccountInfoEntity);
                bundle.putString("money", money);
                bundle.putString("name", transferAccountInfoEntity.getTruename());
                bundle.putString("flag", flag);
                bundle.putString("targetId", targetId);
                Log.d("TAG", "dian-->" + money + "name-->" + name + "transferAccountInfoEntity-->" + transferAccountInfoEntity);
                dialog.setArguments(bundle);
                dialog.show(this.getFragmentManager(), "TransferDialog02Fragment");
                this.getDialog().dismiss();
                break;
        }
    }
}
