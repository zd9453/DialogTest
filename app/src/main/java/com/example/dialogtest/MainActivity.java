package com.example.dialogtest;

import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.app.SkinAppCompatDelegateImpl;

import com.example.dialogtest.dialog.AudioPlayDialogFragment;
import com.example.dialogtest.dialog.AudioProgressBarView;
import com.example.dialogtest.dialog.AudioProgressLayout;
import com.example.dialogtest.dialog.DialogStyle1;
import com.zd.baselibrary.util.Utils;

public class MainActivity extends AppCompatActivity {

    AudioProgressBarView barView;

    String[] str = {"你说啥",
            "咋地呀大苏打的大道阿达大",
            "对口高考佛啊koadia",
            "大苏打大v啊，bewew",
            "高价格i奥机构i屏发",
            "高价格ii啊 截屏发",
            "高价格i奥啊 截屏发",
            "高价格i奥机构i啊 截屏发",
            "高价格i",
            "高价格i奥发"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        barView = findViewById(R.id.bar_view);

//        barView.setMax(100);
//        barView.setProgress(0);

        int[] ints = Utils.getInstance().displaySize();
        Log.e(">>>>", "onCreate: " + ints[0] + "  " + ints[1]);


        final AudioProgressLayout progressLayout = (AudioProgressLayout) findViewById(R.id.progress_lay);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(1000);
                        progressLayout.changeState(progressLayout.getProgress() + 1);

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        /*FlowLayout flowLayout = (FlowLayout) findViewById(R.id.flow_layout);
        for (int i = 0; i < 10; i++) {
            TextView textView = new TextView(this);
            textView.setPadding(10, 5, 10, 5);
            textView.setText(str[i]);
            textView.setBackgroundColor(Color.GREEN);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String s = ((TextView) v).getText().toString();
                    Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });

            flowLayout.addView(textView);
        }*/

        ((ImageView) findViewById(R.id.image))
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        addCover();

                    }
                });

        TextView viewById = (TextView) findViewById(R.id.tv);

        viewById.setText(Html.fromHtml("<p  style=\"text-align: justify;text-align-last: justify;font-size: 120px\" >就是测试</p>"));
    }

    private boolean isAdd;

    private void addCover() {
        View decorView = getWindow().getDecorView();
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();

        if (isAdd) {
            isAdd = false;
            cm.setSaturation(1);
        } else {
            isAdd = true;
            cm.setSaturation(0);
        }
        paint.setColorFilter(new ColorMatrixColorFilter(cm));
        decorView.setLayerType(View.LAYER_TYPE_HARDWARE, paint);

//        View decorView = getWindow().getDecorView();
//        Canvas canvas = new Canvas();
//        decorView.draw();
    }

    public void showDialog1(View view) {

        DialogStyle1 style1 = new DialogStyle1();
        style1.show(getSupportFragmentManager(), "1");


    }

    public void showDialog2(View view) {

        AudioPlayDialogFragment fragment = new AudioPlayDialogFragment();
        fragment.show(getSupportFragmentManager(), "2");


    }

    public void showDialog3(View view) {

    }

    public void showDialog4(View view) {

    }

    public void showDialog5(View view) {

    }

    long time = 0;
    long end;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((end = System.currentTimeMillis()) - time > 2000) {
                time = end;
                Toast.makeText(this, "再试试", Toast.LENGTH_SHORT).show();
                return true;
            }
        }

        return super.onKeyDown(keyCode, event);
    }

    @NonNull
    @Override
    public AppCompatDelegate getDelegate() {
        return SkinAppCompatDelegateImpl.get(this, this);
    }
}
