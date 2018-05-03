package com.likeit.currenciesapp.ui.chat.redMessage;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.BaseActivity;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GroupRedListActivity extends BaseActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_name)
    TextView tv_name;
    List<RedMemberModel> list;
    //    @BindView(R.id.myListView)
//    ListView mListview;
    GroupRedListAdapter mAdapter;
    private String list01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_red_list);
        list = new ArrayList<>();
        ButterKnife.bind(this);
        tvHeader.setText("红包");
        initData();
    }

    private void initData() {
        Bundle bundle = getIntent().getExtras();
        list01 = bundle.getString("RedList01");
        try {
            JSONArray array = new JSONArray(list01);
            for (int i = 0; i < array.length(); i++) {
                JSONObject object = array.optJSONObject(i);
                RedMemberModel redMemberModel = new RedMemberModel();
                redMemberModel.setId(object.optString("id"));
                redMemberModel.setRed_id(object.optString("red_id"));
                redMemberModel.setRongcloud_id(object.optString("rongcloud_id"));
                redMemberModel.setUser_id(object.optString("user_id"));
                redMemberModel.setMoney(object.optString("money"));
                redMemberModel.setAddtime(object.optString("addtime"));
                redMemberModel.setLuck(object.optString("luck"));
                redMemberModel.setPic(object.optString("pic"));
                redMemberModel.setUser_name(object.optString("user_name"));
                redMemberModel.setTruename(object.optString("truename"));
                list.add(redMemberModel);
            }
            //s    mAdapter = new GroupRedListAdapter(context, list);
            Log.d("TAG999", list.toString());
            RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycleView);
            LinearLayoutManager layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);
            GroupRedListAdapter mAdapter = new GroupRedListAdapter(list);
            recyclerView.setAdapter(mAdapter);
            //mListview.setAdapter(mAdapter);
//            mAdapter.notifyDataSetChanged();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    @OnClick(R.id.backBtn)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                onBackPressed();
                break;
        }
    }
}
