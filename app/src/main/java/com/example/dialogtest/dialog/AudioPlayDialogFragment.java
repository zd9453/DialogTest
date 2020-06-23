package com.example.dialogtest.dialog;

import android.view.View;
import android.view.Window;

import com.example.dialogtest.R;

/**
 * Package: com.example.dialogtest.dialog
 * <p>
 * describe:音频播放弹框
 *
 * @author zhangdong on 2019/12/12
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class AudioPlayDialogFragment extends BaseDialogFragment {

    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_fragment_2;
    }

    @Override
    protected void reSetShowSize(Window window) {

    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected boolean setBottom() {
        return false;
    }


}
