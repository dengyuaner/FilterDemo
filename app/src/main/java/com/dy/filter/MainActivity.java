package com.dy.filter;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.dy.filter.bean.FilterDataItem;

public class MainActivity extends FragmentActivity implements FilterFragment.OnFragmentListener {
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerContent;
    private TextView tvFilter, tvFilterExponent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerContent = (FrameLayout) findViewById(R.id.drawer_content);
        tvFilter = (TextView) findViewById(R.id.tvFilter);
        tvFilterExponent = (TextView) findViewById(R.id.tvFilterExponent);
        tvFilterExponent.setText("首页");

        Fragment fragment = new FilterFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        fragmentManager.beginTransaction().replace(R.id.drawer_content, fragment).commit();

        tvFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(mDrawerContent);
            }
        });
    }

    @Override
    public void onProcess(FilterDataItem exponent, FilterDataItem group, FilterDataItem time) {
        System.out.println("获取到数据");
    }
}
