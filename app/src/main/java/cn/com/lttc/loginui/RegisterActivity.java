package cn.com.lttc.loginui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.gson.Gson;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_register_step_one);

        findViewById(R.id.ib_navigation_back).setOnClickListener(this);
        findViewById(R.id.bt_register_submit).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ib_navigation_back:
                finish();
                break;//switch语句不要忘了在最后添加break关键字
            case R.id.bt_register_submit:
                //1.用户名
                TextView username = findViewById(R.id.et_register_username);
                //2.密码
                TextView pwd = findViewById(R.id.et_register_pwd);
                //3.真实姓名
                TextView realname = findViewById(R.id.et_register_realname);
                //4.手机号
                TextView phone = findViewById(R.id.et_register_phone);
                //5.地址
                TextView address = findViewById(R.id.et_register_address);
                //6.小区编号
//                TextView communitynum = findViewById(R.id.et_register_communitynum);
                //发送信息
                OkHttpClient okHttpClient = new OkHttpClient();
                Map<String, Object> data = new HashMap<>();
                data.put("resident_username", username.getText().toString());
                data.put("resident_password", pwd.getText().toString());
                data.put("resident_realname", realname.getText().toString());
                data.put("resident_tel", phone.getText().toString());
                data.put("resident_address_detail", address.getText().toString());
                data.put("resident_belong_communitynum", "0001");
                if (username.getText().toString().equals("")) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(RegisterActivity.this, "用户名不能为空!", Toast.LENGTH_SHORT).show();
                        }
                    });
                    break;
                }
                String json = new Gson().toJson(data);
                RequestBody body = RequestBody.create(MediaType.parse("application/json"), json);
                Request request = new Request.Builder()
                        .url("http://106.14.93.164:8090/user/register")
                        .post(body)
                        .build();
                okHttpClient.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String res = response.body().string();
                        try {
                            JSONObject resJson = new JSONObject(res);
                            if (resJson.get("status").equals(0)) {
                                //状态码:0 注册成功
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            } else {
                                //status:1 注册失败
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(RegisterActivity.this, "注册失败!请重新再试!", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
                break;
            default:
                break;
        }
    }
}