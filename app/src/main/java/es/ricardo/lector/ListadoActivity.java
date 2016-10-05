package es.ricardo.lector;

import android.content.Intent;
import android.graphics.Rect;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
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

import java.util.Collection;

public class ListadoActivity extends AppCompatActivity {

    private TextToSpeech tts;
    //TTSManager ttsManager = null;
    private TareaAsincrona tareaAsincrona=null;
    private Lector app = null;

    static final int ACTION_VALUE=1;

    private GestureDetector mGestureDetector;
    ProgressBar circulo = null;
    FrameLayout listaHorizontal = null;

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

        listaHorizontal = (FrameLayout)findViewById(R.id.scroll);

        //Paths paths = new Paths();
        //paths.glob("//mnt", "**/*.mp3");
        //Log.v("lector00","Archivos encontrados : "+ Long.toString(paths.getFiles().size()));

        //mGestureDetector = new GestureDetector(this, new GestureListener());
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
        int i=0;

        /*  runOnUiThread(new Runnable() {
            @Override
            public void run() {
                app.getTtsManager().addBooks(files, listaHorizontal);
            }
        });
        */
        app.getTtsManager().addBooks(files, listaHorizontal);

    }

    @Override
    protected void onActivityResult(final int requestCode, final int resultCode, final Intent data) {
        switch (requestCode) {
            case ACTION_VALUE:
                if (!app.getFiles().isEmpty()) {
                    app.getTtsManager().addBooks(app.getFiles(), listaHorizontal);
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

    public void animation(){
        final int amountToMoveRight = 100;
        TranslateAnimation anim = new TranslateAnimation(0, amountToMoveRight, 0, 0);
        anim.setDuration(1000);

        anim.setAnimationListener(new TranslateAnimation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) { }

            @Override
            public void onAnimationRepeat(Animation animation) { }

            @Override
            public void onAnimationEnd(Animation animation)
            {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)listaHorizontal.getLayoutParams();
                //params.topMargin += amountToMoveDown;
                params.leftMargin += amountToMoveRight;
                listaHorizontal.setLayoutParams(params);
            }
        });

        listaHorizontal.startAnimation(anim);

    /*    FrameLayout.LayoutParams params = (FrameLayout.LayoutParams)listaHorizontal.getLayoutParams();
        //params.topMargin += amountToMoveDown;
        params.leftMargin += amountToMoveRight;
        listaHorizontal.setLayoutParams(params);
*/

    }

}
