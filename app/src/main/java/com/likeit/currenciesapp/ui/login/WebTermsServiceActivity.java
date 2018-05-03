package com.likeit.currenciesapp.ui.login;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.likeit.currenciesapp.R;
import com.likeit.currenciesapp.ui.base.Container;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WebTermsServiceActivity extends Container {

    @BindView(R.id.tv_header)
    TextView tvHeader;
    @BindView(R.id.webview)
    WebView mWebView;
    private String webUrl;
    private ProgressDialog nDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_terms_service);
//        bundle.putString(WebActivity.WEB_TITLE, "服務條款");
//        bundle.putString(WebActivity.WEB_URL, "http://dtb.wbteam.cn/api.php?m=login&a=reg_detail");
        ButterKnife.bind(this);
        webUrl = "http://dtb.wbteam.cn/api.php?m=login&a=reg_detail";
        initView();
    }

    private void initView() {
        tvHeader.setText("服務條款");
        mWebView.loadUrl(webUrl);
        mWebView.setWebViewClient(new MyWebViewClient());

        // 设置WebView属性，能够执行JavaScript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        // 设置可以支持缩放
        mWebView.getSettings().setSupportZoom(true);
        // 设置出现缩放工具
        mWebView.getSettings().setBuiltInZoomControls(true);
        // 为图片添加放大缩小功能
        mWebView.getSettings().setUseWideViewPort(true);
        mWebView.getSettings().setBuiltInZoomControls(true);
        mWebView.setInitialScale(70);   //100代表不缩放

    }

    private class MyWebViewClient extends WebViewClient {
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {// 网页页面开始加载的时候
            if (nDialog == null) {
                nDialog = new ProgressDialog(mContext);
                nDialog.setMessage("数据加载中，请稍后。。。");
                nDialog.show();
                mWebView.setEnabled(false);// 当加载网页的时候将网页进行隐藏
            }
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {// 网页加载结束的时候
            // super.onPageFinished(view, url);
            if (nDialog != null && nDialog.isShowing()) {
                nDialog.dismiss();
                nDialog = null;
                mWebView.setEnabled(true);
            }
        }

        @SuppressWarnings("static-access")
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) { // 网页加载时的连接的网址
            view.loadUrl(url);
            return false;
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            // TODO Auto-generated method stub
            super.onReceivedError(view, errorCode, description, failingUrl);
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
