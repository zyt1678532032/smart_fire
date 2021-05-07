package cn.com.lttc.loginui;

import android.app.Application;
import cn.com.lttc.loginui.utils.MqttManager;
import com.beardedhen.androidbootstrap.TypefaceProvider;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Android-Bootstrap UI框架
 */
public class SampleApplication extends Application {
    private static SampleApplication instance;
    //接警信息(用户)
    private List<String> receiveData = new LinkedList<>();
    //报警信息(消防员)
    private List<String> callData = new LinkedList<>();

    public List<String> getReceiveData() {
        return receiveData;
    }

    public static SampleApplication getInstance() {
        return instance;
    }

    public List<String> getCallData() {
        return callData;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        TypefaceProvider.registerDefaultIconSets();
    }
}
