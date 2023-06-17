package com.bearya.robot.programme.view;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.bearya.robot.programme.R;

import java.util.Locale;

/**
 * 成功保存游戏配置信息提示框
 */
public class SaveGamePopup extends AbsBasePopup {

    public SaveGamePopup(Context context, long id) {
        super(context);
        ((TextView) findViewById(R.id.order_id)).setText(String.format(Locale.CHINA, context.getString(R.string.work_no), id));
    }

    @Override
    protected int inflateLayoutId() {
        return R.layout.popup_save_game;
    }

    @Override
    protected void onViewInflated() {
        setWidth(900);
    }

    public final void withConfirm(View.OnClickListener showListener, View.OnClickListener cancelListener) {
        withClick(R.id.cancel, cancelListener);
        withClick(R.id.show_now, showListener);
    }
}