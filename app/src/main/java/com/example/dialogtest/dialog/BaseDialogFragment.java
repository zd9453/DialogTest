package com.example.dialogtest.dialog;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.dialogtest.R;

/**
 * Package: com.example.dialogtest.dialog
 * <p>
 * describe:
 *
 * @author zhangdong on 2019/12/12
 * @version 1.0
 * @see .
 * @since 1.0
 */
public abstract class BaseDialogFragment extends DialogFragment {

    public Context mContext;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_FRAME, R.style.AudioPlayFragmentDialog);
    }

//    @NonNull
//    @Override
//    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
//        return new DragDialog(getActivity(), getStyle() == 0 ? R.style.BaseFragmentDialog : getStyle());
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater,
                             @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initialization();
        View view = inflater.inflate(getLayoutRes(), container);
        initView(view);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (null != getDialog() && null != getDialog().getWindow())
            reSetShowSize(getDialog().getWindow());
    }

    protected abstract int getLayoutRes();

    protected int getStyle() {
        return 0;
    }

    protected abstract void reSetShowSize(Window window);

    protected abstract void initView(View view);

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    //设置弹窗显示要求
    private void initialization() {

        WindowManager.LayoutParams params = getDialog().getWindow().getAttributes();
        if (setBottom()) {
            params.gravity = Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL;
        } else {
            params.gravity = Gravity.CENTER;
        }

        params.width = ((int) (mContext.getResources().getDisplayMetrics().widthPixels * 0.5));
        params.height = (int) (50*getResources().getDisplayMetrics().density);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setAttributes(params);
        getDialog().setCanceledOnTouchOutside(false);
        getDialog().setCancelable(true);
//        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
//            @Override
//            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
//                if (keyCode == KeyEvent.KEYCODE_BACK) {
//
//                    return ((Activity) mContext).dispatchKeyEvent(event);
//                }
//                return false;
//            }
//        });

    }

    protected abstract boolean setBottom();

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

}
