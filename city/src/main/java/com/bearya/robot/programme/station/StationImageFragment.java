package com.bearya.robot.programme.station;

import android.app.Activity;
import android.content.Intent;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageView;

import com.bearya.robot.base.play.FacePlay;
import com.bearya.robot.base.play.FaceType;
import com.bearya.robot.base.play.PlayData;
import com.bearya.robot.base.util.ResourceUtil;
import com.bearya.robot.programme.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

public class StationImageFragment extends BaseFragment implements View.OnClickListener {
    private static final int REQUEST_CODE_IMAGE = 1000;
    private static final int REQUEST_TAKE_PHOTO_CODE = 1001;
    private static final int REQUEST_IMAGE_LOCAL = 1002;

    private AppCompatImageView ivPreView;
    private FacePlay mFacePlay;

    public static StationImageFragment newInstance() {
        return new StationImageFragment();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_station_config_image;
    }

    @Override
    public void initView(View rootView) {
        AppCompatButton btnImageLib = rootView.findViewById(R.id.btnImageLib);
        AppCompatButton btnImageLocal = rootView.findViewById(R.id.btnImageLocal);
        AppCompatImageView ivTakePhoto = rootView.findViewById(R.id.ivTakePhoto);
        ivPreView = rootView.findViewById(R.id.ivPreView);
        ivTakePhoto.setOnClickListener(this);
        btnImageLib.setOnClickListener(this);
        btnImageLocal.setOnClickListener(this);
        ivPreView.setOnClickListener(this);
        ivPreView.setOnLongClickListener(view -> {
            PlayData station = getStation();
            if (station != null) {
                station.facePlay = null;
                saveStation();
            }
            ivPreView.setVisibility(View.GONE);
            ivPreView.setImageResource(0);
            return true;
        });
    }

    @Override
    protected void loadLastStationConfig() {
        PlayData station = getStation();
        if (station != null && station.facePlay != null && !TextUtils.isEmpty(station.facePlay.getFace())) {
            mFacePlay = station.facePlay;
            showLastFace(station.facePlay);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        FacePlay facePlay = null;
        if (requestCode == REQUEST_TAKE_PHOTO_CODE && resultCode == Activity.RESULT_OK) {
            String filePath = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
            facePlay = new FacePlay(filePath, FaceType.Image);
        } else if (requestCode == REQUEST_CODE_IMAGE && resultCode == Activity.RESULT_OK) {
            LibItem libItem = data.getParcelableExtra("data");
            if (libItem != null) {
                facePlay = new FacePlay(libItem.image, libItem.getFaceType());
            }
        } else if (requestCode == REQUEST_IMAGE_LOCAL && resultCode == Activity.RESULT_OK) {
            String localImageUri = data.getStringExtra(MediaStore.EXTRA_OUTPUT);
            facePlay = new FacePlay(localImageUri, FaceType.LocalImage);
        }
        PlayData station = getStation();
        if (station != null && facePlay != null) {
            station.facePlay = facePlay;
        }
        loadLastStationConfig();
    }

    public void showLastFace(FacePlay facePlay) {
        if (!TextUtils.isEmpty(facePlay.getFace())) {
            ivPreView.setVisibility(View.VISIBLE);
            Object url = facePlay.getFace();
            if (facePlay.getFaceType() == FaceType.Lottie) {
                url = ResourceUtil.getMipmapId(facePlay.getFace());
            } else if (facePlay.getFaceType() == FaceType.Image && !facePlay.getFace().contains("storage")) {
                url = ResourceUtil.getMipmapId(facePlay.getFace());
            } else if (facePlay.getFaceType() == FaceType.LocalImage) {
                url = facePlay.getFace();
            }
            Glide.with(requireContext()).load(url)
                    .centerCrop()
                    .bitmapTransform(new GlideCircleTransform(requireContext()))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .into(ivPreView);
        }
    }

    @Override
    public void onClick(View view) {
        int viewId = view.getId();
        if (viewId == R.id.ivTakePhoto) {
            CameraActivity.start(requireActivity(), REQUEST_TAKE_PHOTO_CODE);
        } else if (viewId == R.id.ivPreView) {
            new ImagePreViewDialog(requireActivity(), mFacePlay).show();
        } else if (viewId == R.id.btnImageLib) {
            LibActivity.start(requireActivity(), REQUEST_CODE_IMAGE, true);
        } else if (viewId == R.id.btnImageLocal) {
            ImageRepositoryActivity.start(requireActivity(), REQUEST_IMAGE_LOCAL);
        }
    }

}