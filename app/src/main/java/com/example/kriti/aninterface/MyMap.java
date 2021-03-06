 package com.example.kriti.aninterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Paint;

import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.kriti.aninterface.Utilities.Node;

import java.util.ArrayList;

/**
 * Created by harshit on 2/3/17.
 */

public class MyMap extends View {

    Context context;
    Bitmap imageBitmap;
    Paint paint;
    Paint paintCircle;
    int WIDTH, HEIGHT;
    ArrayList<Node> mapData;
    Node location;

    public void setLocation(Node location1){
        this.location = location1;
    }

    public void setMapData(ArrayList<Node> mapData){
        this.mapData = mapData;
    }

    public MyMap(Context context) {
        super(context);
        init(context);
    }

    public MyMap(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MyMap(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }


    private void init(Context context) {

        this.context = context;
        paint = new Paint();
        paint.setStyle(Paint.Style.STROKE);
        paint.setColor(ContextCompat.getColor(context, android.R.color.holo_blue_dark));
        paint.setStrokeWidth(15f);


        paintCircle = new Paint();
        paintCircle.setStyle(Paint.Style.STROKE);
        paintCircle.setColor(ContextCompat.getColor(context, android.R.color.holo_blue_bright));
        paintCircle.setStrokeWidth(5f);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        HEIGHT = MeasureSpec.getSize(heightMeasureSpec);
        new BitmapDecoder().execute();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (imageBitmap != null) {
            canvas.drawBitmap(imageBitmap, 0f, 0f, null);
        }

        if(mapData!=null){
            for(int i = 0 ; i < mapData.size()-1; ++i){
                Node n1 = mapData.get(i);
                Node n2 = mapData.get(i+1);
                canvas.drawLine(n1.getX(), n1.getY(), n2.getX(), n2.getY(), paint);
                canvas.drawCircle(n1.getX(),n1.getY(),50f,paintCircle);

            }
        }
        if (location!=null){
            canvas.drawCircle(location.getX(),location.getY(),40f,paintCircle);
            canvas.drawPoint(location.getX(),location.getY(),paint);
        }
    }


  /*  @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
*/


    class BitmapDecoder extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            imageBitmap = getBitmap();
            return null;
        }

        Bitmap getBitmap() {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.hall);
            return Bitmap.createScaledBitmap(bitmap, WIDTH, HEIGHT, true);
            //return bitmap;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MY_APP", "Drawable done");
            Log.d("MY_APP", WIDTH + " " + HEIGHT);

            invalidate();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.w("TOUCH",event.getX() + " " + event.getY());
        return super.onTouchEvent(event);
    }
}
