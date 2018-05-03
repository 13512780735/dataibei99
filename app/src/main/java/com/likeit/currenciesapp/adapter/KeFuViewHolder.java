package com.likeit.currenciesapp.adapter;

import android.net.Uri;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.KeFuEntity;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.pk4pk.baseappmoudle.ui.GlideCircleTransform;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.rong.imkit.RongIM;
import io.rong.imkit.userInfoCache.RongUserInfoManager;
import io.rong.imlib.model.UserInfo;


public class KeFuViewHolder extends BaseViewHolder<KeFuEntity> {
    @BindView(R.id.img_tv)
    ImageView imgTv;
    @BindView(R.id.name_tv)
    TextView nameTv;
    BaseActivity baseActivity;

    public void setBaseActivity(BaseActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public KeFuViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    @Override
    public void onBind(final KeFuEntity keFuEntity, int position) {
        nameTv.setText(keFuEntity.getTruename());

        if(RongUserInfoManager.getInstance().getUserInfo(keFuEntity.getRongcloud())==null){
            UserInfo userInfo=new UserInfo(keFuEntity.getRongcloud()
                    ,keFuEntity.getTruename()
                    , Uri.parse(AppConfig.LIKEIT_LOGO1+keFuEntity.getPic()));
            RongIM.getInstance().refreshUserInfoCache(userInfo);
        }

        String headUrl = AppConfig.LIKEIT_LOGO1 + keFuEntity.getPic();
        Glide.with(context).load(headUrl)
                .bitmapTransform(new GlideCircleTransform(context))
                .error(context.getResources().getDrawable(R.mipmap.default_user_head))
                .into(imgTv);

        itemView.setTag(R.id.kk_order_id,keFuEntity.getRongcloud());
        //itemView.setTag(R.id.kk_order_id,"rJvdynl3Y");
        itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Intent intent=new Intent(context, ServiceChatActivity.class);
//                intent.putExtra(EaseConstant.EXTRA_USER_ID, (String) itemView.getTag(R.id.kk_order_id));
//                context.startActivity(intent);
                String displayName = keFuEntity.getTruename();
                if (!TextUtils.isEmpty(displayName)) {
                    RongIM.getInstance().startPrivateChat(context, keFuEntity.getRongcloud(), displayName);
                } else {
                    RongIM.getInstance().startPrivateChat(context, keFuEntity.getRongcloud(), keFuEntity.getTruename());
                }
// Xy3BHoEaT
//                if (!TextUtils.isEmpty(displayName)) {
//                    RongIM.getInstance().startPrivateChat(context, "rJvdynl3Y", displayName);
//                } else {
//                    RongIM.getInstance().startPrivateChat(context, "rJvdynl3Y", keFuEntity.getTruename());
//                }

            }
        });
    }


}
