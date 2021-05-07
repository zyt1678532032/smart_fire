package cn.com.lttc.loginui;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.utils.DataGenerator;
import cn.com.lttc.loginui.utils.MqttManager;
import cn.com.lttc.loginui.utils.ScreenAdaptUtils;

import java.util.UUID;

/**
 * 消防员布局活动
 */

public class FiremanRadioTabActivity extends BaseActivity {

    private RadioGroup mRadioGroup;
    private Fragment[] mFragments;
    private RadioButton mRadioButtonHome;
    //
    private MqttManager m_mqttManager = MqttManager.getInstance(this);
    Handler handler = new Handler();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fireman_tab_layout);
        Log.e("消防员数据", getIntent().getStringExtra("data"));

        //from:帧的标题
        mFragments = DataGenerator.getFireFragments("");
        initView();
        String uuid = UUID.randomUUID().toString().replaceAll("-", "");
        //连接
        //用
        m_mqttManager.connect(uuid, handler);


    }

    /**
     * 返回键不销毁程序
     *
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            moveTaskToBack(true);//true对任何Activity都适用
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void initView() {
        mRadioGroup = findViewById(R.id.radio_group_button);
        mRadioButtonHome = findViewById(R.id.radio_button_home);
        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            Fragment mFragment = null;

            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                switch (checkedId) {
                    case R.id.radio_button_home:
                        mFragment = mFragments[0];
                        break;
                    case R.id.radio_button_discovery:
                        mFragment = mFragments[1];
                        break;
                    case R.id.radio_button_profile:
                        mFragment = mFragments[2];
                        break;
                    case R.id.radio_button_my:
                        mFragment = mFragments[3];
                        break;
                    default:
                        break;
                }
                if (mFragments != null) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.home_container, mFragment).commit();
                }
            }
        });
        // 保证第一次会回调OnCheckedChangeListener
        mRadioButtonHome.setChecked(true);
    }
}
