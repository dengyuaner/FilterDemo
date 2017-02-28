package com.dy.filter;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * 类名 ListViewInSrcollView
 * 作者 dy
 * 功能 ScrollView嵌套ListView
 * 创建日期 2016/7/12 16:55
 * 修改日期 2016/7/12 16:55
 */


public class ListViewInSrcollView extends ListView {
    public ListViewInSrcollView(Context context) {
        super(context);
    }

    public ListViewInSrcollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    //重写该方法，达到使ListView适应ScrollView的效果
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);

    }
}
