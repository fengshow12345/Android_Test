package com.example.fengshow.appforricenitrogen.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fengshow on 2017/5/10.
 */

public class CustView extends View {

    private Paint mPaint;//定义画笔

    public CustView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //初始化画笔
        initPaint();
        setLayerType(View.LAYER_TYPE_SOFTWARE,mPaint);
    }
    private void initPaint()
    {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(5f);//设置线宽
        mPaint.setAlpha(180);
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawRect(new Rect(250,450,800,800),mPaint);//绘制矩形
    }
}
