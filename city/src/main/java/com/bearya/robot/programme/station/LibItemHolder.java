package com.bearya.robot.programme.station;

import android.view.ViewGroup;
import android.widget.TextView;

import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.material.imageview.ShapeableImageView;

class LibItemHolder extends RecyclerViewHolder<LibItem> {
    ShapeableImageView iconView;
    TextView nameView;

    public LibItemHolder(ViewGroup parent) {
        super(parent, R.layout.lib_item_view);
        iconView = itemView.findViewById(R.id.iconView);
        nameView = itemView.findViewById(R.id.nameView);
    }

    @Override
    public void setData(LibItem libItem) {
        Glide.with(iconView.getContext())
                .load(ResourceUtil.getMipmapId(libItem.image))
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .thumbnail(0.1f)
                .into(iconView);
        nameView.setText(libItem.name);
    }

}