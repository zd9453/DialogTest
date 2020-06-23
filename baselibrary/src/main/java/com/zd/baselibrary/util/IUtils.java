package com.zd.baselibrary.util;

/**
 * Package: com.zd.baselibrary.utils
 * <p>
 * describe: 工具类
 *
 * @author zhangdong on 2020/6/23
 * @version 1.0
 * @see .
 * @since 1.0
 */
public interface IUtils {

    void release();

    /**
     * dp转px
     *
     * @param dpValue dp值
     * @return 对应的px值
     */
    float dp2px(float dpValue);

    /**
     * 获取屏幕宽高尺寸
     *
     * @return Int[2] --> int[0]=width,int[1]=height
     */
    int[] displaySize();
}
