package com.dy.filter;

import android.animation.ObjectAnimator;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.ColorRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.text.Html;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.dy.filter.bean.FilterDataItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * 类名 MySpinnerView
 * 作者 dy
 * 功能 下拉列表
 * 创建日期 2017/2/28 14:55
 * 修改日期 2017/2/28 14:55
 */


public class MySpinnerView extends LinearLayout implements MySpinnerAdapter.RadioButtonClick {
    //用于下拉图标的动画
    private static final int MAX_LEVEL = 10000;

    //用于状态保存
    private static final String INSTANCE_STATE = "instance_state";
    private static final String SELECTED_INDEX = "selected_index";
    private static final String IS_POPUP_SHOWING = "is_popup_showing";


    private int selectedIndex;//选中的位置
    private Drawable drawable;//下拉图标
    private ListView listView;
    private MySpinnerAdapter adapter;//用于listview的adapter
    private AdapterView.OnItemClickListener onItemClickListener;
    private AdapterView.OnItemSelectedListener onItemSelectedListener;
    private boolean isArrowHide, isShowing;//是否显示
    private int textColor;
    private int backgroundSelector;//选中颜色
    private int datasId;
    private TextView titleView;//title

    private String titleText;
    private int defaultPadding;//默认padding
    private List<FilterDataItem> mFilterDataItems;//筛选的数据源
    private String startDate, endDate;
    private int lastPos = -1;//保存最近一次选中的位置


    public MySpinnerView(Context context) {
        super(context);
        getAttrs(context, null);
        init(context, null);
    }

    public MySpinnerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        init(context, attrs);
    }

    public MySpinnerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        init(context, attrs);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable(INSTANCE_STATE, super.onSaveInstanceState());
        bundle.putInt(SELECTED_INDEX, selectedIndex);
        bundle.putBoolean(IS_POPUP_SHOWING, isShowing);
        dismissDropDown();

        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            selectedIndex = bundle.getInt(SELECTED_INDEX);

            if (adapter != null) {
                setTitle(selectedIndex);

                // adapter.notifyItemSelected(selectedIndex);
            }

            if (bundle.getBoolean(IS_POPUP_SHOWING)) {
                if (isShowing) {
                    post(new Runnable() {
                        @Override
                        public void run() {
                            showDropDown();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable(INSTANCE_STATE);
        }
        super.onRestoreInstanceState(savedState);
    }

    /**
     * 获取相关属性attr
     *
     * @param context
     * @param attrs
     */
    private void getAttrs(Context context, AttributeSet attrs) {
        // TODO Auto-generated constructor stub
        // 通过这个方法，将你在attrs.xml中定义的declare=styleable
        // 的所有属性的值存储到TypedArray中
        TypedArray ta = context.obtainStyledAttributes(attrs,
                R.styleable.MySpinnerView);
        // 从TypedArray中取出对应的值来为要设置的属性赋值
        titleText = ta.getString(R.styleable.MySpinnerView_msv_title);
        datasId = ta.getResourceId(R.styleable.MySpinnerView_msv_array, 0);


        defaultPadding = getResources().getDimensionPixelSize(R.dimen.one_and_a_half_grid_unit);

        setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
//        setPadding(getResources().getDimensionPixelSize(R.dimen.three_grid_unit), defaultPadding, defaultPadding,
//                defaultPadding);
        setClickable(true);

        backgroundSelector = ta.getResourceId(R.styleable.MySpinnerView_msv_backgroundSelector,
                R.drawable.spinner_selector);

        textColor = ta.getColor(R.styleable.MySpinnerView_msv_textTint, -1);
        isShowing = ta.getBoolean(R.styleable.MySpinnerView_msv_isShow, false);

        isArrowHide = ta.getBoolean(R.styleable.MySpinnerView_msv_hideArrow, false);
        if (!isArrowHide) {
            //不隐藏则显示图标
            Drawable basicDrawable = ContextCompat.getDrawable(context, R.drawable.spinner_arrow);
            int resId = ta.getColor(R.styleable.MySpinnerView_msv_arrowTint, -1);
            if (basicDrawable != null) {
                drawable = DrawableCompat.wrap(basicDrawable);
                if (resId != -1) {
                    DrawableCompat.setTint(drawable, resId);
                }
            }
        }

        ta.recycle();
    }

    private void init(final Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);

        titleView = new TextView(context);
        titleView.setText(titleText);
        titleView.setPadding(defaultPadding, defaultPadding, defaultPadding, defaultPadding);
        if (!isArrowHide) {
            titleView.setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);
        }


        listView = new ListViewInSrcollView(context);

        listView.setId(getId());
        listView.setDivider(null);
        listView.setItemsCanFocus(true);
        //隐藏scrollbars
        listView.setVerticalScrollBarEnabled(false);
        listView.setHorizontalScrollBarEnabled(false);
        listView.setBackgroundResource(backgroundSelector);
        //数据源，这里测试用的是assets中的json，开发者可自行构造bean和数据源
        if (datasId != 0) {
            final String[] temp = getResources().getStringArray(datasId);

            mFilterDataItems = new ArrayList<>();
            for (String aTemp : temp) {
                mFilterDataItems.add(new FilterDataItem(0, aTemp, false));
            }
            adapter = new MySpinnerAdapter(mFilterDataItems, this);
            setAdapterInternal(adapter);

        }


//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                spinnerClick(position);
//
//
//            }
//        });


        addView(titleView);
        addView(listView);
        if (isShowing) {
            listView.setVisibility(View.VISIBLE);
        } else {
            listView.setVisibility(View.GONE);
        }
    }

    /**
     * 获取当前选中的位置
     *
     * @return
     */
    public int getSelectedIndex() {
        return selectedIndex;
    }

    /**
     * 设置当前选中位置
     *
     * @param position 位置
     */
    public void setSelectedIndex(int position) {
        if (adapter != null) {
            if (position >= 0 && position <= adapter.getCount()) {

                //adapter.notifyItemSelected(position);
                selectedIndex = position;
                setTitle(position);

            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
    }

    public void addOnItemClickListener(@NonNull AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemSelectedListener(@NonNull AdapterView.OnItemSelectedListener onItemSelectedListener) {
        this.onItemSelectedListener = onItemSelectedListener;
    }

    /**
     * 设置adapter
     *
     * @param adapter
     */
    private void setAdapterInternal(MySpinnerAdapter adapter) {
        // If the adapter needs to be settled again, ensure to reset the selected index as well
        //selectedIndex = 0;
        listView.setAdapter(adapter);
        //setTitle(selectedIndex);

    }

    /**
     * 设置adapter
     *
     * @param filterDataItems 数据源
     */
    public void setAdapter(List<FilterDataItem> filterDataItems) {
        this.mFilterDataItems = filterDataItems;
        this.adapter = new MySpinnerAdapter(filterDataItems, this);
        setAdapterInternal(adapter);
    }


    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (!isShowing) {
                showDropDown();
            } else {
                dismissDropDown();
            }
        }

        return super.onTouchEvent(event);
    }

    /**
     * 点击动画效果
     *
     * @param shouldRotateUp
     */
    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : MAX_LEVEL;
        int end = shouldRotateUp ? MAX_LEVEL : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(drawable, "level", start, end);
        animator.setInterpolator(new LinearOutSlowInInterpolator());
        animator.start();
    }

    /**
     * 隐藏
     */
    public void dismissDropDown() {
        if (!isArrowHide) {
            animateArrow(false);
        }
        listView.setVisibility(View.GONE);
        isShowing = false;
    }

    /**
     * 展开
     */
    public void showDropDown() {
        if (!isArrowHide) {
            animateArrow(true);
        }
        listView.setVisibility(View.VISIBLE);
        isShowing = true;
    }

    public void setTintColor(@ColorRes int resId) {
        if (drawable != null && !isArrowHide) {
            DrawableCompat.setTint(drawable, ContextCompat.getColor(getContext(), resId));
        }
    }

    public void setTitle(int position) {
        String text = null;
        lastPos = position;
        if (adapter.getItem(position) instanceof String) {
            text = titleText + "<font color=\"red\"> " +
                    "(" + adapter.getItem(position).toString() + ")" + "</font>";

        }
        if (adapter.getItem(position) instanceof FilterDataItem) {
            FilterDataItem bean = (FilterDataItem) adapter.getItem(position);
            text = titleText + "<font color=\"#3b597b\"> " +
                    "(" + bean.getName() + ")" + "</font>";

        }
        titleView.setText(Html.fromHtml(text));


    }

    /**
     * 获取选中的数据源
     *
     * @return
     */
    public FilterDataItem getSelectedData() {
        if (selectedIndex >= 8) {
            FilterDataItem item = new FilterDataItem();
            item.setName(startDate + "|" + endDate);
            item.setId(-5);
            if (TextUtils.isEmpty(startDate)
                    || TextUtils.isEmpty(endDate)) {
                item.setChecked(false);
            } else {
                item.setChecked(true);
            }
            return item;
        } else {
            return mFilterDataItems.get(selectedIndex);
        }

    }


    @Override
    public void click(int position) {
        spinnerClick(position);

    }

    /**
     * 点击方法
     *
     * @param position
     */
    private void spinnerClick(int position) {
        final FilterDataItem bean = mFilterDataItems.get(position);
        System.out.println("点击的位置" + position);
        if (!bean.isChecked()) {
            //更改点击状态
            bean.setChecked(true);

            //把最近的已经设为true的变为false
            if (lastPos != -1) {
                FilterDataItem item = mFilterDataItems.get(lastPos);
                item.setChecked(false);
            }

        }

        selectedIndex = position;

        final int tempPos = position;
        if (bean.getName().contains("时间")) {
            Calendar c = Calendar.getInstance();
            // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
            new DatePickerDialog(getContext(),
                    // 绑定监听器
                    new DatePickerDialog.OnDateSetListener() {

                        @Override
                        public void onDateSet(DatePicker view, int year,
                                              int monthOfYear, int dayOfMonth) {
                            if (bean.getName().contains("开始时间")) {
                                startDate = year + "." + (monthOfYear + 1)
                                        + "." + dayOfMonth;
                                bean.setName("开始时间  " + startDate);

                            } else {
                                endDate = year + "." + (monthOfYear + 1)
                                        + "." + dayOfMonth;
                                bean.setName("结束时间  " + endDate);
                            }
                            adapter.notifyDataSetChanged();
                            setTitle(tempPos);
                        }
                    }
                    // 设置初始日期
                    , c.get(Calendar.YEAR), c.get(Calendar.MONTH), c
                    .get(Calendar.DAY_OF_MONTH)).show();

        } else {
            postDelayed(new Runnable() {
                @Override
                public void run() {
                    dismissDropDown();
                }
            }, 500);
            adapter.notifyDataSetChanged();
            setTitle(position);
        }
    }
}
