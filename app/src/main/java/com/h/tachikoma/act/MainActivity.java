package com.h.tachikoma.act;

import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.widget.TextView;

import com.h.tachikoma.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_first)
    TextView tvFirst;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void setContentView() {
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @Override
    protected void initViews() {
        tvFirst.setText("kjskdfjsalkf");
    }

    @Override
    protected void initListeners() {

    }

    @Override
    protected void initData() {

    }


    @OnClick(R.id.tv_first)
    public void onClick() {
        Snackbar.make(tvFirst,"snackbar",Snackbar.LENGTH_SHORT).show();
    }
}

