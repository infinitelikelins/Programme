package com.bearya.robot.programme.adapter;

import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.ImageSearchEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

public class ImageSearchAdapter extends BaseQuickAdapter<String , BaseViewHolder> {

    public ImageSearchAdapter() {
        super(R.layout.lib_item_view);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {
        helper.setGone(R.id.nameView,true);
    }

}