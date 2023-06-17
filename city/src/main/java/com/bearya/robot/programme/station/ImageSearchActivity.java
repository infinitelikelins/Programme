package com.bearya.robot.programme.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.ImageSearchAdapter;

public class ImageSearchActivity extends BaseActivity {

    public static void start(Activity context, int requestCode) {
        context.startActivityForResult(new Intent(context, ImageSearchActivity.class), requestCode);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_search);
        findViewById(R.id.ivBack).setOnClickListener(view -> finish());

        RecyclerView searchList = findViewById(R.id.searchList);
        searchList.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));
        ImageSearchAdapter adapter = new ImageSearchAdapter();
        adapter.bindToRecyclerView(searchList);
        adapter.isUseEmpty(true);
        adapter.setEmptyView(R.layout.empty_search);

    }

}