package com.example.fengshow.appforricenitrogen;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * Created by fengshow on 2017/5/13.
 */

public class ResultActivityForLeafWithWireframe extends Activity {

    private ImageView imageView;
    private Bitmap bitmap_temporary;
    //
    TextView tvNitLevNumb;
    TextView tvNitConNumb;
    String tv_n_l_n;
    String tv_n_c_n;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.result_for_leaf_view_with_wireframe_layout);
        String path = getIntent().getStringExtra("picPath_leaf_with_wireframe");//获取intent中传过来的picPath对象
        imageView = (ImageView) findViewById(R.id.pic_leaf_with_wireframe);//
        //
        tvNitLevNumb= (TextView) findViewById(R.id.tv_nitrogen_level_numb);
        tvNitConNumb= (TextView) findViewById(R.id.tv_nitrogen_content_numb);
        //imageView.setOnTouchListener(new TouchListener());
        try {
            FileInputStream fis = new FileInputStream(path);
            Bitmap bitmap = BitmapFactory.decodeStream(fis);
            Matrix matrix = new Matrix();
            matrix.setRotate(0);//设置角度
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
            //matrix为调整矩阵，数值即为旋转角度
            //
            //二值化
            bitmap_temporary=gray2Binary(bitmap);

            imageView.setImageBitmap(bitmap_temporary);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
//        Bitmap bitmap = BitmapFactory.decodeFile(path);//获取磁盘上的资源
//        imageView.setImageBitmap(bitmap);
        tvNitLevNumb.setText(tv_n_l_n);
        tvNitConNumb.setText(tv_n_c_n);
    }

    // 该函数实现对图像进行二值化处理
    public Bitmap gray2Binary(Bitmap graymap) {
        //得到图形的宽度和长度
        int width = graymap.getWidth();
        int height = graymap.getHeight();
        //创建二值化图像
        Bitmap binarymap = null;
        binarymap = graymap.copy(Bitmap.Config.ARGB_8888, true);
        //依次循环，对图像的像素进行处理
        //统计
//        float tn;
//        float x=1;
//        float y=1;
        //统计绿色G
        int numb=0;
        double gValue_sum=0;
        double gValue;
        int gNumb;
        double nNumb;


        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                //得到当前像素的值
                int col = binarymap.getPixel(i, j);
                //得到alpha通道的值
                int alpha = col & 0xFF000000;
                //得到图像的像素RGB的值
                int red = (col & 0x00FF0000) >> 16;
                int green = (col & 0x0000FF00) >> 8;
                int blue = (col & 0x000000FF);
                // 用公式X = 0.3×R+0.59×G+0.11×B计算出X代替原来的RGB
                int gray = (int) ((float) red * 0.3 + (float) green * 0.59 + (float) blue * 0.11);
                //对图像进行二值化处理
                if (gray <= 95) {
//                    gray = col;
                    //x=x+1;
                    numb=numb+1;
                    gValue_sum=gValue_sum+(green/(red+green+blue));//计算标准化绿光值(G/(R+G+B))的总和
                    binarymap.setPixel(i,j,col);
                } else {
                    gray= 0;
                    //gray = 255;
                    //y=y+1;
                    int newColor=alpha|(gray<<16)|(gray<<8)|gray;
                    binarymap.setPixel(i,j,newColor);
                }
//                // 新的ARGB
//                int newColor = alpha | (gray << 16) | (gray << 8) | gray;
//                //设置新图像的当前像素值
//                binarymap.setPixel(i, j, newColor);
//                //统计


            }
        }
        //tn=(y/(x+y));
        //Toast.makeText(getApplicationContext(),"孔隙度T="+tn,Toast.LENGTH_LONG).show();
        //
        //tv_c_r=String.valueOf(tn);
        //统计绿色g,方案一
        gValue=gValue_sum/numb;
        String g_rgb=String.format("%.4f",gValue);
        Toast.makeText(getApplicationContext(),"绿色标准化值为："+g_rgb,Toast.LENGTH_LONG).show();

        if (gValue>0.400&&gValue<=0.450){
            gNumb=1;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else if (gValue>0.450&&gValue<=0.455){
            gNumb=2;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else if (gValue>0.455&&gValue<=0.462){
            gNumb=3;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else if (gValue>0.462&&gValue<=0.470){
            gNumb=4;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else if (gValue>0.470&&gValue<=0.477){
            gNumb=5;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else if (gValue>0.477&&gValue<=0.490){
            gNumb=6;
            nNumb=(1.45-gValue*(10/3));
            tv_n_l_n=String.valueOf(gNumb);
            tv_n_c_n=String.format("%.4f",nNumb);
        }
        else {
            tv_n_l_n="Error";
            tv_n_c_n="Error";
        }

        //方案二

//        if(gValue>115&&gValue<=125){
//            gNumb=1;
//            nNumb=12.0;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
//        else if (gValue>105&&gValue<=115){
//            gNumb=2;
//            nNumb=12.0;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
//        else if (gValue>95&&gValue<=105){
//            gNumb=3;
//            nNumb=8.5;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
////        else if (gValue>50&&gValue<=95){
////            gNumb=4;
////            nNumb=5.0;
////            tv_n_l_n=String.valueOf(gNumb);
////            tv_n_c_n=String.valueOf(nNumb);
////        }
//
//        //方案二
//        else if (gValue>75&&gValue<=95){
//            gNumb=4;
//            nNumb=5.0;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
//        else if (gValue>65&&gValue<=75){
//            gNumb=5;
//            nNumb=5.0;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
//        else if (gValue>50&&gValue<=55){
//            gNumb=6;
//            nNumb=5.0;
//            tv_n_l_n=String.valueOf(gNumb);
//            tv_n_c_n=String.valueOf(nNumb);
//        }
//
//
//        else {
//            tv_n_l_n="Error";
//            tv_n_c_n="Error";
//        }

        //Toast.makeText(getApplicationContext(),"绿色平均值G为："+G,Toast.LENGTH_LONG).show();

        return binarymap;
    }


    //废弃
    private final class TouchListener implements View.OnTouchListener {

        /**
         * 记录是拖拉照片模式还是放大缩小照片模式
         */
        private int mode = 0;// 初始状态
        /**
         * 拖拉照片模式
         */
        private static final int MODE_DRAG = 1;
        /**
         * 放大缩小照片模式
         */
        private static final int MODE_ZOOM = 2;

        /**
         * 用于记录开始时候的坐标位置
         */
        private PointF startPoint = new PointF();
        /**
         * 用于记录拖拉图片移动的坐标位置
         */
        private Matrix matrix = new Matrix();
        /**
         * 用于记录图片要进行拖拉时候的坐标位置
         */
        private Matrix currentMatrix = new Matrix();

        /**
         * 两个手指的开始距离
         */
        private float startDis;
        /**
         * 两个手指的中间点
         */
        private PointF midPoint;

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            /** 通过与运算保留最后八位 MotionEvent.ACTION_MASK = 255 */
            switch (event.getAction() & MotionEvent.ACTION_MASK) {
                // 手指压下屏幕
                case MotionEvent.ACTION_DOWN:
                    mode = MODE_DRAG;
                    // 记录ImageView当前的移动位置
                    currentMatrix.set(imageView.getImageMatrix());
                    startPoint.set(event.getX(), event.getY());
                    break;
                // 手指在屏幕上移动，改事件会被不断触发
                case MotionEvent.ACTION_MOVE:
                    // 拖拉图片
                    if (mode == MODE_DRAG) {
                        float dx = event.getX() - startPoint.x; // 得到x轴的移动距离
                        float dy = event.getY() - startPoint.y; // 得到x轴的移动距离
                        // 在没有移动之前的位置上进行移动
                        matrix.set(currentMatrix);
                        matrix.postTranslate(dx, dy);
                    }
                    // 放大缩小图片
                    else if (mode == MODE_ZOOM) {
                        float endDis = distance(event);// 结束距离
                        if (endDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                            float scale = endDis / startDis;// 得到缩放倍数
                            matrix.set(currentMatrix);
                            matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                        }
                    }
                    break;
                // 手指离开屏幕
                case MotionEvent.ACTION_UP:
                    // 当触点离开屏幕，但是屏幕上还有触点(手指)
                case MotionEvent.ACTION_POINTER_UP:
                    mode = 0;
                    break;
                // 当屏幕上已经有触点(手指)，再有一个触点压下屏幕
                case MotionEvent.ACTION_POINTER_DOWN:
                    mode = MODE_ZOOM;
                    /** 计算两个手指间的距离 */
                    startDis = distance(event);
                    /** 计算两个手指间的中间点 */
                    if (startDis > 10f) { // 两个手指并拢在一起的时候像素大于10
                        midPoint = mid(event);
                        //记录当前ImageView的缩放倍数
                        currentMatrix.set(imageView.getImageMatrix());
                    }
                    break;
            }
            imageView.setImageMatrix(matrix);
            return true;
        }
        /** 计算两个手指间的距离 */
        private float distance(MotionEvent event) {
            float dx = event.getX(1) - event.getX(0);
            float dy = event.getY(1) - event.getY(0);
            /** 使用勾股定理返回两点之间的距离 */
            //return FloatMath.sqrt(dx * dx + dy * dy);
            return (float) Math.sqrt(dx * dx + dy * dy);
        }

        /** 计算两个手指间的中间点 */
        private PointF mid(MotionEvent event) {
            float midX = (event.getX(1) + event.getX(0)) / 2;
            float midY = (event.getY(1) + event.getY(0)) / 2;
            return new PointF(midX, midY);
        }
    }
}
