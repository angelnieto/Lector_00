package es.ricardo.lector;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.media.MediaMetadataRetriever;
import android.net.Uri;

import java.io.File;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Aarón Formación y Co on 03/10/2016.
 */

public class Lector extends Application {


    private Activity mCurrentActivity = null;

    private TTSManager ttsManager = null;

    private Collection<AudioLibro> files = new LinkedList<>();

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

        this.files = getAudioLibros(files);
    }

    public Collection<AudioLibro> getFiles(){
        return files;
    }

    private Collection<AudioLibro> getAudioLibros(Collection files) {
        Collection<AudioLibro> lista = new LinkedList<>();

        Map<Date, AudioLibro> audiolibros = new LinkedHashMap<>();

        Iterator it = files.iterator();
        while(it.hasNext()){
            File libro = (File) it.next();

            Calendar calendario = GregorianCalendar.getInstance();
            calendario.setTimeInMillis(libro.lastModified());

              if(!audiolibros.containsKey(calendario.getTime())){

                  AudioLibro audioLibro = new AudioLibro();

                  MediaMetadataRetriever mediaMetadataRetriever = (MediaMetadataRetriever) new MediaMetadataRetriever();
                  Uri uri = (Uri) Uri.fromFile(libro);
                  mediaMetadataRetriever.setDataSource(getApplicationContext(), uri);
                  String album = (String) mediaMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
                  if(album != null) {
                      audioLibro.setNombre(album);
                  }else{
                      audioLibro.setNombre(libro.getName().substring(0, libro.getName().lastIndexOf(".")));
                  }
                  audioLibro.setFecha(calendario.getTime());
                  audioLibro.setCapitulos(new LinkedList<File>());

                  audioLibro.getCapitulos().add(libro);

                  audiolibros.put(calendario.getTime(), audioLibro);
              }else{
                  audiolibros.get(calendario.getTime()).getCapitulos().add(libro);
              }
        }

        if(!audiolibros.isEmpty()){
            lista = audiolibros.values();
        }

        return lista;
    }



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

    public boolean isHomeButtonPressed(){
        Context context = getApplicationContext();
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
        if (!taskInfo.isEmpty()) {
            ComponentName topActivity = taskInfo.get(0).topActivity;
            if (!topActivity.getPackageName().equals(context.getPackageName()))
                return true;
        }
        return false;
    }

}
