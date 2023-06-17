package com.bearya.robot.base.play;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.airbnb.lottie.LottieAnimationView;
import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.util.CodeUtils;
import com.bearya.robot.base.util.DebugUtil;
import com.bearya.robot.base.util.MusicUtil;
import com.bearya.robot.base.util.ResourceUtil;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.bearya.robot.base.play.PlayData.ONLY_ACTION;

public class Director implements MediaPlayer.OnCompletionListener {
    private LottieAnimationView view;
    private static final int FRAME_DURING = 100;
    private LoadPlay loadPlay;
    private PlayData playData;
    private PlayListener listener;
    private final Map<String, LoadPlay> loadPlayMap = new HashMap<>();
    private final Runnable frameAnimationCompleteRunnable = () -> {
        DebugUtil.debug("执行定时complete====================>");
        complete(PlayData.ONLY_FRAME_ANIMATION);
    };
    private final Runnable imageCompleteRunnable = () -> complete(PlayData.ONLY_IMAGE);
    private final List<Runnable> actionRunnables = new ArrayList<>();
    private static Director instance;
    private String soundParam;
    private AnimationsContainer.FramesSequenceAnimation animation;

    public static Director getInstance() {
        if (instance == null) {
            instance = new Director();
        }
        return instance;
    }

    public void register(String key, LoadPlay play) {
        if (loadPlayMap.containsKey(key)) {
            DebugUtil.info("  ----------   动画事件注册失败 ,当前 Key == %s", key);
        } else {
            loadPlayMap.put(key, play);
            DebugUtil.info("  ++++++++++   动画事件注册成功 ,当前 Key == %s", key);
        }
    }

    public void director(String key, PlayListener listener) {
        director(key, null, listener);
    }

    public void director(String key, String soundParam, PlayListener listener) {
        this.listener = listener;
        this.soundParam = soundParam;
        BaseApplication.getInstance().getHandler().removeCallbacks(frameAnimationCompleteRunnable);
        LoadPlay mLoadPlay = loadPlayMap.remove(key);
        if (mLoadPlay != null) {
            this.loadPlay = mLoadPlay;
            if (!TextUtils.isEmpty(loadPlay.playAction)) {
                if (!TextUtils.isEmpty(soundParam) && loadPlay.playAction.contains("%s")) {
                    loadPlay.playAction = String.format(loadPlay.playAction, soundParam);
                }
                BaseApplication.sendAction(loadPlay.playAction);
            }
            if ("GOLD_SUCCESS".equals(key) || "DONE_SUCCESS".equals(key) || "STEP_SUCCESS".equals(key)) {
                directNextInPlayResult();
            } else {
                directNext();
            }
        }
    }

    private void directNextInPlayResult() {
        if (loadPlay != null) {
            playData = loadPlay.getPlay();
        }
        if (playData == null && listener != null) {
            loadPlay = null;
            listener.onComplete();
        } else {
            directPlay(playData);
        }
    }

    private void directNext() {
        if (loadPlay != null) {
            playData = loadPlay.getPlay();
        }
        if (playData == null && listener != null) {
            loadPlay = null;
            listener.onComplete();
            listener = null;
        } else {
            directPlay(playData);
        }
    }

    private void directPlay(PlayData playData) {
        if (view != null) {
            view.removeCallbacks(imageCompleteRunnable);
        }
        if (playData != null) {
            playData.countCompleteCondition();
            playFace(playData.getFacePlay());
            playSound(playData.getSound());
            playLight(playData.getColor(), playData.getMode());
            playAction(playData.getRobotAction());
        }
    }

    public void setView(LottieAnimationView view) {
        this.view = view;
    }

    public LottieAnimationView getView() {
        return view;
    }

    private void playEmotion(String fileName) {
        view.setAnimation(String.format("emotion/%s.json", fileName));
        view.playAnimation();
        view.postDelayed(imageCompleteRunnable, 3000);
    }

    private void playFace(final FacePlay facePlay) {
        if (view == null) return;
        if (facePlay != null && !TextUtils.isEmpty(facePlay.getFace())) {
            Drawable drawable = view.getDrawable();
            if (drawable instanceof AnimationDrawable) {
                AnimationDrawable animationDrawable = (AnimationDrawable) drawable;
                animationDrawable.stop();
            }
            DebugUtil.error("playFace type=%s,file=%s", facePlay.getFaceType().name(), facePlay.getFace());
            BaseApplication.getInstance().getHandler().post(() -> {
                switch (facePlay.getFaceType()) {
                    case Lottie:
                        if (animation != null) {
                            animation.stop();
                        }
                        playEmotion(facePlay.getFace());
                        break;
                    case LocalImage:
                        playImage(facePlay.getFace());
                        break;
                    case Image:
                        try {
                            int imageId = 0;
                            if (facePlay.getFace() != null) {
                                boolean digitsOnly = TextUtils.isDigitsOnly(facePlay.getFace());
                                if (digitsOnly) {
                                    imageId = Integer.parseInt(facePlay.getFace());
                                } else if ((facePlay.getFace().endsWith(".png") || facePlay.getFace().endsWith(".webp") || facePlay.getFace().endsWith(".jpg"))) {
                                    playImage(facePlay.getFace());
                                    return;
                                } else {
                                    imageId = ResourceUtil.getMipmapId(facePlay.getFace());
                                    if (imageId <= 0) {
                                        imageId = ResourceUtil.getDrawableId(facePlay.getFace());
                                    }
                                }
                            }
                            if (imageId > 0) {
                                playImage(imageId);
                            }
                        } catch (Exception e) {
                            DebugUtil.error("imageId error : %s ", e.getMessage());
                        }
                        break;
                    case FrameAnimate:
                        playAnimation(Integer.parseInt(facePlay.getFace()));
                        break;
                    case Arrays:
                        int array = Integer.parseInt(facePlay.getFace());
                        TypedArray arrays = BaseApplication.getInstance().getResources().obtainTypedArray(array);
                        int len = arrays.length();
                        animation = AnimationsContainer.getInstance(array, len).createProgressDialogAnim(view);
                        arrays.recycle();
                        animation.start();
                        animation.setOnAnimStopListener(() -> {
                            DebugUtil.debug("动画播放完毕");
                            complete(PlayData.ONLY_FRAME_ANIMATION);
                        });
                        break;
                }
            });
        }
    }

    private void playImage(int res) {
        playImage(BitmapFactory.decodeResource(BaseApplication.getInstance().getResources(), res));
    }

    private void playImage(String file) {

        if (!TextUtils.isEmpty(file) && file.startsWith("file:///android_asset/")) {
            try (InputStream inputStream = view.getContext().getAssets().open(file.substring("file:///android_asset/".length()))) {
                view.setImageDrawable(Drawable.createFromStream(inputStream, null));
            } catch (Exception e) {
                DebugUtil.report("error file = %s", file);
            }
            view.postDelayed(imageCompleteRunnable, 3000);
        } else if (file != null && (file.startsWith("http://") || file.startsWith("https://")) && view != null) {
            Glide.with(view.getContext()).load(file)
                    .asBitmap()
                    .diskCacheStrategy(DiskCacheStrategy.RESULT)
                    .skipMemoryCache(true)
                    .into(view);
            view.postDelayed(imageCompleteRunnable, 3000);
        } else {
            playImage(BitmapFactory.decodeFile(file));
        }
    }

    private void playImage(Bitmap bitmap) {
        if (view != null && bitmap != null) {
            view.setImageBitmap(bitmap);
            view.postDelayed(imageCompleteRunnable, 3000);
            return;
        }
        complete(PlayData.ONLY_IMAGE);
    }

    private void playAnimation(int res) {
        AnimationDrawable animationDrawable = (AnimationDrawable) BaseApplication.getInstance().getResources().getDrawable(res, null);
        view.setImageDrawable(animationDrawable);
        long delay = animationDrawable.getNumberOfFrames() * FRAME_DURING;
        animationDrawable.start();
        BaseApplication.getInstance().getHandler().postDelayed(frameAnimationCompleteRunnable, delay);
    }

    private void playSound(String file) {
        if (!TextUtils.isEmpty(file)) {
            MusicUtil.stopMusic();
            if (!TextUtils.isEmpty(soundParam) && file.contains("%s")) {
                file = String.format(file, soundParam);
            }
            DebugUtil.debug("playSound=%s", file);
            MusicUtil.playAssetsAudio(file, this);
        }
    }

    private void playAction(TimeAction[] actions) {
        removeActionRunnable();
        if (actions != null && actions.length > 0) {
            int during = 0;
            for (TimeAction action : actions) {
                if (action == null) {
                    continue;
                }
                ActionRunnable runnable = new ActionRunnable(action.getAction());
                actionRunnables.add(runnable);
                BaseApplication.getInstance().getHandler().postDelayed(runnable, during);
                during += (action.getTime() * 1000);
            }
            Runnable stopActionRunnable = () -> {
                RobotActionManager.reset();
                complete(ONLY_ACTION);
            };
            actionRunnables.add(stopActionRunnable);
            BaseApplication.getInstance().getHandler().postDelayed(stopActionRunnable, during);
        }
    }

    private void playLight(RobotActionManager.LightColor color, RobotActionManager.LightMode mode) {
        if (color != null) {
            DebugUtil.debug("颜色:%s,模式:%s", color.name(), mode.name());
        }
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        complete(PlayData.ONLY_SOUND);
    }

    private void complete(int condition) {
        if (playData != null) {
            playData.complete(condition);
            if (playData.isComplete()) {
                directNext();
            }
        } else {
            directNext();
        }
    }

    public void reset() {
        playData = null;
        loadPlay = null;
        listener = null;
        loadPlayMap.clear();
        BaseApplication.getInstance().getHandler().removeCallbacks(frameAnimationCompleteRunnable);
    }

    public void playMovingEmotion() {
        playFace(new FacePlay("hg", FaceType.Lottie));
    }

    public void release() {
        reset();
        stop();
        view = null;
    }

    public void stop() {
        MusicUtil.stopMusic();
        removeActionRunnable();
        RobotActionManager.reset();
        removeActionRunnable();
    }

    private void removeActionRunnable() {
        if (!CodeUtils.isEmpty(actionRunnables)) {
            for (Runnable runnable : actionRunnables) {
                BaseApplication.getInstance().getHandler().removeCallbacks(runnable);
            }
        }
        actionRunnables.clear();
    }

    private static class ActionRunnable implements Runnable {
        private final int action;

        ActionRunnable(int action) {
            this.action = action;
        }

        @Override
        public void run() {
            RobotActionManager.reset();
            BaseApplication.getInstance().getHandler().postDelayed(this::doAction, 1000);
        }

        private void doAction() {
            if (action == 1) {
                RobotActionManager.handShake(50);
            } else if (action == 2) {
                RobotActionManager.ctrlLeftHand(0, 50, 10);
            } else if (action == 3) {
                RobotActionManager.ctrlRighttHand(0, 50, 10);
            } else if (action == 4) {
                RobotActionManager.headerShake((byte) 10);
            } else if (action == 5) {
                RobotActionManager.turnHead(8, 50, 10);
            } else if (action == 6) {
                RobotActionManager.turnHead(0, 50, 10);
            }
        }
    }
}
