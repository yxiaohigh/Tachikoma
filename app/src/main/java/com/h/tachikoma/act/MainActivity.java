package com.h.tachikoma.act;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewStub;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.h.tachikoma.R;
import com.h.tachikoma.utli.NetUtil;
import com.h.tachikoma.utli.PicUtil;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


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
    protected void initViews() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        progrssBar.setVisibility(View.GONE);
        webView.setWebViewClient(new MyWebViewClient());

    }


    @Override
    protected void initData() {

        collapsingToolbar.setTitle("sssssssssssss");

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
        int rgb2 = PicUtil.getRgb2(bitmap);
        collapsingToolbar.setContentScrimColor(rgb2);
        webView.loadUrl("http://m.xiachufang.com/");

    }

    /**
     * webViewClient
     */
    private class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return super.shouldOverrideUrlLoading(view, url);
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
        public void onReceivedHttpError(WebView view, WebResourceRequest request, WebResourceResponse errorResponse) {
            super.onReceivedHttpError(view, request, errorResponse);
            stubErr.inflate();

        }
    }
}

