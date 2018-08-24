package com.example.fengshow.appforricenitrogen;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    //Button Click事件
    //点击跳转至CameraForCanopy
    public void turnToCanopyCamera(View view){
        Intent intent=new Intent();
        intent.setClass(MainActivity.this,CameraForCanopy.class);
        startActivity(intent);
    }

//    //Button Click事件
//    //点击跳转至CameraForLeafNitrogen
//    public void turnToLeafNitrogenCamera(View view){
//        Intent intent1=new Intent();
//        intent1.setClass(MainActivity.this,CameraForLeafNitrogen.class);
//        startActivity(intent1);
//    }

    //Button Click事件
    //点击跳转至CameraForLeafNitrogenWithWireframe
    public void turnToLeafNitrogenCameraWithWireframe(View view){
        Intent intent1=new Intent();
        intent1.setClass(MainActivity.this,CameraForLeafNitrogenWithWireframe.class);
        startActivity(intent1);
    }
}
