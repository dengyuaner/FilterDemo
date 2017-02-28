package com.dy.filter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * 类名 BaseFragment
 * 作者 dy
 * 功能 基类Fragment
 * 创建日期 2016/10/21 14:18
 * 修改日期 2016/10/21 14:18
 */


public abstract class BaseFragment extends Fragment {
    private View mRootView;
    private boolean firstLoad;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firstLoad = true;
        initVariables();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (null == mRootView) {
            mRootView = inflater.inflate(getLayoutId(), container, false);
        }

        //复用rootView，要判断之前的有没有parentView，有的话要移除
        ViewGroup viewGroup = (ViewGroup) mRootView.getParent();
        if (viewGroup != null) {
            viewGroup.removeAllViewsInLayout();
        }

        initViews(mRootView);
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (firstLoad) {
            loadData();
            firstLoad = false;
        }

    }

    /**
     * 获取资源id
     *
     * @return 返回资源id
     */
    protected abstract int getLayoutId();


    /**
     * initVariables(初始化变量，如intent，全局变量等)
     */
    protected abstract void initVariables();

    /**
     * initViews(初始化组件，如findViewById)
     *
     * @param rootView view
     */
    protected abstract void initViews(View rootView);

    /**
     * loadData(初始化加载数据，如从网络加载数据)
     */
    protected abstract void loadData();


}
