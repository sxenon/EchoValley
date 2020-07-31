package com.sxenon.echovalley.arch.viewcase;

import android.app.Activity;
import android.widget.Toast;

public class PressBackAgainToExitCase {
    private static long lastPressTime = -1L;
    private long intervalMills;
    private boolean onlyToBackground;
    private String tip;
    private Activity activity;

    public PressBackAgainToExitCase(Activity activity, String tip, long intervalMills, boolean onlyToBackground) {
        this.tip = tip;
        this.intervalMills = intervalMills;
        this.onlyToBackground = onlyToBackground;
        this.activity = activity;
    }

    public PressBackAgainToExitCase(Activity activity,String tip,long intervalMills) {
        this(activity,tip,intervalMills,false);
    }

    public void onBackPressed(){
        long currentTime = System.currentTimeMillis();
        if (lastPressTime ==1L||currentTime-lastPressTime>intervalMills){
            Toast.makeText(activity,tip,Toast.LENGTH_SHORT).show();
            lastPressTime = currentTime;
        }else {
            if (onlyToBackground){
                activity.moveTaskToBack(true);
            }else {
                activity.finish();
            }
        }
    }
}
