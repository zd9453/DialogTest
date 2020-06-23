package com.example.dialogtest.dialog;

import android.os.Bundle;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

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
public class DialogStyle1 extends BaseDialogFragment {


    public static DialogStyle1 newInstance() {

        Bundle args = new Bundle();
        DialogStyle1 fragment = new DialogStyle1();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.dialog_fragment_1;
    }

    @Override
    protected int getStyle() {
        return /*R.style.transparencyDialog*/0;
    }

    @Override
    protected void reSetShowSize(Window window) {
        window.setLayout((int) (getResources().getDisplayMetrics().widthPixels * 0.8),
                ((int) (getResources().getDisplayMetrics().heightPixels * 0.5)));

    }

    @Override
    protected void initView(View view) {
        TextView textView = (TextView) view.findViewById(R.id.show);
        textView.setText(Html.fromHtml("提示内容为HTML格式： <a href='http://www.baidu.com' style='color:gray'>this is a tag click </a>"));
        textView.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    protected boolean setBottom() {
        return false;
    }

}
