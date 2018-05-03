package com.likeit.currenciesapp.ui.chat.ui;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.KeFuListAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.KeFuEntity;
import com.likeit.currenciesapp.ui.BaseActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class KeFuListActivity extends BaseActivity {

    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;
    private KeFuListAdapter kefuListAdapter;
    private boolean isHttp = true;

    private LinearLayoutManager linearLayoutManager;
    private ArrayList<KeFuEntity> kefuData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_fu_list);
        ButterKnife.bind(this);
        kefuData = new ArrayList<>();
        initTopBar("客服");
        initSwipeRefresh();
        refreshItems();
    }

    private void initSwipeRefresh() {
        kefuListAdapter = new KeFuListAdapter();
        kefuListAdapter.setBaseActivity(this);
        linearLayoutManager = new LinearLayoutManager(context);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(kefuListAdapter);

//        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
//                R.color.holo_orange_light, R.color.holo_red_light);
//        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                // Refresh items
//                if (isHttp) {
//                    isHttp = true;
//                    kefuListAdapter.cleanDatas();
//                    refreshItems();
//                } else {
//                    showToast("没有更多数据了");
//                }
//            }
//        });

    }


    void refreshItems() {
        String url = AppConfig.LIKEIT_GET_KEFU;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG",response);
                JSONObject object = null;
                try {
                    object = new JSONObject(response);
                    String status = object.optString("status");
                    if ("true".equals(status)) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            KeFuEntity keFuEntity = new KeFuEntity();
                            JSONObject obj = array.optJSONObject(i);
                            keFuEntity.setRongcloud(obj.optString("rongcloud"));
                            keFuEntity.setTruename(obj.optString("truename"));
                            keFuEntity.setPic(obj.optString("pic"));
                            kefuData.add(keFuEntity);
                        }
                        kefuListAdapter.addDatas(kefuData);
                    } else {
                        isHttp = false;
                        showToast("没有更多数据了");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void failed(Throwable e) {

            }
        });

        onItemsLoadComplete();
    }

    void onItemsLoadComplete() {
        // Update the adapter and notify data set changed
        // ...

        // Stop refresh animation
        swipeRefreshLayout.setRefreshing(false);
    }
}