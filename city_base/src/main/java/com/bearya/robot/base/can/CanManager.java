package com.bearya.robot.base.can;

import android.content.Context;
import android.media.AudioManager;

import com.bearya.actionlib.can.CanDataHandler;
import com.bearya.actionlib.can.CanDataType;
import com.bearya.actionlib.canjni.Canjni;
import com.bearya.actionlib.utils.RobotActionManager;
import com.bearya.actionlib.utils.ThreadPoolManager;
import com.bearya.robot.base.BaseApplication;
import com.bearya.actionlib.light.ElectricityCanDataHandler;
import com.bearya.robot.base.ui.DialogVoice;
import com.bearya.robot.base.util.DebugUtil;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

public class CanManager {

    private static byte[] readBuffer = new byte[]{0x00, 0x00, 0x15, 0x55, 0x04, 0x40, 0x01, 0x00, 0x00, 0, 0, 0, 0, 0, 0, 0};
    private Thread mCanReadThread;
    private boolean bRunning;
    private static CanManager instance;

    private  byte[] upperRfReadBuffer = new byte[]{0,0,0,0,  0,0,0,0};

    private List<CanDataListener> listeners = new ArrayList<>();
    private TouchHandle touchHandle;

    private CanManager() {
        touchHandle = new TouchHandle();
    }

    public static CanManager getInstance() {
        if(instance==null){
            instance = new CanManager();
        }
        return instance;
    }

    public void startScan(){
        DebugUtil.debug("start-Can");
        if(mCanReadThread==null) {
            Canjni.open_can(Canjni.CAN_CTRLMODE_3_SAMPLES, 1000000);
            bRunning = true;
            mCanReadThread = new Thread(new ScannerRunnable());
            mCanReadThread.setName("Can-Scanner");
            mCanReadThread.start();
        }
    }

    public void reStartCan(){
        closeCan();
        startScan();
    }

    public void closeCan(){
        DebugUtil.debug("close-Can");
        listeners.clear();
        bRunning = false;
        if(mCanReadThread!=null){
            try {
                mCanReadThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mCanReadThread = null;
        Canjni.close_can();
    }

    private void onReadData() {
        if(get(8) == CanDataType.PRODUCT_CODE.getValue()){
            onCanDataType(CanDataType.PRODUCT_CODE);
            return;
        }
        CanDataType dataTtype;
        //新格式的避障防跌
        if (get(9) == 0x04){
            dataTtype = CanDataType.getType(get(10));
        }else {
            dataTtype = CanDataType.getType(get(8));
            if(dataTtype ==  CanDataType.UNKNOW){
                dataTtype = CanDataType.getType(get(9));
            }
        }
        if(dataTtype==CanDataType.ELECTRICITY){
            RobotActionManager.sendHeartbeat();//发送心跳包
        }
        onCanDataType(dataTtype);
    }

    private ElectricityCanDataHandler electricityCanDataHandler = new ElectricityCanDataHandler(BaseApplication.getInstance());


    private void onCanDataType(CanDataType dataType){
        switch (dataType){
            case ELECTRICITY:
                electricityCanDataHandler.onElectricityData(readBuffer);
                break;
            case CUTOFF_CHARGING:
                electricityCanDataHandler.onChargeStatechange(readBuffer);
                break;
            case HEAD_SCANNER: {
                int oid = parseToOid(readBuffer);
                if (oid>0 && listeners!=null && listeners.size()>0){
                    Iterator<CanDataListener> iterator = listeners.iterator();
                    while (iterator.hasNext()){
                        CanDataListener canDataListener = iterator.next();
                        canDataListener.onFrontOid(oid);
                    }
                }
                break;
            }
            case TAIL_SCANNER:{
                int oid = parseToOid(readBuffer);
                if (oid>0 && listeners!=null && listeners.size()>0){
                    Iterator<CanDataListener> iterator = listeners.iterator();
                    while (iterator.hasNext()){
                        CanDataListener canDataListener = iterator.next();
                        canDataListener.onBackOid(oid);
                    }
                }
                break;
            }
            case EXCEPTION:
                if (get(readBuffer,10) == 0x1 || get(readBuffer,10) == 0x2) {

                } else if (get(readBuffer,10) == 0x7){

                } else if (get(readBuffer,10) == 0x8){

                }
                break;

            case TOUCH_BODY: {
//触摸
                if (get(readBuffer, 11) == 0x9) {//左手
                    if (touchHandle.touch()) {
                        onTouch(Body.LEFT_ARM,"music/touch_hand.mp3");
                    }
                } else if (get(readBuffer, 11) == 0xa) {//右手
                    if (touchHandle.touch()) {
                        onTouch(Body.RIGHT_ARM,"music/touch_hand.mp3");
                    }
                } else if (get(readBuffer, 11) == 0x11) {//Home键

                } else if (get(readBuffer, 11) == 0x5) {//胸前
                    if (get(readBuffer, 10) == 0x01){

                    }else if (get(readBuffer, 10) == 0x02) {

                        if (touchHandle.touch()) {
                            onTouch(Body.CHEST, "music/touch_chest.mp3");
                        }
                    }
                } else if (get(readBuffer, 11) == 0x6) {
                    if (get(readBuffer, 10) == 0x01){

                    }else if (get(readBuffer, 10) == 0x02) {
                        if (touchHandle.touch()) {
                            onTouch(Body.BACK, "music/touch_back.wav");
                        }
                    }
                }
                break;
            }
            case INC_VOICE_BTN:
                onVoiceIncData(readBuffer);
                break;
            case DES_VOICE_BTN:
                onVoiceDesData(readBuffer);
                break;
            case POWER_BTN:
                onPowerBtnData(readBuffer);
                break;
        }
    }

    public void onPowerBtnData(byte[] data){
        if (get(data,10) == 0x1) {
            //短按
        } else if (get(data,10) == 0x2) {
            onPowerBtnLongClick();
        } else if (get(data,10) == 0x3) {
            //强制关机LongLongClick
        }
    }

    private void onPowerBtnLongClick() {
        electricityCanDataHandler.popPowerOperation();
    }

    public void onVoiceDesData(byte[] data){
        if (get(data,10) == 0x1) {
            Observable.just(this).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CanManager>() {
                @Override
                public void call(CanManager commonCanDataHandler) {
                    changeVolume(true);
                }
            });
        }
    }

    public void onVoiceIncData(byte[] data){
        if (get(data,10) == 0x1) {
            Observable.just(this).subscribeOn(AndroidSchedulers.mainThread()).subscribe(new Action1<CanManager>() {
                @Override
                public void call(CanManager manager) {
                    changeVolume(false);
                }
            });
        }
    }

    private void changeVolume(boolean isIncr) {
        AudioManager audioManager = (AudioManager) BaseApplication.getInstance().getSystemService(Context.AUDIO_SERVICE);
        int volume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
        int maxVolumeOfStreamMusic = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        if (isIncr) {
            volume += 1;
        } else  {
            volume -= 1;
        }
        if (volume > maxVolumeOfStreamMusic) {
            volume = maxVolumeOfStreamMusic;
        } else if (volume < 0){
            volume = 0;
        }
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
        DialogVoice dialogVoice = new DialogVoice(BaseApplication.getInstance());
        dialogVoice.show();
        dialogVoice.refreshProgress();

    }

    private void onTouch(Body body, String effect) {
//        MusicUtil.playAssetsAudio(effect);
        DebugUtil.debug("touchBody=%s",body.name());
        if(listeners!=null && listeners.size()>0){
            for(CanDataListener listener:listeners) {
                DebugUtil.debug("touchBody listener=%s",listener.getClass().getSimpleName());
                listener.onTouchBody(body);
            }
        }
    }



    /**
     * 从Can缓冲区读取数据
     * @param index
     * @return
     * @throws ArrayIndexOutOfBoundsException
     */
    private int get(int index) throws ArrayIndexOutOfBoundsException {
        return CanDataHandler.get(readBuffer,index);
    }

    public void release() {
        if(listeners!=null && !listeners.isEmpty()){
            listeners.clear();
        }
        if(electricityCanDataHandler!=null){
            electricityCanDataHandler.release();
        }
    }

    public boolean isChargeing() {
        return electricityCanDataHandler.isChargeing;
    }


    class ScannerRunnable implements IUpdater,Runnable {

        public ScannerRunnable() {
        }

        @Override
        public void update(float dt) {
            try {
                readCan();
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        @Override
        public void run() {
            long lastTime = System.nanoTime() / 1000000;
            long currTime = lastTime;
            while (bRunning) {
                // 更新游戏对象
                currTime = System.nanoTime() / 1000000;
                update((currTime - lastTime));
                lastTime = currTime;
            }
        }


        void readCan() throws Exception {
            int result = Canjni.read_can(readBuffer, 12);
            if (result == -2) {//can未打开，重新打开
                openCan();
            } else {
                onReadData();
            }
        }
    }

    private int openCan(){
        DebugUtil.debug("open can %s", Thread.currentThread().getName());
        return Canjni.open_can(Canjni.CAN_CTRLMODE_3_SAMPLES, 1000000);
    }

    private int count = 0;
    private Set<Integer> countSet = new HashSet<>();
    private int lastNum0;

    private int getValue(int origin){
        if (origin < 0){
            return origin + 256;
        }
       return origin;
    }

    private String getLostDataFirst(){
        String content = "";
        for (int i = 1 ;i <=254 ;i++){
            if (!countSet.contains(i)) {
                content += i + ",";
            }
        }
        //异常情况
        if ("".equals(content)){
            return "";
        }
        return content.substring(0,content.length() -1);
    }

    private String getLostData(){
        String content = "";
        for (int i = 1 ;i <=255 ;i++){
            if (!countSet.contains(i)) {
                content += i + ",";
            }
        }
        //异常情况
        if ("".equals(content)){
            return "";
        }
        return content.substring(0,content.length() -1);
    }

    private String getBufferAddress(){
        return upperRfReadBuffer[4] + "," + upperRfReadBuffer[5]
                + "," + upperRfReadBuffer[6] + "," + upperRfReadBuffer[7];
    }

    private void getOidChangeLight() {
        ThreadPoolManager.getLongPool().execute(new Runnable() {
            @Override
            public void run() {
                try {
                    RobotActionManager.ctrlEarLight(0, 0);
                    RobotActionManager.ctrlEarLight(1, 0);
                    RobotActionManager.ctrlEarLight(2, 1);
                    Thread.sleep(400);
                    RobotActionManager.ctrlEarLight(0, 0);
                    RobotActionManager.ctrlEarLight(1, 0);
                    RobotActionManager.ctrlEarLight(2, 0);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public int parseToOid(byte[] data){
        int heigh;
        int low;
        if (get(data,10) >= 0) {
            heigh = get(data,10) * 256;
        } else {
            heigh = get(data,10);
            heigh = (heigh & 0xff) * 256;
        }
        if (get(data,11) >= 0) {
            low = get(data,11);
        } else {
            low = get(data,11);
            low = low & 0xff;
        }

        int oid = heigh + low;
        return oid;
    }

        public  int get(byte[] data,int index){
            return CanDataHandler.get(data,index);
        }


    public boolean isbRunning(){
        return bRunning;
    }


    public void addListener(CanDataListener listener) {
        if(!listeners.contains(listener)){
            DebugUtil.debug("添加监听:%s-%d",listener.getClass().getSimpleName(),listener.hashCode());
            listeners.add(listener);
        }
    }

    public void removeListener(CanDataListener listener){
        Iterator<CanDataListener> iterator = listeners.iterator();
        CanDataListener equalseListener = null;
        while (iterator.hasNext()){
            CanDataListener canDataListener = iterator.next();
            if(canDataListener==listener){
                equalseListener = canDataListener;
                break;
            }
        }
        if(equalseListener!=null){
            DebugUtil.debug("移除监听:%s-%d",equalseListener.getClass().getSimpleName(),listener.hashCode());
            listeners.remove(equalseListener);
        }
    }

    public boolean containListener(CanDataListener listener){
        return listeners.contains(listener);
    }

}
