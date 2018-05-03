package com.likeit.currenciesapp.ui.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.DefaultSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;
import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.app.MyApplication;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.CurrencyBean;
import com.likeit.currenciesapp.model.DianInfoEntity;
import com.likeit.currenciesapp.model.GreenteahyInfoEntity;
import com.likeit.currenciesapp.model.HomeInfoEntity;
import com.likeit.currenciesapp.model.LoginUserInfoEntity;
import com.likeit.currenciesapp.model.RateInfoEntity;
import com.likeit.currenciesapp.model.UserInfo;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellInputRateActivity;
import com.likeit.currenciesapp.ui.Greenteahy.IncomeExpendActivity;
import com.likeit.currenciesapp.ui.base.BaseFragment;
import com.likeit.currenciesapp.ui.home.AlipayActivity;
import com.likeit.currenciesapp.ui.home.BuyInActivity;
import com.likeit.currenciesapp.ui.home.ExchangeRateActivity;
import com.likeit.currenciesapp.ui.home.NegotiationActivity;
import com.likeit.currenciesapp.ui.home.NoticeListActivity;
import com.likeit.currenciesapp.ui.home.PreOrderActivity;
import com.likeit.currenciesapp.ui.home.RechargeFragment;
import com.likeit.currenciesapp.ui.home.SellActivity;
import com.likeit.currenciesapp.ui.home.WithdrawDialogFragment;
import com.likeit.currenciesapp.ui.me.OrderListActivity;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.likeit.currenciesapp.views.CircleImageView;
import com.likeit.currenciesapp.views.NoScrollViewPager;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends BaseFragment implements PullToRefreshBase.OnRefreshListener2<ScrollView>, View.OnClickListener {


    private ImageView ivLeft;
    private ImageView ivRight;
    private TextView tvHeader;
    private SliderLayout sliderShow;
    private TextView tvNotice;
    private PullToRefreshScrollView mPullToRefreshScrollView;
    private RelativeLayout rlMoveExchangeRrate;
    private TextView tvSale, tvBuyIn, tvAlipay, tvNegotiation, tvPreOrder;
    private TextView tv_sale_, tv_buy_in_, tv_alipay_, tv_negotiation_, tv_Pre_order_;
    private TextView tvWithdraw, tv_Withdraw_;
    private RateInfoEntity mRateInfoEntity;
    private JSONObject object;
    private Bundle bundle;
    //private List<HomeADlistBean> adData;
    private String pmd;
    private List<CurrencyBean> currencyData;
    private String name;
    private boolean is_kaipan;
    private HomeInfoEntity mHomeInfoEntity;
    private RelativeLayout rl_my_greentadhy;
    public final static String COIN_TYPE = "coin_type";
    public final static String OPERATE_TYPE = "operate_type";
    public final static String COIN_ID = "coin_id";
    public final static String RATE_INFO = "rate_info";
    public final static String NOW_Bl = "nowBl";
    public final static String NOW_RATE = "nowRate";
    public final static String REAL_GET = "real_get";
    public final static String INPUT_VALUE = "inputValue";
    public final static String FORMULA = "formula";
    public final static String COIN_NAME = "coin_Name";
    public final static String HAND = "hand";
    public final static String PRE_INFO_ID = "pre_info_id";
    public final static String MODIFY_ORDER = "modify_order";
    public final static String ORDER_ID = "order_id";
    public final static String AlipayGive = "alipaygive";
    public final static String Balance = "balance";

    public final static String HBANK_1 = "HBANK_1";
    public final static String HBANK_2 = "HBANK_2";
    public final static String HBANK_3 = "HBANK_3";
    public final static String HBANK_4 = "HBANK_4";
    public final static String HBANK_5 = "HBANK_5";
    public final static String HBANK_6 = "HBANK_6";

    public final static String BANK_1 = "BANK_1";
    public final static String BANK_2 = "BANK_2";
    public final static String BANK_3 = "BANK_3";
    public final static String BANK_4 = "BANK_4";
    public final static String BANK_5 = "BANK_5";
    public final static String BANK_6 = "BANK_6";
    public final static String BANK_11 = "BANK_11";
    public final static String BANK_12 = "BANK_12";
    public final static String ORDER_PTY = "ORDER_PTY";


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    initData();
                    break;
            }
        }
    };
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            mHandler.postDelayed(runnable, 18000);
            Message message = new Message();
            message.what = 1;
            mHandler.sendMessage(message);
        }
    };
    private DianInfoEntity mDianInfoEntity;
    private String userName;
    private String userUrl;
    private CircleImageView user_Avatar;
    private UserInfo mUserInfo;
    private TextView tv_nian;
    private RelativeLayout rl_my_greentadhy01;
    private LinearLayout ll_income, ll_expend;
    private RelativeLayout rl_income, rl_expend;
    private TextView tv_income, tv_income01, tv_income02, tv_income03, tv_expend, tv_expend01, tv_expend02, tv_expend03;
    private List<HomeInfoEntity.AdsBean> imgId;
    private SliderLayout sliderLayout;
    private DefaultSliderView textSliderView;


    @Override
    protected int setContentView() {
        return R.layout.fragment_home;
    }

    @Override
    protected void lazyLoad() {
        imgId = new ArrayList();
        dataList = new ArrayList<GreenteahyInfoEntity>();//收入
        dataList01 = new ArrayList<GreenteahyInfoEntity>();//支出
        sliderShow = findViewById(R.id.slider);//Logo banner
        initView();
        loaddingDialog.show();
        // inithomeData();//首页数据
        initData();//汇率获取
        inithomeData();
        ininWithdraw();
        initUserInfo();
        initIncome();
        initExpend();
        String ukey = UtilPreference.getStringValue(getActivity(), "ukey");
        LoginUserInfoEntity mLoginUserInfoEntity = (LoginUserInfoEntity) getActivity().getIntent().getSerializableExtra("userInfo");
        Log.d("TAG", "ukey-->" + ukey);
        Log.d("TAG", "mLoginUserInfoEntity-->" + mLoginUserInfoEntity);
        mHandler.post(runnable);

    }

    @Override
    public void onResume() {
        super.onResume();
        ininWithdraw();
        initUserInfo();
        // initView();
    }

    private void initExpend() {
        String url = AppConfig.LIKEIT_GET_DIAN_LOG;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("log_type", 1);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.optString("status");
                    if ("true".equals(status)) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            GreenteahyInfoEntity greenteahyInfoEntity = JSON.parseObject(array.optString(i), GreenteahyInfoEntity.class);
                            dataList01.add(greenteahyInfoEntity);

                        }
                        tv_expend01.setText(dataList01.get(0).getDian());
                        tv_expend02.setText(dataList01.get(0).getType_name());
                        tv_expend03.setText(dataList01.get(0).getAddtime());
                    } else {
                        rl_expend.setVisibility(View.GONE);
                        tv_expend.setVisibility(View.VISIBLE);
                        tv_expend.setText("暫無數據");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private List<GreenteahyInfoEntity> dataList;
    private List<GreenteahyInfoEntity> dataList01;

    private void initIncome() {
        String url = AppConfig.LIKEIT_GET_DIAN_LOG;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("log_type", 2);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.optString("status");
                    if ("true".equals(status)) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            GreenteahyInfoEntity greenteahyInfoEntity = JSON.parseObject(array.optString(i), GreenteahyInfoEntity.class);
                            dataList.add(greenteahyInfoEntity);

                        }
                        tv_income01.setText(dataList.get(0).getDian());
                        tv_income02.setText(dataList.get(0).getType_name());
                        tv_income03.setText(dataList.get(0).getAddtime());

                    } else {
                        rl_income.setVisibility(View.GONE);
                        tv_income.setVisibility(View.VISIBLE);
                        tv_income.setText("暫無數據");

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void initUserInfo() {
        String url = AppConfig.LIKEIT_GET_INFO;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            public JSONObject object;

            @Override
            public void success(String response) {
                Log.d("TAG", response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        object = obj.optJSONObject("info");
                        mUserInfo = JSON.parseObject(obj.optString("info"), UserInfo.class);
                        tv_name.setText(mUserInfo.getTruename());
                        //  String portraitUri=AppConfig.LIKEIT_LOGO1 + mUserInfo.getPic();
                        io.rong.imageloader.core.ImageLoader.getInstance().displayImage(mUserInfo.getPic(), user_Avatar, MyApplication.getOptions());
                    } else {
                        ToastUtil.showS(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void ininWithdraw() {
        String url = AppConfig.LIKEIT_GET_DIAN;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    String code = obj.optString("code");
                    if ("true".equals(status)) {
                        mDianInfoEntity = JSON.parseObject(obj.optString("info"), DianInfoEntity.class);
                        tv_dianshu.setText(mDianInfoEntity.getDianshu());
                        tv_nian.setText("年紅利 " + mDianInfoEntity.getNian());
                    } else {
                        ToastUtil.showS(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void inithomeData() {
        String url = AppConfig.LIKEIT_HOME_ADL;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        object = obj.optJSONObject("info");
                        mHomeInfoEntity = JSON.parseObject(obj.optString("info"), HomeInfoEntity.class);
                        imgId = mHomeInfoEntity.getAds();
                        tvNotice.setText(mHomeInfoEntity.getPmd());
                        imageSlider();
                        //人民币
                        for (int i = 0; i < mHomeInfoEntity.getBarray().size(); i++) {
                            name = mHomeInfoEntity.getBarray().get(i).getName();
                            Log.d("TAG333", name + "");
                            if ("人民币".equals(name)) {
                                is_kaipan = mHomeInfoEntity.getBarray().get(i).isIs_kaipan();
                                Log.d("TAG", is_kaipan + "");
                                Log.d("TAG1", name + "");
                                if (!is_kaipan) {
                                    tvSale.setEnabled(false);
                                    tvBuyIn.setEnabled(false);
                                    tvAlipay.setEnabled(false);
                                    tvNegotiation.setEnabled(false);
                                    tvPreOrder.setEnabled(false);
                                    tvWithdraw.setEnabled(false);

                                    tvSale.setBackgroundResource(R.drawable.currency_isclosed);
                                    tvWithdraw.setBackgroundResource(R.drawable.currency_isclosed);
                                    tvBuyIn.setBackgroundResource(R.drawable.currency_isclosed);
                                    tvAlipay.setBackgroundResource(R.drawable.currency_isclosed);
                                    tvNegotiation.setBackgroundResource(R.drawable.currency_isclosed);
                                    tvPreOrder.setBackgroundResource(R.drawable.currency_isclosed);
                                }
                            }
                        }
                        Log.d("TAG", mHomeInfoEntity.toString());
                    } else {
                        ToastUtil.showS(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void initData() {
        String url = AppConfig.LIKEIT_HUILV;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("huilvid", 2);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {

                Log.d("TAG", "huilv-->" + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    String msg = obj.optString("msg");
                    if ("true".equals(status)) {
                        object = obj.optJSONObject("info");
                        mRateInfoEntity = JSON.parseObject(obj.optString("info"), RateInfoEntity.class);
                        Log.d("TAG", object.toString());
                        UtilPreference.saveString(getActivity(), "sold", object.optString("sold"));
                        tv_sale_.setText(String.valueOf(object.optString("sold")));
                        tv_buy_in_.setText(String.valueOf(object.optString("buy")));
                        tv_alipay_.setText(String.valueOf(object.optString("chongzhi")));
                        tv_negotiation_.setText(String.valueOf(object.optString("daifu")));
                        tv_Pre_order_.setText(String.valueOf(object.optString("yugou")));
                        tv_Withdraw_.setText(String.valueOf(object.optString("sold")));
                    } else {
                        ToastUtil.showS(getActivity(), msg);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                loaddingDialog.dismiss();
            }

            @Override
            public void onFinish() {
                super.onFinish();
            }
        });
    }

    private void imageSlider() {
        for (int i = 0; i < mHomeInfoEntity.getAds().size(); i++) {
            DefaultSliderView defaultSliderView = new DefaultSliderView(getActivity());
            // textSliderView.description("");//设置标题
            defaultSliderView.image(AppConfig.LIKEIT_LOGO1 + mHomeInfoEntity.getAds().get(i).getImg());//设置图片的网络地址
            final int finalI = i;
            defaultSliderView.setOnSliderClickListener(new BaseSliderView.OnSliderClickListener() {
                @Override
                public void onSliderClick(BaseSliderView slider) {
                    //广告点击事件
//                    String adlUrl = adData.get(finalI).getUrl();
//                    Intent intent = new Intent(getActivity(), WebADLActivity.class);
//                    intent.putExtra("adlUrl", adlUrl);
//                    startActivity(intent);
                }
            });
            //添加到布局中显示
            sliderShow.addSlider(defaultSliderView);

        }
        //其他设置
        sliderShow.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);//使用默认指示器，在底部显示
        sliderShow.setDuration(5000);//停留时间
        //sliderShow.setPresetTransformer(SliderLayout.Transformer.Accordion);
        sliderShow.setPresetTransformer(SliderLayout.Transformer.Default);
        sliderShow.addOnPageChangeListener(new ViewPagerEx.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        sliderShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        sliderLayout.stopAutoCycle();
//    }

    TextView tv_name, tv_dianshu;

    private void initView() {
        tv_nian = findViewById(R.id.tv_nianlv);
        ivLeft = findViewById(R.id.iv_header_left);
        ivRight = findViewById(R.id.iv_header_right);
        tvHeader = findViewById(R.id.tv_header);
        user_Avatar = findViewById(R.id.user_Avatar);
        tv_name = findViewById(R.id.tv_name);
        tv_dianshu = findViewById(R.id.tv_dianshu);
        tv_sale_ = findViewById(R.id.tv_sale_);//卖出
        tv_buy_in_ = findViewById(R.id.tv_buy_in_);//买进
        tv_alipay_ = findViewById(R.id.tv_alipay_);//支付宝充值
        tv_negotiation_ = findViewById(R.id.tv_negotiation_);//代付
        tv_Pre_order_ = findViewById(R.id.tv_Pre_order_);//预购
        tv_Withdraw_ = findViewById(R.id.tv_Withdraw_);//提领
        rl_my_greentadhy = findViewById(R.id.rl_my_greentadhy);//赚赚宝
        rl_my_greentadhy01 = findViewById(R.id.rl_my_greentadhy01);//赚赚宝
        ll_income = findViewById(R.id.ll_income);//收入更多记录
        ll_expend = findViewById(R.id.ll_expend);//支出更多记录
        rl_income = findViewById(R.id.rl_income);//最新收入记录
        tv_income = findViewById(R.id.tv_income);//收入无数据
        tv_income01 = findViewById(R.id.tv_income01);//收入无数据
        tv_income02 = findViewById(R.id.tv_income02);//收入无数据
        tv_income03 = findViewById(R.id.tv_income03);//收入无数据
        rl_expend = findViewById(R.id.rl_expend);//最新支出记录
        tv_expend = findViewById(R.id.tv_expend);//支出無數據
        tv_expend01 = findViewById(R.id.tv_expend01);//支出無數據
        tv_expend02 = findViewById(R.id.tv_expend02);//支出無數據
        tv_expend03 = findViewById(R.id.tv_expend03);//支出無數據

        // tv_sale_.setText(String.valueOf(object.optDouble("buy")));
        //更多汇率
        rlMoveExchangeRrate = findViewById(R.id.rl_move_exchange_rate);
        //卖出
        tvSale = findViewById(R.id.tv_sale);
        //买进
        tvBuyIn = findViewById(R.id.tv_buy_in);
        //支付宝充值
        tvAlipay = findViewById(R.id.tv_alipay);
        //代付
        tvNegotiation = findViewById(R.id.tv_negotiation);
        tvWithdraw = findViewById(R.id.tv_Withdraw);//提领
        //预购
        tvPreOrder = findViewById(R.id.tv_Pre_order);
        mPullToRefreshScrollView = findViewById(R.id.ll_home_scrollview);
        mPullToRefreshScrollView.setMode(PullToRefreshBase.Mode.BOTH);

        mPullToRefreshScrollView.getLoadingLayoutProxy().setLastUpdatedLabel(
                "上次刷新时间");
        mPullToRefreshScrollView.getLoadingLayoutProxy()
                .setPullLabel("下拉刷新");
        mPullToRefreshScrollView.getLoadingLayoutProxy().setReleaseLabel(
                "松开即可刷新");

        tvNotice = findViewById(R.id.notice_msg_tv);//广告条滚动
        tvNotice.setSelected(true);
        tvHeader.setText(this.getResources().getString(R.string.app_name));
        ivLeft.setImageResource(R.mipmap.nav_news);
        ivRight.setImageResource(R.mipmap.nav_indent);
        initListener();
        //   pmd=object.optString("pmd");

    }

    private void initListener() {
        mPullToRefreshScrollView.setOnRefreshListener(this);
        rlMoveExchangeRrate.setOnClickListener(this);//获取更多汇率
        tvSale.setOnClickListener(this);
        tvBuyIn.setOnClickListener(this);
        tvAlipay.setOnClickListener(this);
        tvNegotiation.setOnClickListener(this);
        tvPreOrder.setOnClickListener(this);
        tvWithdraw.setOnClickListener(this);
        ivRight.setOnClickListener(this);
        ivLeft.setOnClickListener(this);
        rl_my_greentadhy.setOnClickListener(this);
        rl_my_greentadhy01.setOnClickListener(this);
        ll_income.setOnClickListener(this);
        ll_expend.setOnClickListener(this);
    }

    @Override
    public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        ininWithdraw();
        initUserInfo();
        mPullToRefreshScrollView.onRefreshComplete();
    }

    @Override
    public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
        mPullToRefreshScrollView.onRefreshComplete();
    }

    private CoinTypes coin_type = CoinTypes.RMB;

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            /**
             * 更多汇率
             */
            case R.id.rl_move_exchange_rate:
                bundle = new Bundle();
                bundle.putSerializable("mRateInfoEntity", mRateInfoEntity);
                bundle.putSerializable("Dian", mDianInfoEntity);
                bundle.putBoolean("is_kaipan", is_kaipan);
                toActivity(ExchangeRateActivity.class, bundle);
                break;
            /**
             * 卖出
             */
            case R.id.tv_sale:
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.SELL);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                toActivity(SellActivity.class, bundle);
                break;
            /**
             * 买进
             */
            case R.id.tv_buy_in:
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.BUY);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                toActivity(BuyInActivity.class, bundle);
                break;
            /**
             * 支付宝充值
             */
            case R.id.tv_alipay:
                bundle = new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                //  toActivity(DianSellInputRateActivity.class, bundle);
                RechargeFragment dialogFragment01 = new RechargeFragment();
                dialogFragment01.setArguments(bundle);
                dialogFragment01.show(getFragmentManager(), "dialogFragment");



                break;
            /**
             * 代付
             */
            case R.id.tv_negotiation:
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.OTHER);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                toActivity(NegotiationActivity.class, bundle);
                break;
            /**
             * 预购
             */
            case R.id.tv_Pre_order:
                bundle = new Bundle();
                bundle.putSerializable(HomeFragment.COIN_TYPE, coin_type);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.PRE);
                bundle.putSerializable(HomeFragment.RATE_INFO, mRateInfoEntity);
                bundle.putString(HomeFragment.COIN_ID, "2");
                toActivity(PreOrderActivity.class, bundle);
                break;
            /**
             * 提领
             */
            case R.id.tv_Withdraw:
                bundle = new Bundle();
                bundle.putSerializable("Dian", mDianInfoEntity);
                //  toActivity(DianSellInputRateActivity.class, bundle);
                WithdrawDialogFragment dialogFragment = new WithdrawDialogFragment();
                dialogFragment.setArguments(bundle);
                dialogFragment.show(getFragmentManager(), "dialogFragment");
                break;
            case R.id.iv_header_right://个人中心
//                 mViewPage = (NoScrollViewPager) getActivity().findViewById(R.id.home_viewpager);
//                mViewPage.setCurrentItem(3);
                toActivity(OrderListActivity.class);
                break;
            case R.id.iv_header_left://消息界面
                toActivity(NoticeListActivity.class);
                break;
            case R.id.rl_my_greentadhy://我的賺賺寶
            case R.id.rl_my_greentadhy01://我的賺賺寶
                NoScrollViewPager mViewPage = (NoScrollViewPager) getActivity().findViewById(R.id.home_viewpager);
                mViewPage.setCurrentItem(1);
                break;
            case R.id.ll_income://更多收入
                if (dataList.size() == 0) {
                    showToast("暫無數據");
                    return;
                } else {
                    bundle = new Bundle();
                    bundle.putInt("log_type", 999);
                    toActivity(IncomeExpendActivity.class, bundle);
                }
                break;
            case R.id.ll_expend://更多支出
                if (dataList01.size() == 0) {
                    showToast("暫無數據");
                    return;
                } else {
                    bundle = new Bundle();
                    bundle.putInt("log_type", 100);
                    toActivity(IncomeExpendActivity.class, bundle);
                }
                break;
        }
    }
}
