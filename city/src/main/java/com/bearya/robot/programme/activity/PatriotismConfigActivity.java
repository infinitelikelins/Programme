package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.entity.PatriotismContent;
import com.bearya.robot.programme.repository.PatriotismRepository;
import com.bearya.robot.programme.view.DeleteConfirmPopup;
import com.bearya.robot.programme.walk.load.KnownLoad;

public class PatriotismConfigActivity extends BaseActivity implements View.OnClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, PatriotismConfigActivity.class));
    }

    private final ImageView[] stationViewArr = new ImageView[6];

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patriotism_config);

        for (int i = 0; i < stationViewArr.length; i++) {
            ImageView view = findViewById(ResourceUtil.getId(getApplicationContext(), "btnStation" + (i + 7)));
            view.setOnClickListener(this);
            stationViewArr[i] = view;
        }

        findViewById(R.id.btnPerform).setOnClickListener(this);
        findViewById(R.id.btnBack).setOnClickListener(this);
        findViewById(R.id.clear_patriotism_config).setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        KnownLoad.clearKnownPlayData();
        for (String theme : PatriotismRepository.getInstance().themes) {
            PatriotismContent[] patriotismContents = PatriotismRepository.getInstance().getPatriotismContents(theme);
            boolean contains = false;
            for (PatriotismContent patriotismContent : patriotismContents) {
                contains |= PatriotismRepository.getInstance().get(patriotismContent.getContentName());
            }
            for (ImageView imageView : stationViewArr) {
                if (TextUtils.equals(theme, ((String) imageView.getTag()))) {
                    imageView.setSelected(contains);
                    break;
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btnBack) {
            finish();
        } else if (id == R.id.btnPerform) {
            GameActivity.start(this);
            finish();
        } else if (id == R.id.clear_patriotism_config) {
            new DeleteConfirmPopup(this)
                    .applyShowTips(getString(R.string.clear_all_patriotism_config))
                    .withConfirm(v1 -> {
                        PatriotismRepository.getInstance().clearAll();
                        for (ImageView imageView : stationViewArr) {
                            imageView.setSelected(false);
                        }
                        Toast.makeText(getApplicationContext(), getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
                    }, null).showPopupWindow();
        } else {
            PatriotismThemeActivity.start(this, ((String) v.getTag()));
        }
    }

}