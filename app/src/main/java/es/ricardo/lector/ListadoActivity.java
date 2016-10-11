package es.ricardo.lector;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

public class ListadoActivity extends AppCompatActivity implements View.OnTouchListener {

    private TextToSpeech tts;
    //TTSManager ttsManager = null;
    private TareaAsincrona tareaAsincrona=null;
    private Lector app = null;

    static final int ACTION_VALUE=1;

    private GestureDetector mGestureDetector;
    private ProgressBar circulo = null;
    private LinearLayout listaHorizontal = null;
    private Rect pantalla;
    private float positionLeft=0;
    private DisplayMetrics dm = null;

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

        pantalla=new Rect();
        windowManager.getDefaultDisplay().getRectSize(pantalla);
        dm = getResources().getDisplayMetrics();

        circulo=(ProgressBar)findViewById(R.id.progressBar);
        circulo.setScaleX(Math.round(Math.floor(0.015*pantalla.width()/dm.density)));
        circulo.setScaleY(circulo.getScaleX());

        listaHorizontal = (LinearLayout)findViewById(R.id.innerLay);

        //Paths paths = new Paths();
        //paths.glob("//mnt", "**/*.mp3");
        //Log.v("lector00","Archivos encontrados : "+ Long.toString(paths.getFiles().size()));

        mGestureDetector = new GestureDetector(this, new GestureListener());

        if(app.getFiles().isEmpty()) {
            circulo.setVisibility(View.VISIBLE);
            listaHorizontal.setVisibility(View.INVISIBLE);
        }else{
            circulo.setVisibility(View.GONE);
            listaHorizontal.setVisibility(View.VISIBLE);
        }
    }


    @Override
    protected void onPause() {
        super.onPause();

        app.getTtsManager().stop();
    }

    public void updateResults(final Collection files) {
        circulo.setVisibility(View.GONE);
        listaHorizontal.setVisibility(View.VISIBLE);

        app.setFiles(files);

        if(!files.isEmpty()) {
            componerListado(files);
            app.getTtsManager().addBooks(files, listaHorizontal);
        }else{
            sinResultados();

            MediaPlayer player = MediaPlayer.create(this.getApplicationContext(), R.raw.sin_resultados);
            player.start();
        }


    }

    private void sinResultados() {
        LinearLayout layout = new LinearLayout(getApplicationContext());
        layout.setLayoutParams(new LinearLayout.LayoutParams(pantalla.width(), LinearLayout.LayoutParams.MATCH_PARENT));
        layout.setGravity(Gravity.CENTER_VERTICAL);
        Drawable fondo = getResources().getDrawable(R.drawable.fondo_libro);
        layout.setBackground(fondo);

        TextView texto = new TextView(getApplicationContext());
        texto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        texto.setTextColor(getResources().getColor(R.color.colorAccent));
        texto.setGravity(Gravity.CENTER);
        texto.setText(getResources().getString(R.string.no_results));
        layout.addView(texto);

        listaHorizontal.addView(layout);
    }

    private void componerListado(Collection files) {
        //Activo el escuchador de eventos
        HorizontalScrollView scrollView= (HorizontalScrollView)findViewById(R.id.scroll);
        scrollView.setOnTouchListener(this);

        Iterator iterator = files.iterator();
        while(iterator.hasNext()) {
            LinearLayout layout = new LinearLayout(getApplicationContext());
            layout.setLayoutParams(new LinearLayout.LayoutParams(pantalla.width(), LinearLayout.LayoutParams.MATCH_PARENT));
            layout.setGravity(Gravity.CENTER_VERTICAL);
            Drawable fondo = getResources().getDrawable(R.drawable.fondo_libro);
            layout.setBackground(fondo);

            TextView texto = new TextView(getApplicationContext());
            texto.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
            texto.setTextColor(getResources().getColor(R.color.colorAccent));
            texto.setGravity(Gravity.CENTER);
            File libro = (File)iterator.next();
            texto.setText(libro.getName().substring(0, libro.getName().lastIndexOf(".")));
            texto.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 50);
            layout.addView(texto);

            listaHorizontal.addView(layout);
        }
    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case ACTION_VALUE:
                if (!app.getFiles().isEmpty()) {
                    componerListado(app.getFiles());
                    app.getTtsManager().addBooks(app.getFiles(), listaHorizontal);
                }
            break;
        }
    }

/*    @Override
    public boolean onTouchEvent(MotionEvent event)
    {
        //method onTouchEvent of GestureDetector class Analyzes the given motion event
        //and if applicable triggers the appropriate callbacks on the GestureDetector.OnGestureListener supplied.
        //Returns true if the GestureDetector.OnGestureListener consumed the event, else false.

        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        if(eventConsumed) {
            switch (GestureListener.currentGestureDetected) {
                case Lector.SINGLE_TAP:
                    if (circulo.getVisibility() == View.GONE) {
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
*/
    public void animation(){

        positionLeft = positionLeft + pantalla.width();
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.scroll);
        ObjectAnimator.ofInt(hsv, "scrollX",  Math.round(positionLeft)).setDuration(500).start();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        boolean eventConsumed = mGestureDetector.onTouchEvent(event);
        if(eventConsumed) {
            switch (GestureListener.currentGestureDetected) {
                case Lector.SINGLE_TAP:
                    if (circulo.getVisibility() == View.GONE && app.getTtsManager().getActualFile() != null) {
                        app.getTtsManager().stop();

                        Intent i = new Intent(this,ReproductorActivity.class);
                        app.setCapituloActual(app.getTtsManager().getActualFile());
                        //i.putExtra("ficheroEscogido",app.getTtsManager().getActualFile());
                        startActivityForResult(i, ACTION_VALUE);
                    }
                    break;
            }
        }

        return true;
    }
}
