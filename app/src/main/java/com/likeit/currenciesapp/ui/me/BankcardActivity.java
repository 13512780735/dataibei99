package com.likeit.currenciesapp.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.adapter.BankListAdapter;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.model.BankInfoEntity;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellResultActivity;
import com.likeit.currenciesapp.ui.base.Container;
import com.likeit.currenciesapp.ui.chat.server.widget.LoadDialog;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class BankcardActivity extends AppCompatActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.tv_myListview)
    ListView mListView;
    private List<BankInfoEntity> bankData;
    private BankListAdapter mAdapter;
    private String bankflag;
    private String bankskey;
    private String bankid;

    private Bundle bundle;
    private String ukey;
    private BankcardActivity mContext;
    private String flag;
    protected String orderId = "0";
    protected String hbank_1, hbank_2, hbank_3, hbank_4, hbank_5, hbank_6;
    protected String bank_1, bank_2, bank_3, bank_4, bank_5, bank_6, bank_11, bank_12;
    protected String order_pty = "1";
    protected String alipayGive = "0";
    private String balance;
    private String inputValue01;
    protected double nowRate = 0;
    protected double nowBl = 0;
    long hand_ = 0;
    String coin_Name = "";
    String inputValue = "";
    long realGet = 0;
    String formula = "";
    protected String nowCoinId = "";
    protected String orderBeforeId = "";
    private String taibi;
    private String type01;
    private Bundle bundel;
    private String bankid01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bankcard);
        ButterKnife.bind(this);
        mContext = this;
        bankData = new ArrayList<>();
        ukey = UtilPreference.getStringValue(this, "ukey");
        bankskey = UtilPreference.getStringValue(this, "bankskey");
        flag = getIntent().getExtras().getString("flag");
        bankflag = getIntent().getExtras().getString("bankflag");
        //bankskey = getIntent().getExtras().getString("bankskey");
        // bankflag = UtilPreference.getStringValue(this, "bankflag");
        nowBl = getIntent().getExtras().getDouble(HomeFragment.NOW_Bl);
        nowRate = getIntent().getExtras().getDouble(HomeFragment.NOW_RATE);
        coin_Name = getIntent().getExtras().getString(HomeFragment.COIN_NAME);
        inputValue = getIntent().getExtras().getString(HomeFragment.INPUT_VALUE);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        realGet = getIntent().getExtras().getLong(HomeFragment.REAL_GET);
        formula = getIntent().getExtras().getString(HomeFragment.FORMULA);
        hand_ = getIntent().getExtras().getLong(HomeFragment.HAND);
        nowCoinId = getIntent().getExtras().getString(HomeFragment.COIN_ID);
        orderId = getIntent().getExtras().getString(HomeFragment.ORDER_ID, "0");
        alipayGive = getIntent().getExtras().getString(HomeFragment.AlipayGive, "0");
        balance = getIntent().getExtras().getString(HomeFragment.Balance);
        hbank_1 = getIntent().getExtras().getString(HomeFragment.HBANK_1, "");
        hbank_2 = getIntent().getExtras().getString(HomeFragment.HBANK_2, "");
        hbank_3 = getIntent().getExtras().getString(HomeFragment.HBANK_4, "");
        hbank_4 = getIntent().getExtras().getString(HomeFragment.HBANK_3, "");
        hbank_5 = getIntent().getExtras().getString(HomeFragment.HBANK_5, "");
        hbank_6 = getIntent().getExtras().getString(HomeFragment.HBANK_6, "");
        taibi = getIntent().getExtras().getString("taibi");
        bank_1 = getIntent().getExtras().getString(HomeFragment.BANK_1, "");
        bank_2 = getIntent().getExtras().getString(HomeFragment.BANK_2, "");
        bank_3 = getIntent().getExtras().getString(HomeFragment.BANK_3, "");
        bank_4 = getIntent().getExtras().getString(HomeFragment.BANK_4, "");
        bank_5 = getIntent().getExtras().getString(HomeFragment.BANK_5, "");
        bank_6 = getIntent().getExtras().getString(HomeFragment.BANK_6, "");
        bank_11 = getIntent().getExtras().getString(HomeFragment.BANK_11, "");
        bank_12 = getIntent().getExtras().getString(HomeFragment.BANK_12, "");
        order_pty = getIntent().getExtras().getString(HomeFragment.ORDER_PTY, "");
        Log.d("TAG2222",bankflag);
        initData();

        initView();
    }

    private void initData() {
        LoadDialog.show(mContext);
        String url = AppConfig.LIKEIT_GET_BANK;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                LoadDialog.dismiss(mContext);
                Log.d("TAG999", "bank" + response);
                try {
                    JSONObject object = new JSONObject(response);
                    String status = object.optString("status");
                    if ("true".equals(status)) {
                        JSONArray array = object.optJSONArray("info");
                        for (int i = 0; i < array.length(); i++) {
                            JSONObject obj = array.optJSONObject(i);
                            BankInfoEntity bankInfoEntity = new BankInfoEntity();
                            bankInfoEntity.setId(obj.optString("id"));
                            bankInfoEntity.setUser_id(obj.optString("user_id"));
                            bankInfoEntity.setBankname(obj.optString("bankname"));
                            bankInfoEntity.setBankid(obj.optString("bankid"));
                            bankInfoEntity.setProvince(obj.optString("province"));
                            bankInfoEntity.setZhi(obj.optString("zhi"));
                            bankInfoEntity.setHuming(obj.optString("huming"));
                            bankInfoEntity.setType(obj.optString("type"));
                            bankInfoEntity.setStatus(obj.optString("status"));
                            bankInfoEntity.setAddtime(obj.optString("addtime"));
                            bankInfoEntity.setUptime(obj.optString("uptime"));
                            bankInfoEntity.setBankcode(obj.optString("bankcode"));
                            bankInfoEntity.setDel(obj.optString("del"));
                            bankData.add(bankInfoEntity);
                        }
                        mAdapter.notifyDataSetChanged();
//                        String str = bankData.get(0).getBankid();
//                        if (str.length() > 4) {
//                            String bankid = str.substring(str.length() - 4, str.length());
//                            Log.d("TAG434", bankid);
//                        }else{
//
//                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable e) {
                LoadDialog.dismiss(mContext);
            }

            @Override
            public void onFinish() {
                super.onFinish();
                LoadDialog.dismiss(mContext);
            }
        });
    }

    private void initView() {
        tvHeader.setText(this.getResources().getString(R.string.me_name37));
        tvRight.setText(this.getResources().getString(R.string.me_name36));


        mAdapter = new BankListAdapter(mContext, bankData);
        mListView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String type = bankData.get(position).getType();
                Log.d("TAG", type + "");
                if ("1".equals(bankflag)) {
                    if ("1".equals(type)) {
                        Intent intent = new Intent(BankcardActivity.this, AddBankActivity.class);
                        bundel = new Bundle();
                        bundel.putString("keys", "2");
                        bundel.putString("bankflag", bankflag);
                        bundel.putString("id",bankData.get(position).getId());
                        bundel.putString("type", bankData.get(position).getType());
                        bundel.putString("bankid", bankData.get(position).getBankid());
                        bundel.putString("huming", bankData.get(position).getHuming());
                        bundel.putString("province", bankData.get(position).getProvince());
                        bundel.putString("zhi", bankData.get(position).getZhi());
                        bundel.putString("bankname", bankData.get(position).getBankname());
                        bundel.putString("banksId", bankData.get(position).getId());//列表ID
                        intent.putExtras(bundel);
                        startActivity(intent);
                        finish();
                    } else if ("2".equals(type)) {
                        Log.d("TAG999",bankData.get(position).getBankcode()+"");
                        Intent intent = new Intent(BankcardActivity.this, AddTaiwanBankActivity.class);
                        bundel = new Bundle();
                        bundel.putString("keys", "2");
                        bundel.putString("bankflag", bankflag);
                        bundel.putString("id",bankData.get(position).getId());
                        bundel.putString("type", bankData.get(position).getType());
                        bundel.putString("bankid", bankData.get(position).getBankid());
                        bundel.putString("huming", bankData.get(position).getHuming());
                        bundel.putString("bankcode", bankData.get(position).getBankcode());
                        bundel.putString("zhi", bankData.get(position).getZhi());
                        bundel.putString("bankname", bankData.get(position).getBankname());
                        bundel.putString("banksId", bankData.get(position).getId());//列表ID
                        intent.putExtras(bundel);
                        startActivity(intent);
                        finish();
                    } else if ("3".equals(type) || "4".equals(type)) {
                        Intent intent = new Intent(BankcardActivity.this, AddWeixinZFBActivity.class);
                        bundel = new Bundle();
                        bundel.putString("keys", "2");
                        bundel.putString("bankflag", bankflag);
                        bundel.putString("id",bankData.get(position).getId());
                        bundel.putString("type", bankData.get(position).getType());
                        bundel.putString("bankid", bankData.get(position).getBankid());
                        bundel.putString("huming", bankData.get(position).getHuming());
                        bundel.putString("province", bankData.get(position).getProvince());
                        bundel.putString("zhi", bankData.get(position).getZhi());
                        bundel.putString("bankname", bankData.get(position).getBankname());
                        bundel.putString("banksId", bankData.get(position).getId());//列表ID
                        intent.putExtras(bundel);
                        startActivity(intent);
                        finish();
                    }
                } else if ("2".equals(bankflag)) {
                    bankid = bankData.get(position).getBankid();
                    type01 = bankData.get(position).getType();
                    if (bankid.length() > 4) {
                        bankid01 = bankid.substring(bankid.length() - 4, bankid.length());
                    } else {
                        bankid01 = bankid;
                    }
                    if ("1".equals(bankskey)) {
                        //  UtilPreference.saveString(mContext, "bankskey", bankskey);
                        if ("2".equals(type01)) {
                            mAdapter.isEnabled(position);
                            ToastUtil.showL(mContext, "请选择其他银行！");
                            return;
                        } else {
//                            UtilPreference.saveString(mContext, "bankflag", "2");
//                            UtilPreference.saveString(mContext, "bankskey", bankskey);
                            Intent intent = new Intent(mContext, DianSellResultActivity.class);
                            bundle = new Bundle();
                            bundle.putString("type", bankData.get(position).getType());
                            bundle.putString("bankid", bankid);
                            bundle.putString("bankid01", bankid01);
                            bundle.putString("bankflag", "2");
                            bundle.putString("bankskey", bankskey);
                            bundle.putString("huming", bankData.get(position).getHuming());
                            bundle.putString("province", bankData.get(position).getProvince());
                            bundle.putString("bankcode", bankData.get(position).getBankcode());
                            bundle.putString("zhi", bankData.get(position).getZhi());
                            bundle.putString("bankname", bankData.get(position).getBankname());
                            bundle.putString("banksId", bankData.get(position).getId());
                            bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                            bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                            bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                            bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                            bundle.putLong(HomeFragment.REAL_GET, realGet);
                            bundle.putLong(HomeFragment.HAND, hand_);
                            bundle.putString(HomeFragment.Balance, balance);
                            bundle.putString("taibi", taibi);
                            bundle.putString(HomeFragment.FORMULA, formula);
                            bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                            bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                            bundle.putString(HomeFragment.COIN_ID, "2");
                            // setResult(Activity.RESULT_OK, intent);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    } else if ("2".equals(bankskey)) {
                        // UtilPreference.saveString(mContext, "bankskey", bankskey);
                        if ("3".equals(type01) || "4".equals(type01)) {
                            mAdapter.isEnabled(position);
                            ToastUtil.showL(mContext, "请选择其他银行！");
                            return;
                        } else {
//                            UtilPreference.saveString(mContext, "bankflag", "2");
//                            UtilPreference.saveString(mContext, "bankskey", bankskey);
                            Intent intent = new Intent(mContext, DianSellResultActivity.class);
                            bundle = new Bundle();
                            bundle.putString("type", bankData.get(position).getType());
                            bundle.putString("bankid", bankid);
                            bundle.putString("bankid01", bankid01);
                            bundle.putString("bankflag", "2");
                            bundle.putString("bankskey", bankskey);
                            bundle.putString("huming", bankData.get(position).getHuming());
                            bundle.putString("province", bankData.get(position).getProvince());
                            bundle.putString("bankcode", bankData.get(position).getBankcode());
                            bundle.putString("zhi", bankData.get(position).getZhi());
                            bundle.putString("bankname", bankData.get(position).getBankname());
                            bundle.putString("banksId", bankData.get(position).getId());
                            bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                            bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                            bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                            bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                            bundle.putLong(HomeFragment.REAL_GET, realGet);
                            bundle.putLong(HomeFragment.HAND, hand_);
                            bundle.putString(HomeFragment.Balance, balance);
                            bundle.putString("taibi", taibi);
                            bundle.putString(HomeFragment.FORMULA, formula);
                            bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                            bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                            bundle.putString(HomeFragment.COIN_ID, "2");
                            // setResult(Activity.RESULT_OK, intent);
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        }
                    }

                }
            }
        });
    }

    @OnClick({R.id.backBtn, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                // onBackPressed();

                if ("2".equals(flag)) {
                    Intent intent = new Intent(mContext, DianSellResultActivity.class);
                    bundle = new Bundle();
                    bundle.putString("type", "");
                    bundle.putString("bankid", "");
                    bundle.putString("bankid01", "");
                    bundle.putString("huming", "");
                    bundle.putString("province", "");
                    bundle.putString("bankcode", "");
                    bundle.putString("zhi", "");
                    bundle.putString("bankname", "");
                    bundle.putString("banksId", "");
                    bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                    bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                    bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                    bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                    bundle.putLong(HomeFragment.REAL_GET, realGet);
                    bundle.putLong(HomeFragment.HAND, hand_);
                    bundle.putString(HomeFragment.Balance, balance);
                    bundle.putString("taibi", taibi);
                    bundle.putString(HomeFragment.FORMULA, formula);
                    bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                    bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                    bundle.putString(HomeFragment.COIN_ID, "2");
                    // setResult(Activity.RESULT_OK, intent);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    finish();
                } else {
                    onBackPressed();
                }
                break;
            case R.id.tv_right:
                Bundle bundle = new Bundle();
                bundle.putString("flag", flag);
                bundle.putString("bankflag", bankflag);
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                BankFragment bankFragment = new BankFragment();
                bankFragment.setArguments(bundle);
                bankFragment.show(this.getSupportFragmentManager(), "BankFragment");
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Do something.
            if ("2".equals(flag)) {
                Intent intent = new Intent(mContext, DianSellResultActivity.class);
                bundle = new Bundle();
                bundle.putString("type", "");
                bundle.putString("bankid", "");
                bundle.putString("bankid01", "");
                bundle.putString("huming", "");
                bundle.putString("province", "");
                bundle.putString("bankcode", "");
                bundle.putString("zhi", "");
                bundle.putString("bankname", "");
                bundle.putString("banksId", "");
                bundle.putSerializable(HomeFragment.COIN_TYPE, CoinTypes.RMB);
                bundle.putSerializable(HomeFragment.OPERATE_TYPE, OperateTypes.ALIPAY);
                bundle.putDouble(HomeFragment.NOW_Bl, nowBl);
                bundle.putDouble(HomeFragment.NOW_RATE, nowRate);
                bundle.putLong(HomeFragment.REAL_GET, realGet);
                bundle.putLong(HomeFragment.HAND, hand_);
                bundle.putString(HomeFragment.Balance, balance);
                bundle.putString("taibi", taibi);
                bundle.putString(HomeFragment.FORMULA, formula);
                bundle.putString(HomeFragment.INPUT_VALUE, inputValue);
                bundle.putString(HomeFragment.COIN_NAME, "人民幣");
                bundle.putString(HomeFragment.COIN_ID, "2");
                // setResult(Activity.RESULT_OK, intent);
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {
                onBackPressed();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }
}
