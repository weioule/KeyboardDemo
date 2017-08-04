package com.example.keyboarddemo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keyboarddemo.util.DensityUtil;
import com.example.keyboarddemo.util.KeyboardUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyboardUtil.TextChangeListener {

    private long clickTime = 0;
    private int childCount = 6;
    private LinearLayout inputLl;
    private String couponCode = "";
    private KeyboardUtil keyboardUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLl = (LinearLayout) findViewById(R.id.input_frends_code_ll);
        findViewById(R.id.btn_use_coupon_code).setOnClickListener(this);
        addView();

    }

    private void addView() {
        //添加输入框
        for (int i = 0; i < childCount; i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.shape_et_code_bg);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(this, 37), LinearLayout.LayoutParams.MATCH_PARENT);
            if (i < childCount - 1) {
                layoutParams.rightMargin = DensityUtil.dip2px(this, 5);
            } else {
                layoutParams.rightMargin = 0;
            }
            tv.setLayoutParams(layoutParams);
            tv.setGravity(Gravity.CENTER);
            tv.setTextColor(0xff000000);
            tv.setTextSize(DensityUtil.dip2px(this, 10));
            tv.getPaint().setFakeBoldText(true);
            tv.setOnClickListener(this);
            inputLl.addView(tv);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_use_coupon_code:
                showCode();
                break;
            default:
                initKeyboard();
                break;
        }

    }

    private void initKeyboard() {
        if (keyboardUtil == null) {
            //初始化Keyboard
            keyboardUtil = new KeyboardUtil(this, childCount);
            keyboardUtil.showKeyboard();
            keyboardUtil.setOnTextChangeListener(this);
        } else {
            //显示软键盘
            if (!keyboardUtil.isShow()) {
                keyboardUtil.showKeyboard();
            }
        }

    }

    private void showCode() {
        //隐藏软键盘
        if (keyboardUtil != null && keyboardUtil.isShow())
            keyboardUtil.hideKeyboard();

        //demo显示
        if (!"".equals(couponCode)) {
            Toast.makeText(this.getApplicationContext(), couponCode, Toast.LENGTH_SHORT).show();
            clearEt();
        }
    }

    @Override
    public void addText(int index, String sub) {
        ((TextView) inputLl.getChildAt(index)).setText(sub);
        couponCode += sub;
    }

    @Override
    public void delText(int index) {
        String friendCode = "";
        ((TextView) inputLl.getChildAt(index)).setText("");
        for (int i = 0; i < couponCode.length() - 1; i++) {
            if (i < index) {
                friendCode += couponCode.charAt(i);
            }
        }
        couponCode = friendCode;
    }

    //清空输入框
    private void clearEt() {
        inputLl.postDelayed(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < inputLl.getChildCount(); i++) {
                    ((TextView) inputLl.getChildAt(i)).setText("");
                }
                if (keyboardUtil != null) {
                    keyboardUtil.setIndex(0);
                }
                couponCode = "";
            }
        }, 1000);

    }

    @Override
    public void onBackPressed() {
        if (keyboardUtil != null && keyboardUtil.isShow()) {
            keyboardUtil.hideKeyboard();
        } else {
            exit();
        }
    }

    private void exit() {
        if ((System.currentTimeMillis() - clickTime) > 2000) {
            Toast.makeText(getApplicationContext(), "再次点击退出", Toast.LENGTH_SHORT).show();
            clickTime = System.currentTimeMillis();
        } else {
            this.finish();
            System.exit(0);
        }
    }


}
