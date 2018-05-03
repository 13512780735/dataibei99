package com.likeit.currenciesapp.adapter;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.OrderDetailInfoEntity;
import com.likeit.currenciesapp.model.OrderInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianBuyResultActivity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellResultActivity;
import com.likeit.currenciesapp.ui.base.BaseViewHolder;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.ui.home.AlipayResultActivity;
import com.likeit.currenciesapp.ui.home.BuyResultActivity;
import com.likeit.currenciesapp.ui.home.OtherResultActivity;
import com.likeit.currenciesapp.ui.home.PreResultActivity;
import com.likeit.currenciesapp.ui.home.SellResultActivity;
import com.likeit.currenciesapp.ui.me.OrderListActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.OrderNoticDialog;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.TipsDialog;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/11/13.
 */

public class OrderViewHolder extends BaseViewHolder<OrderInfoEntity> {

    @BindView(R.id.coin_img_tv)
    ImageView coinImgTv;
    @BindView(R.id.coin_name_tv)
    TextView coinNameTv;
    @BindView(R.id.time_tv)
    TextView timeTv;
    @BindView(R.id.rate_info_tv)
    TextView rateInfoTv;
    @BindView(R.id.money_tv)
    TextView moneyTv;
    @BindView(R.id.hand_money_tv)
    TextView handMoneyTv;
    @BindView(R.id.result_money_tv)
    TextView resultMoneyTv;
    @BindView(R.id.notice_hui_tv)
    TextView noticeHuiTv;
    @BindView(R.id.modify_tv)
    TextView modifyTv;
    @BindView(R.id.del_tv)
    TextView delTv;
    @BindView(R.id.status_tv)
    TextView statusTv;
    @BindView(R.id.yugou_layout)
    LinearLayout yugouLayout;
    @BindView(R.id.days_tv)
    TextView daysTV;
    @BindView(R.id.date_tv)
    TextView dateTv;

    OrderListActivity baseActivity;
    private String alipayGive = "0";
    private OrderDetailInfoEntity orderDetailInfoEntity;

    public void setBaseActivity(OrderListActivity baseActivity) {
        this.baseActivity = baseActivity;
    }

    public OrderViewHolder(View itemView) {
        super(itemView);
        ButterKnife.bind(this, itemView);
    }


    @Override
    public void onBind(final OrderInfoEntity orderInfoEntity, int position) {
        initStatus(orderInfoEntity.getZt());
        initCoinType(orderInfoEntity.getHbinfo().getId());
        if (TextUtils.equals("1", orderInfoEntity.getHkzt())) {
            noticeHuiTv.setText("已 經 通 知");
            noticeHuiTv.setBackgroundResource(R.drawable.shape_round_order_unable_item_butt);
            noticeHuiTv.setEnabled(false);
            isCanDel = false;
        } else {
            noticeHuiTv.setText("通 知 匯 款");
            noticeHuiTv.setBackgroundResource(R.drawable.shape_round_order_item_butt);
            noticeHuiTv.setEnabled(true);
        }

        alipayGive = orderInfoEntity.getZs_money();
        timeTv.setText(orderInfoEntity.getAddtime());
        rateInfoTv.setText("交易比數(" + orderInfoEntity.getHv() + ")");
        handMoneyTv.setText("手續費:" + orderInfoEntity.getSmoney());
        if (TextUtils.equals("3", orderInfoEntity.getTy()) &&
                !TextUtils.isEmpty(alipayGive) && !TextUtils.equals("0", alipayGive)) {
            handMoneyTv.setText("手續費:" + orderInfoEntity.getSmoney() + "     赠送:" + alipayGive);
        }

        resultMoneyTv.setText("交易金額(NT):" + orderInfoEntity.getHmoney());
        moneyTv.setText(orderInfoEntity.getMoney());

        coinNameTv.setText(orderInfoEntity.getHbinfo().getName() + getOperType(orderInfoEntity.getTy()));

        noticeHuiTv.setTag(R.id.kk_order_id, orderInfoEntity.getId());
        modifyTv.setTag(R.id.kk_order_id, orderInfoEntity.getId());
        statusTv.setTag(R.id.kk_order_id, orderInfoEntity.getId());

        daysTV.setText(orderInfoEntity.getZyq_day());
        dateTv.setText(orderInfoEntity.getZyq_end_time());

        delTv.setTag(R.id.kk_order_id, orderInfoEntity.getId());
        if (!isCanDel) {
            delTv.setVisibility(View.INVISIBLE);
        } else {
            delTv.setVisibility(View.VISIBLE);
        }

        delTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                TipsDialog tipsDialog = new TipsDialog(context);
                tipsDialog.setTips("確定要刪除此項訂單嗎？");
                tipsDialog.setLeftButt("取消");
                tipsDialog.setRightButt("確定");
                tipsDialog.setOnClickListener(new TipsDialog.OnClickListener() {
                    @Override
                    public void onRightClick() {
                        String url = AppConfig.LIKEIT_DEL_ORDER;
                        RequestParams params = new RequestParams();
                        params.put("ukey", UtilPreference.getStringValue(context, "ukey"));
                        params.put("id", (String) v.getTag(R.id.kk_order_id));
                        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                            @Override
                            public void success(String response) {
                                try {
                                    JSONObject obj = new JSONObject(response);
                                    String status = obj.optString("status");
                                    String code = obj.optString("code");
                                    String msg = obj.optString("msg");
                                    if ("true".equals(status)) {
                                        showToast("刪除成功");
                                        baseActivity.page = 1;
                                        baseActivity.refreshItems();
                                    } else {
                                        showToast(msg);
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }

                            @Override
                            public void failed(Throwable e) {

                            }
                        });
                    }

                    @Override
                    public void onLeftClick() {

                    }
                });
                tipsDialog.show();
            }
        });
    }


    private String getOperType(String ty) {
        yugouLayout.setVisibility(View.INVISIBLE);
        switch (ty) {
            case "1":
                return " (賣出)";
            case "2":
                return " (買進)";
            case "3":
                return " (充值)";
            case "4":
                return " (代付)";
            case "5":
                yugouLayout.setVisibility(View.VISIBLE);
                return " (預購)";
            case "6":
                return " (購買點數)";
            case "7":
                return " (代購)";
            case "8":
                return " (提領點數)";
        }
        return "";
    }

    private void initCoinType(String coinId) {
        switch (coinId) {
            case "2"://人民幣
                coinImgTv.setImageResource(R.mipmap.icon_rmb);
                break;
            case "3"://美元
                coinImgTv.setImageResource(R.mipmap.icon_dollar);
                break;
            case "4"://泰銖
                coinImgTv.setImageResource(R.mipmap.icon_baht);
                break;
            case "5"://日元
                coinImgTv.setImageResource(R.mipmap.icon_yen);
                break;
            case "6"://韓幣
                coinImgTv.setImageResource(R.mipmap.icon_krw);
                break;
            case "7"://港幣
                coinImgTv.setImageResource(R.mipmap.icon_hkd);
                break;
        }
    }

    private boolean isCanDel = false;

    private void initStatus(String zt) {
        switch (zt) {
            case "0":
                statusTv.setText("受理中");
                isCanDel = true;
                break;
            case "1":
                statusTv.setText("已付款");
                isCanDel = false;
                break;
            case "2":
                statusTv.setText("已受理");
                isCanDel = false;
                break;
            case "3":
                isCanDel = false;
                statusTv.setText("已到期");
                break;
            case "4":
                isCanDel = false;
                statusTv.setText("已完成");
                break;
            case "98":
            case "99":
                isCanDel = true;
                statusTv.setText("已取消");
                break;
        }
    }


    @OnClick({R.id.notice_hui_tv, R.id.modify_tv, R.id.status_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.notice_hui_tv:
                noticeHui((String) noticeHuiTv.getTag(R.id.kk_order_id));
                break;
            case R.id.modify_tv:
                modifyOrder((String) noticeHuiTv.getTag(R.id.kk_order_id));
                break;
            case R.id.status_tv:
                break;
        }
    }


    private void modifyOrder(String orderId) {
        String url = AppConfig.LIKEIT_ORDER_DETAIL;
        RequestParams params = new RequestParams();
        params.put("ukey", UtilPreference.getStringValue(context, "ukey"));
        params.put("id", orderId);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        orderDetailInfoEntity = JSON.parseObject(obj.optString("info"), OrderDetailInfoEntity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(HomeFragment.COIN_TYPE, orderDetailInfoEntity.getHb2());
//                    bundle.putSerializable(HomeFragment.RATE_INFO,orderDetailInfoEntity.getHv());

                        bundle.putDouble(HomeFragment.NOW_Bl, Double.valueOf(orderDetailInfoEntity.getHv()));
                        bundle.putDouble(HomeFragment.NOW_RATE, Double.valueOf(orderDetailInfoEntity.getHv()));
                        double d1 = Double.valueOf(orderDetailInfoEntity.getHmoney());
                        bundle.putLong(HomeFragment.REAL_GET, (long) d1);
                        double d2 = Double.valueOf(orderDetailInfoEntity.getSmoney());
                        bundle.putLong(HomeFragment.HAND, (long) d2);

                        bundle.putString(HomeFragment.FORMULA, "");
                        bundle.putString(HomeFragment.INPUT_VALUE, orderDetailInfoEntity.getMoney());
                        bundle.putString(HomeFragment.COIN_NAME, initCoinName(orderDetailInfoEntity.getHb2()));
                        bundle.putString(HomeFragment.COIN_ID, orderDetailInfoEntity.getHb2());
                        bundle.putString(HomeFragment.PRE_INFO_ID, orderDetailInfoEntity.getId());
                        bundle.putBoolean(HomeFragment.MODIFY_ORDER, true);
                        bundle.putString(HomeFragment.ORDER_ID, orderDetailInfoEntity.getId());
                        bundle.putString(HomeFragment.AlipayGive, alipayGive);
                        bundle.putString(HomeFragment.ORDER_PTY, orderDetailInfoEntity.getPty());

                        bundle.putString(HomeFragment.HBANK_1, orderDetailInfoEntity.getHbank1());
                        bundle.putString(HomeFragment.HBANK_2, orderDetailInfoEntity.getHbank2());
                        bundle.putString(HomeFragment.HBANK_3, orderDetailInfoEntity.getHbank3());
                        bundle.putString(HomeFragment.HBANK_4, orderDetailInfoEntity.getHbank4());
                        bundle.putString(HomeFragment.HBANK_5, orderDetailInfoEntity.getHbank5());
                        bundle.putString(HomeFragment.HBANK_6, orderDetailInfoEntity.getHbank6());

                        bundle.putString(HomeFragment.BANK_1, orderDetailInfoEntity.getBank1());
                        bundle.putString(HomeFragment.BANK_2, orderDetailInfoEntity.getBank2());
                        bundle.putString(HomeFragment.BANK_3, orderDetailInfoEntity.getBank3());
                        bundle.putString(HomeFragment.BANK_4, orderDetailInfoEntity.getBank4());
                        bundle.putString(HomeFragment.BANK_5, orderDetailInfoEntity.getBank5());
                        bundle.putString(HomeFragment.BANK_6, orderDetailInfoEntity.getBank6());
                        bundle.putString(HomeFragment.BANK_11, orderDetailInfoEntity.getBank11());
                        bundle.putString(HomeFragment.BANK_12, orderDetailInfoEntity.getBank12());
                        Log.d("TAG", "bundle-->" + bundle);
                        switch (orderDetailInfoEntity.getTy()) {
                            case "1"://1，卖出
                                toActivity(SellResultActivity.class, bundle);
                                break;
                            case "2"://2，买进
                                toActivity(BuyResultActivity.class, bundle);
                                break;
                            case "3"://3，充值
                                toActivity(AlipayResultActivity.class, bundle);
                                break;
                            case "4"://4，代付
                                toActivity(OtherResultActivity.class, bundle);
                                break;
                            case "5"://5，预购
                                toActivity(PreResultActivity.class, bundle);
                                break;
                            case "6"://6，购买点数
                                toActivity(DianBuyResultActivity.class, bundle);
                                break;
                            case "7"://7，代购
                                break;
                            case "8"://8，提领点数
                                //toActivity(DianSellResultActivity.class, bundle);
                                ToastUtil.showL(context, "暂不支持修改");
                                break;
                        }
                    } else {
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }
        });
    }


    private void noticeHui(final String orderId) {
//
        OrderNoticDialog noticDialog = new OrderNoticDialog(context);


        noticDialog.setOnClickListener(new OrderNoticDialog.OnClickListener() {
            @Override
            public void onRightClick(String name, String bank, String num) {
                String url = AppConfig.LIKEIT_DO_HUIKUAN;
                RequestParams params = new RequestParams();
                params.put("ukey", UtilPreference.getStringValue(context, "ukey"));
                params.put("huming", name);
                params.put("bankname", bank);
                params.put("bankcode5", num);
                params.put("id", orderId);
                HttpUtil.post(url, params, new HttpUtil.RequestListener() {
                    @Override
                    public void success(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String status = obj.optString("status");
                            String code = obj.optString("code");
                            String msg = obj.optString("msg");
                            if ("true".equals(status)) {
                                showToast("发送汇款通知成功");
                                //  RxBus.get().post(new RefreshEvent(RefreshEvent.GET_ORDER_LIST));
                                baseActivity.page = 1;
                                baseActivity.refreshItems();
                            } else {
                                showToast(msg);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void failed(Throwable e) {

                    }
                });
            }

            @Override
            public void onLeftClick() {

            }
        });

        noticDialog.show();
    }

    private void toActivity(Class activity, Bundle bundle) {
        Intent intent = new Intent(context, activity);
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private String initCoinName(String coinId) {
        switch (coinId) {
            case "2":
                return "人民幣";
            case "3":
                return "美元";
            case "4":
                return "泰銖";
            case "5":
                return "日元";
            case "6":
                return "韓幣";
            case "7":
                return "港幣";
        }
        return "";
    }
}
