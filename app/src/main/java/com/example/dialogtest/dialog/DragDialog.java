package com.example.dialogtest.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.WindowManager;
import android.widget.Scroller;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DragDialog extends Dialog implements Handler.Callback {
    private float startx, starty;
    private WindowManager mWindowManager;
    private int oldx, oldy;
    private WindowManager.LayoutParams layoutParams;
    private Scroller scroller;
    private Handler handler;
    float changeX;
    float changeY;
    private int width, height;
    private final static int SCROLLER_HANDLER = 0x711;

    public DragDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    public DragDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        init(context);
    }

    protected DragDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    private void init(Context context) {
        mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        scroller = new Scroller(context);
        handler = new Handler(this);
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public void setHeigh(int height) {
        this.height = height;
    }

    @Override
    public void dismiss() {
        scroller.abortAnimation();
        handler.removeMessages(SCROLLER_HANDLER);
        super.dismiss();
    }

    @Override
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        layoutParams = (WindowManager.LayoutParams) getWindow().getDecorView().getLayoutParams();
        if (width != 0) {
            layoutParams.width = width;
        }
        if (height != 0) {
            layoutParams.height = height;
        }
        mWindowManager.updateViewLayout(getWindow().getDecorView(), layoutParams);
        oldx = layoutParams.x;
        oldy = layoutParams.y;
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                starty = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                changeX = event.getX() - startx;
                changeY = event.getY() - starty;
                translateXY(changeX, changeY);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                stop();
                break;
            default:
        }
        return super.onTouchEvent(event);
    }


    public void stop() {
        scroller.startScroll(layoutParams.x, layoutParams.y, oldx - layoutParams.x, oldy - layoutParams.y);
        handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
    }

    private void translateXY(float x, float y) {
        scroller.startScroll(layoutParams.x, layoutParams.y, (int) x, (int) y);
        handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
    }

    @Override
    public boolean handleMessage(Message msg) {
        if (msg.what == SCROLLER_HANDLER && scroller.computeScrollOffset()) {
            layoutParams.x = scroller.getCurrX();
            layoutParams.y = scroller.getCurrY();
            mWindowManager.updateViewLayout(getWindow().getDecorView(), layoutParams);
            handler.obtainMessage(SCROLLER_HANDLER).sendToTarget();
        }
        return false;
    }
}
