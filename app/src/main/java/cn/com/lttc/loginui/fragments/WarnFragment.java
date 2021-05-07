package cn.com.lttc.loginui.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.*;
import android.widget.*;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import cn.com.lttc.loginui.R;
import cn.com.lttc.loginui.utils.GridImageAdapter;
import com.luck.picture.lib.entity.LocalMedia;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by zhouwei on 17/4/23.
 */

public class WarnFragment extends Fragment {
    private String mFrom;
    private UserWarnFragmentOne mWarnFragmentOne;
    private UserWarnFragmentTwo mWarnFragmentTwo;
    private UserWarnFragmentThree mWarnFragmentThree;

    public static WarnFragment newInstance(String from) {
        WarnFragment fragment = new WarnFragment();
        Bundle bundle = new Bundle();
        bundle.putString("Name", "张彦通");
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mFrom = getArguments().getString("from");
        }
        mWarnFragmentOne = new UserWarnFragmentOne();
        mWarnFragmentTwo = new UserWarnFragmentTwo();
        mWarnFragmentThree = new UserWarnFragmentThree();
        //可以拿到数据
        //System.out.println(getActivity().getIntent().getStringExtra("data"));
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_main_police, null);
        final TextView text1 = view.findViewById(R.id.text1);
        final TextView text2 = view.findViewById(R.id.text2);
        final TextView text3 = view.findViewById(R.id.text3);

//        ImageView back = view.findViewById(R.id.bt_warn_back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                getActivity().finish();
//            }
//        });

        getFragmentManager().beginTransaction()
                .replace(R.id.warn_container, mWarnFragmentOne)
                .commit();

        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text1.setTextColor(Color.WHITE);
                text1.setBackgroundResource(R.drawable.button_warn_background);
                text2.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text2.setBackgroundResource(0);
                text3.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text3.setBackgroundResource(0);
                getFragmentManager().beginTransaction()
                        .replace(R.id.warn_container, mWarnFragmentOne)
                        .commit();

            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text2.setTextColor(Color.WHITE);
                text2.setBackgroundResource(R.drawable.button_warn_background);
                text1.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text1.setBackgroundResource(0);
                text3.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text3.setBackgroundResource(0);
                getFragmentManager().beginTransaction()
                        .replace(R.id.warn_container, mWarnFragmentTwo)
                        .commit();

            }
        });
        text3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                text3.setTextColor(Color.WHITE);
                text3.setBackgroundResource(R.drawable.button_warn_background);
                text1.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text1.setBackgroundResource(0);
                text2.setTextColor(getActivity().getResources().getColor(R.color.warn_text_color));
                text2.setBackgroundResource(0);
                getFragmentManager().beginTransaction()
                        .replace(R.id.warn_container, mWarnFragmentThree)
                        .commit();

            }
        });
        //报警按钮
        view.findViewById(R.id.call_fire).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog alertDialog2 = new AlertDialog.Builder(getContext())
                        .setTitle("一键报警")
                        .setMessage("请确认是否真的要报警?")
                        .setIcon(R.drawable.alert_police)
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // TODO: 2021/3/21 掉两个借口,报警接口和图片上传(上传后返回图片url地址,然后点击报警)
                                OkHttpClient okHttpClient = new OkHttpClient();
                                /**
                                 * 1.将报警人信息以post方式发送,那么消防员如何接收到该消息呢
                                 * 2.如果消防员收到了报警信息,那么就将报警信息显示出来
                                 * 3.消防员接警后,居民app提示已经接警的通知信息
                                 *  居民的登录状态需要
                                 */
                                RequestBody requestBody = null;
                                try {
                                    JSONObject userData = new JSONObject(getActivity().getIntent().getStringExtra("data"));
                                    requestBody = new FormBody.Builder()
                                            .add("address_detail", "火宅地址")
                                            .add("community_num", userData.getString("resident_belong_communitynum"))
                                            .add("role", userData.getString("role"))
                                            .add("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()))
                                            .add("username", userData.getString("resident_username"))
                                            .build();
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                                //post方式
                                final Request request = new Request.Builder()
                                        .url("http://106.14.93.164:8090/firewarning/report")
                                        .post(requestBody)
                                        .build();
                                //异步方式
                                okHttpClient.newCall(request).enqueue(new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        System.out.println("失败");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        System.out.println("Response:" + "报警成功" + (response.body() != null ? response.body().string() : null));
                                        //报警成功提示
                                        getActivity().runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(getContext(), "报警成功!", Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }
                                });
                            }
                        })
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {//添加取消
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                            }
                        })
                        .create();
                alertDialog2.show();
            }
        });
        return view;
    }

}
