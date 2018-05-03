package com.pk4pk.baseappmoudle.base;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

public abstract class KKBaseViewHolder<T> extends RecyclerView.ViewHolder{
    protected Context context;
    protected View itemView;

    public KKBaseViewHolder(View itemView) {
        super(itemView);
        this.itemView=itemView;
        this.context=itemView.getContext();
    }
    public abstract void onBind(T t,int position);

    protected void showToast(String msg){
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
