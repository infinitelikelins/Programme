package com.bearya.robot.programme.station;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Toast;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bearya.robot.base.ui.BaseActivity;
import com.bearya.robot.programme.R;
import com.bearya.robot.programme.adapter.ImageRepositoryAdapter;
import com.bearya.robot.programme.view.DeleteConfirmPopup;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;

public class ImageRepositoryActivity extends BaseActivity implements BaseQuickAdapter.OnItemClickListener, BaseQuickAdapter.OnItemLongClickListener {

    public static void start(Activity context, int requestCode) {
        context.startActivityForResult(new Intent(context, ImageRepositoryActivity.class), requestCode);
    }

    private ImageRepositoryAdapter imageRepositoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_repository);

        findViewById(R.id.ivBack).setOnClickListener(view -> {
            setResult(RESULT_CANCELED);
            finish();
        });
        findViewById(R.id.socket_upload_toggle).setOnClickListener(view -> {
            if (requestImageSize() < 100) {
                ImageSocketActivity.start(this);
            } else {
                Toast.makeText(this, "可保存的照片数量超出上限100张,请先删除整理照片。", Toast.LENGTH_SHORT).show();
            }
        });

        RecyclerView imageRepositoryRecyclerView = findViewById(R.id.image_repository_recycler_view);
        imageRepositoryRecyclerView.setLayoutManager(new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false));

        imageRepositoryAdapter = new ImageRepositoryAdapter();

        imageRepositoryAdapter.setOnItemClickListener(this);
        imageRepositoryAdapter.setOnItemLongClickListener(this);
        imageRepositoryAdapter.bindToRecyclerView(imageRepositoryRecyclerView);

        imageRepositoryAdapter.setEmptyView(R.layout.empty_local_image);
    }

    @Override
    protected void onResume() {
        super.onResume();
        requestUploadImages();
    }

    private int requestImageSize() {
        File imageDir = new File(getFilesDir() + "/local/");
        if (imageDir.exists()) {
            File[] files = imageDir.listFiles();
            return files != null ? files.length : 0;
        }
        return 0;
    }

    private void requestUploadImages() {
        File imageDir = new File(getFilesDir() + "/local/");
        if (!imageDir.exists()){
            imageDir.mkdirs();
        } else {
            File[] files = imageDir.listFiles();
            if (files != null && files.length > 0) {
                ArrayList<File> list = new ArrayList<>(files.length);
                Collections.addAll(list, files);
                imageRepositoryAdapter.setNewData(list);
            }
        }
    }

    @Override
    public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
        File item = imageRepositoryAdapter.getItem(position);
        if (item != null) {
            String mUri = item.getAbsolutePath();
            setResult(RESULT_OK, new Intent().putExtra(MediaStore.EXTRA_OUTPUT, mUri));
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    @Override
    public boolean onItemLongClick(BaseQuickAdapter adapter, View view, final int position) {
        new DeleteConfirmPopup(this)
                .applyShowTips("确定要删除这个图片吗?")
                .withConfirm(view1 -> {
                    File item = imageRepositoryAdapter.getItem(position);
                    if (item != null) {
                        if (item.delete()) {
                            imageRepositoryAdapter.remove(position);
                            Toast.makeText(this, "删除成功", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(this, "删除失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, null).showPopupWindow();
        return false;
    }

}