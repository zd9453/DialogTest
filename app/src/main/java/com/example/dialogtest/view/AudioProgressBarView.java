package com.example.dialogtest.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class AudioProgressBarView extends View {

    private int ARC_FULL_DEGREE = 270;   //进度条所占用的角度
    private int startAngle;              //扇形绘制起始角度
    private int STROKE_WIDTH;            //弧线的宽度
    private int circleRadius;            //圆弧的半径
    private int centerX, centerY;        //圆弧圆心位置
    private RectF baseArcRectF;          //默认细线圆弧的矩形
    private Paint basePaint;             //绘制最前端的白色圆点画笔
    private Paint progressPaint;         //绘制扫过扇形的画笔
    private Paint thumbPaint;            //绘制扫过粗弧线的画笔
    private float max, progress;         //进度条最大值和当前进度值

    private AudioProgressListener mAudioProgressListener;
    private boolean isDragging = false;

    /**
     * 是否允许拖动进度条
     */
    private boolean draggingEnabled = false;
    private float newProgress;

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
        //关闭硬件加速
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        //绘制点
        basePaint = new Paint();
        basePaint.setAntiAlias(true);
        basePaint.setStyle(Paint.Style.FILL);
        //绘制扇形
        progressPaint = new Paint();
        progressPaint.setAntiAlias(true);
        progressPaint.setStyle(Paint.Style.FILL);
        progressPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OVER));
        //绘制弧线
        thumbPaint = new Paint();
        thumbPaint.setAntiAlias(true);
        thumbPaint.setStrokeCap(Paint.Cap.ROUND);
        thumbPaint.setStyle(Paint.Style.STROKE);
    }

    public float getProgress() {
        return progress;
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //中心点
        centerX = w >> 1;
        centerY = h >> 1;
        //根据效果图计算出外围圆弧宽度占总宽度6%
        STROKE_WIDTH = ((int) (w * 0.06));
        //半径
        circleRadius = ((Math.min(w, h)) >> 1) - (STROKE_WIDTH >> 1);
        //进度条起始点角度
        startAngle = 90 + ((360 - ARC_FULL_DEGREE) >> 1);

        //默认细线圆弧的矩形
        baseArcRectF = new RectF(STROKE_WIDTH >> 1, STROKE_WIDTH >> 1,
                w - (STROKE_WIDTH >> 1), h - (STROKE_WIDTH >> 1));

        //起始点和结束点坐标
        float startX = ((float) (centerX + circleRadius * Math.cos(getRadius(0))));
        float startY = ((float) (centerY + circleRadius * Math.sin(getRadius(0))));
        float endX = ((float) (centerX + circleRadius * Math.cos(getRadius(ARC_FULL_DEGREE))));
        float endY = ((float) (centerY + circleRadius * Math.sin(getRadius(ARC_FULL_DEGREE))));

        Shader lineShader = new LinearGradient(0, 0, w, h,
                Color.parseColor("#58D6F9"),
                Color.parseColor("#3291FF"), Shader.TileMode.CLAMP);
        thumbPaint.setShader(lineShader);

        int[] colors = new int[]{
                Color.parseColor("#303291FF"),
                Color.parseColor("#603291FF"),
                Color.parseColor("#2058D6F9"),
                Color.parseColor("#303291FF")};
        float[] position = new float[]{0, .125f, .375f, .75f};
        Shader sweepShader = new SweepGradient(centerX, centerY, colors, position);
        progressPaint.setShader(sweepShader);

        //底部背景色
        Bitmap bgBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_4444);
        Canvas canvas = new Canvas(bgBitmap);
        drawBaseView(canvas, startX, startY, endX, endY);
        setBackground(new BitmapDrawable(null, bgBitmap));
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
        //进度划过的角度
        float sweepNow = ARC_FULL_DEGREE * (progress / max);
        if (sweepNow <= 0)
            return;
        //扫过的扇形
        canvas.drawArc(baseArcRectF, startAngle, sweepNow, true, progressPaint);
        //扫过的粗弧形
        thumbPaint.setStrokeWidth(STROKE_WIDTH);
        canvas.drawArc(baseArcRectF, startAngle, sweepNow, false, thumbPaint);
        //跑在最前面的白圆点
        double radius = getRadius(sweepNow);
        float nowX = ((float) (centerX + circleRadius * Math.cos(radius)));
        float nowY = ((float) (centerY + circleRadius * Math.sin(radius)));
        basePaint.setColor(Color.WHITE);
        canvas.drawCircle(nowX, nowY, STROKE_WIDTH >> 1, basePaint);
    }

    /***
     * 绘制基础view 画成背景设置给view
     * @param canvas .
     * @param startX 起点蓝色小点x坐标
     * @param startY 起点蓝色小点y坐标
     * @param endX 终点蓝色小点x坐标
     * @param endY 终点蓝色小点y坐标
     */
    private void drawBaseView(Canvas canvas, float startX, float startY, float endX, float endY) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
        //绘制底层背景
        basePaint.setColor(Color.argb(5, 0, 0, 0));
        canvas.drawCircle(centerX, centerY, circleRadius, basePaint);

        //绘制一圈默认细线的圆弧
        thumbPaint.setStrokeWidth(STROKE_WIDTH >> 3);
        canvas.drawArc(baseArcRectF, startAngle, ARC_FULL_DEGREE, false, thumbPaint);

        //绘制起始位置的大蓝色点
        basePaint.setColor(Color.parseColor("#3392FF"));
        canvas.drawCircle(startX, startY, STROKE_WIDTH >> 2, basePaint);
        //绘制结束位置的大蓝色点
        canvas.drawCircle(endX, endY, STROKE_WIDTH >> 2, basePaint);
    }

    public void setAudioProgressListener(AudioProgressListener mAudioProgressListener) {
        this.mAudioProgressListener = mAudioProgressListener;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (!draggingEnabled) {
            return super.onTouchEvent(event);
        }

        //处理拖动事件
        float currentX = event.getX();
        float currentY = event.getY();

        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                //判断是否在进度条thumb位置
                if (checkOnArc(currentX, currentY)) {
                    newProgress = calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max;
                    setProgress(newProgress);
                    isDragging = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (isDragging) {
                    //判断拖动时是否移出去了
                    if (checkOnArc(currentX, currentY)) {
                        setProgress(calDegreeByPosition(currentX, currentY) / ARC_FULL_DEGREE * max);
                    } else {
                        isDragging = false;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                isDragging = false;
                if (null != mAudioProgressListener) {
                    mAudioProgressListener.progress((int) newProgress);
                }
                break;
        }
        return true;
    }

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
        invalidate();
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

    public interface AudioProgressListener {

        void progress(int nowProgress);

    }
}