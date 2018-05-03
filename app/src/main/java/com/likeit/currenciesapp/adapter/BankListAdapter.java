package com.likeit.currenciesapp.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.BankInfoEntity;

import java.util.List;

/**
 * Created by Administrator on 2017/12/6.
 */

public class BankListAdapter extends MyBaseAdapter<BankInfoEntity> {
    private Drawable img;
    private String bankid;

    public BankListAdapter(Context context, List<BankInfoEntity> bankInfoEntities) {
        super(context, bankInfoEntities);
    }

    @Override
    public View getItemView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = getInflater().inflate(
                    R.layout.layout_bank_listitems, parent, false);
            holder.bank_list_items = (LinearLayout) convertView
                    .findViewById(R.id.bank_list_items);
            holder.backName = (TextView) convertView
                    .findViewById(R.id.backName);
            holder.tv_account = (TextView) convertView
                    .findViewById(R.id.tv_account);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        BankInfoEntity data1 = getItem(position);
        String type = data1.getType();
//        蓝色#67a3fc
//        绿色#39d497
//        黄色#ffca76
//        红色#fe6263
        String str = data1.getBankid();
        if (str.length() > 4) {
            bankid = str.substring(str.length() - 4, str.length());
        } else {
            bankid = str;
        }
        if ("1".equals(type)) {
            img = context.getResources().getDrawable(R.mipmap.icon_bank);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            holder.backName.setCompoundDrawables(img, null, null, null); //设置左图标
            holder.bank_list_items.setBackground(context.getResources().getDrawable(R.drawable.dialog_bank__red_bg_a));
            holder.backName.setText(data1.getBankname());
            holder.tv_account.setText("* * * *" + "  " + "* * * *" + "  " + "* * * *" + "  " + bankid);
        } else if ("2".equals(type)) {
            img = context.getResources().getDrawable(R.mipmap.icon_bank);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            holder.backName.setCompoundDrawables(img, null, null, null); //设置左图标
            holder.bank_list_items.setBackground(context.getResources().getDrawable(R.drawable.dialog_bank__yellow_bg_a));
            holder.backName.setText(data1.getBankname());
            holder.tv_account.setText("* * * *" + "  " + "* * * *" + "  " + "* * * *" + "  " + bankid);
        } else if ("3".equals(type)) {
            img = context.getResources().getDrawable(R.mipmap.icon_zfb);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            holder.backName.setCompoundDrawables(img, null, null, null); //设置左图标
            holder.bank_list_items.setBackground(context.getResources().getDrawable(R.drawable.dialog_bank__blue_bg_a));
            holder.backName.setText(data1.getBankname());
            holder.tv_account.setText("* * *" + "  " + "* * *" + "  " + bankid);
        } else if ("4".equals(type)) {
            img = context.getResources().getDrawable(R.mipmap.icon_weixin);
            img.setBounds(0, 0, img.getMinimumWidth(), img.getMinimumHeight());
            holder.backName.setCompoundDrawables(img, null, null, null); //设置左图标
            holder.bank_list_items.setBackground(context.getResources().getDrawable(R.drawable.dialog_bank__green_bg_a));
            holder.backName.setText(data1.getBankname());
            holder.tv_account.setText("* * *" + "  " + "* * *" + "  " + bankid);
        }
        return convertView;
    }

    private class ViewHolder {
        TextView backName;
        TextView tv_account;
        LinearLayout bank_list_items;

    }
}
