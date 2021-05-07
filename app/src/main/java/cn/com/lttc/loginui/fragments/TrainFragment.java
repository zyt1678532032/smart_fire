package cn.com.lttc.loginui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import cn.com.lttc.loginui.R;

/**
 * 接警页面
 * Created by zhouwei on 17/4/23.
 */

public class TrainFragment extends Fragment {

    private String mFrom;
    public static TrainFragment newInstance(String from){
        TrainFragment fragment = new TrainFragment();
        Bundle bundle = new Bundle();
        bundle.putString("from",from);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mFrom = getArguments().getString("from");
        }

    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.train_fragment_layout,null);
        //报警信息显示出来

        //1.获取报警信息
        return view;
    }
}
