package es.ricardo.lector;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.PowerManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.List;

import es.ricardo.servicio.BackgroundService;

public class ReproductorActivity extends AppCompatActivity {

    private MediaPlayer player;
    private GestureDetector mGestureDetector;
    private Lector app = null;
    private Intent servicio = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

        if(app == null) {
            app = (Lector) this.getApplicationContext();
        }
        app.setCurrentActivity(this);

        //Paro el servicio de escucha para "meneos"
        if(servicio == null) {
            servicio = new Intent(getApplicationContext(), BackgroundService.class);
        }

        try {
            mGestureDetector = new GestureDetector(this, new GestureListener());

            mostrarCapitulo();

            player = new MediaPlayer();
            player.setDataSource(app.getCapituloActual().getPath());
            player.prepare();
            player.seekTo(app.getPosicionActual());
            player.start();


        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onResume(){
        super.onResume();

        getApplicationContext().stopService(servicio);
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
                    if (player.isPlaying()) {
                        player.pause();
                        showToast("Pause");
                    } else {
                        player.start();
                        showToast("Play");
                    }
                    break;
                case Lector.DOUBLE_TAP:
                    volver();
                    break;
                case Lector.SWIPE_TO_LEFT:
                    showToast("Fast Forward");
                    player.seekTo(player.getCurrentPosition() + 2000);
                    break;
                case Lector.SWIPE_TO_RIGHT:
                    showToast("Rewind");
                    player.seekTo(player.getCurrentPosition() - 2000);
                    break;
            }
        }else if(GestureListener.currentGestureDetected == Lector.LONG_PRESS && GestureListener.longPressed){
            GestureListener.longPressed = false;

            float coordenada = event.getX();

            WindowManager windowManager =  (WindowManager) getSystemService(WINDOW_SERVICE);
            Rect pantalla=new Rect();
            windowManager.getDefaultDisplay().getRectSize(pantalla);

            try {
                if(coordenada < pantalla.width()/2) {
                    app.setCapituloActual(app.getPrevious(app.getCapituloActual()));
                    showToast("Previous");
                }else{
                    app.setCapituloActual(app.getNext(app.getCapituloActual()));
                    showToast("Next");
                }
                player.reset();
                player.setDataSource(app.getCapituloActual().getPath());
                player.prepare();
                player.start();
                //reseteo la variable currentPosition del delegado
                app.setPosicionActual(0);
            } catch (IOException e) {
                e.printStackTrace();
            }

            mostrarCapitulo();
        }

        return true;
    }

    private void mostrarCapitulo() {
        String fileName = app.getCapituloActual().getName().substring(0, app.getCapituloActual().getName().lastIndexOf("."));
        ((TextView)findViewById(R.id.capitulo)).setText(fileName);
    }


    private void volver(){

       // this.stopService(servicio);

        if(player!=null && player.isPlaying()){
            player.stop();
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();

        PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        if(pm.isScreenOn()) {
            player.stop();
        }
    }

    @Override
    protected void onPause(){
        super.onPause();

        //Lanzo el servicio de escucha para "meneos"
        if(this == app.getCurrentActivity() && app.isHomeButtonPressed()) {
            getApplicationContext().startService(servicio);
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        app.setPosicionActual(player.getCurrentPosition());
        player.release();
    }

    public void showToast(final String toast) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), toast, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
