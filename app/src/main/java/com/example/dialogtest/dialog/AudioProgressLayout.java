package com.example.dialogtest.dialog;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.dialogtest.BuildConfig;

/**
 * Package: com.wzyk.screen4k.view.museum
 * <p>
 * describe:音频播放进度的布局
 *
 * @author zhangdong on 2020/4/16
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class AudioProgressLayout extends FrameLayout {

    private Paint basePaint;
    private int ARC_FULL_DEGREE = 270;          //进度条所占用的角度
    private int startAngle;                     //扇形绘制起始角度
    private int STROKE_WIDTH;                   //弧线的宽度
    private int circleRadius;                   //圆弧的半径
    private int centerX, centerY;               //圆弧圆心位置
    private String bgColor = "#10000000";       //底部背景色默认25%透明色

    private RectF baseArcRectF;                 //默认细线圆弧的矩形
    private float startX, startY, endX, endY;   //起始点和结束点蓝色圆的坐标值
    private AudioProgressBarView progressBarView;

    public AudioProgressLayout(@NonNull Context context) {
        super(context);
        init();
    }

    public AudioProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public AudioProgressLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        //基础画笔
        basePaint = new Paint();

        progressBarView = new AudioProgressBarView(getContext());
        progressBarView.setBackgroundResource(0);
        addView(progressBarView, LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Log.e(">>>>", "onSizeChanged: ------ parent");
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
        startX = ((float) (centerX + circleRadius * Math.cos(getRadius(0))));
        startY = ((float) (centerY + circleRadius * Math.sin(getRadius(0))));
        endX = ((float) (centerX + circleRadius * Math.cos(getRadius(ARC_FULL_DEGREE))));
        endY = ((float) (centerY + circleRadius * Math.sin(getRadius(ARC_FULL_DEGREE))));

        //设置内部扫描view的相关参数
        progressBarView.setArcFullDegree(ARC_FULL_DEGREE);
        progressBarView.setSTROKE_WIDTH(STROKE_WIDTH);
        progressBarView.setCenter(centerX, centerY, circleRadius);
        progressBarView.setBaseArcRectF(baseArcRectF);
        progressBarView.setMax(20);
        progressBarView.setProgress(0);
    }

    /***
     * @param sweep 扫过的角度
     * @return 转换后的弧度
     */
    private double getRadius(float sweep) {
        return Math.toRadians(startAngle + sweep);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        //默认浅色背景、细圆弧、两端小点绘制
        drawBaseView(canvas);
        super.dispatchDraw(canvas);
    }

    /***
     * 绘制基础view
     * @param canvas .
     */
    private void drawBaseView(Canvas canvas) {
        if (BuildConfig.DEBUG)
            Log.e(">>>>", "AudioProgressLayout : ----------- dispatchDraw -> drawBaseView");
        //绘制底层背景
        basePaint.reset();
        basePaint.setAntiAlias(true);
        basePaint.setColor(Color.parseColor(bgColor));
        canvas.drawCircle(centerX, centerY, circleRadius, basePaint);

        //绘制一圈默认细线的圆弧
        basePaint.reset();
        basePaint.setAntiAlias(true);
        basePaint.setStrokeWidth(STROKE_WIDTH >> 3);
        basePaint.setStyle(Paint.Style.STROKE);
        Shader lineShader = new LinearGradient(0, 0, getWidth(), getHeight(),
                Color.parseColor("#58D6F9"),
                Color.parseColor("#3291FF"), Shader.TileMode.CLAMP);
        basePaint.setShader(lineShader);
        canvas.drawArc(baseArcRectF, startAngle, ARC_FULL_DEGREE, false, basePaint);

        //绘制起始位置的大蓝色点
        basePaint.reset();
        basePaint.setAntiAlias(true);
        basePaint.setStrokeWidth(0);
        basePaint.setColor(Color.parseColor("#3392FF"));
        canvas.drawCircle(startX, startY, STROKE_WIDTH >> 2, basePaint);
        //绘制结束位置的大蓝色点
        canvas.drawCircle(endX, endY, STROKE_WIDTH >> 2, basePaint);
    }

    public void changeState(float progress) {
        if (null != progressBarView)
            progressBarView.setProgress(progress);
    }

    public float getProgress() {
        if (null == progressBarView)
            return 0;
        return progressBarView.getProgress();
    }


    public void setMaxProgress(float max) {
        if (null == progressBarView)
            return;
        progressBarView.setMax(max);
    }
}
