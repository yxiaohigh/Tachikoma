package com.h.tachikoma.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.util.AttributeSet;
import android.view.View;

import com.h.tachikoma.R;

/**
 * ProgressView
 * Created by tony on 2016/8/18.
 */
public class ProgressView extends View {


    private int wsize;
    private int hsize;
    private float dis = 1;//变化距离

    public ProgressView(Context context) {
        super(context);
    }


    public ProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.mProgressView);
        int dimensionPixelSize = typedArray.getDimensionPixelSize(R.styleable.mProgressView_mh, 0);
        int dimensionPixelSize1 = typedArray.getDimensionPixelSize(R.styleable.mProgressView_mw, 0);
        typedArray.recycle();
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        wsize = MeasureSpec.getSize(widthMeasureSpec);
        hsize = MeasureSpec.getSize(heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.save();
        canvas.translate(wsize / 2, hsize / 2);
        Path path = new Path();
        path.moveTo(0, hsize / 2);
        path.lineTo(0, -hsize / 2);

        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        path.addPath(path, matrix);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(2);
        paint.setColor(Color.BLACK);
        canvas.drawPath(path, paint);
        canvas.restore();

        paint.setColor(Color.GREEN);
        Path path1 = new Path();
        path1.moveTo(wsize / 3, 20);
        path1.lineTo(2 * wsize / 3, 20);
        path1.lineTo(2 * wsize / 3, hsize / 2);
        path1.lineTo(wsize / 3, hsize / 2);
        path1.lineTo(wsize / 3, hsize - 20);
        path1.lineTo(2 * wsize / 3, hsize - 20);
        canvas.skew(0,0.5f);
        canvas.translate(0,-hsize/4);
        canvas.drawPath(path1, paint);


        float[] pos = new float[2];
        float[] tan = new float[2];
        PathMeasure pathMeasure = new PathMeasure();
        pathMeasure.setPath(path1, false);
        float length = pathMeasure.getLength();

        dis += 0.05f;
        if (dis >= 1) {
            dis=0;
        }

        float v = dis * length;
        pathMeasure.getPosTan(v, pos, tan);
        canvas.drawCircle(pos[0], pos[1], 10, paint);

        invalidate();

    }
}
