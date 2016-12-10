package com.example.carlo.swagathon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.SurfaceView;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.JavaCameraView;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.android.OpenCVLoader;
import org.opencv.imgproc.Imgproc;

public class MainActivity extends AppCompatActivity implements CameraBridgeViewBase.CvCameraViewListener2 {
    JavaCameraView myCamera;
    Mat mRgba, imgGrey , imgCanny;
    BaseLoaderCallback mLoaderCallBack = new BaseLoaderCallback(this) {
        @Override
        public void onManagerConnected(int status) {
            switch(status){
                case BaseLoaderCallback.SUCCESS:
                    myCamera.enableView();
                    break;
                default:{
                    super.onManagerConnected(status);
                    break;
                }
            }
            super.onManagerConnected(status);
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        myCamera = (JavaCameraView)findViewById(R.id.java_camera_view);
        myCamera.setVisibility(SurfaceView.VISIBLE);
        myCamera.setCvCameraViewListener(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        if(myCamera != null){
            myCamera.disableView();
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        if(myCamera != null){
            myCamera.disableView();
        }
    }

    @Override
    protected void onResume(){
        super.onPause();
        if(OpenCVLoader.initDebug()){
            mLoaderCallBack.onManagerConnected(LoaderCallbackInterface.SUCCESS);
        }
        else{
            OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_3_1_0,this,mLoaderCallBack);
        }
    }

    @Override
    public void onCameraViewStarted(int width, int height) {
        mRgba = new Mat(height, width, CvType.CV_8UC4);
        imgGrey = new Mat(height, width, CvType.CV_8UC1);
        imgCanny = new Mat(height, width, CvType.CV_8UC1);
    }

    @Override
    public void onCameraViewStopped() {
        mRgba.release();
    }

    @Override
    public Mat onCameraFrame(CameraBridgeViewBase.CvCameraViewFrame inputFrame) {
        mRgba = inputFrame.rgba();
        Imgproc.cvtColor(mRgba,imgGrey,Imgproc.COLOR_RGB2GRAY);
        Imgproc.Canny(imgGrey,imgCanny,100,150);
        return imgCanny;
    }
}