package es.ricardo.lector;

import android.content.Intent;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ProgressBar;

import java.util.Collection;

public class ListadoActivity extends AppCompatActivity {

    private TextToSpeech tts;
    //TTSManager ttsManager = null;
    private TareaAsincrona tareaAsincrona=null;
    private Lector app = null;

    static final int ACTION_VALUE=1;

    private GestureDetector mGestureDetector;
    ProgressBar circulo = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listado);

        if(app == null) {
            app = (Lector) this.getApplicationContext();
        }
        app.setCurrentActivity(this);

        //Escalo la barra de progreso acorde a la resolución de la pantalla
        WindowManager windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);

        Rect pantalla=new Rect();
        windowManager.getDefaultDisplay().getRectSize(pantalla);
        DisplayMetrics dm = getResources().getDisplayMetrics();

        circulo=(ProgressBar)findViewById(R.id.progressBar);

        circulo.setScaleX(Math.round(Math.floor(0.015*pantalla.width()/dm.density)));
        circulo.setScaleY(circulo.getScaleX());

        //Paths paths = new Paths();
        //paths.glob("//mnt", "**/*.mp3");
        //Log.v("lector00","Archivos encontrados : "+ Long.toString(paths.getFiles().size()));

        mGestureDetector = new GestureDetector(this, new GestureListener());

        //ttsManager = new TTSManager();
        //ttsManager.init(getApplicationContext());

        if(app.getFiles().isEmpty()) {
            circulo.setVisibility(View.VISIBLE);
        }else{
            circulo.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        app.getTtsManager().stop();
    }

    public void updateResults(Collection files) {
        circulo.setVisibility(View.INVISIBLE);
        app.setFiles(files);
        int i=0;

        //ttsManager.addBooks(files);
        app.getTtsManager().addBooks(files);
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case ACTION_VALUE:
                if (!app.getFiles().isEmpty()) {
                    app.getTtsManager().addBooks(app.getFiles());
                    //ttsManager.addBooks(app.getFiles());
                }
            break;
        }
    }


    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //method onTouchEvent of GestureDetector class Analyzes the given motion event
        //and if applicable triggers the appropriate callbacks on the GestureDetector.OnGestureListener supplied.
        //Returns true if the GestureDetector.OnGestureListener consumed the event, else false.

        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        if(eventConsumed) {
            switch (GestureListener.currentGestureDetected) {
                case Lector.SINGLE_TAP:
                    if (circulo.getVisibility() == View.INVISIBLE) {
                        app.getTtsManager().stop();

                         Intent i = new Intent(this,ReproductorActivity.class);
                         i.putExtra("ficheroEscogido",app.getTtsManager().getActualFile());
                         startActivityForResult(i, ACTION_VALUE);
                    }
                    break;
            }
        }

        return true;

    }

}
