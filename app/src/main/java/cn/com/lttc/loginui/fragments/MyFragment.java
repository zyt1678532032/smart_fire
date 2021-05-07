package cn.com.lttc.loginui.fragments;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.R;
import org.json.JSONException;
import org.json.JSONObject;

public class MyFragment extends Fragment {
    private String mFrom;

    //实例
    public static MyFragment newInstance(String from) {
        //新建fragment这里要修改
        MyFragment fragment = new MyFragment();
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
    public View onCreateView(LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        //这里修改加载的布局文件
        String data = getActivity().getIntent().getStringExtra("data");
        View view = null;
        try {
            JSONObject object = new JSONObject(data);
            String role = object.getString("role");
            if (TextUtils.equals(role, "3")) {
                //消防员
                view = inflater.inflate(R.layout.fireman_my_fragment_layout, null);
            } else {
                view = inflater.inflate(R.layout.my_fragment_layout, null);
                ImageView back = view.findViewById(R.id.back_my);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return view;
    }
}
