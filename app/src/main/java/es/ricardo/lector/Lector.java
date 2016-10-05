package es.ricardo.lector;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by Aarón Formación y Co on 03/10/2016.
 */

public class Lector extends Application {

    private Activity mCurrentActivity = null;

    private TTSManager ttsManager = null;

    private Collection files = new LinkedList();

    static final int   SINGLE_TAP       = 1;
    static final int   DOUBLE_TAP       = 2;
    static final int   SWIPE_TO_RIGHT   = 3;
    static final int   SWIPE_TO_LEFT    = 4;


    public Activity getCurrentActivity(){
        return mCurrentActivity;
    }
    public void setCurrentActivity(Activity mCurrentActivity){
        this.mCurrentActivity = mCurrentActivity;
    }

    public void setFiles(Collection files){
        this.files = files;
    }

    public Collection getFiles(){
        return files;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        ttsManager = new TTSManager();
        ttsManager.init(getApplicationContext());
    }

    @Override
    public void onTerminate() {
        super.onTerminate();

        ttsManager.shutDown();
    }

    public TTSManager getTtsManager() {
        return ttsManager;
    }

    public void setTtsManager(TTSManager ttsManager) {
        this.ttsManager = ttsManager;
    }



}
