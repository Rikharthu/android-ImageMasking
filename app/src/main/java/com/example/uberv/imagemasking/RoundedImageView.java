package com.example.uberv.imagemasking;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

public class RoundedImageView extends View {

    private Bitmap mImage;
    private float mRadius = 25.0f;
    private Paint mBitmapPaint;
    private RectF mBounds;

    public RoundedImageView(Context context) {
        super(context);
        init();
    }

    public RoundedImageView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RoundedImageView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // Create image paint
        mBitmapPaint = new Paint();
        // Create rect for drawing bounds
        mBounds = new RectF();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = 0;
        int height = 0;

        //Requested size is the image content size
        int imageHeight, imageWidth;
        if (mImage == null) {
            imageHeight = imageWidth = 0;
        } else {
            imageHeight = mImage.getHeight();
            imageWidth = mImage.getWidth();
        }
        //Get the best measurement and set it on the view
//        width = getMeasurement(widthMeasureSpec, imageWidth);
//        height = getMeasurement(heightMeasureSpec, imageHeight);
        setMeasuredDimension(width, height);
    }

}
