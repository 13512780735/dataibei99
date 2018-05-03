package com.likeit.currenciesapp.ui.me;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.api.AppConfig;
import com.likeit.currenciesapp.configs.CoinTypes;
import com.likeit.currenciesapp.configs.OperateTypes;
import com.likeit.currenciesapp.ui.Greenteahy.DianSellResultActivity;
import com.likeit.currenciesapp.ui.fragment.HomeFragment;
import com.likeit.currenciesapp.utils.HttpUtil;
import com.likeit.currenciesapp.utils.LoaddingDialog;
import com.likeit.currenciesapp.utils.MyActivityManager;
import com.likeit.currenciesapp.utils.StringUtil;
import com.likeit.currenciesapp.utils.ToastUtil;
import com.likeit.currenciesapp.utils.UtilPreference;
import com.loopj.android.http.RequestParams;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddBankActivity extends AppCompatActivity {
    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.ll_select_bank)
    LinearLayout ll_select_bank;

    @BindView(R.id.apply_et_select_bank)
    TextView apply_et_select_bank;
    @BindView(R.id.apply_et_account)
    EditText apply_et_account;
    @BindView(R.id.apply_et_name)
    EditText apply_et_name;
    @BindView(R.id.apply_et_bankName)
    EditText apply_et_bankName;
    @BindView(R.id.apply_et_bankCity)
    EditText apply_et_bankCity;
    @BindView(R.id.apply_et_bankZH)
    EditText apply_et_bankZH;
    @BindView(R.id.tv_apply)
    TextView tv_apply;
    private String keys;
    private String type = "0";
    private LoaddingDialog loaddingDialog;
    String bankAccount, bankName, bankBankName, bankBankCity, bankBankZH;
    private String banksId;
    private String ukey;
    private String flag="1";
    private String bankflag="1";

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
    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_bank);
        MyActivityManager.getInstance().addActivity(this);
        ButterKnife.bind(this);
        keys = getIntent().getExtras().getString("keys");
        type = getIntent().getExtras().getString("type");
        flag = getIntent().getExtras().getString("flag");
        bankflag = getIntent().getExtras().getString("bankflag");
        ukey = UtilPreference.getStringValue(this, "ukey");
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
        if ("2".equals(keys)) {
            bankflag = getIntent().getExtras().getString("bankflag");
            bankAccount = getIntent().getExtras().getString("bankid");
            bankName =getIntent().getExtras().getString("huming");
            bankBankCity = getIntent().getExtras().getString("province");
            bankBankZH =getIntent().getExtras().getString("zhi");
            bankBankName = getIntent().getExtras().getString("bankname");
            banksId = getIntent().getExtras().getString("banksId");
            id=getIntent().getExtras().getString("id");
        }
        loaddingDialog = new LoaddingDialog(this);
        initView();
    }

    private void initView() {
        if ("1".equals(keys)) {
            tvHeader.setText("添加銀行賬號");
            tv_apply.setText("提交");
            // apply_et_select_bank.setText("請選擇賬號類型");
            if ("1".equals(type)) {
                apply_et_select_bank.setText("大陸銀行");
            } else if ("2".equals(type)) {
                apply_et_select_bank.setText("台灣銀行");
            }
        } else if ("2".equals(keys)) {
            tvHeader.setText("銀行賬號");
            tv_apply.setText("修改");
            tvRight.setText("删除");
            tvRight.setTextColor(this.getResources().getColor(R.color.white));
            if ("1".equals(type)) {
                apply_et_select_bank.setText("大陸銀行");
            } else if ("2".equals(type)) {
                apply_et_select_bank.setText("台灣銀行");
            }
            apply_et_account.setText(bankAccount);
            apply_et_name.setText(bankName);
            apply_et_bankName.setText(bankBankName);
            apply_et_bankCity.setText(bankBankCity);
            apply_et_bankZH.setText(bankBankZH);

        }
    }


    public class Activity_AirCondition_AdvancedSettings extends Activity // 这里要implements刚才的接口
            implements BankTypeFragment.MyDialogFragment_Listener {

        // 引用接口中定义的方法
        @Override
        public void getDataFrom_DialogFragment(String data01) {
            Log.d("Tag", "DialogFragment回传的数据为：" + data01);
            type = data01;
            if ("1".equals(data01)) {
                apply_et_select_bank.setText("大陸銀行");
            } else if ("2".equals(data01)) {
                apply_et_select_bank.setText("台灣銀行");
            }
        }

    }

    @OnClick({R.id.backBtn, R.id.tv_apply, R.id.tv_right})
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backBtn:
                Intent intent = new Intent(AddBankActivity.this, BankcardActivity.class);
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
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
                break;
            case R.id.tv_apply:
                bankAccount = apply_et_account.getText().toString().trim();
                bankName = apply_et_name.getText().toString().trim();
                bankBankName = apply_et_bankName.getText().toString().trim();
                bankBankCity = apply_et_bankCity.getText().toString().trim();
                bankBankZH = apply_et_bankZH.getText().toString().trim();
                if ("1".equals(keys)) {
                    addBank();
                } else if ("2".equals(keys)) {
                    editBank();
                }
                break;
            case R.id.tv_right:
                del();
                break;
        }
    }


    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            // Do something.
            Intent intent = new Intent(AddBankActivity.this, BankcardActivity.class);
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

            intent.putExtras(bundle);
            startActivity(intent);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void del() {
        Log.d("TAG2222",bankflag);
        loaddingDialog.show();
        String url = AppConfig.LIKEIT_EEL_BANK;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        ToastUtil.showS(AddBankActivity.this, "删除成功");
                        Intent intent = new Intent(AddBankActivity.this, BankcardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("flag", flag);
                        bundle.putString("bankflag", bankflag);

                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showS(AddBankActivity.this, obj.optString("msg"));
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
                loaddingDialog.dismiss();
            }
        });
    }

    /**
     * 编辑银行
     */
    private void editBank() {
        loaddingDialog.show();
        String url = AppConfig.LIKEIT_EDIT_BANK;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("bankid", bankAccount);
        params.put("huming", bankName);
        params.put("bankname", bankBankName);
        params.put("province", bankBankCity);
        params.put("zhi", bankBankZH);
        params.put("type", type);
        params.put("id", id);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        ToastUtil.showS(AddBankActivity.this, "修改成功");
                        Intent intent = new Intent(AddBankActivity.this, BankcardActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("flag", flag);
                        bundle.putString("bankflag", bankflag);


                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showS(AddBankActivity.this, obj.optString("msg"));
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
                loaddingDialog.dismiss();
            }
        });
    }

    /**
     * 添加银行
     */


    private void addBank() {

        if ("0".equals(type)) {
            ToastUtil.showS(AddBankActivity.this, "請選擇賬號類型");
            return;
        }
        if (StringUtil.isBlank(bankAccount) || StringUtil.isBlank(bankName) || StringUtil.isBlank(bankBankName) || StringUtil.isBlank(bankBankCity) || StringUtil.isBlank(bankBankZH)) {
            ToastUtil.showS(AddBankActivity.this, "请完善资料");
        }
        loaddingDialog.show();
        String url = AppConfig.LIKEIT_ADD_BANK;
        RequestParams params = new RequestParams();
        params.put("ukey", ukey);
        params.put("bankid", bankAccount);
        params.put("huming", bankName);
        params.put("bankname", bankBankName);
        params.put("province", bankBankCity);
        params.put("zhi", bankBankZH);
        params.put("type", type);
        HttpUtil.post(url, params, new HttpUtil.RequestListener() {
            @Override
            public void success(String response) {
                loaddingDialog.dismiss();
                try {
                    JSONObject obj = new JSONObject(response);
                    String status = obj.optString("status");
                    if ("true".equals(status)) {
                        ToastUtil.showS(AddBankActivity.this, "添加成功");
                        Intent intent = new Intent(AddBankActivity.this, BankcardActivity.class);
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
                        intent.putExtras(bundle);
                        startActivity(intent);
                        finish();
                    } else {
                        ToastUtil.showS(AddBankActivity.this, obj.optString("msg"));
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
                loaddingDialog.dismiss();
            }
        });

    }
}
