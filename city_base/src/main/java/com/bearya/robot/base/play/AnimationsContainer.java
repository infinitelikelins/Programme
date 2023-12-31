package com.bearya.robot.base.play;

import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.widget.ImageView;

import com.bearya.robot.base.BaseApplication;
import com.bearya.robot.base.util.DebugUtil;

import java.lang.ref.SoftReference;

public class AnimationsContainer {
    public int FPS = 58;  // 每秒播放帧数，fps = 1/t，t-动画两帧时间间隔
    private int resId; //图片资源

    // 单例
    private static AnimationsContainer mInstance;
    private FramesSequenceAnimation animation;

    private AnimationsContainer() {
    }

    //获取单例
    public static AnimationsContainer getInstance(int resId, int fps) {
        if (mInstance == null)
            mInstance = new AnimationsContainer();
        mInstance.setResId(resId, fps);
        return mInstance;
    }

    public void setResId(int resId, int fps) {
        this.resId = resId;
        this.FPS = fps;
    }

    public static AnimationsContainer getInstance() {
        return mInstance;
    }

    public FramesSequenceAnimation getProgressDialogAnim() {
        return animation;
    }

    public FramesSequenceAnimation createProgressDialogAnim(ImageView imageView) {
        animation = new FramesSequenceAnimation(imageView, getData(resId), FPS);
        return animation;
    }

    /**
     * 循环读取帧---循环播放帧
     */
    public class FramesSequenceAnimation {
        private int[] mFrames; // 帧数组
        private int mIndex; // 当前帧
        private boolean mShouldRun; // 开始/停止播放用
        private boolean mIsRunning; // 动画是否正在播放，防止重复播放
        private SoftReference<ImageView> mSoftReferenceImageView; // 软引用ImageView，以便及时释放掉
        private Handler mHandler;
        private int mDelayMillis;
        private OnAnimationStoppedListener mOnAnimationStoppedListener; //播放停止监听

        private Bitmap mBitmap = null;
        private BitmapFactory.Options mBitmapOptions;//Bitmap管理类，可有效减少Bitmap的OOM问题
        private boolean isRepair;//是否循环播放

        public FramesSequenceAnimation(ImageView imageView, int[] frames, int fps) {
            mHandler = new Handler();
            mFrames = frames;
            mIndex = -1;
            mSoftReferenceImageView = new SoftReference<>(imageView);
            mShouldRun = false;
            mIsRunning = false;
            isRepair = false;
//            mDelayMillis = 100;//帧动画时间间隔，毫秒
            mDelayMillis = 1000 / fps;//帧动画时间间隔，毫秒
            if (mDelayMillis < 100){
                mDelayMillis = 100;
            }
            DebugUtil.debug("动画播放帧数：%d", mDelayMillis);
            imageView.setImageResource(mFrames[0]);

            // 当图片大小类型相同时进行复用，避免频繁GC
            Bitmap bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
            int width = bmp.getWidth();
            int height = bmp.getHeight();
            Bitmap.Config config = bmp.getConfig();
            mBitmap = Bitmap.createBitmap(width, height, config);
            mBitmapOptions = new BitmapFactory.Options();
            //设置Bitmap内存复用
            mBitmapOptions.inBitmap = mBitmap;//Bitmap复用内存块，类似对象池，避免不必要的内存分配和回收
            mBitmapOptions.inMutable = true;//解码时返回可变Bitmap
            mBitmapOptions.inSampleSize = 1;//缩放比例
        }

        public void setRepair(boolean repair) {//开启循环播放
            isRepair = repair;
        }

        //循环读取下一帧
        private int getNext() {
            mIndex++;
            if (mIndex >= mFrames.length) {
                if (!isRepair) {
                    mIndex = mFrames.length - 1;
                    mShouldRun = false;
                } else {
                    mIndex = 0;
                }

            }
            return mFrames[mIndex];
        }

        /**
         * 播放动画，同步锁防止多线程读帧时，数据安全问题
         */
        public synchronized void start() {
            mShouldRun = true;
            if (mIsRunning)
                return;

            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    ImageView imageView = mSoftReferenceImageView.get();
                    if (!mShouldRun || imageView == null) {
                        mIsRunning = false;
                        if (mOnAnimationStoppedListener != null) {
                            mOnAnimationStoppedListener.AnimationStopped();
                        }
                        return;
                    }

                    mIsRunning = true;
                    //新开线程去读下一帧
                    mHandler.postDelayed(this, mDelayMillis);

                    if (imageView.isShown()) {
                        int imageRes = getNext();
                        if (mBitmap != null) { // so Build.VERSION.SDK_INT >= 11
                            Bitmap bitmap = null;
                            try {
                                bitmap = BitmapFactory.decodeResource(imageView.getResources(), imageRes, mBitmapOptions);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            if (bitmap != null) {
                                imageView.setImageBitmap(bitmap);
                            } else {
                                imageView.setImageResource(imageRes);
                                mBitmap.recycle();
                                mBitmap = null;
                            }
                        } else {
                            imageView.setImageResource(imageRes);
                        }
                    }

                }
            };

            mHandler.post(runnable);
        }

        /**
         * 停止播放
         */
        public synchronized void stop() {
            mShouldRun = false;
        }


        public void release() {
            if (mSoftReferenceImageView != null) {
                ImageView animationView = mSoftReferenceImageView.get();
                if (animationView != null) {
                    DebugUtil.debug("===================删除animationView中的bitmap===================");
                    animationView.setImageBitmap(null);
                    animationView.setImageResource(0);
                }
                mSoftReferenceImageView.clear();
            }
            if (mHandler != null) {
                mHandler.removeCallbacksAndMessages(null);
                mHandler = null;
            }
            mFrames = null;
        }

        /**
         * 设置停止播放监听
         */
        public void setOnAnimStopListener(OnAnimationStoppedListener listener) {
            this.mOnAnimationStoppedListener = listener;
        }
    }

    /**
     * 从xml中读取帧数组
     */
    private int[] getData(int resId) {
        TypedArray array = BaseApplication.getInstance().getResources().obtainTypedArray(resId);

        int len = array.length();
        int[] intArray = new int[array.length()];

        for (int i = 0; i < len; i++) {
            intArray[i] = array.getResourceId(i, 0);
        }
        array.recycle();
        return intArray;
    }

    /**
     * 停止播放监听
     */
    public interface OnAnimationStoppedListener {
        void AnimationStopped();
    }
}