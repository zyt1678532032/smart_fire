package cn.com.lttc.loginui.utils;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import android.widget.Toast;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.FiremanRadioTabActivity;
import cn.com.lttc.loginui.R;
import cn.com.lttc.loginui.SampleApplication;
import cn.com.lttc.loginui.fragments.InfoFragment;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Random;

/**
 * Mqtt工具
 */
public class MqttManager {


    private Context mContext;
    private Handler mHandler;
    private static MqttManager mMqttManager;
    private MqttAndroidClient mMqttAndroidClient;
    private MqttConnectOptions mMqttConnectOptions;
    //MQTT相关配置
    //服务器地址（协议+地址+端口号）
    private final static String HOST = "tcp://106.14.93.164:1883";
    //用户名
    private final static String USERNAME = "admin";
    //密码
    private final static String PASSWORD = "public";
    //发布主题=消防站站编号 ---- 向某处发消息
    // TODO: 2021/3/13 用户发布消息---->消防站  消防员 ---> 报警人手机号
    // TODO: 2021/3/14 后台已经根据报警人的小区编号获得消防站信息进而将用户报警信息进行发布,下面这个发布主题参数没用了
    private static String PUBLISH_TOPIC = "";
    //订阅主题=用户手机号  ---- 收某处的消息
    // TODO: 2021/3/13  用户收到接警消息--->本人手机号  消防员 ---> 消防站
    private static String SUBSCRIBE_TOPIC = "";
    //服务质量,0最多一次，1最少一次，2只一次
    private final static int QUALITY_OF_SERVICE = 1;
    //重连hand
    public final static int HANDLE_CONNECT_FAILED = 0x1000;
    public final static int HANDLE_CONNECT_BROKEN = 0x1001;

    public MqttManager() {
    }

    private MqttManager(Context context) {
        this.mContext = context;
        init();
    }

    /**
     * 私有构造器--->单例模式
     * @param context 当前处境
     * @return MqttManager
     */
    public static MqttManager getInstance(Context context) {
        if (mMqttManager == null) {
            mMqttManager = new MqttManager(context);
        }
        return mMqttManager;
    }

    /**
     * 设置订阅主题
     * @param subscribeTopic 订阅主题
     */
    public static void setSubscribeTopic(String subscribeTopic,String role) {
        SUBSCRIBE_TOPIC = subscribeTopic;
        Log.e(role+"订阅主题", subscribeTopic);
    }

    /**
     * 设置发布主题
     * @param publishTopic 发布主题
     */
    public static void setPublishTopic(String publishTopic,String role) {
        PUBLISH_TOPIC = publishTopic;
        Log.e(role+"发布主题", publishTopic);
    }

    private void init() {
        mMqttConnectOptions = new MqttConnectOptions();
        mMqttConnectOptions.setCleanSession(true); //设置是否清除缓存
        mMqttConnectOptions.setConnectionTimeout(10); //设置超时时间，单位：秒
        mMqttConnectOptions.setKeepAliveInterval(30); //设置心跳包发送间隔，单位：秒
        mMqttConnectOptions.setUserName(USERNAME); //设置用户名
        mMqttConnectOptions.setPassword(PASSWORD.toCharArray()); //设置密码
        // 异常断开连接前发送的信息
        String message = "{\"Dced\":\"" + 1 + "\"}";
        String topic = PUBLISH_TOPIC;
        Integer qos = QUALITY_OF_SERVICE;
        Boolean retained = false;
        try {
            //mMqttConnectOptions.setWill(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * MQTT是否连接成功的监听
     */
    private IMqttActionListener iMqttActionListener = new IMqttActionListener() {
        @Override
        public void onSuccess(IMqttToken arg0) {
            Log.i("--->mqtt", "连接成功 ");
            try {
                //订阅主题，参数：主题、服务质量
                mMqttAndroidClient.subscribe(SUBSCRIBE_TOPIC, QUALITY_OF_SERVICE);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onFailure(IMqttToken arg0, Throwable arg1) {
            arg1.printStackTrace();
            Log.i("--->mqtt", "onFailure 连接失败:" + arg1.getMessage());
            sendHandlerMsg(HANDLE_CONNECT_FAILED, 0, null);
        }
    };

    /**
     * 订阅主题的回调
     */
    private MqttCallback mqttCallback = new MqttCallback() {
        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        public void messageArrived(String topic, MqttMessage message) throws Exception {
            Log.e("message", new String(message.getPayload()));
            String message_ = new String(message.getPayload());
            if (message_.contains("报警时间")) {//消防员
                ((LinkedList) SampleApplication.getInstance().getCallData()).addFirst(new String(message.getPayload()) + "\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                InfoFragment.adapter.notifyDataSetChanged();
                //写入本地文件(缓存)
                //FileTools.addWriteFile("Callinfor.txt", new String(message.getPayload()) + "\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n\n");
            }else{//用户
                Log.e("用户", "接警反馈信息");
                LinkedList linkedList = (LinkedList) SampleApplication.getInstance().getReceiveData();
                linkedList.clear();
                linkedList.addFirst(new String(message.getPayload()) + "\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                InfoFragment.adapter.notifyDataSetChanged();
                //写入本地文件
                //FileTools.addWriteFile("ReceiveInfor.txt", new String(message.getPayload()) + "\n" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "\n\n");
            }


        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken arg0) {
            Log.i("--->mqtt", "deliveryComplete");
        }

        @Override
        public void connectionLost(Throwable arg0) {
            Log.i("--->mqtt", "连接断开");
            sendHandlerMsg(HANDLE_CONNECT_BROKEN, 0, null);
        }
    };

    /**
     * 建立mqtt连接，连接MQTT服务器
     */
    public boolean connect(String clientId, Handler handler) {
        this.mHandler = handler;
        //创建Mqtt客户端
        mMqttAndroidClient = new MqttAndroidClient(mContext, HOST, clientId);
        mMqttAndroidClient.setCallback(mqttCallback); //设置订阅消息的回调
        //建立连接
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mMqttAndroidClient.connect(mMqttConnectOptions, null, iMqttActionListener);
                } catch (Exception e) {
                    Log.e("--->mqtt", "connect: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }).start();
        return true;
    }

    /**
     * 发布消息
     */
    public void publish(String message) {
        String topic = PUBLISH_TOPIC;
        Integer qos = QUALITY_OF_SERVICE;
        Boolean retained = false;
        try {
            if (mMqttAndroidClient != null && mMqttAndroidClient.isConnected()) {
                //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
                mMqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
            } else {
                Log.i("--->mqtt", "mqttAndroidClient is Null or is not connected");
            }
        } catch (Exception e) {
            Log.i("--->mqtt", "publish MqttException:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 响应订阅消息
     */
    public void response(String message) {
        String topic = SUBSCRIBE_TOPIC;
        Integer qos = QUALITY_OF_SERVICE;
        Boolean retained = false;
        try {
            //参数分别为：主题、消息的字节数组、服务质量、是否在服务器保留断开连接后的最后一条消息
            mMqttAndroidClient.publish(topic, message.getBytes(), qos.intValue(), retained.booleanValue());
        } catch (Exception e) {
            Log.i("--->mqtt", "publish:" + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * 断开链接
     */
    public void disconnect() {
        try {
            if (mMqttAndroidClient != null) {

//                mMqttAndroidClient.unregisterResources();
//                mMqttAndroidClient.disconnect();
                mMqttAndroidClient = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送handler消息
     */
    private void sendHandlerMsg(int arg1, int arg2, Object obj) {
        if (mHandler == null) {
            return;
        }
        Message message = new Message();
        message.arg1 = arg1;
        message.arg2 = arg2;
        message.obj = obj;
        mHandler.sendMessage(message);
    }



}

