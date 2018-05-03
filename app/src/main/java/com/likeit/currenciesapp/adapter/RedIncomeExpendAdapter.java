package com.likeit.currenciesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.base.KKBaseAdapter;
import com.likeit.currenciesapp.ui.me.RedPacketActivity;

/**
 * Created by Administrator on 2018/1/12.
 */

public class RedIncomeExpendAdapter extends KKBaseAdapter<GreenteahyInfoEntity>{
    @Override
    public BaseViewHolder<GreenteahyInfoEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_greenteahy_income_listview_items, parent, false);
        RedViewHolder redViewHolder = new RedViewHolder(itemView);
        redViewHolder.setBaseActivity(baseActivity);
        return redViewHolder;
    }

    RedPacketActivity baseActivity;

    public void setBaseActivity(RedPacketActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
