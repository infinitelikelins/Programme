package com.bearya.robot.programme.adapter;

import com.bearya.robot.programme.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class MessageAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public MessageAdapter() {
        super(R.layout.item_message);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setText(R.id.message, item);
    }

}