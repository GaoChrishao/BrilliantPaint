package com.gaoch.brilliantpic.myview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.gaoch.brilliantpic.R;
import com.gaoch.brilliantpic.util.Utility;

public class CircleExp extends View {
    int angle=30;
    private Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleExp(Context context) {
        super(context);
        mPaint.setColor(getResources().getColor(R.color.colorExp));
    }

    public CircleExp(Context context,AttributeSet attrs) {
        super(context, attrs);
        mPaint.setColor(getResources().getColor(R.color.colorExp));
    }

    public CircleExp(Context context,  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint.setColor(getResources().getColor(R.color.colorExp));
    }

    public CircleExp(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        mPaint.setColor(getResources().getColor(R.color.colorExp));
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int width = getWidth();
        int height = getHeight();
        int radius = Math.min(width, height) / 2;
        RectF oval = new RectF(0, 0, 2*radius, 2*radius);
        mPaint.setColor(getResources().getColor(R.color.colorExp));
        canvas.drawArc(oval, -90, angle-90, true, mPaint);
        mPaint.setColor(getResources().getColor(R.color.colorExp_un));
        canvas.drawCircle(radius,radius,radius,mPaint);
    }
    public void setAngle(int angle){
        this.angle=angle;
        invalidate();

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        final int minimumWidth = getSuggestedMinimumWidth();
        final int minimumHeight = getSuggestedMinimumHeight();
        int width = measureWidth(minimumWidth, widthMeasureSpec);
        int height = measureHeight(minimumHeight, heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int measureWidth(int defaultWidth, int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultWidth =  getPaddingLeft() + getPaddingRight();
                Log.e("GGG","at_most:"+Utility.px2dp(getContext(),defaultWidth));
                break;
            case MeasureSpec.EXACTLY:
                defaultWidth = specSize;
                Log.e("GGG","exactly:"+ Utility.px2dp(getContext(),defaultWidth));
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultWidth = Math.max(defaultWidth, specSize);
                Log.e("GGG","unspecified:"+Utility.px2dp(getContext(),defaultWidth));
        }
        return defaultWidth;
    }


    private int measureHeight(int defaultHeight, int measureSpec) {

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        switch (specMode) {
            case MeasureSpec.AT_MOST:
                defaultHeight =  getPaddingTop() + getPaddingBottom();
                break;
            case MeasureSpec.EXACTLY:
                defaultHeight = specSize;
                break;
            case MeasureSpec.UNSPECIFIED:
                defaultHeight = Math.max(defaultHeight, specSize);
                break;
        }
        return defaultHeight;


    }
}
