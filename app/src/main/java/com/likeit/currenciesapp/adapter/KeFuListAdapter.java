package com.likeit.currenciesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.KeFuEntity;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.base.KKBaseAdapter;

/**
 * Created by Administrator on 2017/12/9.
 */

public class KeFuListAdapter extends KKBaseAdapter<KeFuEntity> {

    @Override
    public BaseViewHolder<KeFuEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_kefu_list, parent, false);
        KeFuViewHolder orderViewHolder = new KeFuViewHolder(itemView);
        orderViewHolder.setBaseActivity(baseActivity);
        return orderViewHolder;
    }

    BaseActivity baseActivity;

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }
}
