package com.likeit.currenciesapp.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

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
            holder.iv_avatar = (CircleImageView) convertView
                    .findViewById(R.id.iv_avatar);
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
        if ("2".equals(data1.getType()) || "8".equals(data1.getType())||"1".equals(data1.getType()) ||"7".equals(data1.getType()) ) {
            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(data1.getOther_user().getPic(), holder.iv_avatar);
        } else {
            holder.iv_avatar.setImageResource(R.mipmap.ic_launcher);
        }
        if("2".equals(data1.getType())||"8".equals(data1.getType())){
            holder.tv_title.setText(data1.getType_name()+"-來自" +"“" +data1.getOther_user().getTruename()+"”");
        }else if("1".equals(data1.getType())){
            holder.tv_title.setText(data1.getType_name()+"給-" +"“" +data1.getOther_user().getTruename()+"”");
        }else if("7".equals(data1.getType())){
            holder.tv_title.setText(data1.getType_name()+"-" +"“" +data1.getOther_user().getTruename()+"”");
        }else {
            holder.tv_title.setText(data1.getType_name());
        }
        holder.tv_number.setText(data1.getDian());
        holder.tv_time.setText(data1.getAddtime());
        return convertView;
    }

    private class ViewHolder {
        TextView tv_title;
        TextView tv_number;
        TextView tv_time;
        TextView tv_tvLimit;
        CircleImageView iv_avatar;
    }
}
