package com.example.keyboarddemo;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.keyboarddemo.util.DensityUtil;
import com.example.keyboarddemo.util.KeyboardUtil;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, KeyboardUtil.TextChangeListener {

    private int childCount = 6;
    private LinearLayout inputLl;
    private String couponCode = "";
    private KeyboardUtil keyboardUtil;
    private KeyboardView keyboardView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputLl = (LinearLayout) findViewById(R.id.input_frends_code_ll);
        keyboardView = (KeyboardView) findViewById(R.id.keyboard_view);
        findViewById(R.id.btn_use_coupon_code).setOnClickListener(this);
        addView();

    }

    private void addView() {
        for (int i = 0; i < childCount; i++) {
            TextView tv = new TextView(this);
            tv.setBackgroundResource(R.drawable.input_frends_code_et_bg);
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
            keyboardUtil = new KeyboardUtil(this, childCount);
            keyboardUtil.showKeyboard();
            keyboardUtil.setOnTextChangeListener(this);
        } else {
            if (!keyboardUtil.isShow()) {
                keyboardUtil.showKeyboard();
            }
        }

    }


    private void showCode() {
        if (keyboardUtil.isShow())
            keyboardUtil.hideKeyboard();

        Toast.makeText(this.getApplicationContext(), couponCode, Toast.LENGTH_SHORT).show();
        inputLl.postDelayed(new Runnable() {
            @Override
            public void run() {
                clearEt();
            }
        }, 1000);
        return;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (keyboardUtil.isShow()) {
                keyboardUtil.hideKeyboard();
            } else {
                finish();
            }
        }
        return false;
    }

    @Override
    public void addText(int index, String sub) {
        ((TextView) inputLl.getChildAt(index)).setText(sub);
        couponCode += sub;
    }

    @Override
    public void delText(int index) {
        ((TextView) inputLl.getChildAt(index)).setText("");
        String friendCode = "";
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
        }, 500);

    }

}
