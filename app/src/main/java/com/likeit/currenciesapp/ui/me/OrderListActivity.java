package com.likeit.currenciesapp.ui.me;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.hwangjr.rxbus.annotation.Subscribe;
import com.hwangjr.rxbus.thread.EventThread;
import com.likeit.currenciesapp.Listener.EndLessOnScrollListener;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.OrderListAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.OrderInfoEntity;
import com.likeit.currenciesapp.rxbus.RefreshEvent;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderListActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;

    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private OrderListAdapter orderListAdapter;
    public int page = 1;

    private LinearLayoutManager linearLayoutManager;
    EndLessOnScrollListener endLessOnScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_inquiry);
        ButterKnife.bind(this);
        initView();
        initSwipeRefresh();
        refreshItems();
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.me_name39));
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }

    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void getImEvent(RefreshEvent event) {
        if (event.getType() == RefreshEvent.GET_ORDER_LIST) {
            page = 1;
            refreshItems();
        }
    }

    private void initSwipeRefresh() {
        orderListAdapter = new OrderListAdapter();
        orderListAdapter.setBaseActivity(OrderListActivity.this);
        linearLayoutManager = new LinearLayoutManager(context);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(orderListAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                page = 1;
                refreshItems();

            }
        });

        endLessOnScrollListener = new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                page++;
                refreshItems();
            }
        };
        itemsRecyclerView.addOnScrollListener(endLessOnScrollListener);
    }


    public void refreshItems() {
        String url = AppConfig.LIKEIT_GET_LIST;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", page);
        params.put("pgsize", 100);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                onRefreshComplete();
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        //OrderInfoEntity mOrderInfoEntity = JSON.parseObject(String.valueOf(obj.optJSONArray("info")), OrderInfoEntity.class);
                        ArrayList<OrderInfoEntity> orderData = (ArrayList<OrderInfoEntity>) JSON.parseArray(obj.optString("info"), OrderInfoEntity.class);
                        Log.d("TAG", orderData.toString());
                        if (page == 1) {
                            orderListAdapter.setDatas(orderData);
                        } else {
                            orderListAdapter.addDatas(orderData);
                        }
                    } else {
                        if (page == 1) {
                            orderListAdapter.cleanDatas();
                        }
                       // showToast("没有更多数据了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                onRefreshComplete();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                onRefreshComplete();
            }
        });

    }

    private void onRefreshComplete() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (endLessOnScrollListener != null) {
            endLessOnScrollListener.onLoadMoreFinish();
        }
    }
}
