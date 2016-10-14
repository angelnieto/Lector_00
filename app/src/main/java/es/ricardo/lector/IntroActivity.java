package es.ricardo.lector;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.LinkedList;

import es.ricardo.servicio.BackgroundService;


public class IntroActivity extends AppCompatActivity implements MediaPlayer.OnCompletionListener {

    private MediaPlayer player = null;
    private TareaAsincrona tareaAsincrona=null;
    ListadoActivity activity = new ListadoActivity();
    private Intent servicio = null;
    private Lector app = null;

    static final int ACTION_VALUE=1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        if(app == null){
            app = (Lector)this.getApplicationContext();
        }
        app.setCurrentActivity(this);

        //Paro el servicio de escucha para "meneos"
        if(servicio == null) {
            servicio = new Intent(getApplicationContext(), BackgroundService.class);
        }

        app.setFiles(new LinkedList());
        app.getTtsManager().stop();

        tareaAsincrona=new TareaAsincrona(this);
        tareaAsincrona.execute();

        player = MediaPlayer.create(this.getApplicationContext(), R.raw.intro);
        player.setOnCompletionListener(this);
        player.start();

    }

    @Override
    protected void onResume(){
        super.onResume();

        getApplicationContext().stopService(servicio);
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
    public void onCompletion(MediaPlayer mp) {
        Intent i = new Intent(this,ListadoActivity.class);
        startActivityForResult(i, ACTION_VALUE);
    }

    @Override
    public void onBackPressed() {
        Log.v("lector00","Back Button pressed");
    }
}
