package com.bearya.robot.base.util;

import android.os.Handler;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;

public class RobotOidReaderRater {
    private Runnable headEmptyRunnable = new Runnable() {
        @Override
        public void run() {
            headEmpty = true;
        }
    };
    private Runnable tailEmptyRunnable = new Runnable() {
        @Override
        public void run() {
            tailEmpty = true;
        }
    };

    private Subscription checkEmptySubscription;

    private Action1<Long> checkEmptyAction = new Action1<Long>() {
        @Override
        public void call(Long aLong) {
            if (listener != null) {
                if (headEmpty) {
                    if (tailEmpty) {
                        listener.onTowEmpty();
                    } else {
                        listener.onHeadEmpty();
                    }
                } else {
                    if (tailEmpty) {
                        listener.onTailEmpty();
                    } else {
                        listener.onFull();
                    }
                }
            }
        }
    };

    public interface OidReaderEmptyListener {
        void onHeadEmpty();
        void onTailEmpty();
        void onTowEmpty();
        void onFull();
    }

    private OidReaderEmptyListener listener;
    private long during;
    private boolean headEmpty;
    private boolean tailEmpty;
    private Handler handler = new Handler();

    public RobotOidReaderRater(int numberOfFull, OidReaderEmptyListener listener) {
        this.listener = listener;
        during = 1000 / numberOfFull;
    }

    public void addHeadOid() {
        headEmpty = false;
        handler.removeCallbacks(headEmptyRunnable);
        handler.postDelayed(headEmptyRunnable, during);
    }

    public void addTailOid() {
        tailEmpty = false;
        handler.removeCallbacks(tailEmptyRunnable);
        handler.postDelayed(tailEmptyRunnable, during);
    }

    public void release() {
        listener = null;
        if (handler != null) {
            handler.removeCallbacks(tailEmptyRunnable);
            handler.removeCallbacks(headEmptyRunnable);
        }
        if (checkEmptySubscription != null) {
            checkEmptySubscription.unsubscribe();
        }
        handler = null;
    }

    public void start() {
        checkEmptySubscription = Observable.timer(0, 1500, TimeUnit.MILLISECONDS).subscribe(checkEmptyAction);
        addHeadOid();
        addTailOid();
    }
}
