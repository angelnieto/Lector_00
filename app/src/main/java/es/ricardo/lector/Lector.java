package es.ricardo.lector;

import android.app.Activity;
import android.app.Application;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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
    static final int   LONG_PRESS       = 5;

    private int posicionActual;
    private File capituloActual;

    public File getCapituloActual() {
        return capituloActual;
    }

    public void setCapituloActual(File capituloActual) {
        this.capituloActual = capituloActual;
    }

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

    public File getNext(File libroActual){
        File libro = null;

        Iterator iterator = files.iterator();
        boolean actualEncontrado = false;
        while(iterator.hasNext() && !actualEncontrado) {
            libro = (File) iterator.next();
            if(libro.getPath().equals(libroActual.getPath())){
                actualEncontrado = true;
            }
        }
        if(actualEncontrado && iterator.hasNext()){
            libro = (File) iterator.next();
        }

        return libro;
    }

    public File getPrevious(File libroActual){
        File libro = null;

        List<?> lista = Arrays.asList(files.toArray());

        Collections.reverse(lista);

        Iterator iterator = lista.iterator();
        boolean actualEncontrado = false;
        while(iterator.hasNext() && !actualEncontrado) {
            libro = (File) iterator.next();
            if(libro.getPath().equals(libroActual.getPath())){
                actualEncontrado = true;
            }
        }
        if(actualEncontrado && iterator.hasNext()){
            libro = (File) iterator.next();
        }

        return libro;
    }

    public int getPosicionActual() {
        return posicionActual;
    }

    public void setPosicionActual(int posicionActual) {
        this.posicionActual = posicionActual;
    }


}
