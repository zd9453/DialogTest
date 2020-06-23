package com.example.dialogtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

/**
 * Package: com.example.dialogtest.view
 * <p>
 * describe:
 *
 * @author zhangdong on 2020/4/22
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class TestView extends View {
    public TestView(Context context) {
        super(context);
    }

    public TestView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int sizeW = MeasureSpec.getSize(widthMeasureSpec);
        int sizeH = MeasureSpec.getSize(heightMeasureSpec);

        Log.e("TestView", "onMeasure: " + sizeW + "  " + sizeH);

        //强制宽高相等
        setMeasuredDimension(Math.min(sizeW, sizeH), Math.min(sizeW, sizeH));
    }
}
