package com.example.keyboarddemo.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.view.View;

import com.example.keyboarddemo.R;

public class KeyboardUtil {


    /**
     * 数字与小写字母键盘
     */
    private Keyboard keyboardLowerCase;
    /**
     * 数字与大写字母键盘
     */

    private int index;
    private int childCount;
    private boolean isChange = true;
    private Keyboard keyboarGapital;
    private KeyboardView mKeyboardView;
    private TextChangeListener textChangeListener;

    public KeyboardUtil(Activity activity, int childCount) {
        this.childCount = childCount;
        keyboardLowerCase = new Keyboard(activity, R.xml.letter_lower_case);
        keyboarGapital = new Keyboard(activity, R.xml.letter_gapital);
        mKeyboardView = (KeyboardView) activity.findViewById(R.id.keyboard_view);
        mKeyboardView.setEnabled(true);
        mKeyboardView.setPreviewEnabled(false);
        mKeyboardView.setKeyboard(keyboarGapital);
        mKeyboardView.setOnKeyboardActionListener(listener);

    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {
        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {
            if (primaryCode == -1) {//大小写切换
                changeKeyboard();
            } else if (primaryCode == -3) {// 回退
                if (textChangeListener != null && index > 0) {
                    index--;
                    textChangeListener.delText(index);
                }
            } else {
                if (textChangeListener != null && index < childCount) {
                    textChangeListener.addText(index, Character.toString((char) primaryCode));
                    index++;
                }
            }
        }
    };

    /**
     * 按切换键时切换软键盘
     */
    public void changeKeyboard() {
        if (isChange) {
            mKeyboardView.setKeyboard(keyboardLowerCase);
        } else {
            mKeyboardView.setKeyboard(keyboarGapital);
        }
        isChange = !isChange;
    }


    /**
     * 软键盘展示状态
     */
    public boolean isShow() {
        return mKeyboardView.getVisibility() == View.VISIBLE;
    }

    /**
     * 软键盘展示
     */
    public void showKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            open();
        }
    }

    /**
     * 软键盘隐藏
     */
    public void hideKeyboard() {
        int visibility = mKeyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            close();
        }
    }

    /**
     * 关闭--动画
     */
    private void close() {
        int origheight = mKeyboardView.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, "translationY", 0, origheight);
        animator.setDuration(100);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mKeyboardView.setVisibility(View.GONE);
            }
        });
        animator.start();
    }

    /**
     * 打开--动画
     */
    private void open() {
        mKeyboardView.setVisibility(View.VISIBLE);
        int origheight = mKeyboardView.getHeight();
        ObjectAnimator animator = ObjectAnimator.ofFloat(mKeyboardView, "translationY", origheight, 0);
        animator.setDuration(100);
        animator.start();
    }

    public void setOnTextChangeListener(TextChangeListener textChangeListener) {
        this.textChangeListener = textChangeListener;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public interface TextChangeListener {
        void addText(int index, String sub);

        void delText(int index);

    }


}
