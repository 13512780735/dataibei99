package com.likeit.currenciesapp.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.NoticeInfoEntity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.home.WebActivity;

import butterknife.BindView;
import butterknife.ButterKnife;


public class NoticViewHolder extends BaseViewHolder<NoticeInfoEntity> {

    @BindView(R.id.title_tv)
    TextView titleTv;
    @BindView(R.id.time_tv)
    TextView timeTv;

    public NoticViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }

    @Override
    public void onBind(final NoticeInfoEntity noticeInfoEntity, int position) {
        titleTv.setText(noticeInfoEntity.getCntitle());
        timeTv.setText(noticeInfoEntity.getAddtime());
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, WebActivity.class);
                intent.putExtra(WebActivity.WEB_TITLE, noticeInfoEntity.getCntitle());
                intent.putExtra(WebActivity.WEB_URL, AppConfig.IMAGE_URL_HOST + "?&m=index&a=notice_detail&id=" + noticeInfoEntity.getId());
                context.startActivity(intent);
            }
        });

    }
}
