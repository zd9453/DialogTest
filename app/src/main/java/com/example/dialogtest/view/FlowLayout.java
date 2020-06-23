package com.example.dialogtest.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.dialogtest.BuildConfig;

import java.util.ArrayList;
import java.util.List;

/**
 * describe: 流式布局
 *
 * @author dong on 2020/4/21
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class FlowLayout extends ViewGroup {

    private int horizontalSpace;        //子view之间水平间隔
    private int verticalSpace;          //子view之间垂直间隔
    private List<List<View>> allViews;  //缓存所有childView
    private List<Integer> allLineHeights;//所有行的宽度

    {
        horizontalSpace = dp2Px(15);
        verticalSpace = dp2Px(10);
        allViews = new ArrayList<>();
        allLineHeights = new ArrayList<>();
    }

    private int dp2Px(int dpValue) {
        return Math.round(getContext().getResources().getDisplayMetrics().density * dpValue);
    }

    public FlowLayout(Context context) {
        super(context);
    }

    public FlowLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        /**
         * MeasureSpec 32位二进制int值
         * .    高2为包含测量模式信息
         *          00 (UNSPECIFIED = 0 << 30) 不限制
         *          01 (EXACTLY = 1 << 30 )    确定的具体数值
         *          10 (AT_MOST = 2 << 30 )    最多不超过多少
         * .    低30位包含测量尺寸的具体值
         *
         * 自定义view的Measure在于测量出view占用的空间大小，是layout 和 draw 的前提
         */

        //容器的padding
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int parentW = MeasureSpec.getSize(widthMeasureSpec);
        int parentH = MeasureSpec.getSize(heightMeasureSpec);

        if (BuildConfig.DEBUG)
            Log.e(">>>", String.format("onMeasure: parent paddingLeft:%d paddingRight:%d " +
                            "paddingTop:%d paddingBottom:%d parentW:%d parentH:%d",
                    paddingLeft, paddingRight, paddingTop, paddingBottom, parentW, parentH));

        int parentNeedW = paddingLeft + paddingRight;   //装下一行需要的宽度
        int parentNeedH = paddingTop + paddingBottom;   //装下一行需要的高度
        int oneLineUseW = paddingLeft + paddingRight;   //当前行内容已使用的宽
        int oneLineUseH = 0;                            //当前行内容已使用的高

        allViews.clear();
        allLineHeights.clear();

        List<View> oneLineViews = null;

        for (int i = 0, childCount = getChildCount(); i < childCount; i++) {
            //循环测量每个子view
            View childView = getChildAt(i);

            if (childView.getVisibility() == GONE)
                continue;

            if (null == oneLineViews)
                oneLineViews = new ArrayList<>();

            LayoutParams childParams = childView.getLayoutParams();

            int childWidthMeasureSpec = getChildMeasureSpec(widthMeasureSpec,
                    paddingLeft + paddingRight, childParams.width);
            int childHeightMeasureSpec = getChildMeasureSpec(heightMeasureSpec,
                    paddingLeft + paddingRight, childParams.height);

            childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);

            int measuredWidth = childView.getMeasuredWidth();
            int measuredHeight = childView.getMeasuredHeight();

            Log.e(">>>", "onMeasure: child " + measuredWidth + " " + measuredHeight);

            //该行添加一个view之后使用的宽度 oneLineUseW + measuredWidth
            if ((!oneLineViews.isEmpty()) && ((oneLineUseW + measuredWidth) > parentW
                    || (oneLineUseW + measuredWidth + horizontalSpace) > parentW)) {
                //需要换行了
                allViews.add(oneLineViews);
                allLineHeights.add(oneLineUseH);

                //需要的行高加
                parentNeedH += oneLineUseH + verticalSpace;

                parentNeedW = Math.max(parentNeedW, oneLineUseW - horizontalSpace);

                Log.e(">>>", "onMeasure: changeLine " + parentNeedW + "  " + parentNeedH);

                oneLineViews = new ArrayList<>();
                oneLineUseW = paddingLeft + paddingRight;
                oneLineUseH = 0;
            }

            oneLineViews.add(childView);
            oneLineUseW = oneLineUseW + (measuredWidth + horizontalSpace);
            oneLineUseH = Math.max(oneLineUseH, measuredHeight);

            if (i == childCount - 1) {
                //最后一个view了,添加最后一行缓存信息
                allViews.add(oneLineViews);
                allLineHeights.add(oneLineUseH);
                parentNeedW = Math.max(parentNeedW, oneLineUseW - horizontalSpace);
                parentNeedH += oneLineUseH;
            }
        }

        //测量父布局
        int realW = 0;
        int realH = 0;

        int modeW = MeasureSpec.getMode(widthMeasureSpec);
        int modeH = MeasureSpec.getMode(heightMeasureSpec);

        if (modeW == MeasureSpec.EXACTLY) {
            realW = parentW;
        } else if (modeW == MeasureSpec.UNSPECIFIED) {
            realW = parentW;
        } else if (modeW == MeasureSpec.AT_MOST) {
            realW = parentNeedW;
        }

        if (modeH == MeasureSpec.EXACTLY) {
            realH = parentH;
        } else if (modeH == MeasureSpec.UNSPECIFIED) {
            realH = parentW;
        } else if (modeH == MeasureSpec.AT_MOST) {
            realH = parentNeedH;
        }

        setMeasuredDimension(realW, realH);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int lines = allViews.size();

        int useW = getPaddingLeft();
        int uswH = getPaddingTop();

        for (int i = 0; i < lines; i++) {
            List<View> oneLine = allViews.get(i);
            int lineH = allLineHeights.get(i);
            for (int j = 0; j < oneLine.size(); j++) {
                View view = oneLine.get(j);

                //每行内容上下居中
                view.layout(useW,
                        ((lineH - view.getMeasuredHeight()) >> 1) + uswH,
                        useW + view.getMeasuredWidth(),
                        ((lineH - view.getMeasuredHeight()) >> 1) + uswH + view.getMeasuredHeight());

                useW += view.getMeasuredWidth() + horizontalSpace;
            }
            useW = getPaddingLeft();
            uswH += (lineH + verticalSpace);
        }
    }
}
