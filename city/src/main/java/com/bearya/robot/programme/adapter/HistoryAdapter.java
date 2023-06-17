package com.bearya.robot.programme.adapter;

import androidx.annotation.NonNull;

import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.WalkHistory;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class HistoryAdapter extends BaseQuickAdapter<WalkHistory, BaseViewHolder> {

    public HistoryAdapter() {
        super(R.layout.item_history);
    }

    @Override
    protected void convert(BaseViewHolder helper, @NonNull WalkHistory item) {
        helper.setText(R.id.history_name, String.valueOf(item.getId()));
        helper.addOnClickListener(R.id.tell);
    }

}