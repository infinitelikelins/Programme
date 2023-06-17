package com.bearya.robot.programme.station;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.view.Display;
import android.view.View;
import android.view.WindowManager;


import androidx.annotation.NonNull;

import com.airbnb.lottie.LottieAnimationView;
import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.bumptech.glide.Glide;

public class ImagePreViewDialog extends Dialog implements View.OnClickListener {

    public ImagePreViewDialog(@NonNull Activity activity, FacePlay facePlay) {
        super(activity, R.style.FullScreenDialog);
        setContentView(R.layout.dialog_image_preview);
        LottieAnimationView imageView = findViewById(R.id.iv_pre_view);
        if (facePlay.getFaceType() == FaceType.Image) {
            if (facePlay.getFaceType() == FaceType.Image) {
                if (facePlay.getFace().contains("storage")) {
                    imageView.setImageBitmap(BitmapFactory.decodeFile(facePlay.getFace()));
                } else {
                    imageView.setImageResource(ResourceUtil.getMipmapId(facePlay.getFace()));
                }
            }
        } else if (facePlay.getFaceType() == FaceType.Lottie) {
            setContentView(imageView);
            imageView.setBackgroundColor(Color.BLACK);
            imageView.setAnimation(String.format("emotion/%s.json", facePlay.getFace()));
            imageView.loop(true);
            imageView.playAnimation();
        } else if (facePlay.getFaceType() == FaceType.LocalImage) {
            Glide.with(getContext()).load(facePlay.getFace()).into(imageView);
        }
        imageView.setOnClickListener(this);
        setFullScreen();
    }

    private void setFullScreen() {
        Display display = getWindow().getWindowManager().getDefaultDisplay();
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.height = display.getHeight(); //设置宽度
        lp.width = display.getWidth(); //设置宽度
        getWindow().setAttributes(lp);
    }

    @Override
    public void onClick(View view) {
        dismiss();
    }

}
