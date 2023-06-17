package com.bearya.robot.programme.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.PatriotismContentAdapter;
import com.bearya.robot.programme.repository.PatriotismRepository;

import java.util.Arrays;

public class PatriotismThemeActivity extends BaseActivity {

    private PatriotismContentAdapter contentAdapter;

    public static void start(Context context, String theme) {
        context.startActivity(new Intent(context, PatriotismThemeActivity.class).putExtra("theme", theme));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patriotism_theme);

        findViewById(R.id.btnBack).setOnClickListener(view-> finish());
        String theme = getIntent().getStringExtra("theme");

        RecyclerView recyclerView = findViewById(R.id.patriotism_theme_list);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        contentAdapter = new PatriotismContentAdapter(Arrays.asList(PatriotismRepository.getInstance().getPatriotismContents(theme)));
        contentAdapter.setOnItemClickListener((adapter, view, position) -> {
            String contentName = contentAdapter.getItem(position).getContentName();
            if (PatriotismRepository.getInstance().get(contentName)) {
                PatriotismRepository.getInstance().remove(contentName);
            } else {
                PatriotismRepository.getInstance().put(contentName);
            }
            contentAdapter.notifyItemChanged(position);
        });
        contentAdapter.bindToRecyclerView(recyclerView);
    }

}