package com.example.dialogtest.dialog;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class AudioProgressBarView extends View {

    private int ARC_FULL_DEGREE = 270;   //进度条所占用的角度
    private int startAngle;              //扇形绘制起始角度
    private int STROKE_WIDTH;            //弧线的宽度
    private int circleRadius;            //圆弧的半径
    private int centerX, centerY;        //圆弧圆心位置

    private RectF baseArcRectF;          //默认细线圆弧的矩形

    private boolean isDragging = false;
//    private AudioProgressListener mAudioProgressListener;

    private float max, progress;//进度条最大值和当前进度值
    /**
     * 是否允许拖动进度条
     */
    private boolean draggingEnabled = false;
//    private Paint basePaint;
    /**
     * 绘制弧线的画笔
     */
    private Paint progressPaint;
    /**
     * 绘制当前进度值的画笔
     */
    private Paint thumbPaint;

    private float newProgress;

    public void setArcFullDegree(int arcFullDegree) {
        ARC_FULL_DEGREE = arcFullDegree;
        this.startAngle = 90 + ((360 - ARC_FULL_DEGREE) >> 1);
    }

    public void setSTROKE_WIDTH(int STROKE_WIDTH) {
        this.STROKE_WIDTH = STROKE_WIDTH;
    }

    public void setCenter(int centerX, int centerY, int circleRadius) {
        this.centerX = centerX;
        this.centerY = centerY;
        this.circleRadius = circleRadius;
    }

    public void setBaseArcRectF(RectF baseArcRectF) {
        this.baseArcRectF = baseArcRectF;
    }

    public AudioProgressBarView(Context context) {
        super(context);
        init();
    }


    public AudioProgressBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public AudioProgressBarView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //绘制扇形画笔
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        //绘制
        thumbPaint = new Paint();
        thumbPaint.setAntiAlias(true);
        thumbPaint.setStrokeCap(Paint.Cap.ROUND);
        thumbPaint.setStyle(Paint.Style.STROKE);
    }

    public float getProgress() {
        return progress;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(">>>>", "onSizeChanged: ------ child");
        //中心点
        centerX = w >> 1;
        centerY = h >> 1;
        //半径
        circleRadius = w >> 1;
        //根据效果图计算出外围圆弧宽度占总宽度7%
        STROKE_WIDTH = ((int) (w * 0.07));
        //进度条起始点角度
        startAngle = 90 + ((360 - ARC_FULL_DEGREE) >> 1);

        //默认细线圆弧的矩形
        baseArcRectF = new RectF(STROKE_WIDTH >> 1, STROKE_WIDTH >> 1,
                getWidth() - (STROKE_WIDTH >> 1), getHeight() - (STROKE_WIDTH >> 1));

        Shader lineShader = new LinearGradient(0, 0, w, h,
                Color.parseColor("#58D6F9"),
                Color.parseColor("#3291FF"), Shader.TileMode.CLAMP);
        thumbPaint.setShader(lineShader);

        int[] colors = new int[]{
                Color.parseColor("#A43291FF"),
                Color.parseColor("#3291FF"),
                Color.parseColor("#58D6F9"),
                Color.parseColor("#A43291FF")};
        float[] position = new float[]{0, .125f, .375f, .75f};
        Shader sweepShader = new SweepGradient(centerX, centerY, colors, position);
        progressPaint.setShader(sweepShader);
    }

    /***
     * @param sweep 扫过的角度
     * @return 转换后的弧度
     */
    private double getRadius(float sweep) {
        return Math.toRadians(startAngle + sweep);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        float sweepNow = ARC_FULL_DEGREE * (progress / max); //进度划过的角度
        if (sweepNow <= 0)
            return;

        canvas.drawArc(baseArcRectF, startAngle, sweepNow, true, progressPaint);

        thumbPaint.setStrokeWidth(STROKE_WIDTH);
        canvas.drawArc(baseArcRectF, startAngle, sweepNow, false, thumbPaint);

//        double radius = getRadius(sweepNow);
//        float nowX = ((float) (centerX + (circleRadius - (STROKE_WIDTH >> 1)) * Math.cos(radius)));
//        float nowY = ((float) (centerY + (circleRadius - (STROKE_WIDTH >> 1)) * Math.sin(radius)));
//        basePaint.setColor(Color.WHITE);
//        canvas.drawCircle(nowX, nowY, STROKE_WIDTH >> 1, basePaint);
    }


//    public void setAudioProgressListener(AudioProgressListener mAudioProgressListener) {
//        this.mAudioProgressListener = mAudioProgressListener;
//    }

//    @Override
//    public boolean onTouchEvent(@NonNull MotionEvent event) {
//        if (!draggingEnabled) {
//            return super.onTouchEvent(event);
//        }
//
//        //处理拖动事件
//        float currentX = event.getX();
//        float currentY = event.getY();
//
//        int action = event.getAction();
//        switch (action) {
//            case MotionEvent.ACTION_DOWN:
//                //判断是否在进度条thumb位置
//                if (checkOnArc(currentX, currentY)) {
//                    newProgress = calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max;
//                    setProgress(newProgress);
//                    isDragging = true;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//                if (isDragging) {
//                    //判断拖动时是否移出去了
//                    if (checkOnArc(currentX, currentY)) {
//                        setProgress(calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max);
//                    } else {
//                        isDragging = false;
//                    }
//                }
//                break;
//            case MotionEvent.ACTION_UP:
//                isDragging = false;
//                if (null != mAudioProgressListener) {
//                    mAudioProgressListener.progress((int) newProgress);
//                }
//                break;
//        }
//        return true;
//    }


    private float calDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt((x1 - x2) * (x1 - x2) + (y1 - y2) * (y1 - y2));
    }

    /**
     * 判断该点是否在弧线上（附近）
     */
    private boolean checkOnArc(float currentX, float currentY) {
        float distance = calDistance(currentX, currentY, centerX, centerY);
        float degree = calDegreeByPosition(currentX, currentY);
        return distance > circleRadius - STROKE_WIDTH * 5 && distance < circleRadius + STROKE_WIDTH * 5
                && (degree >= -8 && degree <= ARC_FULL_DEGREE + 8);
    }

    /**
     * 根据当前位置，计算出进度条已经转过的角度。
     */
    private float calDegreeByPosition(float currentX, float currentY) {
        float a1 = (float) (Math.atan(1.0f * (centerX - currentX) / (currentY - centerY)) / Math.PI * 180);
        if (currentY < centerY) {
            a1 += 180;
        } else if (currentY > centerY && currentX > centerX) {
            a1 += 360;
        }
        return a1 - ((360 - ARC_FULL_DEGREE) >> 1);
    }


    public void setMax(float max) {
        this.max = max;
        invalidate();
    }


    public void setProgress(float progress) {
        this.progress = checkProgress(progress);
        postInvalidate();
    }

    //保证progress的值位于[0,max]
    private float checkProgress(float progress) {
        if (progress < 0) {
            return 0;
        }
        return progress > max ? max : progress;
    }

    public void setDraggingEnabled(boolean draggingEnabled) {
        this.draggingEnabled = draggingEnabled;
    }
}