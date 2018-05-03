package com.likeit.currenciesapp.ui.chat.ui.activity;

import android.os.Bundle;
import android.util.Log;

import com.likeit.currenciesapp.R;

public class PublicServiceInfoActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("", "---------PublicServiceInfoActivity------------");
        setContentView(R.layout.activity_public_service_info);
        setTitle("公众号信息");

    }
}
