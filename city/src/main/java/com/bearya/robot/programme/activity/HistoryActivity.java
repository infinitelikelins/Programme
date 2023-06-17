package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.HistoryAdapter;
import com.bearya.robot.programme.entity.WalkHistory;
import com.bearya.robot.programme.repository.CityRecordRepository;
import com.bearya.robot.programme.view.DeleteConfirmPopup;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.List;

public class HistoryActivity extends BaseActivity implements View.OnClickListener, BaseQuickAdapter.OnItemChildClickListener, BaseQuickAdapter.OnItemLongClickListener {

    public static void start(Context context) {
        context.startActivity(new Intent(context, HistoryActivity.class));
    }

    private HistoryAdapter historyAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        MusicUtil.stopBgMusic();
        MusicUtil.stopMusic();

        findViewById(R.id.btnBack).setOnClickListener(this);

        RecyclerView historyRecyclerView = findViewById(R.id.history_list);
        historyRecyclerView.setLayoutManager(new GridLayoutManager(this, 2, GridLayoutManager.HORIZONTAL, false));
        historyAdapter = new HistoryAdapter();
        historyAdapter.setOnItemLongClickListener(this);
        historyAdapter.setOnItemChildClickListener(this);
        historyAdapter.bindToRecyclerView(historyRecyclerView);
        historyAdapter.setEmptyView(R.layout.empty_history);
        historyAdapter.isUseEmpty(true);

        String[] keys = CityRecordRepository.getInstance().allKey();
        if (keys != null && keys.length > 0) {
            int length = keys.length;
            for (int i = length - 1; i >= 0; i--) {
                historyAdapter.addData(CityRecordRepository.getInstance().getItem(keys[i]));
            }
        }

    }

    @Override
    public void onClick(View v) {
        if (R.id.btnBack == v.getId()) {
            finish();
        }
    }

    @Override
    public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
        if (view.getId() == R.id.tell) {
            WalkHistory item = historyAdapter.getItem(position);
            assert item != null;
            List<PlayData> playDataArrayList = item.getPlayDataArrayList();
            if (playDataArrayList != null && playDataArrayList.size() > 0) {
                TellStoryActivity.start(HistoryActivity.this, item);
            } else {
                Toast.makeText(getApplicationContext(), getString(R.string.no_tell_story), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        new DeleteConfirmPopup(this)
                .applyShowTips(getString(R.string.delete_work))
                .withConfirm(v -> deleteHistoryWalk(position), null)
                .showPopupWindow();
        return true;
    }

    public void deleteHistoryWalk(int position) {
        WalkHistory item = historyAdapter.getItem(position);
        assert item != null;
        CityRecordRepository.getInstance().remove(String.valueOf(item.getId()));
        historyAdapter.remove(position);
    }

}