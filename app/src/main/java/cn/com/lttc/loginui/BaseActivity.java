package cn.com.lttc.loginui;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import cn.com.lttc.loginui.utils.ScreenAdaptUtils;

public class BaseActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ScreenAdaptUtils.setCustomDesity(this, getApplication(), 360);
    }
}
