package com.bearya.robot.programme.station;

import android.view.View;

import com.bearya.robot.programme.R;

public abstract class OnItemClickedListener<T> implements View.OnClickListener {
    public abstract void onItemClicked(T data,int flag);

    @Override
    public void onClick(View view) {
        T d = (T) view.getTag();
        int flag = (int) view.getTag(R.id.item_click_flag);
        onItemClicked(d,flag);
    }
}
