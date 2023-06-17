package com.bearya.robot.programme.view;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;

import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;

public class DeleteConfirmPopup extends AbsBasePopup {

    private String popupShowAudio;

    public DeleteConfirmPopup(Context context) {
        super(context);
    }

    @Override
    protected int inflateLayoutId() {
        return R.layout.popup_delete;
    }

    @Override
    protected void onViewInflated() {
        setWidth(850);
    }

    @Override
    protected void onPopupShow() {
        super.onPopupShow();
        if (!TextUtils.isEmpty(popupShowAudio)) {
            MusicUtil.playAssetsAudio(popupShowAudio);
        }
    }

    public final DeleteConfirmPopup applyShowAudio(String popupShowAudio) {
        if (popupShowAudio != null) {
            this.popupShowAudio = popupShowAudio;
        }
        return this;
    }

    public final DeleteConfirmPopup applyShowTips(String tips) {
        ((TextView) findViewById(R.id.del_tips)).setText(tips);
        return this;
    }

    public final DeleteConfirmPopup withConfirm(final View.OnClickListener deleteListener, View.OnClickListener cancelListener) {
        withClick(R.id.cancel, cancelListener);
        withClick(R.id.delete, v -> {
            if (deleteListener != null) {
                deleteListener.onClick(v);
                MusicUtil.playAssetsAudio("station/zh/delete_success.mp3");
            }
        });
        return this;
    }

}
