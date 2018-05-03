package com.likeit.currenciesapp.ui.Greenteahy;

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
import com.likeit.currenciesapp.adapter.GreenteahyIncomeAdapter01;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
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

public class IncomeExpendActivity extends BaseActivity {

    private int log_type;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    //    @BindView(R.id.expend_listview)
//    MyListview mListView;
    private GreenteahyIncomeAdapter01 mGreenteahyIncomeAdapter;


    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    public int page = 1;


    private LinearLayoutManager linearLayoutManager;
    EndLessOnScrollListener endLessOnScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getIntent() == null || getIntent().getExtras() == null) {
            toFinish();
            return;
        }
        log_type = getIntent().getExtras().getInt("log_type");
        setContentView(R.layout.activity_income_expend);
        ButterKnife.bind(this);
        initView();
        initSwipeRefresh();
        refreshItems();

    }


    @Subscribe(thread = EventThread.MAIN_THREAD)
    public void getImEvent(RefreshEvent event) {
        if (event.getType() == RefreshEvent.GET_ORDER_LIST) {
            page = 1;
            refreshItems();
        }
    }

    private void initSwipeRefresh() {
        mGreenteahyIncomeAdapter = new GreenteahyIncomeAdapter01();
        mGreenteahyIncomeAdapter.setBaseActivity(IncomeExpendActivity.this);
        linearLayoutManager = new LinearLayoutManager(context);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(mGreenteahyIncomeAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                //  page = 1;
                refreshItems();

            }
        });

        endLessOnScrollListener = new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                // page++;
                refreshItems();
            }
        };
        itemsRecyclerView.addOnScrollListener(endLessOnScrollListener);
    }


    public void refreshItems() {
        String url = AppConfig.LIKEIT_GET_DIAN_LOG;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("log_type", log_type);
        // params.put("page", page);
        // params.put("pgsize", 100);
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
                        ArrayList<GreenteahyInfoEntity> orderData = (ArrayList<GreenteahyInfoEntity>) JSON.parseArray(obj.optString("info"), GreenteahyInfoEntity.class);
                        Log.d("TAG", orderData.toString());
                        mGreenteahyIncomeAdapter.setDatas(orderData);
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


    private void initView() {
        if (log_type == 100) {
            tvHeader.setText("支出記錄");
        } else {
            tvHeader.setText("收入記錄");
        }
    }

    @OnClick({R.id.backBtn})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
