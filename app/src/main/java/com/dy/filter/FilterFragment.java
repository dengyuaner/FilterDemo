package com.dy.filter;

import android.content.Context;
import android.support.v4.widget.DrawerLayout;

import android.view.View;
import android.widget.FrameLayout;

import android.widget.TextView;
import android.widget.Toast;


import com.alibaba.fastjson.JSON;
import com.dy.filter.bean.FilterData;
import com.dy.filter.bean.FilterDataItem;

import java.io.BufferedReader;
import java.io.InputStreamReader;


/**
 * 类名 FilterFragment
 * 作者 dy
 * 功能 过滤fragment
 * 创建日期 2017/2/28 14:54
 * 修改日期 2017/2/28 14:54
 */


public class FilterFragment extends BaseFragment implements View.OnClickListener {
    private DrawerLayout mDrawerLayout;
    private FrameLayout mDrawerContent;



    private FilterData mFilterDatas;
    private MySpinnerView groupView, timeView, exponentView;
    private OnFragmentListener mListener;
    private TextView tvEnsure;
    private String cusType;
    private int groupFlag, exponentFlag, timeFlag;
    private String[] mTimes;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentListener) {
            mListener = (OnFragmentListener) context;
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_filter;
    }

    @Override
    protected void initVariables() {
        mFilterDatas = JSON.parseObject(getFromAssets("sale_type.json", getActivity()), FilterData.class);
        cusType = getArguments().getString("cusType");
        groupFlag = getArguments().getInt("groupFlag");
        exponentFlag = getArguments().getInt("exponentFlag");
        timeFlag = getArguments().getInt("timeFlag");
        mTimes = getArguments().getStringArray("time");

    }

    @Override
    protected void initViews(View rootView) {

        mDrawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
        mDrawerContent = (FrameLayout) getActivity().findViewById(R.id.drawer_content);
        groupView = (MySpinnerView) rootView.findViewById(R.id.groupView);
        timeView = (MySpinnerView) rootView.findViewById(R.id.timeView);
        exponentView = (MySpinnerView) rootView.findViewById(R.id.exponentView);
        tvEnsure = (TextView) rootView.findViewById(R.id.tvEnsure);
        tvEnsure.setOnClickListener(this);
    }

    @Override
    protected void loadData() {

        exponentView.setAdapter(mFilterDatas.getExponent());
        timeView.setAdapter(mFilterDatas.getTime());
        groupView.setAdapter(mFilterDatas.getGroup());

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tvEnsure) {
            if (mListener != null) {
                if (!timeView.getSelectedData().isChecked()) {
                    Toast.makeText(getActivity(), "请输入开始或结束日期", Toast.LENGTH_SHORT).show();
                    return;
                }
                mListener.onProcess(exponentView.getSelectedData(),
                        groupView.getSelectedData(),
                        timeView.getSelectedData());
                mDrawerLayout.closeDrawer(mDrawerContent);
            }

        }

    }

    public interface OnFragmentListener {
        void onProcess(FilterDataItem exponent, FilterDataItem group, FilterDataItem time);
    }

    public static String getFromAssets(String fileName, Context context) {
        String result = "";
        try {
            InputStreamReader inputReader = new InputStreamReader(context
                    .getResources().getAssets().open(fileName));
            BufferedReader bufReader = new BufferedReader(inputReader);
            String line;

            while ((line = bufReader.readLine()) != null)
                result += line;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}