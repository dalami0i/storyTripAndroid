package com.tripkorea.on.ontripkorea.util;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.widget.Toast;

import com.tripkorea.on.ontripkorea.R;

/**
 * Created by Edward Won on 2018-07-13.
 */

public class LinedEditText extends android.support.v7.widget.AppCompatEditText {

    private static final String LINE_MAX_TEXT = "Max Lines";
    private static final String CHARACTER_MAX_TEXT = "Max characters";

    private Rect mRect;
    private Paint mPaint;
    /**
     * Max lines to be present in editable text field
     */
    private int maxLines = 3;

    /**
     * Max characters to be present in editable text field
     */
    private int maxCharacters = 10;

    /**
     * application context;
     */
    private Context context;

    private String characterToastMassage = CHARACTER_MAX_TEXT + maxCharacters;
    private String LineToastMassage = LINE_MAX_TEXT +maxLines;

    public int getMaxCharacters() {

        return maxCharacters;
    }

    public void setMaxCharacters(int maxCharacters) {

        this.maxCharacters = maxCharacters;
        this.characterToastMassage = CHARACTER_MAX_TEXT + maxCharacters;
    }

    @Override
    public int getMaxLines() {
        return maxLines;
    }

    @Override
    public void setMaxLines(int maxLines) {
        this.maxLines = maxLines;
        this.LineToastMassage = LINE_MAX_TEXT + maxLines;
    }

    public LinedEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
    }

    public LinedEditText(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();

        TextWatcher watcher = new TextWatcher() {

            private String text;
            private int beforeCursorPosition = 0;

            @Override
            public void onTextChanged(CharSequence s, int start, int before,
                                      int count) {
                //TODO sth
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count,
                                          int after) {
                text = s.toString();
                beforeCursorPosition = start;
            }

            @Override
            public void afterTextChanged(Editable s) {

            /* turning off listener */
                removeTextChangedListener(this);

            /* handling lines limit exceed */
                if (LinedEditText.this.getLineCount() > maxLines) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                    Toast.makeText(context, LineToastMassage, Toast.LENGTH_SHORT)
                            .show();
                }

            /* handling character limit exceed */
                if (s.toString().length() > maxCharacters) {
                    LinedEditText.this.setText(text);
                    LinedEditText.this.setSelection(beforeCursorPosition);
                    Toast.makeText(context, characterToastMassage, Toast.LENGTH_SHORT)
                            .show();
                }

            /* turning on listener */
                addTextChangedListener(this);

            }
        };

        this.addTextChangedListener(watcher);
    }


    // we need this constructor for LayoutInflater
    public LinedEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        mRect = new Rect();
        mPaint = new Paint();
        this.context = context;
        mPaint.setStyle(Paint.Style.STROKE);

        // getColor() deprecated 처리
        //mPaint.setColor(getResources().getColor(R.color.color_memo_edit_underline));
        mPaint.setColor(ContextCompat.getColor(MyApplication.getContext(), R.color.grey));
    }

//    @Override
//    protected void onDraw(Canvas canvas) {
//        //int count = getLineCount();
//
//        int height = getHeight();
//        int line_height = getLineHeight();
//
//        int count = height / line_height;
//
//        if (getLineCount() > count)
//            count = getLineCount();//for long text with scrolling
//
//        Rect r = mRect;
//        Paint paint = mPaint;
//        // 선 두께
//        mPaint.setStrokeWidth(3.0f);
//        int baseline = getLineBounds(0, r);//first line
//
//        for (int i = 0; i < count; i++) {
//
//            // 숫자 조절하면 여백 가능
//            canvas.drawLine(r.left, baseline + 10, r.right, baseline + 10, paint);
//            baseline += getLineHeight();//next line
//        }
//
//        super.onDraw(canvas);
//    }
}
