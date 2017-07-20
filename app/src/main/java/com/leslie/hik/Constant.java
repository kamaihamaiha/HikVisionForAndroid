package com.leslie.hik;


/**
 * Created by Administrator on 2016/12/14 0014.
 */
public interface Constant {

    //摄像头数据
    String IP_CAMERA_IP_ADDRESS = "192.168.60.22";
    int IP_CAMERA_PORT_NUM = 8000;
    String IP_CAMERA_USER_NAME = "admin";
    String IP_CAMERA_USER_PWD = "eyecool2016";
    boolean IS_USB_CAMERA = false;
    int HIK_MAIN_STREAM_CODE = 0;
    int HIK_BRANCH_STREAM_CODE = 1;

    //SP 文件中 key 名
    String KEY_IS_REFRESH_SIGNED_TIME = "is_refresh_signed_time";
    String KEY_PAGE_ADDRESS = "page_address";


    //网络请求数据
    String FACE_SIGN_SEARCH_URL = "http://192.168.60.52:8080/cmbcvip/face/search";
    String RES_SUCCESS_CODE = "0000";
    String RES_REPEAT_CODE = "4000";
    String RES_NULL_CODE = "3000";
    String RES_LOW_SCORE_CODE = "5000";
    String JSON_STRING_NAME_RES_CODE = "res_code";
    int FACE_SEARCH_SUCCESS_CODE = 2001;
    int FACE_SEARCH_REPEAT_CODE = 2003;
    int FACE_SEARCH_NULL_CODE = 2005;
    int FACE_SEARCH_ERROR_CODE = 2007;
    int FACE_SEARCH_FINISH_CODE = 2009;
    int FACE_SEARCH_SUCCESS_USED_TIME_CODE = 2002;
    int FACE_SEARCH_REPEAT_USED_TIME_CODE = 2004;
    int FACE_SEARCH_NULL_USED_TIME_CODE = 2006;
    int FACE_SEARCH_ERROR_USED_TIME_CODE = 2008;

    int AYSN_REQUEST_THREAD_SLEEP_TIME = 300;
    /**
     * 截图人脸 base64 集合大小
     */
    int FACE_BASE64_LIST_SIZE = 3;

    /**
     * 连续不截图次数
     */
    int CAN_NOT_CAPTURE_COUNT = 5;
    /**
     * 连续不请求结果次数
     */
    int CAN_NOT_REQUEST_COUNT = 8;

    /**
     * 截原图集合默认大小
     */
    int FACE_BITMAP_LIST_SIZE = 5;

    /**
     * 异步get网络请求线程锁对象
     */
    /**
     * 人脸加框线程
     */

    String CAMERA_BRAND_HIKVISION = "HIKVISION";
    String CAMERA_BRAND_USB = "USB";

    /**
     * 海康摄像头后缀
     */
    String RTSP_URL_SUFFIX_1 = Constant.PORT_NUM_OF_HIKVISION + "/h264/ch1/sub/av_stream";


    /**
     * 海康摄像头前缀
     */
    String RTSP_URL_PREFIX_HIKVISION = "rtsp://admin:eyecool2016@";

    /**
     * 海康摄像头端口号
     */
    String PORT_NUM_OF_HIKVISION = ":554";
    /**
     * 设置界面数据存储文件名（不包括扩展名）
     */
    String SETTING_DATA_SP_FILE = "setting_data";

    /**
     * 1.横屏正
     */
    int SCREEN_STATE_UP = 1;

    /**
     * 2.横屏反
     */
    int SCREEN_STATE_DOWN = 2;

    /**
     * 3.竖屏正
     */
    int SCREEN_STATE_LEFT = 3;

    /**
     * 4.竖屏反
     */
    int SCREEN_STATE_RIGHT = 4;


    /**
     * 蒙版黑色值
     */
    int BG_BLACK_VALUE = 180;

    /**
     * 广告地址测试用
     */
    String AD_URL_1 = "http://192.168.86.89:8080/icool/main/pages/login.html";

    /**
     * 切换广告时间，单位是秒
     */
    int SHIFT_AD_SECONDS = 10;


    /**
     * 开启广告模式
     */
    boolean SHOW_AD_MODE = false;

    /**
     * RTSP视频流地址
     */
    String urlSony1 = "rtsp://admin:admin@192.168.1.229/media/video1";

    /**
     * 后台注册接口
     */
    String REGISTER_URL = "http://192.168.1.18:8080/cmbcvip/metting/register.jsp";

    /**
     * 后台比对人脸接口
     */
    String CHECK_FACE_URL = "http://192.168.1.18:8080/cmbcvip/face/get";
    /**
     * 服务器IP地址
     */
    String SERVER_IP_ADDRESS = "192.168.1.18";

    /**
     * 存放签到用户图片的文件夹
     */
    String SIGNED_USER_PICS_DIR = "/user_pics";

    /**
     * 存放签到用户信息的文件
     */
    String SIGNED_USER_INFO_FILE = "signed_user_info.txt";
    String SIGNED_USER_EARLY_INFO_FILE = "signed_user_early_info.txt";

    /**
     * 签到用户id信息文件
     */
    String SIGNED_USER_ID_INFO_FILE = "signed_user_id_info.txt";


    /**
     * 检测日志的文件名
     */

    String CHECK_LOG = "check_log.txt";

    /**
     * 弹窗时的log
     */
    String SHOW_WECLOME_LOG = "show_welcome_log.txt";

    String POP_LOG = "pop_log.txt";

    /**
     * 注册日志文件名
     */
    String REGISTER_LOG = "注册日志.txt";

    /**
     * 启动界面日志文件名,包括MainSettingActivity和InitActivity
     */
    String MAIN_ACTIVITY_LOG = "启动界面日志.txt";

    /**
     * 请求网络日志
     */
    String REQUEST_NET_LOG = "请求网络日志.txt";

    /**
     * 累计比对人次日志文件名
     */
    String TOTAL_CHECK_FACE_TIMES_LOG = "累计比对人次日志.txt";

    /**
     * 存放在SP中的累计比对人次的文件名
     */
    String TOTAL_CHECK_FACE_TIMES_SP = "total_check_face_times";

    /**
     * 时钟和视频流刷新日志文件名
     */
    String CLOCK_AND_RTSP_REFRESH_LOG = "时钟和视频流刷新日志.txt";

    /**
     *
     */
    String CHECK_UI_DESTORY_LOG = "检测界面销毁日志.txt";

    /**
     * 累计比对人次图片保存的文件夹
     */
    String TOTAL_CHECK_PERSON_TIME_PICS_DIR = "累计比对人次图片";

    /**
     * 打开广告的识别码
     */
    int OPEN_AD_PAGE_CODE = 999;

    /**
     * 关闭广告的识别码
     */
    int CLOSE_AD_PAGE_CODE = 9999;

    /**
     * 获取后台比对结果的间隔时间
     */
    int GET_CHECK_RESULT_INTERVAL_TIME = 300;

    /**
     * 从视频流中截取完一帧图片的到下一次开始截取的间隔时间
     */
    int CAPTURE_STREAM_INTERVAL_TIME = 0;

    /**
     * 刷新视频流的时间，单位为毫秒
     */
    int REFRESH_RTSP_TIME = 30000;

    /**
     * 刷新时钟的时间，单位为毫秒
     */
    int REFRESH_CLOCK_TIME = 1000;

    /**
     * 弹窗显示时间
     */
    int POP_WINDOW_TIME = 1000;

    /**
     * 欢迎用户的卡片显示用户图片的View的宽
     */
    int BIG_CARD_USER_INFO_HEAD_IMG_WIDTH = 218;

    /**
     * 用户条目头像图片的View的宽
     */
    int USER_ITEM_HEAD_IMG_WIDTH = 130;

    /**
     * 用户条目头像图片的View的高
     */
    int USER_ITEM_HEAD_IMG_HEIGHT = 130;

    /**
     * 欢迎用户的卡片显示用户图片的View的高
     */
    int BIG_CARD_USER_INFO_HEAD_IMG_HEIGHT = 218;

    int REQUEST_CHECK_FACE_CODE = 9;                                                             //后台比对人脸的响应码

    int VIDEO_VIEW_SUCCESS_CODE = 1;                                                            //videoView播放成功标记码
    int START_CUT_DOWN = 2;                                                                      //可以倒计时拍照的标记码
    int REFRESH_CLOCK_TIME_CODE = 4;                                                            //刷新时间的标记码

    int CHECK_START_CODE = 3;                                                                    //开始比对的标记码
    int START_TAKE_PHOTO_CODE = 5;                                                              //开始拍照的标记码

    int TIME_OUT_BACK_TO_DEFAULT_UI = 6;
    int SHOW_NEW_USER_INFO = 7;


    int REFRESH_RTSP_CODE = 8;
    int SHOW_POP_WINDOW_CODE = 10;
    int SHOW_WECLOME_VIEW_CODE = 11;
    int ON_SUCCESS_CODE = 1991;
    int ON_NULL_CODE = 1992;
    int ON_ERROR_CODE = 1993;
    int ON_IMG_SAVE_FINISH_CODE = 1994;
    int LOGIN_DEVICE_SUCCESS_CODE = 1995;
    int ON_PIC_CAPTURED_CODE = 1996;
    /**
     * 视频一帧图片数据获取成功
     */
    int VIDEO_FRAME_PIC_DATA_SAVED = 2011;


    String DEFAULT_COMPANY_NAME = "公司名";
    String DEFAULT_POSITION_NAME = "职位";
}
