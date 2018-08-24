package com.example.fengshow.appforricenitrogen.image;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by fengshow on 2017/5/10.
 */

public class DrawImageView extends android.support.v7.widget.AppCompatImageView {

    private Paint mPaint;//定义画笔

    public DrawImageView(Context context,AttributeSet attrs){
        super(context,attrs);
        //初始化画笔
        initPaint();
        setLayerType(View.LAYER_TYPE_SOFTWARE,mPaint);
    }

    private void initPaint()
    {
        mPaint=new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStyle(Style.STROKE);
        mPaint.setStrokeWidth(2.5f);//设置线宽
        mPaint.setAlpha(100);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawRect(new Rect(100,200,400,500),mPaint);//绘制矩形
    }
}
