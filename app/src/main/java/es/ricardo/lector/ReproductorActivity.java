package es.ricardo.lector;

import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.View;
import android.view.MotionEvent;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class ReproductorActivity extends AppCompatActivity {

    MediaPlayer player = new MediaPlayer();

    static File ficheroEscogido;
    private GestureDetector mGestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reproductor);

    //    final Button boton = (Button) findViewById(R.id.botonReproductor);


        try {
            Intent intent = getIntent();
            ficheroEscogido = (File)intent.getExtras().get("ficheroEscogido");

            mGestureDetector = new GestureDetector(this, new GestureListener());
            //boton.setText(ficheroEscogido.getName());

            player.setDataSource(ficheroEscogido.getPath());
            //player.setDataSource(ttsManager.actualFile.getPath());
            player.prepare();
            player.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

  /*     boton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                    //player = MediaPlayer.create(this, Uri.parse(ttsManager.actualFile.getPath().toString()));

                }

        });
*/
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
        }
   //     if (eventConsumed)
   //     {
   //         Toast.makeText(this,GestureListener.currentGestureDetected,Toast.LENGTH_LONG).show();
            return true;
   //     }
   //     else
   //         return false;
    }


    private void volver(){
        if(player!=null && player.isPlaying()){
            player.stop();
        }
        setResult(RESULT_OK);
        finish();
    }

    @Override
    protected void onStop(){
        super.onStop();

        volver();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();

        if (player!=null)
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
