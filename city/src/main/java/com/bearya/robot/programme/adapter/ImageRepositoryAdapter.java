package com.bearya.robot.programme.adapter;

import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.programme.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.io.File;

public class ImageRepositoryAdapter extends BaseQuickAdapter<File, BaseViewHolder> {

    public ImageRepositoryAdapter() {
        super(R.layout.item_image_local);
    }

    @Override
    protected void convert(BaseViewHolder helper, File item) {
        AppCompatImageView localImage = helper.getView(R.id.iconView);
        Glide.with(localImage.getContext())
                .load(item)
                .into(localImage);
    }

}