package com.example.treciasdarbas;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;

public class CanvasView extends View {
    Context context;
    private Bitmap mBitmap;
    private Canvas mCanvas;
    private int currentColor;

    private int mPenColor = Color.WHITE;
    private float mX, mY;
    private static final float TOLERANCE = 5;

    private Path mPath;
    private Paint mPaint;
    private ArrayList<Path> mPaths;
    private ArrayList<Paint> mPaints;

    private boolean canDraw = false;


    public CanvasView(Context context, AttributeSet attributeSet){
        super(context, attributeSet);
        this.context = context;

        this.mPaths = new ArrayList<Path>();
        this.mPaints = new ArrayList<Paint>();
        addPath();
    }

    public void setCanDraw(boolean canDraw){
        this.canDraw = canDraw;
    }
    public void setmPenColor(int penColor){
        this.mPenColor = penColor;
    }


    public void addPath(){
        mPath = new Path();
        mPaths.add(mPath);
        mPaint = new Paint();
        mPaints.add(mPaint);
        mPaint.setAntiAlias(true);
        mPaint.setColor(mPenColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
        mPaint.setStrokeWidth(10f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh){
        super.onSizeChanged(w, h, oldw, oldh);

        mBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(mBitmap);
    }

    @Override
    protected void onDraw(Canvas canvas){
        super.onDraw(canvas);
        for (int i = 0; i < mPaths.size(); i++){
            canvas.drawPath(mPaths.get(i), mPaints.get(i));
        }
        drawText(canvas);
    }

    private void drawText(Canvas canvas){
        int x = 0;
        int y = 0;
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        canvas.translate(100, 200);
        paint.setTextSize(20);
        paint.setStyle(Paint.Style.FILL);
        paint.setAntiAlias(true);
    }

    private void startTouch(float x, float y){
        mPath.moveTo(x, y);
        mX = x;
        mY = y;
    }

    private void moveTouch(float x, float y){
        float dx = Math.abs(x - mX);
        float dy = Math.abs(y - mY);
        if(dx >= TOLERANCE || dy >= TOLERANCE){
            mPath.quadTo(mX, mY, (x + mX) / 2, (y + mY) / 2);
            mX = x;
            mY = y;
        }
    }

    public void clearCanvas(){
        mPaths.clear();
        mPaints.clear();
        mPath.reset();
        invalidate();
    }
    public void setColor (int color) {

        currentColor = color;

    }

    private void upTouch(){
        mPath.lineTo(mX, mY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        if(canDraw){
            float x = event.getX();
            float y = event.getY();

            switch (event.getAction()){
                case MotionEvent.ACTION_DOWN:
                    this.addPath();
                    startTouch(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_MOVE:
                    moveTouch(x, y);
                    invalidate();
                    break;
                case MotionEvent.ACTION_UP:
                    upTouch();
                    invalidate();
                    break;
            }
        }
        return true;
    }
}
