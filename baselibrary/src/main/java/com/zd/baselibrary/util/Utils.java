package com.zd.baselibrary.util;

import android.content.Context;

/**
 * Package: com.zd.baselibrary
 * <p>
 * describe: 在application中初始化
 *
 * @author zhangdong on 2020/6/23
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class Utils implements IUtils {

    private volatile static Utils utils;
    private volatile static Context context;

    private Utils(Context context) {
        Utils.context = context;
    }

    public static Utils getInstance() {
        if (null == utils)
            throw new NullPointerException("Utils need init on application");
        return utils;
    }

    public static void init(Context context) {
        if (null == context || utils != null)
            return;
        utils = new Utils(context.getApplicationContext());
    }

    @Override
    public void release() {
        if (null != context)
            context = null;
        if (null != utils)
            utils = null;
    }

    @Override
    public float dp2px(float dpValue) {
        return context.getResources().getDisplayMetrics().density * dpValue;
    }

    @Override
    public int[] displaySize() {
        int[] size = new int[2];
        size[0] = context.getResources().getDisplayMetrics().widthPixels;
        size[1] = context.getResources().getDisplayMetrics().heightPixels;
        return size;
    }


}
