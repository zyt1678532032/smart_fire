package cn.com.lttc.loginui;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

/**
 * 报警信息详情活动
 */
public class DetailPoliceActivity extends AppCompatActivity {
    private ImageView back;
    private Button button7;//接警按钮

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_item_fire);
        initView();

    }

    private void initView() {
        back = findViewById(R.id.back);
        button7 = findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //接警功能
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    JSONObject data = new JSONObject(getIntent().getStringExtra("data"));
                    Log.e("data", data.toString());
                    String firecar_num = data.getString("fireman_belong_firehouse");
                    String username = data.getString("fireman_username");
                    //调用接警接口,接警信息发送到用户订阅的主题(手机号)
                    // TODO: 2021/3/21 弹出页面选择消防车，先调用查询所有消防车接口,然后将信息展示出来进行选择,然后点击触发接警接口
                    //表单格式
                    OkHttpClient okHttpClient = new OkHttpClient();
                    RequestBody body = new FormBody.Builder()
                            .add("firecar_num", firecar_num)
                            .add("fireman_username", username)
                            .add("firewarning_id", "72")
                            .build();
                    //post方式
                    final Request request = new Request.Builder()
                            .url("http://106.14.93.164:8090/feedback/alarm")
                            .post(body)
                            .build();
                    okHttpClient.newCall(request).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            Log.e("消防员接警状态", "接警失败");
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            Log.e("消防员接警状态", response.body().string());
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Toast.makeText(DetailPoliceActivity.this, "接警成功!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
