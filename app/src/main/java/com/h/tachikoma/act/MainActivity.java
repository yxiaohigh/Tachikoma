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
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        tvFirst.setText("kjskdfjsalkf");
    }

    @Override
    protected void setContentView() {

    }

    @Override
    protected void initViews() {

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

