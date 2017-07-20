package com.leslie.hik.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hikvision.netsdk.ExceptionCallBack;
import com.hikvision.netsdk.HCNetSDK;
import com.hikvision.netsdk.NET_DVR_DEVICEINFO_V30;
import com.hikvision.netsdk.NET_DVR_PREVIEWINFO;
import com.hikvision.netsdk.RealPlayCallBack;
import com.leslie.hik.Constant;

import org.MediaPlayer.PlayM4.Player;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;

/**
 * Created date: 2017/4/11
 * Author:  Leslie
 * 使用海康SDK播放视频流的工具类.
 * 前提：1->/libs/下放入：AudioEngineSDK.jar,HCNetSDK.jar,PlayerSDK.jar
 * 2->/src/main/jniLibs/下放入：很多 .so 文件.
 * 3->添加网络权限
 * 目前只处理海康摄像头(室内枪型网络摄像机-【型号：DS-2CD5026EFWD】-【软件版本：V5.4.5_170222】)
 * 但是该例子不仅限于这种型号的。
 * 使用方法：
 * 1.HikUtil.initSDK();
 * 2.HikUtil.initView(surfaceView);
 * 3.HikUtil.setDeviceData("192.168.1.22",8000,"admin","eyecool2016");
 * 4.HikUtil.loginDevice(mHandler,LOGIN_SUCCESS_CODE);
 * 5.HikUtil.playOrStopStream();
 */

public class HikUtil {
    private static final String TAG = "HikUtil";
    private static NET_DVR_DEVICEINFO_V30 m_oNetDvrDeviceInfoV30 = null;
    private static int m_iStartChan = 0;
    private static int m_iPort = -1;
    private static int m_iPlaybackID = -1;
    private static int logId = -1;
    private static int playId = -1;
    private static SurfaceView mSurfaceView;
    public static String mIpAddress;
    private static int mPort;
    private static String mUserName;
    private static String mPassWord;
    public static onPicCapturedListener mPicCapturedListener;
    private static SimpleDateFormat sDateFormat;
    private static Player.MPInteger stWidth;
    private static Player.MPInteger stHeight;
    private static Player.MPInteger stSize;

    /**
     * 定义接口，用于监听图片截图成功
     */
    public interface onPicCapturedListener {
        void onPicCaptured(Bitmap bitmap, String bitmapFileAbsolutePath);

        void onPicDataSaved(byte[] picData);
    }


    /**
     * 初始化HCNet SDK
     *
     * @return
     */
    public static boolean initSDK() {

        if (!HCNetSDK.getInstance().NET_DVR_Init()) {
            Log.e(TAG, "HCNetSDK ---------初始化失败!");
            return false;
        }
        //打印日志到本地，暂时不用打印
//        HCNetSDK.getInstance().NET_DVR_SetLogToFile(3, "/mnt/sdcard/sdklog/", true);
        return true;
    }

    public static void initView(SurfaceView surfaceView) {
        mSurfaceView = surfaceView;
        mSurfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mSurfaceView.getHolder().setFormat(PixelFormat.TRANSLUCENT);
                Log.i(TAG, "surface is created" + m_iPort);
                if (-1 == m_iPort) {
                    return;
                }
                Surface surface = holder.getSurface();
                if (true == surface.isValid()) {
                    if (false == Player.getInstance().setVideoWindow(m_iPort, 0, holder)) {
                        Log.e(TAG, "播放器设置或销毁显示区域失败!");
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                Log.i(TAG, "Player setVideoWindow release!" + m_iPort);
                if (-1 == m_iPort) {
                    return;
                }
                if (true == holder.getSurface().isValid()) {
                    if (false == Player.getInstance().setVideoWindow(m_iPort, 0, null)) {
                        Log.e(TAG, "播放器设置或销毁显示区域失败!");
                    }
                }
            }
        });
    }

    /**
     * 配置网络摄像头参数
     * @param ipAddress IP 地址
     * @param port 端口号，默认是 8000
     * @param userName 用户名
     * @param passWord 密码
     */
    public static void setDeviceData(String ipAddress, int port, String userName, String passWord) {
        mIpAddress = ipAddress;
        mPort = port;
        mUserName = userName;
        mPassWord = passWord;

    }

    public static void loginDevice(final Handler handler, final int resultCode) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean loginState = login(mIpAddress, mPort, mUserName, mPassWord);
                Message message = handler.obtainMessage();
                message.obj = loginState;
                message.what = resultCode;
                handler.sendMessage(message);
            }
        }).start();
    }

    /**
     * 播放或者停止播放视频流
     */
    public static void playOrStopStream() {

        if (logId < 0) {
            Log.e(TAG, "请先登录设备");
            return;
        }
        if (playId < 0) {   //播放

            RealPlayCallBack fRealDataCallBack = getRealPlayerCbf();
            if (fRealDataCallBack == null) {
                Log.e(TAG, "fRealDataCallBack object is failed!");
                return;
            }
            Log.i(TAG, "m_iStartChan:" + m_iStartChan);

            NET_DVR_PREVIEWINFO previewInfo = new NET_DVR_PREVIEWINFO();
            previewInfo.lChannel = m_iStartChan;
            previewInfo.dwStreamType = Constant.HIK_BRANCH_STREAM_CODE;                                                             //子码流
            previewInfo.bBlocked = 1;
            // HCNetSDK start preview
            playId = HCNetSDK.getInstance().NET_DVR_RealPlay_V40(logId, previewInfo, fRealDataCallBack);
            if (playId < 0) {
                Log.e(TAG, "实时预览失败!-----------------Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return;
            }

            Log.i(TAG, "NetSdk 播放成功 ！");
//            mPlayButton.setText("停止");
        } else {    //停止播放
            if (playId < 0) {
                Log.e(TAG, "m_iPlayID < 0");
                return;
            }

            //  net sdk stop preview
            if (!HCNetSDK.getInstance().NET_DVR_StopRealPlay(playId)) {
                Log.e(TAG, "停止预览失败!----------------错误:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
                return;
            }

            playId = -1;
            Player.getInstance().stopSound();
            // player stop play
            if (!Player.getInstance().stop(m_iPort)) {
                Log.e(TAG, "-------------------暂停失败!");
                return;
            }

            if (!Player.getInstance().closeStream(m_iPort)) {
                Log.e(TAG, "-------------------关流失败!");
                return;
            }
            if (!Player.getInstance().freePort(m_iPort)) {
                Log.e(TAG, "-------------------释放播放端口失败!" + m_iPort);
                return;
            }
            m_iPort = -1;
            logId = -1;
            playId = -1;
//            mPlayButton.setText("播放");
        }

    }

    private static RealPlayCallBack getRealPlayerCbf() {
        RealPlayCallBack cbf = new RealPlayCallBack() {
            public void fRealDataCallBack(int iRealHandle, int iDataType, byte[] pDataBuffer, int iDataSize) {
                // 播放通道1
                processRealData(1, iDataType, pDataBuffer, iDataSize, Player.STREAM_REALTIME);
            }
        };
        return cbf;
    }

    public static void processRealData(int iPlayViewNo, int iDataType, byte[] pDataBuffer, int iDataSize, int iStreamMode) {
        if (HCNetSDK.NET_DVR_SYSHEAD == iDataType) {
            if (m_iPort >= 0) {
                return;
            }
            m_iPort = Player.getInstance().getPort();
            if (m_iPort == -1) {
                Log.e(TAG, "获取端口失败！: " + Player.getInstance().getLastError(m_iPort));
                return;
            }
            Log.i(TAG, "获取端口成功！: " + m_iPort);
            if (iDataSize > 0) {
                if (!Player.getInstance().setStreamOpenMode(m_iPort, iStreamMode))  //set stream mode
                {
                    Log.e(TAG, "设置流播放模式失败！");
                    return;
                }
                if (!Player.getInstance().openStream(m_iPort, pDataBuffer, iDataSize, 2 * 1024 * 1024)) //open stream
                {
                    Log.e(TAG, "打开流失败！");
                    return;
                }
                if (!Player.getInstance().play(m_iPort, mSurfaceView.getHolder())) {
                    Log.e(TAG, "播放失败！");
                    return;
                }
                if (!Player.getInstance().playSound(m_iPort)) {
                    Log.e(TAG, "以独占方式播放音频失败！失败码 :" + Player.getInstance().getLastError(m_iPort));
                    return;
                }
            }
        } else {
            if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize)) {
//		    		Log.e(TAG, "inputData failed with: " + Player.getInstance().getLastError(m_iPort));
                for (int i = 0; i < 4000 && m_iPlaybackID >= 0; i++) {
                    if (!Player.getInstance().inputData(m_iPort, pDataBuffer, iDataSize))
                        Log.e(TAG, "输入流数据失败: " + Player.getInstance().getLastError(m_iPort));
                    else
                        break;
                    try {
                        Thread.sleep(10);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();

                    }
                }
            }

        }

    }


    private static boolean login(String ipAddress, int portNum, String userName, String passWord) {
        try {
            if (logId < 0) {
                // 登录设备
                logId = loginDevice(ipAddress, portNum, userName, passWord);
                if (logId < 0) {
                    Log.e(TAG, "设备登录失败！");
                    return false;
                }
                // 获取异常回调和异常设置的回调
                ExceptionCallBack oexceptionCbf = getExceptiongCbf();
                if (oexceptionCbf == null) {
                    Log.e(TAG, "异常回调对象失败！");
                    return false;
                }

                if (!HCNetSDK.getInstance().NET_DVR_SetExceptionCallBack(oexceptionCbf)) {
                    Log.e(TAG, "注册接收异常、重连消息回调函数失败 !");
                    return false;
                }

//                loginButton.setText("注销");
                Log.i(TAG, "登录成功 ！");
                return true;
            } else {
                // 是否登出
                if (!HCNetSDK.getInstance().NET_DVR_Logout_V30(logId)) {
                    Log.e(TAG, " 用户注销失败!");
                    return false;
                }
//                loginButton.setText("登录");
                logId = -1;
                return true;
            }
        } catch (Exception err) {
            Log.e(TAG, "错误: " + err.toString());
            return false;
        }
    }

    private static int loginDevice(String ipAddress, int portNum, String userName, String passWord) {
        //实例化设备信息对象
        m_oNetDvrDeviceInfoV30 = new NET_DVR_DEVICEINFO_V30();
        if (null == m_oNetDvrDeviceInfoV30) {
            Log.e(TAG, "实例化设备信息(NET_DVR_DEVICEINFO_V30)失败!");
            return -1;
        }
        // call NET_DVR_Login_v30 to login on, port 8000 as default
        int iLogID = HCNetSDK.getInstance().NET_DVR_Login_V30(ipAddress, portNum, userName, passWord, m_oNetDvrDeviceInfoV30);
        if (iLogID < 0) {
            Log.e(TAG, "网络设备登录失败!-------------Err:" + HCNetSDK.getInstance().NET_DVR_GetLastError());
            return -1;
        }
        if (m_oNetDvrDeviceInfoV30.byChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartChan;
        } else if (m_oNetDvrDeviceInfoV30.byIPChanNum > 0) {
            m_iStartChan = m_oNetDvrDeviceInfoV30.byStartDChan;
        }
        Log.i(TAG, "网络设备登录成功!");

        return iLogID;
    }

    private static ExceptionCallBack getExceptiongCbf() {
        ExceptionCallBack oExceptionCbf = new ExceptionCallBack() {
            public void fExceptionCallBack(int iType, int iUserID, int iHandle) {
                System.out.println("recv exception------------------------------, type:" + iType);
            }
        };
        return oExceptionCbf;
    }

    /**
     * 截取一帧图片,成功返回bitmap对象，失败返回null
     * 经测试得出：
     * 获取截图数据耗时 <10ms
     * 获取截图数据后保存到磁盘耗时 ≈25ms
     * 从获取截图数-保存到磁盘-解码文件到 bitmap 耗时 ≈45ms
     */
    public static Bitmap captureFrame(onPicCapturedListener picCapturedListener) {
        try {
            long time1 = System.currentTimeMillis();
            mPicCapturedListener = picCapturedListener;
            Player.MPInteger stWidth = new Player.MPInteger();
            Player.MPInteger stHeight = new Player.MPInteger();
            if (!Player.getInstance().getPictureSize(m_iPort, stWidth, stHeight)) {
                Log.e(TAG, "获取图片尺寸失败---> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            int nSize = 5 * stWidth.value * stHeight.value;
            byte[] picBuf = new byte[nSize];
            Player.MPInteger stSize = new Player.MPInteger();
            if (!Player.getInstance().getBMP(m_iPort, picBuf, nSize, stSize)) {
                Log.e(TAG, "获取位图失败----> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            long time2 = System.currentTimeMillis();
            if (sDateFormat == null) {
                sDateFormat = new SimpleDateFormat("yyyy-MM-dd-hh_mm_ss_Sss");
            }
            String date = sDateFormat.format(new java.util.Date());
            File dir = new File(Environment.getExternalStorageDirectory() + "/capture");
            if (!dir.exists()) {
                dir.mkdir();
            }
            File file = new File(dir, date + ".jpg");
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(picBuf, 0, stSize.value);
            fos.close();
            long time3 = System.currentTimeMillis();
            Bitmap bitmap = BitmapFactory.decodeFile(dir.getAbsolutePath() + "/" + date + ".jpg");
            long time4 = System.currentTimeMillis();
            //图片保存成功了，通知给外面
            mPicCapturedListener.onPicCaptured(bitmap, file.getAbsolutePath());
            return bitmap;
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        } finally {

            return null;
        }
    }

    /**
     * 截取一帧图片,成功返回bitmap对象，失败返回null
     * 图片数据存放在内存中
     */
    public static byte[] captureFrameOnMemroy(onPicCapturedListener picCapturedListener, Handler handler) {
        try {
            long start = System.currentTimeMillis();
            mPicCapturedListener = picCapturedListener;
            if (stWidth == null) {
                stWidth = new Player.MPInteger();
            }
            if (stHeight == null) {
                stHeight = new Player.MPInteger();
            }
            if (!Player.getInstance().getPictureSize(m_iPort, stWidth, stHeight)) {
                Log.e(TAG, "获取图片尺寸失败---> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            int nSize = 5 * stWidth.value * stHeight.value;
            byte[] picBuf = new byte[nSize];
            if (stSize == null) {

                stSize = new Player.MPInteger();
            }
            if (!Player.getInstance().getBMP(m_iPort, picBuf, nSize, stSize)) {
//                mPicCapturedListener.onPicDataSavedError();
                Log.e(TAG, "获取位图失败----> error code:" + Player.getInstance().getLastError(m_iPort));
                return null;
            }
            //图片保存数据获取成功了，通知给外面。或者用handler发送出去
            mPicCapturedListener.onPicDataSaved(picBuf);
            Message message = handler.obtainMessage();
            message.obj = picBuf;
            message.what = Constant.VIDEO_FRAME_PIC_DATA_SAVED;
            handler.sendMessage(message);
            long end = System.currentTimeMillis();
            return picBuf;
        } catch (Exception err) {
            Log.e(TAG, "error: " + err.toString());
        }
        return null;
    }
}
