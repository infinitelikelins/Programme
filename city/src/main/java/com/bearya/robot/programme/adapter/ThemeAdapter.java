package com.bearya.robot.programme.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.ThemeEntity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class ThemeAdapter extends BaseQuickAdapter<ThemeEntity, BaseViewHolder> {

    public ThemeAdapter(@Nullable List<ThemeEntity> data) {
        super(R.layout.item_theme, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, ThemeEntity item) {
        Context context = helper.itemView.getContext();

        helper.setText(R.id.name, item.getTheme());
        Glide.with(context).load(item.getCover()).into(((AppCompatImageView) helper.getView(R.id.cover)));

        helper.addOnClickListener(R.id.travelGo);
        helper.addOnClickListener(R.id.cover_over);
    }

}