package com.bearya.robot.programme.station;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.bearya.robot.programme.R;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;

public class CameraActivity extends Activity {

    public static void start(Activity context, int requestChoosePhotoCode) {
        File file = new File(context.getExternalFilesDir(Environment.DIRECTORY_DCIM), System.currentTimeMillis() + ".jpg");
        File parentFile = file.getParentFile();
        if (parentFile != null && !parentFile.exists()) {
            boolean mkdirs = parentFile.mkdirs();
            if (!mkdirs) {
                Toast.makeText(context, "照片目录创建失败", Toast.LENGTH_LONG).show();
                return;
            }
        }

        context.startActivityForResult(
                new Intent(context, CameraActivity.class).putExtra(MediaStore.EXTRA_OUTPUT, file.getAbsolutePath()),
                requestChoosePhotoCode);
    }

    private CameraSurfaceView mCameraSurfaceView;
    private String mUri;
    private View preViewLayout;
    private ImageView ivPreView;
    private ImageView ivTakePhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        ivTakePhoto = findViewById(R.id.img_take_photo);
        ivPreView = findViewById(R.id.iv_pre_view);
        preViewLayout = findViewById(R.id.pre_view_layout);
        findViewById(R.id.btnOk).setOnClickListener(view -> {
            Intent data = new Intent();
            data.putExtra(MediaStore.EXTRA_OUTPUT, mUri);
            setResult(RESULT_OK, data);
            finish();
        });
        findViewById(R.id.btnCancel).setOnClickListener(view -> {
            preViewLayout.setVisibility(View.GONE);
            ivTakePhoto.setVisibility(View.VISIBLE);
        });
        mCameraSurfaceView = findViewById(R.id.sv_camera);
        ivTakePhoto.setOnClickListener(v -> takePhoto());
        mUri = getIntent().getStringExtra(MediaStore.EXTRA_OUTPUT);
        findViewById(R.id.btnBack).setOnClickListener(view -> finish());
    }

    public void takePhoto() {
        mCameraSurfaceView.takePicture(() -> {
        }, (data, camera) -> {
        }, (data, camera) -> {
            mCameraSurfaceView.startPreview();
            saveFile(data);
            ivTakePhoto.setVisibility(View.INVISIBLE);
            preViewLayout.setVisibility(View.VISIBLE);
            try {
                ivPreView.setImageBitmap(BitmapFactory.decodeFile(mUri));
            } catch (Exception e) {
                e.printStackTrace();
            }
            Toast.makeText(CameraActivity.this, "拍照成功", Toast.LENGTH_SHORT).show();
        });
    }

    public void saveFile(byte[] data) {
        try (BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(mUri))) {
            File parentFile = new File(mUri).getParentFile();
            if (parentFile != null && !parentFile.exists()) {
                parentFile.mkdirs();
            }
            bufferedOutputStream.write(data, 0, data.length);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}