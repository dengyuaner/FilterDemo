package com.dy.filter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.dy.filter.bean.FilterDataItem;

import java.util.List;

/**
 * Created by dy on 2017/2/23 15:05.
 */


public class MySpinnerAdapter extends BaseAdapter {
    private List<FilterDataItem> filterDataItems;
    private Context mContext;
    private RadioButtonClick mClick;
    private int lasPos = -1;

    public MySpinnerAdapter(List<FilterDataItem> filterDataItems, MySpinnerView view) {
        this.filterDataItems = filterDataItems;
        this.mContext = view.getContext();
        mClick = view;
    }

    @Override
    public int getCount() {
        return filterDataItems.size();
    }

    @Override
    public Object getItem(int position) {
        return filterDataItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.spinner_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.textView = (TextView) convertView.findViewById(R.id.tvText);
            viewHolder.mRadioButton = (RadioButton) convertView.findViewById(R.id.checkBox);
            viewHolder.mMyOnCLick = new MyOnCLick();
            viewHolder.mRadioButton.setOnClickListener(viewHolder.mMyOnCLick);
            viewHolder.updatePosition(position);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.updatePosition(position);
        }


        final FilterDataItem bean = filterDataItems.get(position);
        if (bean.isChecked()) {
            lasPos = position;
        }

        viewHolder.textView.setText(bean.getName());
        viewHolder.mRadioButton.setChecked(bean.isChecked());

        if (bean.getName().contains("时间")) {
            viewHolder.mRadioButton.setVisibility(View.INVISIBLE);
        } else {
            viewHolder.mRadioButton.setVisibility(View.VISIBLE);
        }


        //viewHolder.textView.setTextColor(mTextColor);


        return convertView;
    }

    static class ViewHolder {

        TextView textView;
        RadioButton mRadioButton;
        MyOnCLick mMyOnCLick;

        public void updatePosition(int position) {
            mMyOnCLick.updatePosition(position);
        }

    }

    public interface RadioButtonClick {
        void click(int position);
    }

    private class MyOnCLick implements View.OnClickListener {
        private int position;

        //更新postion
        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            System.out.println("last:" + lasPos + "pos:" + position);
            if (lasPos == -1) {
                filterDataItems.get(position).setChecked(true);
            } else {
                if (lasPos != position) {
                    if (filterDataItems.get(lasPos).isChecked()) {
                        filterDataItems.get(lasPos).setChecked(false);
                    }
                    filterDataItems.get(position).setChecked(true);
                } else {
                    filterDataItems.get(position).setChecked(!filterDataItems.get(position).isChecked());
                }
            }

            lasPos = position;
            notifyDataSetChanged();
        }


    }
}
