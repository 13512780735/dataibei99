package com.likeit.currenciesapp.ui.chat.redMessage;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.views.CircleImageView;

import java.util.List;

/**
 * Created by Administrator on 2018/2/6.
 */

public class GroupRedListAdapter extends RecyclerView.Adapter<GroupRedListAdapter.ViewHolder> {
    private List<RedMemberModel> mRedList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name;
        TextView tv_time;
        TextView tv_money;
        TextView tv_luck;
        CircleImageView iv_Avatar;

        public ViewHolder(View view) {
            super(view);
            iv_Avatar = (CircleImageView) view.
                    findViewById(R.id.iv_Avatar);
            tv_name = (TextView) view
                    .findViewById(R.id.tv_name);
            tv_time = (TextView) view
                    .findViewById(R.id.tv_time);
            tv_money = (TextView) view
                    .findViewById(R.id.tv_money);
            tv_luck = (TextView) view
                    .findViewById(R.id.tv_luck);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.group_redlist_items, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        //       // RedMemberModel data1 = getItem(position);
//       // io.rong.imageloader.core.ImageLoader.getInstance().displayImage(data1.getPic(), holder.iv_Avatar, MyApplication.getOptions());
//       // holder.tv_name.setText("11");
////        holder.tv_time.setText(data1.getAddtime());
////        holder.tv_money.setText(data1.getMoney()+"元");
////        if("0".equals(data1.getLuck())){
////            holder.tv_luck.setVisibility(View.VISIBLE);
////        }
        RedMemberModel redMemberModel = mRedList.get(position);
        io.rong.imageloader.core.ImageLoader.getInstance().displayImage(redMemberModel.getPic(), holder.iv_Avatar, MyApplication.getOptions());
        holder.tv_name.setText(redMemberModel.getUser_name());
        holder.tv_time.setText(redMemberModel.getAddtime());
        holder.tv_money.setText(redMemberModel.getMoney() + "元");
        if ("0".equals(redMemberModel.getLuck())) {
            holder.tv_luck.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return mRedList.size();
    }


    public GroupRedListAdapter(List<RedMemberModel> redList) {
        mRedList = redList;
    }

}
//    public GroupRedListAdapter(Context context, List<RedMemberModel> redMemberModels) {
//        super(context, redMemberModels);
//    }
//
//    @Override
//    public View getItemView(int position, View convertView, ViewGroup parent) {
//        ViewHolder holder = null;
//
//        if (convertView == null) {
//            holder = new ViewHolder();
//            convertView = getInflater().inflate(
//                    R.layout.group_redlist_items, parent, false);
//            holder.iv_Avatar = (CircleImageView) convertView
//                    .findViewById(R.id.iv_Avatar);
//            holder.tv_name = (TextView) convertView
//                    .findViewById(R.id.tv_name);
//            holder.tv_time = (TextView) convertView
//                    .findViewById(R.id.tv_time);
//            holder.tv_money = (TextView) convertView
//                    .findViewById(R.id.tv_money);
//            holder.tv_luck = (TextView) convertView
//                    .findViewById(R.id.tv_luck);
//
//            convertView.setTag(holder);
//        } else {
//            holder = (ViewHolder) convertView.getTag();
//        }
//       // RedMemberModel data1 = getItem(position);
//       // io.rong.imageloader.core.ImageLoader.getInstance().displayImage(data1.getPic(), holder.iv_Avatar, MyApplication.getOptions());
//       // holder.tv_name.setText("11");
////        holder.tv_time.setText(data1.getAddtime());
////        holder.tv_money.setText(data1.getMoney()+"元");
////        if("0".equals(data1.getLuck())){
////            holder.tv_luck.setVisibility(View.VISIBLE);
////        }
//        return convertView;
//    }
//
//    private class ViewHolder {
//        CircleImageView iv_Avatar;
//        TextView tv_name;
//        TextView tv_time;
//        TextView tv_money;
//        TextView tv_luck;
//
//    }
//}
