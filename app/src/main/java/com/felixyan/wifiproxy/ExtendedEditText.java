package com.felixyan.wifiproxy;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.AppCompatEditText;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by yanfei on 2017/7/19.
 *
 * ExtendedEditText
 *
 * 在原有EditText上增加了
 * 1. 可以清除文本的icon
 * 2. 设置默认的前缀、后缀文本
 *
 * 为使用Material Design风格，这里继承了AppCompatEditText，如不需要MD风格，可以改为继承EditText
 *
 * 可清除文本的icon 参考：
 * https://github.com/yanchenko/droidparts/blob/develop/droidparts/src/org/droidparts/widget/ClearableEditText.java
 *
 * 默认的clear icon可以通过以下方式修改：
 * <pre>
 * android:drawable(Right|Left)="@drawable/custom_icon"
 * </pre>
 */
public class ExtendedEditText extends AppCompatEditText
        implements View.OnTouchListener, View.OnFocusChangeListener {
    private static int CLEAR_ICON_LOC_NONE = -1;
    private static int CLEAR_ICON_LOC_LEFT = 0;
    private static int CLEAR_ICON_LOC_RIGHT = 2;

    private int mClearIconLoc = CLEAR_ICON_LOC_RIGHT; // clear icon位置
    private Drawable mClearIconDrawable; // clear icon的图标
    private OnTextClearedListener mOnTextClearedListener; // 文本被clear icon清空后的回调接口

    private OnTouchListener mOnTouchListener; // EditText的OnTouchListener
    private OnFocusChangeListener mOnFocusChangeListener; // EditText的OnFocusChangeListener

    private Paint mAdditionalTextPaint; // 绘制前/后缀文字的画笔
    private String mPrefixText; // 前缀文字
    private String mSuffixText; // 后缀文字
    private float mPrefixTextHeight; // 前缀文字高度
    private float mPrefixTextWidth; // 前缀文字宽度
    private float mSuffixTextHeight; // 后缀文字高度
    private float mSuffixTextWidth; // 后缀文字宽度
    private float mAdditionalTextPadding; // 前/后缀文字的左/右padding

    public ExtendedEditText(Context context) {
        super(context);
        init(context, null);
    }

    public ExtendedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public ExtendedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if(attrs != null) {
            TypedArray arr = context.obtainStyledAttributes(attrs, R.styleable.ExtendedEditText);
            mClearIconLoc = arr.getInt(R.styleable.ExtendedEditText_EetClearIconLocation, mClearIconLoc);
            mPrefixText = arr.getString(R.styleable.ExtendedEditText_EetPrefixText);
            mSuffixText = arr.getString(R.styleable.ExtendedEditText_EetSuffixText);
        }

        super.setOnTouchListener(this);
        super.setOnFocusChangeListener(this);
        addTextChangedListener(new TextWatcherAdapter(){
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (isFocused()) {
                    setClearIconVisible(!TextUtils.isEmpty(s.toString()));
                }
            }
        });

        // 初始化绘制前/后缀文字的画笔
        mAdditionalTextPaint = new Paint();
        mAdditionalTextPaint.setColor(getTextColors().getDefaultColor());
        mAdditionalTextPaint.setTextSize(getTextSize());
        mAdditionalTextPaint.setAntiAlias(true);

        // 设置前/后缀文字的左/右padding默认值
        mAdditionalTextPadding = dip2px(getContext(), 8);

        initClearIcon();
        setClearIconVisible(false);

        if(!TextUtils.isEmpty(mPrefixText)) {
            setPrefixText(mPrefixText);
        }
        if(!TextUtils.isEmpty(mSuffixText)) {
            setSuffixText(mSuffixText);
        }
    }

    private void initClearIcon() {
        mClearIconDrawable = null;
        if (mClearIconLoc != CLEAR_ICON_LOC_NONE) {
            mClearIconDrawable = getCompoundDrawables()[mClearIconLoc];
        }
        if (mClearIconDrawable == null) {
            mClearIconDrawable = getResources().getDrawable(android.R.drawable.presence_offline);
        }
        mClearIconDrawable.setBounds(0, 0, mClearIconDrawable.getIntrinsicWidth(), mClearIconDrawable.getIntrinsicHeight());
        int min = getPaddingTop() + mClearIconDrawable.getIntrinsicHeight() + getPaddingBottom();
        if (getSuggestedMinimumHeight() < min) {
            setMinimumHeight(min);
        }
    }

    /**
     * 设置clear icon的位置
     *
     * CLEAR_ICON_LOC_NONE
     * CLEAR_ICON_LOC_LEFT
     * CLEAR_ICON_LOC_RIGHT
     */
    public void setClearIconLocation(int loc) {
        this.mClearIconLoc = loc;
        initClearIcon();
    }

    /**
     * 设置文本被clear icon清空后的回调接口
     * @param onTextClearedListener
     */
    public void setOnTextClearedListener(OnTextClearedListener onTextClearedListener) {
        this.mOnTextClearedListener = onTextClearedListener;
    }

    /**
     * 设置前缀文字
     * @param text
     */
    public void setPrefixText(String text) {
        mPrefixText = text;

        float originalPaddingLeft = getPaddingLeft();
        if(mPrefixTextWidth != 0) { // 之前设置过PrefixText，减去额外的宽度，得到原始的PaddingLeft
            originalPaddingLeft -= (mPrefixTextWidth + mAdditionalTextPadding * 2);
        }

        if(!TextUtils.isEmpty(text)) {
            Rect rect = new Rect();
            mAdditionalTextPaint.getTextBounds(text, 0, text.length(), rect);
            mPrefixTextHeight = rect.height();
            mPrefixTextWidth = rect.width();
            //mPrefixTextWidth = mAdditionalTextPaint.measureText(text);
            // 将前缀文字的宽加到getPaddingLeft
            setPadding((int)(originalPaddingLeft + mPrefixTextWidth + mAdditionalTextPadding * 2), getPaddingTop(), getPaddingRight(), getPaddingBottom());
            invalidate();
        } else if(mPrefixTextWidth != 0) {
            // 将之前添加的前缀文字的宽及padding从getPaddingLeft中去除
            setPadding((int)originalPaddingLeft, getPaddingTop(), getPaddingRight(), getPaddingBottom());
            invalidate();
        }
    }

    /**
     * 设置后缀文字
     * @param text
     */
    public void setSuffixText(String text) {
        mSuffixText = text;

        float originalPaddingRight = getPaddingRight();
        if(mSuffixTextWidth != 0) { // 之前设置过SuffixText，减去额外的宽度，得到原始的PaddingRight
            originalPaddingRight -= (mSuffixTextWidth + mAdditionalTextPadding * 2);
        }

        if(!TextUtils.isEmpty(text)) {
            Rect rect = new Rect();
            mAdditionalTextPaint.getTextBounds(text, 0, text.length(), rect);
            mSuffixTextHeight = rect.height();
            mSuffixTextWidth = rect.width();
            //mSuffixTextWidth = mAdditionalTextPaint.measureText(text);
            // 将后缀文字的宽及padding加到getPaddingRight
            setPadding(getPaddingLeft(), getPaddingTop(), (int)(originalPaddingRight + mSuffixTextWidth + mAdditionalTextPadding * 2), getPaddingBottom());
            invalidate();
        } else if(mSuffixTextWidth != 0) {
            // 将之前添加的后缀文字的宽及padding从getPaddingRight中去除
            setPadding(getPaddingLeft(), getPaddingTop(), (int)originalPaddingRight, getPaddingBottom());
            mSuffixTextWidth = 0;
            invalidate();
        }
    }

    @Override
    public void setCompoundDrawables(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        super.setCompoundDrawables(left, top, right, bottom);
        initClearIcon();
    }

    private Drawable getDisplayedDrawable() {
        return (mClearIconLoc != -1) ? getCompoundDrawables()[mClearIconLoc] : null;
    }

    protected void setClearIconVisible(boolean visible) {
        Drawable[] cd = getCompoundDrawables();
        Drawable displayed = getDisplayedDrawable();
        boolean wasVisible = (displayed != null);
        if (visible != wasVisible) {
            Drawable x = visible ? mClearIconDrawable : null;
            super.setCompoundDrawables((mClearIconLoc == CLEAR_ICON_LOC_LEFT) ? x : cd[0], cd[1], (mClearIconLoc == CLEAR_ICON_LOC_RIGHT) ? x : cd[2],
                    cd[3]);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 绘制前缀文字
        if(!TextUtils.isEmpty(mPrefixText)) {
            mAdditionalTextPaint.setTextAlign(Paint.Align.LEFT);
            int baseLine = (int) (getScrollY()  + getCompoundPaddingTop()
                    + (getHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop() - mPrefixTextHeight) / 2
                    + mPrefixTextHeight);
            // 将之前添加的前缀文字的宽及一倍padding从getPaddingLeft中去除
            canvas.drawText(mPrefixText, getPaddingLeft() - mPrefixTextWidth - mAdditionalTextPadding, baseLine,
                    mAdditionalTextPaint);
        }

        // 绘制后缀文字
        if(!TextUtils.isEmpty(mSuffixText)) {
            mAdditionalTextPaint.setTextAlign(Paint.Align.RIGHT);
            int baseLine = (int) (getScrollY()  + getCompoundPaddingTop()
                    + (getHeight() - getCompoundPaddingBottom() - getCompoundPaddingTop() - mSuffixTextHeight) / 2
                    + mSuffixTextHeight);
            // 将之前添加的后缀文字的宽及一倍padding从getPaddingRight中去除
            canvas.drawText(mSuffixText, getScrollX() + getWidth() - (getPaddingRight() - mSuffixTextWidth - mAdditionalTextPadding), baseLine,
                    mAdditionalTextPaint);
        }
    }

    @Override
    public void setOnTouchListener(OnTouchListener l) {
        this.mOnTouchListener = l;
    }

    @Override
    public void setOnFocusChangeListener(OnFocusChangeListener f) {
        this.mOnFocusChangeListener = f;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (getDisplayedDrawable() != null) {
            int x = (int) event.getX();
            int y = (int) event.getY();
            int left = (mClearIconLoc == CLEAR_ICON_LOC_LEFT) ? 0 : getWidth() - getPaddingRight() - mClearIconDrawable.getIntrinsicWidth();
            int right = (mClearIconLoc == CLEAR_ICON_LOC_LEFT) ? getPaddingLeft() + mClearIconDrawable.getIntrinsicWidth() : getWidth();
            boolean tappedX = x >= left && x <= right && y >= 0 && y <= (getBottom() - getTop());
            if (tappedX) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    setText("");
                    if (mOnTextClearedListener != null) {
                        mOnTextClearedListener.onTextCleared();
                    }
                }
                return true;
            }
        }
        if (mOnTouchListener != null) {
            return mOnTouchListener.onTouch(v, event);
        }
        return false;
    }

    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        if (hasFocus) {
            setClearIconVisible(!TextUtils.isEmpty(getText()));
        } else {
            setClearIconVisible(false);
        }
        if (mOnFocusChangeListener != null) {
            mOnFocusChangeListener.onFocusChange(v, hasFocus);
        }
    }

    private static int dip2px(Context context, float dipValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public interface OnTextClearedListener {
        void onTextCleared();
    }

    protected abstract class TextWatcherAdapter implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            // pass
        }

        @Override
        public void afterTextChanged(Editable s) {
            // pass
        }
    }
}
