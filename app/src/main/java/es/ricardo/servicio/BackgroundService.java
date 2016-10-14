package es.ricardo.servicio;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import es.ricardo.lector.IntroActivity;
import es.ricardo.lector.Lector;
import es.ricardo.lector.ReproductorActivity;


public class BackgroundService extends Service implements es.ricardo.servicio.Shake.Listener{
    Shake sd = null;

    @Override
    public void onCreate()
    {
        super.onCreate();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sd = new Shake(this);

        //Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();

        sd.start(sensorManager);
    }

    @Override
    public void hearShake()
    {
        sd.stop();

        Toast.makeText(getApplicationContext(),"Shaked",Toast.LENGTH_LONG).show();

        Lector app = (Lector) getApplicationContext();

        Intent notificationIntent = new Intent(this, app.getCurrentActivity().getClass());
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(notificationIntent);
    }

    public void onSensorChanged(SensorEvent event) {
        // TODO Auto-generated method stub

    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        // TODO Auto-generated method stub

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

}
