package com.likeit.currenciesapp.ui.home;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.likeit.currenciesapp.Listener.EndLessOnScrollListener;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.NoticAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.NoticeInfoEntity;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NoticeListActivity extends BaseActivity {

    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private NoticAdapter noticeAdapter;
    private LinearLayoutManager linearLayoutManager;
    EndLessOnScrollListener endLessOnScrollListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_message);
        ButterKnife.bind(this);
        initTopBar("通知公告");
        initSwipeRefresh();
        refreshItems();
    }

    private void initSwipeRefresh() {
        noticeAdapter = new NoticAdapter();
        linearLayoutManager = new LinearLayoutManager(context);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(noticeAdapter);

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


    private void refreshItems() {
        String url = AppConfig.LIKEIT_GET_NOTICE;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("page", page);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String code = obj.optString("code");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        ArrayList<NoticeInfoEntity> mData = (ArrayList<NoticeInfoEntity>) JSON.parseArray(obj.optString("info"), NoticeInfoEntity.class);
                        if (page == 1) {
                            noticeAdapter.setDatas(mData);
                        } else {
                            noticeAdapter.addDatas(mData);
                        }
                    } else {
                        showToast("没有更多数据了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {

            }

            @Override
            public void onFinish() {
                super.onFinish();
                onRefreshComplete();
            }
        });
//        HttpMethods.getInstance().getNoticList(new MySubscriber<ArrayList<NoticeInfoEntity>>(this) {
//            @Override
        //            public void onHttpCompleted(HttpResult<ArrayList<NoticeInfoEntity>> httpResult) {
//                onRefreshComplete();
//                if (httpResult.isStatus()) {
//                    if (httpResult.getInfo() != null && httpResult.getInfo().size() > 0) {
//                        if (page == 1) {
//                            noticeAdapter.setDatas(httpResult.getInfo());
//                        } else {
//                            noticeAdapter.addDatas(httpResult.getInfo());
//                        }
//                    } else {
//                        showToast("没有更多数据了");
//                    }
//                } else {
//                    showToast("没有更多数据了");
//                }
//            }
//
//            @Override
//            public void onHttpError(Throwable e) {
//                onRefreshComplete();
//            }
//        }, uKey, page, 100);


    }

    void onRefreshComplete() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (endLessOnScrollListener != null) {
            endLessOnScrollListener.onLoadMoreFinish();
        }
    }
}
