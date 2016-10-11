package es.ricardo.lector;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import static java.security.AccessController.getContext;

/**
 * Created by Aarón Formación y Co on 03/10/2016.
 */

class GestureListener extends GestureDetector.SimpleOnGestureListener
{

    static int currentGestureDetected;
    static boolean longPressed = false;
    private static final int SWIPE_MIN_DISTANCE = 80;
    private static final int SWIPE_THRESHOLD_VELOCITY = 50;



    // Override s all the callback methods of GestureDetector.SimpleOnGestureListener
    @Override
    public boolean onSingleTapUp(MotionEvent ev) {
        currentGestureDetected = Lector.SINGLE_TAP;
        return true;
    }

    @Override
    public boolean onDoubleTap(MotionEvent ev){
        currentGestureDetected = Lector.DOUBLE_TAP;
        return true;
    }

    @Override
    public void onLongPress(MotionEvent ev) {
        currentGestureDetected = Lector.LONG_PRESS;
        longPressed = true;
    }

  /*  @Override
    public void onShowPress(MotionEvent ev) {
        currentGestureDetected=ev.toString();

    }
    @Override
    public void onLongPress(MotionEvent ev) {
        currentGestureDetected=ev.toString();

    }
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        currentGestureDetected=e1.toString()+ "  "+e2.toString();

        return true;
    }
    @Override
    public boolean onDown(MotionEvent ev) {
        currentGestureDetected=ev.toString();

        return true;
    }
    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        currentGestureDetected=e1.toString()+ "  "+e2.toString();
        return true;
    }
    */

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("SWIPE", "right to left");
            currentGestureDetected = Lector.SWIPE_TO_LEFT;
            //return true; //Right to left
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("SWIPE", "left to right");
            currentGestureDetected = Lector.SWIPE_TO_RIGHT;
            //return true; //Left to right
        }
        return true;
    }

}