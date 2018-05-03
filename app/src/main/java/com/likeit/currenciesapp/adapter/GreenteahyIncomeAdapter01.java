package com.likeit.currenciesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.IncomeExpendActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.base.KKBaseAdapter;

/**
 * Created by Administrator on 2018/1/12.
 */

public class GreenteahyIncomeAdapter01 extends KKBaseAdapter<GreenteahyInfoEntity> {
    @Override
    public BaseViewHolder<GreenteahyInfoEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_greenteahy_income_listview_items, parent, false);
        GreenteahyViewHolder greenteahyViewHolder = new GreenteahyViewHolder(itemView);
        greenteahyViewHolder.setBaseActivity(baseActivity);
        return greenteahyViewHolder;
    }

    IncomeExpendActivity baseActivity;

    public void setBaseActivity(IncomeExpendActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
