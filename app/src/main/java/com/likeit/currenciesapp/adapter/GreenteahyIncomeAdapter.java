package com.likeit.currenciesapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/19.
 */

public class GreenteahyIncomeAdapter extends MyBaseAdapter<GreenteahyInfoEntity> {
    public GreenteahyIncomeAdapter(Context context, List<GreenteahyInfoEntity> greenteahyInfoEntities) {
        super(context, greenteahyInfoEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_greenteahy_income_listview_items, parent, false);
            holder.tv_title = (TextView) convertView
                    .findViewById(R.id.tv_title);
            holder.tv_number = (TextView) convertView
                    .findViewById(R.id.tv_number);
            holder.tv_time = (TextView) convertView
                    .findViewById(R.id.tv_time);
            holder.tv_tvLimit = (TextView) convertView
                    .findViewById(R.id.tv_tvLimit);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        GreenteahyInfoEntity data1 = getItem(position);
        holder.tv_title.setText(data1.getType_name());
        holder.tv_number.setText(data1.getDian());
        holder.tv_time.setText(data1.getAddtime());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_number;
        TextView tv_time;
        TextView tv_tvLimit;

    }
}
