package com.bearya.robot.programme.view;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;

import androidx.annotation.CallSuper;
import androidx.annotation.IdRes;
import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;

import com.bearya.robot.base.can.CanDataListener;
import com.bearya.robot.base.can.CanManager;
import com.bearya.robot.base.util.DebugUtil;

import razerdp.basepopup.BasePopupWindow;
import razerdp.util.animation.AlphaConfig;
import razerdp.util.animation.AnimationHelper;
import razerdp.util.animation.ScaleConfig;

public abstract class AbsBasePopup extends BasePopupWindow {

    public AbsBasePopup(Context context) {
        super(context);
        onViewInflated();
    }

    @Override
    public View onCreateContentView() {
        return createPopupById(inflateLayoutId());
    }

    @LayoutRes
    protected abstract int inflateLayoutId();

    protected void onViewInflated() {

    }

    @Override
    protected Animation onCreateShowAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER.duration(onPopupShowDuration()))
                .withAlpha(AlphaConfig.IN.duration(onPopupShowDuration()))
                .toShow(new AnimationHelper.OnAnimationCreateListener() {
                    @Override
                    public void onAnimationCreated(@NonNull Animation animation) {

                    }

                    @Override
                    public void onAnimationCreateFinish(@NonNull AnimationSet animationSet) {
                        onPopupShow();
                    }
                });
    }

    protected int onPopupShowDuration() {
        return 300;
    }

    protected int onPopupDismissDuration() {
        return 300;
    }

    @Override
    protected Animation onCreateDismissAnimation() {
        return AnimationHelper.asAnimation()
                .withScale(ScaleConfig.CENTER.duration(onPopupDismissDuration()))
                .withAlpha(AlphaConfig.OUT.duration(onPopupDismissDuration()))
                .toDismiss();
    }

    @CallSuper
    protected void onPopupShow() {
        if (getContext() instanceof CanDataListener) {
            DebugUtil.debug("context 是 CanDataListener 的子类 ，移除监听");
            CanManager.getInstance().removeListener((CanDataListener) getContext());
        }
    }

    @CallSuper
    protected void onPopupDismiss() {
        if (getContext() instanceof CanDataListener) {
            DebugUtil.debug("context 是 CanDataListener 的子类 ，重新添加监听");
            CanManager.getInstance().addListener((CanDataListener) getContext());
        }
    }

    @Override
    public void onDismiss() {
        super.onDismiss();
        onPopupDismiss();
    }

    public final void withClick(@IdRes int id, View.OnClickListener onClickListener) {
        withClick(id, onClickListener, true);
    }

    public final void withClick(@IdRes int id, final View.OnClickListener onClickListener, final boolean isDismiss) {
        View view = findViewById(id);
        if (view != null) {
            view.setOnClickListener(v -> {
                if (onClickListener != null)
                    onClickListener.onClick(v);
                if (isDismiss) dismiss(true);
            });
        }
    }
}