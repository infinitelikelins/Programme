package com.bearya.robot.programme.adapter;

import android.content.Context;

import androidx.annotation.Nullable;

import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.IntroductionEntity;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public class IntroductionAdapter extends BaseQuickAdapter<IntroductionEntity, BaseViewHolder> {

    public IntroductionAdapter(@Nullable List<IntroductionEntity> data) {
        super(R.layout.item_introduction, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IntroductionEntity item) {
        Context context = helper.itemView.getContext();
    }

}
