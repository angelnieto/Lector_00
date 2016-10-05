package es.ricardo.lector;

import android.content.Context;
import android.speech.tts.TextToSpeech;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import java.io.File;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;

/**
 * Created by Nilanchala
 * http://www.stacktips.com
 */
public class TTSManager  {

    private TextToSpeech mTts = null;
    private boolean isLoaded = false;
    private String actualFileName = null;
    private File actualFile = null;
    private Lector app = null;

    public File getActualFile() {
        return actualFile;
    }

    public void setActualFile(File actualFile) {
        this.actualFile = actualFile;
    }

    public String getActualFileName() {
        return actualFileName;
    }

    public void setActualFileName(String actualFileName) {
        this.actualFileName = actualFileName;
    }

    public void init(Context context) {

        try {
            mTts = new TextToSpeech(context, onInitListener);

            if(app == null) {
                app = (Lector) context.getApplicationContext();
            }
         } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void stop(){
        if(mTts.isSpeaking()) {
            mTts.stop();
        }
    }

    private TextToSpeech.OnInitListener onInitListener = new TextToSpeech.OnInitListener() {
        @Override
        public void onInit(int status) {
            if (status == TextToSpeech.SUCCESS) {
                int result =  mTts.setLanguage(new Locale("spa", "ESP"));
                isLoaded = true;

                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("error", "This Language is not supported");
                }

                mTts.setSpeechRate(Float.valueOf("0.9"));
                mTts.addSpeech(" ","es.ricardo.lector",R.raw.silencio);

            } else {
                Log.e("error", "Initialization Failed!");
            }
        }
    };


    public void shutDown() {
        mTts.stop();
        mTts.shutdown();
    }

    private void addQueue(String text, HashMap<String, String> map) {
        if (isLoaded)
            mTts.speak(text, TextToSpeech.QUEUE_ADD, map);
        else
            Log.e("error", "TTS Not Initialized");

    }

    public void initQueue(String text) {

            if (isLoaded)
                mTts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
            else
                Log.e("error", "TTS Not Initialized");
    }

    public void addBooks(Collection libros, FrameLayout listaHorizontal){
        Iterator iterator = libros.iterator();
        if(iterator.hasNext() && isLoaded){
            decirTitulo(iterator, listaHorizontal);
        }
    }

    private void decirTitulo(final Iterator iterator, final FrameLayout listaHorizontal) {
        File file = (File) iterator.next();
        final String fileName = file.getName().substring(0, file.getName().lastIndexOf("."));
        initQueue(fileName);

        HashMap<String, String> myHashAlarm = new HashMap<String, String>();
        myHashAlarm.put(TextToSpeech.Engine.KEY_PARAM_UTTERANCE_ID, "FINISHED PLAYING");

        addQueue(" ", myHashAlarm);

        actualFile = file;
        actualFileName = fileName;

        TextToSpeech.OnUtteranceCompletedListener ttsListener=new TextToSpeech.OnUtteranceCompletedListener() {

                    @Override
                    public void onUtteranceCompleted(String utteranceId) {

                        if(iterator.hasNext()){
                            ((ListadoActivity)app.getCurrentActivity()).animation();
                            decirTitulo(iterator, listaHorizontal);
                        }
                    }
                };
                mTts.setOnUtteranceCompletedListener(ttsListener);


    }



 /*   @Override
    public void onUtteranceCompleted(String utteranceId) {
        if(iterator.hasNext()){
            decirTitulo(iterator);
        }
    }*/
}