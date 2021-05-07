package cn.com.lttc.loginui.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.DetailPoliceActivity;
import cn.com.lttc.loginui.R;
import cn.com.lttc.loginui.SampleApplication;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.*;


/**
 * Created by zhouwei on 17/4/23.
 */

public class InfoFragment extends Fragment {

    private String mFrom;
    private ListView listView;
    public static ArrayAdapter<String> adapter;

    public static InfoFragment newInstance(String from) {
        InfoFragment fragment = new InfoFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from", from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fire_recevice, null);
        listView = view.findViewById(R.id.listView);
        listView.setAdapter(new FireTaskAdapter(getContext(), 0, null));
        //1.获取登录信息
        String data = getActivity().getIntent().getStringExtra("data");

        //报警信息
        LinkedList<String> receiveData = (LinkedList<String>) SampleApplication.getInstance().getReceiveData();
        LinkedList<String> callData = (LinkedList<String>) SampleApplication.getInstance().getCallData();

        try {
            //获取登录状态 居民还是消防员
            JSONObject jsonObject = new JSONObject(data);
            String role = jsonObject.getString("role");
            // 0:居民 3:消防员
            if ("0".equals(role)) {
//                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, receiveData);
//                listViewForUser();
            } else if ("3".equals(role)) {
//                adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, callData);
                //listView
//                listViewForFireman();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    private class FireTaskAdapter extends ArrayAdapter {

        public FireTaskAdapter(@NonNull Context context, int resource, @NonNull List objects) {
            super(context, resource, objects);
        }

        @Override
        public int getCount() {
            return 2;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(getContext()).inflate(R.layout.task_item_fire, null);
            }
            TextView tvName = convertView.findViewById(R.id.tvName);
            TextView tvTime = convertView.findViewById(R.id.tvTime);
            //详情功能
            ImageView detail = convertView.findViewById(R.id.detail);
            TextView tvDetail = convertView.findViewById(R.id.tvDetail);
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().beginTransaction()
                            .replace(R.id.home_container, new DetailFragment())
                            .commit();
//                    String data = getActivity().getIntent().getStringExtra("data");
//                    Intent intent = new Intent(getContext(), DetailPoliceActivity.class);
//                    intent.putExtra("data", data);
//                    startActivity(intent);
                }
            };
            tvDetail.setOnClickListener(listener);
            detail.setOnClickListener(listener);

            return convertView;
        }
    }

    private void listViewForUser() {
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
    }

    //消防员
    private void listViewForFireman() {
        listView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        //AdapterView不是View中的
        // TODO: 2021/3/12 是否删除报警信息
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                //弹出对话框是否删除
                new AlertDialog.Builder(getContext())
                        .setMessage("是否要删除该报警信息?")
                        .setPositiveButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setNegativeButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                Log.e("信息数目删除前", String.valueOf(adapter.getCount()));
                                // TODO: 2021/3/10 这里只是将适配器中的元素删除了,但是本地文件的内容还是没有删除
                                adapter.remove(adapter.getItem(position));
//                                Log.e("信息数目删除后", String.valueOf(adapter.getCount()));
                            }
                        })
                        .create().show();
//                adapter.notifyDataSetInvalidated();
                //更信息报警信息
                return true;
            }

        });
        // TODO: 2021/3/12 是否接警
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //从适配器中获取当前元素的报警信息
                String item = adapter.getItem(position);

                String[] strings = Objects.requireNonNull(item).split("\n");
                //进入报警详情页面
                new AlertDialog.Builder(getActivity())
                        .setTitle("报警详情")
                        .setIcon(R.drawable.warn_infor)
                        .setMessage(item)
                        .setNegativeButton("接警", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("接警:", "测试");
                                /**  0:为接警 1:处理中 2:处理完成
                                 * 1.判断报警状态 是否已经有消防员接警
                                 * 2.报警已有人接警 提示该警报已经有消防员接受
                                 * 3.该报警信息还未接警 将该接警状态设置为2
                                 */
                                // TODO: 2021/3/14 接警参数 消防站编号(firecar_num) 消防员名字(fireman_username) 报警id(firewarning_id）
//                                Log.e("消防员响应数据", getActivity().getIntent().getStringExtra("data"));

                                try {
                                    JSONObject data = new JSONObject(getActivity().getIntent().getStringExtra("data"));
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

                                        }

                                        @Override
                                        public void onResponse(Call call, Response response) throws IOException {
                                            Log.e("消防员接警状态", response.body().string());
                                        }
                                    });
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //Fragment跳转到消防车选择页面
                                //以下方式可以实现
                                //getFragmentManager().beginTransaction().replace(R.id.home_container, new WarnFragment()).commit();
                            }
                        })
                        .setPositiveButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Log.e("取消:", "测试");
                            }
                        }).create().show();
            }
        });
    }
}
