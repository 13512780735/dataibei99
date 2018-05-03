package com.likeit.currenciesapp.ui.fragment;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.GreenteahyIncomeAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianBuyInputRateActivity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellInputRateActivity;
import com.likeit.currenciesapp.ui.Greenteahy.TransferAccountsActivity;
import com.likeit.currenciesapp.ui.base.BaseFragment;
import com.likeit.currenciesapp.ui.home.WithdrawDialogFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.MyListview;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class GreenteahyFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener, PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {


    private TextView tvHeader;
    private ListView mIncomeListView;
    private MyListview mExpendListView;
    private RadioGroup rdGroup;
    private RadioButton rdIncome;
    private RadioButton rdExpend;
    private View lineIncome;
    private View lineExpend;
    /**
     * 收入列表
     *
     * @param savedInstanceState
     */
    private List<GreenteahyInfoEntity> dataList;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private TextView tvWithdraw;
    private TextView tvBuy, tvTotalDian, tvAnnualRate, tvYesterDayAccrual, tvAddUpAccrual;
    private ProgressDialog mDialog;
    private DianInfoEntity mDianInfoEntity;
    private TextView tv_rlGreenteahy_transfer;
    private int log_type;
    private GreenteahyIncomeAdapter mAdapter;
    private TextView tvNodata;
    private Bundle bundle1;
    private String work;

    @Override
    protected int setContentView() {
        return R.layout.fragment_greenteahy;
    }


    @Override
    protected void lazyLoad() {
        //获取点数
        work= UtilPreference.getStringValue(getActivity(),"work");
        dataList = new ArrayList<GreenteahyInfoEntity>();
        log_type = 2;
        initData();
        initDian(log_type);
        initView();
    }
//    private void firstRefresh() {
//        initDian(log_type);
//    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        // initView();
    }

    private void initDian(int log_type) {
        String url = AppConfig.LIKEIT_GET_DIAN_LOG;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("log_type", log_type);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.optString("status");
                    if ("true".equals(status)) {
                        tvNodata.setVisibility(View.GONE);
                        mExpendListView.setVisibility(View.VISIBLE);
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            GreenteahyInfoEntity greenteahyInfoEntity = JSON.parseObject(array.optString(i), GreenteahyInfoEntity.class);
                            dataList.add(greenteahyInfoEntity);
                        }
                        mAdapter.notifyDataSetChanged();
                    } else {
                        // showToast("暂无数据");
                        mExpendListView.setVisibility(View.GONE);
                        tvNodata.setVisibility(View.VISIBLE);
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
                mDialog.dismiss();
            }
        });
    }

    private void initData() {
        mDialog = new ProgressDialog(getActivity());
        mDialog.setTitle("Loading...");
        mDialog.show();
        String url = AppConfig.LIKEIT_GET_DIAN;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                mDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        mDianInfoEntity = JSON.parseObject(obj.optString("info"), DianInfoEntity.class);
                        tvTotalDian.setText(mDianInfoEntity.getDianshu());
                        tvAnnualRate.setText("年紅利" + mDianInfoEntity.getNian());
                        tvYesterDayAccrual.setText(mDianInfoEntity.getY_dianshu());
                        tvAddUpAccrual.setText(mDianInfoEntity.getTotal());
                    } else {
                        ToastUtil.showS(getActivity(), msg);
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
                mDialog.dismiss();

            }
        });
    }

    private void initView() {
        tvHeader = findViewById(R.id.tv_header);
        tvHeader.setText(this.getResources().getString(R.string.greenteahy_name01));
        tvBuy = findViewById(R.id.tv_buy);//购买
        tvWithdraw = findViewById(R.id.tv_Withdraw);//提领
        tvTotalDian = findViewById(R.id.tv_total_dian);//总计点数
        tvAnnualRate = findViewById(R.id.tv_annual_rate);//年利率
        tvYesterDayAccrual = findViewById(R.id.textView02);//昨日利息
        tvAddUpAccrual = findViewById(R.id.textView04);//累计利息
        tvNodata = findViewById(R.id.nodata);
        tv_rlGreenteahy_transfer = findViewById(R.id.tv_tvGreenteahy_transfer);//转账
        rdGroup = findViewById(R.id.greenteahy_radio_group);
        rdIncome = findViewById(R.id.radio_income);
        rdExpend = findViewById(R.id.radio_expend);
        lineExpend = findViewById(R.id.line_expend);
        lineIncome = findViewById(R.id.line_income);
        mIncomeListView = findViewById(R.id.income_listview);
        mExpendListView = findViewById(R.id.expend_listview);
        mPullToRefreshScrollView = findViewById(R.id.ll_home_scrollview);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);
        mPullToRefreshScrollView.setOnRefreshListener(this);
        tvBuy.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        tv_rlGreenteahy_transfer.setOnClickListener(this);
        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");
        rdGroup.setOnCheckedChangeListener(this);
        /**
         * 收入数据初始化
         */
        mAdapter = new GreenteahyIncomeAdapter(getActivity(), dataList);
        //  simpleAdapter = new SimpleAdapter(getActivity(), dataList, R.layout.layout_greenteahy_income_listview_items, from, to);
        // mIncomeListView.setAdapter(simpleAdapter);
        mExpendListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();

    }


    @Override
    public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
        switch (checkedId) {
            case R.id.radio_income:
                mAdapter.addAll(dataList, true);
                mAdapter.notifyDataSetChanged();
                log_type = 2;
                initDian(log_type);
                lineIncome.setVisibility(View.VISIBLE);
                lineExpend.setVisibility(View.INVISIBLE);
                mIncomeListView.setVisibility(View.VISIBLE);
                mPullToRefreshScrollView.onRefreshComplete();
                break;
            case R.id.radio_expend:
                mAdapter.addAll(dataList, true);
                mAdapter.notifyDataSetChanged();
                log_type = 1;
                initDian(log_type);
                lineIncome.setVisibility(View.INVISIBLE);
                lineExpend.setVisibility(View.VISIBLE);
                mPullToRefreshScrollView.onRefreshComplete();
                break;
        }
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        initData();
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_buy:
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                Bundle bundle = new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                toActivity(DianBuyInputRateActivity.class, bundle);}
                break;
            case R.id.tv_Withdraw:
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else{
                bundle1 = new Bundle();
                bundle1.putSerializable("Dian", mDianInfoEntity);
                WithdrawDialogFragment dialogFragment = new WithdrawDialogFragment();
                dialogFragment.setArguments(bundle1);
                dialogFragment.show(getFragmentManager(), "dialogFragment");}
                break;
            case R.id.tv_tvGreenteahy_transfer:
                if("0".equals(work)){
                    ToastUtil.showL(getActivity(),"您沒有權限操作，請聯系管理員！");
                    return;
                }else {
                    bundle1 = new Bundle();
                    bundle1.putSerializable("Dian", mDianInfoEntity);
                    toActivity(TransferAccountsActivity.class, bundle1);
                }
                break;
        }
    }
}
