package cn.com.bcf.sendemailtest;

import java.util.concurrent.locks.Lock;

import android.R.bool;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;

public class LocationThread extends Thread {
    private static Object mLock = null;
    private Handler handler;
    
    Boolean isLoop = false;
    public LocationThread(Handler handler) {
        super();
        mLock = new Object();
        this.handler = handler;
    }
    
    void setLoop(Boolean loop) {
        synchronized(mLock) {
            isLoop = loop;
        }       
    }
    
    Boolean getLoop() {
        synchronized(mLock) {
            return isLoop;
        }       
    }
    
    public void run(){
        String content = null;
        while(true) {
            synchronized(mLock) {
                if (isLoop) {
                    Message msg = new Message();
                    msg.what = 0x123;
                    msg.obj = content;
                    handler.sendMessage(msg);
                    
                } else {
                    Message msg = new Message();
                    msg.what = 0x124;
                    msg.obj = content;
                    handler.sendMessage(msg);
                    return;
                }
            }    
            SystemClock.sleep(5000); 
        }      
    }
}
