package es.ricardo.servicio;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.widget.Toast;

import es.ricardo.lector.IntroActivity;
import es.ricardo.lector.ReproductorActivity;


public class BackgroundService extends Service implements es.ricardo.servicio.Shake.Listener{
    @Override
    public void onCreate()
    {
        super.onCreate();

        SensorManager sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        Shake sd = new Shake(this);

        //Toast.makeText(this, "Servicio creado", Toast.LENGTH_LONG).show();

        sd.start(sensorManager);
    }

    @Override
    public void hearShake()
    {
        Toast.makeText(getApplicationContext(),"Shaked",Toast.LENGTH_LONG).show();

        Intent notificationIntent = new Intent(this, ReproductorActivity.class);
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
