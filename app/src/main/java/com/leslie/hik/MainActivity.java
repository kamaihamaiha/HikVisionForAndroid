package com.leslie.hik;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;

import com.leslie.hik.utils.HikUtil;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    //----------------------------------------------------------------------------------------------
    SurfaceView surfaceViewLeft;
    SurfaceView surfaceViewRight;
    //----------------------------------------------------------------------------------------------
    private static final int PLAY_HIK_STREAM_CODE = 1001;
    private static final int PLAY_HIK_STREAM_CODE_2 = 1002;
    private static final String IP_ADDRESS = "192.168.70.222";
    private static final String IP_ADDRESS_2 = "192.168.60.199";
    private static final int PORT = 8000;
    private static final String USER_NAME = "admin";
    private static final String USER_NAME_2 = "admin";
    private static final String PASSWORD = "eyecool2016";
    private static final String PASSWORD_2 = "techshino2017";
    //----------------------------------------------------------------------------------------------

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_HIK_STREAM_CODE:
                    hikUtil.playOrStopStream();
                    break;
                case PLAY_HIK_STREAM_CODE_2:
                    hikUtil2.playOrStopStream();
                    break;
                default:
                    break;
            }
            return false;
        }
    });
    private HikUtil hikUtil;
    private HikUtil hikUtil2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceViewLeft = findViewById(R.id.surfaceViewLeft);
        surfaceViewRight = findViewById(R.id.surfaceViewRight);

        HikUtil.initSDK();
        hikUtil = new HikUtil();
        hikUtil.initView(surfaceViewLeft);
        hikUtil.setDeviceData(IP_ADDRESS, PORT, USER_NAME, PASSWORD);
        hikUtil.loginDevice(mHandler, PLAY_HIK_STREAM_CODE);

        hikUtil2 = new HikUtil();
        hikUtil2.initView(surfaceViewRight);
        hikUtil2.setDeviceData(IP_ADDRESS_2, PORT, USER_NAME_2, PASSWORD_2);
        hikUtil2.loginDevice(mHandler, PLAY_HIK_STREAM_CODE_2);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        hikUtil.playOrStopStream();
        hikUtil2.playOrStopStream();
    }
}
