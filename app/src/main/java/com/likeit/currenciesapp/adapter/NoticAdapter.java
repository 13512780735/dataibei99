package com.likeit.currenciesapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.NoticeInfoEntity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.base.KKBaseAdapter;

/**
 * Created by Administrator on 2017/11/15.
 */

public class NoticAdapter extends KKBaseAdapter<NoticeInfoEntity> {

    @Override
    public BaseViewHolder<NoticeInfoEntity> onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_notic, parent, false);
        return new NoticViewHolder(itemView);
    }
}
