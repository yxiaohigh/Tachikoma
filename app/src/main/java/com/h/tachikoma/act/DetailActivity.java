package com.h.tachikoma.act;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.h.tachikoma.R;
import com.h.tachikoma.base.BaseActivity;
import com.h.tachikoma.utli.NetUtil;
import com.h.tachikoma.utli.PicUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends BaseActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.collapsing_toolbar)
    CollapsingToolbarLayout collapsingToolbar;
    @BindView(R.id.web_view)
    WebView webView;
    @BindView(R.id.progrss_bar)
    ProgressBar progrssBar;
    @BindView(R.id.stub_err)
    ViewStub stubErr;
    private boolean netWorkAvailable;

    @Override
    protected void setContent() {
        setContentView(R.layout.activity_detail);
    }


    @Override
    protected void initViews() {
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        progrssBar.setVisibility(View.GONE);
        webView.setWebViewClient(new MyWebViewClient());
        ActionBar supportActionBar = getSupportActionBar();
        supportActionBar.setDisplayHomeAsUpEnabled(true);
        supportActionBar.setDisplayShowHomeEnabled(true);

    }


    @Override
    protected void initData() {

        collapsingToolbar.setTitle("Sssssssssssss");

        WebSettings settings = webView.getSettings();
        netWorkAvailable = NetUtil.isNetWorkAvailable(getApplicationContext());

        //网络可用用默认缓存 不可用直接用缓存
        if (netWorkAvailable) {
            settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        } else {
            Snackbar.make(webView, "当前无网络正在使用缓存", Snackbar.LENGTH_SHORT).show();
            settings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.bg_1);
        int rgb2 = PicUtil.getRgb2(bitmap, 2);
        collapsingToolbar.setContentScrimColor(rgb2);
        webView.loadUrl("http://m.xiachufang.com/");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // TODO: add setContentView(...) invocation
        ButterKnife.bind(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * webViewClient
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(url));
            startActivity(intent);
            return true;
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            if (netWorkAvailable) {
                progrssBar.setVisibility(View.VISIBLE);
            }

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (netWorkAvailable) {
                progrssBar.setVisibility(View.GONE);
            }

        }

        @Override
        public void onReceivedError(WebView view, WebResourceRequest request, WebResourceError error) {
           // super.onReceivedError(view, request, error);
            Toast.makeText(DetailActivity.this, "kljkljljl", Toast.LENGTH_SHORT).show();
            stubErr.inflate();
        }
    }
}

