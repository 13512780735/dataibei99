package com.likeit.currenciesapp.adapter;

import android.view.View;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.IncomeExpendActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.RoundImageView;
import com.nostra13.universalimageloader.core.ImageLoader;

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
    @BindView(R.id.iv_avatar)
    CircleImageView iv_avatar;


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

        tv_number.setText(greenteahyInfoEntity.getDian());
        tv_time.setText(greenteahyInfoEntity.getAddtime());
        //  tv_tvLimit.setText();
        if ("2".equals(greenteahyInfoEntity.getType()) || "8".equals(greenteahyInfoEntity.getType()) || "1".equals(greenteahyInfoEntity.getType()) || "7".equals(greenteahyInfoEntity.getType())) {
            io.rong.imageloader.core.ImageLoader.getInstance().displayImage(greenteahyInfoEntity.getOther_user().getPic(), iv_avatar);
        } else {
            iv_avatar.setImageResource(R.mipmap.ic_launcher);
        }
        if ("2".equals(greenteahyInfoEntity.getType()) || "8".equals(greenteahyInfoEntity.getType())) {
            tv_title.setText(greenteahyInfoEntity.getType_name() + "-來自"+"“"   + greenteahyInfoEntity.getOther_user().getTruename()+"”");
        } else if ("1".equals(greenteahyInfoEntity.getType())) {
            tv_title.setText(greenteahyInfoEntity.getType_name() + "給-" +"“"  + greenteahyInfoEntity.getOther_user().getTruename()+"”");
        } else if ("7".equals(greenteahyInfoEntity.getType())) {
            tv_title.setText(greenteahyInfoEntity.getType_name() + "-" +"“"  + greenteahyInfoEntity.getOther_user().getTruename()+"”");
        } else {
            tv_title.setText(greenteahyInfoEntity.getType_name());
        }
    }
}
