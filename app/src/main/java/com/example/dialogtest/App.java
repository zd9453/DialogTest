package com.example.dialogtest;

import android.app.Application;

import com.zd.baselibrary.util.Utils;

import skin.support.SkinCompatManager;
import skin.support.app.SkinAppCompatViewInflater;
import skin.support.app.SkinCardViewInflater;
import skin.support.constraint.app.SkinConstraintViewInflater;
import skin.support.design.app.SkinMaterialViewInflater;

/**
 * Package: com.example.dialogtest
 * <p>
 * describe:
 *
 * @author zhangdong on 2020/6/8
 * @version 1.0
 * @see .
 * @since 1.0
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Utils.init(this);

        SkinCompatManager.withoutActivity(this)
                .addInflater(new SkinAppCompatViewInflater())           // 基础控件换肤初始化
                .addInflater(new SkinMaterialViewInflater())            // material design 控件换肤初始化[可选]
                .addInflater(new SkinConstraintViewInflater())          // ConstraintLayout 控件换肤初始化[可选]
                .addInflater(new SkinCardViewInflater())                // CardView v7 控件换肤初始化[可选]
//                .setSkinStatusBarColorEnable(false)                     // 关闭状态栏换肤，默认打开[可选]
                .setSkinWindowBackgroundEnable(false)                   // 关闭windowBackground换肤，默认打开[可选]
                .loadSkin();
    }
}
