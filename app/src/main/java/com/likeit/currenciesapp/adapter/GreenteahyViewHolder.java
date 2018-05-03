package com.likeit.currenciesapp.adapter;

import android.view.View;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.IncomeExpendActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Administrator on 2018/1/12.
 */

public class GreenteahyViewHolder extends BaseViewHolder<GreenteahyInfoEntity> {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.tv_number)
    TextView tv_number;
    @BindView(R.id.tv_time)
    TextView tv_time;
    @BindView(R.id.tv_tvLimit)
    TextView tv_tvLimit;


    IncomeExpendActivity baseActivity;

    public void setBaseActivity(IncomeExpendActivity baseActivity) {
        this.baseActivity = baseActivity;
    }


    public GreenteahyViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(GreenteahyInfoEntity greenteahyInfoEntity, int position) {
        tv_title.setText(greenteahyInfoEntity.getType_name());
        tv_number.setText(greenteahyInfoEntity.getDian());
        tv_time.setText(greenteahyInfoEntity.getAddtime());
        //  tv_tvLimit.setText();
    }
}
