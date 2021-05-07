package cn.com.lttc.loginui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * 欢迎页面活动
 */
public class WelcomeActivity extends BaseActivity {
    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_welcome);

        SharedPreferences preferences = getPreferences(Context.MODE_PRIVATE);
        boolean isFirst = preferences.getBoolean("isFirst", true);
        mViewPager = findViewById(R.id.viewPage);
        List<View> views = new ArrayList<>();
        LayoutInflater inflater = getLayoutInflater();

        if (isFirst) {//第一次使用该应用
            preferences.edit().putBoolean("isFirst", false).apply();
            views.add(inflater.inflate(R.layout.welcome1_layout, null));
            views.add(inflater.inflate(R.layout.welcome2_layout, null));
            views.add(inflater.inflate(R.layout.welcome3_layout, null));
            mViewPager.setAdapter(new ViewAdapter(views));
        } else {
            // TODO: 2021/4/6 这里会闪一下子
            startActivity(new Intent(this, MainActivity.class));
            //解决在MainActivity点击导航栏的返回按钮出现的问题
            finish();
        }
    }

    class ViewAdapter extends PagerAdapter {
        private List<View> mViews;

        public ViewAdapter(List<View> views) {
            mViews = views;
        }

        @Override
        public int getCount() {
            return mViews.size();
        }

        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }

        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            View view = mViews.get(position);
            if (position == getCount() - 1) {//最后一个
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        startActivity(new Intent(WelcomeActivity.this, MainActivity.class));
                        finish();
                    }
                });
            }
            container.addView(view);
            return mViews.get(position);
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            container.removeView(mViews.get(position));
        }
    }
}
