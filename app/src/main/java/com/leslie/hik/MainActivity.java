package com.leslie.hik;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.SurfaceView;

import com.leslie.hik.utils.HikUtil;

import butterknife.ButterKnife;

public class MainActivity extends Activity {
    private static final String TAG = "MainActivity";
    //----------------------------------------------------------------------------------------------
    SurfaceView surfaceView;
    //----------------------------------------------------------------------------------------------
    private static final int PLAY_HIK_STREAM_CODE = 1001;
    private static final String IP_ADDRESS = "192.168.70.222";
    private static final int PORT = 8000;
    private static final String USER_NAME = "admin";
    private static final String PASSWORD = "eyecool2016";
    //----------------------------------------------------------------------------------------------

    private Handler mHandler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {
                case PLAY_HIK_STREAM_CODE:
                    HikUtil.playOrStopStream();
                    break;
                default:
                    break;
            }
            return false;
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        surfaceView = findViewById(R.id.surfaceView);

        HikUtil.initSDK();
        HikUtil.initView(surfaceView);
        HikUtil.setDeviceData(IP_ADDRESS, PORT, USER_NAME, PASSWORD);
        HikUtil.loginDevice(mHandler, PLAY_HIK_STREAM_CODE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        HikUtil.playOrStopStream();
    }
}
