package com.bearya.robot.programme.adapter;

import android.content.Context;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.PatriotismContent;
import com.bearya.robot.programme.repository.PatriotismRepository;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class PatriotismContentAdapter extends BaseQuickAdapter<PatriotismContent, BaseViewHolder> {

    public PatriotismContentAdapter(@Nullable List<PatriotismContent> data) {
        super(R.layout.item_patriotism_content, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, final PatriotismContent item) {
        Context context = helper.itemView.getContext();

        Glide.with(context).load(item.getContentCover())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(((AppCompatImageView) helper.getView(R.id.patriotism_theme_item)));

        helper.setVisible(R.id.patriotism_theme_selected, PatriotismRepository.getInstance().get(item.getContentName()));

    }

}