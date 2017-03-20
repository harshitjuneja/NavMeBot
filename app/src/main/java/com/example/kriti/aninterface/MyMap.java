package com.example.kriti.aninterface;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * Created by harshit on 2/3/17.
 */

public class MyMap extends View {

    Context context;
    Bitmap imageBitmap;
    Paint paint;
    int WIDTH, HEIGHT;

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

    private void init(Context context){

        this.context = context;
        paint = new Paint();
        paint.setColor(ContextCompat.getColor(context, android.R.color.black));


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        WIDTH = MeasureSpec.getSize(widthMeasureSpec);
        HEIGHT = MeasureSpec.getSize(heightMeasureSpec);
        new BitmapDecoder().execute();
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        if(imageBitmap!=null){
            canvas.drawBitmap(imageBitmap,0f,0f,null);
        }
    }

    class BitmapDecoder extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            imageBitmap = getBitmap();
            return null;
        }

        Bitmap getBitmap(){
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.drawable.collegep);
            return Bitmap.createScaledBitmap(bitmap,WIDTH,HEIGHT,true);
            //return bitmap;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("MY_APP","Drawable done");
            Log.d("MY_APP", WIDTH + " " + HEIGHT);

            invalidate();
        }
    }
}
