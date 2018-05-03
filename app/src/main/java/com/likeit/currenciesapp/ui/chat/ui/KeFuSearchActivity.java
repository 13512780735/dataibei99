package com.likeit.currenciesapp.ui.chat.ui;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.likeit.currenciesapp.Listener.EndLessOnScrollListener;
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
import butterknife.OnClick;

public class KeFuSearchActivity extends BaseActivity {

    @BindView(R.id.top_bar_back_img)
    ImageView topBarBackImg;
    @BindView(R.id.search_content_et)
    EditText searchContentEt;
    @BindView(R.id.search_tv)
    TextView searchTv;
    @BindView(R.id.itemsRecyclerView)
    RecyclerView itemsRecyclerView;
    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout swipeRefreshLayout;

    private LinearLayoutManager linearLayoutManager;
    EndLessOnScrollListener endLessOnScrollListener;
    private KeFuListAdapter kefuListAdapter;
    private boolean isHttp = true;
    private ArrayList<KeFuEntity> kefuData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ke_fu_search);
        ButterKnife.bind(this);
        kefuData = new ArrayList<>();
        initSwipeRefresh();
    }


    private void initSwipeRefresh() {
        kefuListAdapter = new KeFuListAdapter();
        kefuListAdapter.setBaseActivity(this);
        linearLayoutManager = new LinearLayoutManager(context);
        itemsRecyclerView.setLayoutManager(linearLayoutManager);
        itemsRecyclerView.setAdapter(kefuListAdapter);

        swipeRefreshLayout.setColorSchemeResources(R.color.holo_blue_bright, R.color.holo_green_light,
                R.color.holo_orange_light, R.color.holo_red_light);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Refresh items
                page = 1;
                search();
            }
        });

        endLessOnScrollListener = new EndLessOnScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore() {
                page++;
                search();
            }
        };
        itemsRecyclerView.addOnScrollListener(endLessOnScrollListener);

    }


    @OnClick({R.id.top_bar_back_img, R.id.search_tv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.top_bar_back_img:
                toFinish();
                break;
            case R.id.search_tv:
                search();
                break;
        }
    }

    private void search() {
        String searchContent = searchContentEt.getText().toString();
        if (TextUtils.isEmpty(searchContent)) {
            showToast("請輸入用戶名或ID");
            onRefreshComplete();
            return;
        }
        String url = AppConfig.LIKEIT_GET_SEARCH_USER;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("keyword", searchContent);
        params.put("page", page);
        params.put("pgsize", 100);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                onRefreshComplete();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    JSONArray array = obj.optJSONArray("info");
                    if("true".equals(status)){
                        for (int i = 0; i < array.length(); i++) {
                            KeFuEntity kefeEntity = new KeFuEntity();
                            JSONObject object = array.optJSONObject(i);
                            kefeEntity.setPic(object.optString("pic"));
                            kefeEntity.setRongcloud(object.optString("rongcloud"));
                            kefeEntity.setTruename(object.optString("truename"));
                            kefuData.add(kefeEntity);
                        }
                        if (page == 1) {
                            kefuListAdapter.setDatas(kefuData);
                        } else {
                            kefuListAdapter.addDatas(kefuData);
                        }
                    }else{
                        showToast(obj.optString("msg"));
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

    private void onRefreshComplete() {
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setRefreshing(false);
        }
        if (endLessOnScrollListener != null) {
            endLessOnScrollListener.onLoadMoreFinish();
        }
    }
}
