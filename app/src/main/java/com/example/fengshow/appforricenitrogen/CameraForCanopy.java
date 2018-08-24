package com.example.fengshow.appforricenitrogen;

import android.app.Activity;
import android.content.Intent;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by fengshow on 2017/5/2.
 */

public class CameraForCanopy extends Activity implements SensorEventListener, SurfaceHolder.Callback {

    //水准仪
    private SensorManager sensorManager;
    private Sensor acc_sensor;
    private Sensor mag_sensor;

    private float[] accValues = new float[3];
    private float[] magValues = new float[3];
    // 旋转矩阵，用来保存磁场和加速度的数据
    private float r[] = new float[9];
    // 模拟方向传感器的数据（原始数据为弧度）
    private float values[] = new float[3];

    private TextView tvHorz;
    private TextView tvVert;

    //
    //private int cameraPosition = 1;//0代表前置摄像头，1代表后置摄像头

    private Camera mCamera;
    private SurfaceView mPreview;
    private SurfaceHolder mHolder;
    private Camera.PictureCallback mpictureCallBack = new Camera.PictureCallback() {
        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            //byte[] data保存着拍照的完整数据
            File tempFile = new File("/sdcard/Pictures/SHZQ/temp-for-Canopy.jpg");
            //将整个data的数据写到这个tempFile文件中去，即可获得图片
            try {
                FileOutputStream fos = new FileOutputStream(tempFile);
                fos.write(data);
                fos.close();//写完之后，关闭FileOutputStream
                //传递
                Intent intent = new Intent(CameraForCanopy.this, ResultActivity.class);
                intent.putExtra("picPath", tempFile.getAbsolutePath());
                startActivity(intent);
                CameraForCanopy.this.finish();//将拍照的Activity finish掉
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.camera_for_canopy);

        //角度
        //角度显示
        tvVert = (TextView) findViewById(R.id.tv_VertNumb);
        tvHorz = (TextView)findViewById(R.id.tv_HoztNumb);
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);

        //
        mPreview = (SurfaceView) findViewById(R.id.sfv_Camera_for_Canopy);
        mHolder = mPreview.getHolder();
        mHolder.addCallback(this);//获取SurfaceView
        mPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.autoFocus(null);
            }
        });
    }

    //设置相机参数
    public void capture(View view) {
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setPictureFormat(ImageFormat.JPEG);//设置拍照模式jpg
        parameters.setPreviewSize(800, 400);//设置预览大小
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);//设置自动对焦
        //实现拍照
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            //
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                //判断对焦是否完全准确，然后拍照
                if (success) {
                    mCamera.takePicture(null, null, mpictureCallBack);
                }
            }
        });
    }

    /**
     * 系统onResume时判断
     * 并获取Camera
     */
    @Override
    protected void onResume() {
        super.onResume();
        //水平仪
        acc_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        mag_sensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        // 给传感器注册监听：
        sensorManager.registerListener(this, acc_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, mag_sensor, SensorManager.SENSOR_DELAY_NORMAL);
        //
        if (mCamera == null) {
            mCamera = getCamera();
            if (mHolder != null) {
                try {
                    setStartPreview(mCamera, mHolder); //讲相机与SurfaceView进行绑定
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 系统onPause时release Camera
     */
    @Override
    protected void onPause() {
        // 取消方向传感器的监听
        sensorManager.unregisterListener(this);
        //
        super.onPause();
        releaseCamera();
    }

    @Override
    protected void onStop() {
        // 取消方向传感器的监听
        sensorManager.unregisterListener(this);
        super.onStop();
    }
    /**通过这两个生命周期
     *从而将Camera的生命周期与Activity的生命周期绑定，
     * 确保Camera的使用资源正确的被初始化和释放
     */

    /**
     * 获取Camera对象
     *
     * @return
     */
    private Camera getCamera() {
        Camera camera;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
            camera = null;
        }
        return camera;
    }

    /**
     * 切换摄像头，前置、后置
     */
//    public void Change(View v) {
//        //切换前后摄像头
//        int cameraCount = 0;
//        Camera.CameraInfo cameraInfo = new Camera.CameraInfo();
//        cameraCount = Camera.getNumberOfCameras();//得到摄像头的个数
//
//        for (int i = 0; i < cameraCount; i++) {
//            Camera.getCameraInfo(i, cameraInfo);//得到每一个摄像头的信息
//            if (cameraPosition == 1) {
//                //现在是后置，变更为前置
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                    mCamera.stopPreview();//停掉原来摄像头的预览
//                    mCamera.release();//释放资源
//                    mCamera = null;//取消原来摄像头
//                    mCamera = Camera.open(i);//打开当前选中的摄像头
//                    try {
//                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//开始预览
//                    cameraPosition = 0;
//                    break;
//                }
//            } else {
//                //现在是前置， 变更为后置
//                if (cameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_BACK) {//代表摄像头的方位，CAMERA_FACING_FRONT前置      CAMERA_FACING_BACK后置
//                    mCamera.stopPreview();//停掉原来摄像头的预览
//                    mCamera.release();//释放资源
//                    mCamera = null;//取消原来摄像头
//                    mCamera = Camera.open(i);//打开当前选中的摄像头
//                    try {
//                        mCamera.setPreviewDisplay(mHolder);//通过surfaceview显示取景画面
//                    } catch (IOException e) {
//                        // TODO Auto-generated catch block
//                        e.printStackTrace();
//                    }
//                    mCamera.startPreview();//开始预览
//                    cameraPosition = 1;
//                    break;
//                }
//            }
//
//        }
//    }
    /**
     *
     */

    /**
     * 开始预览相机内容
     */
    private void setStartPreview(Camera camera, SurfaceHolder holder) throws IOException {
        try {
            camera.setPreviewDisplay(holder);
            camera.setDisplayOrientation(90);//设置旋转角度为90度，将横屏转换为竖屏
            camera.startPreview();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 释放相机资源与SurfaceView
     */
    private void releaseCamera() {
        if (mCamera != null) {
            mCamera.setPreviewCallback(null);
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;//将整个相机资源释放
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //调用setStartPreview()方法，实现SurfaceView与Camera的绑定
        try {
            setStartPreview(mCamera, mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        //重启Preview
        mCamera.stopPreview();
        try {
            setStartPreview(mCamera, mHolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //释放掉所有资源
        releaseCamera();
    }

    //水平仪
    @Override
    public void onSensorChanged(SensorEvent event) {
        // 获取手机触发event的传感器的类型
        int sensorType = event.sensor.getType();
        switch (sensorType) {
            case Sensor.TYPE_ACCELEROMETER:
                accValues = event.values.clone();
                break;
            case Sensor.TYPE_MAGNETIC_FIELD:
                magValues = event.values.clone();
                break;

        }

        SensorManager.getRotationMatrix(r, null, accValues, magValues);
        SensorManager.getOrientation(r, values);

        // 获取　沿着Z轴转过的角度
        float azimuth = values[0];

        // 获取　沿着X轴倾斜时　与Y轴的夹角
        float pitchAngle = values[1];

        // 获取　沿着Y轴的滚动时　与X轴的角度
        //此处与官方文档描述不一致，所在加了符号（https://developer.android.google.cn/reference/android/hardware/SensorManager.html#getOrientation(float[], float[])）
        float rollAngle = -values[2];

        onAngleChanged(rollAngle, pitchAngle, azimuth);

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    /**
     * 角度变更后显示到界面
     *
     * @param rollAngle
     * @param pitchAngle
     * @param azimuth
     */
    private void onAngleChanged(float rollAngle, float pitchAngle, float azimuth) {

        //levelView.setAngle(rollAngle, pitchAngle);

        tvHorz.setText(String.valueOf((int)Math.toDegrees(rollAngle)) + "°");
        tvVert.setText(String.valueOf((int) Math.toDegrees(pitchAngle)) + "°");

    }

}
