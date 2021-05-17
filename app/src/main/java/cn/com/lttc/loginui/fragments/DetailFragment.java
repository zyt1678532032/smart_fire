package cn.com.lttc.loginui.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.DetailPoliceActivity;
import cn.com.lttc.loginui.R;
import cn.com.lttc.loginui.utils.DataGenerator;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

public class DetailFragment extends Fragment {
    private ImageView back;
    private Button button7;//接警按钮

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.detail_item_fire, null);
        back = view.findViewById(R.id.back);
        button7 = view.findViewById(R.id.button7);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.home_container, DataGenerator.getFireFragments("")[0])
                        .commit();
            }
        });
        //接警功能
        button7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //调用接警接口,接警信息发送到用户订阅的主题(手机号)
                // TODO: 2021/3/21 弹出页面选择消防车，先调用查询所有消防车接口,然后将信息展示出来进行选择,然后点击触发接警接口
                //表单格式
                OkHttpClient okHttpClient = new OkHttpClient();
                RequestBody body = new FormBody.Builder()
                        .add("firecar_num", "0527")
                        .add("fireman_username", "消防员A")
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
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getContext(), "接警成功!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });
        return view;
    }

    private void initView() {

    }
}
