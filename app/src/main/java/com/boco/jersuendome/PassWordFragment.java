package com.boco.jersuendome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;


public class PassWordFragment extends Fragment  implements LockPatternView.OnPatterChangeLisrener, View.OnClickListener {

    public static final String TYPE_SETTING="setting";
    public static final String TYPE_CHECK="check";

    private LockPatternView mLockPatternView;
    private Context mContext;
    private LinearLayout btnLayout;
    private String  passwordStr;

    private static final String ARG_TYPE = "type";

    private String mParam1;
    private Button btn;

    public static PassWordFragment newInstance(Context mContext,String type) {
        PassWordFragment fragment = new PassWordFragment(mContext);

        Bundle args = new Bundle();
        args.putString(ARG_TYPE, type);
        fragment.setArguments(args);
        return fragment;
    }

    public PassWordFragment(Context mContext) {
        this.mContext = mContext;
    }

    public PassWordFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View conteentView = inflater.inflate(R.layout.fragment_pass_word, container, false);
        mLockPatternView = (LockPatternView) conteentView.findViewById(R.id.lv__pass);
        mLockPatternView.setOnPatterChangeLisrener(this);
        btnLayout = (LinearLayout) conteentView.findViewById(R.id.btn_lay_pass);
        if(getArguments() != null){
            if(TYPE_SETTING.equals(getArguments().get(ARG_TYPE))){
                btnLayout.setVisibility(View.VISIBLE);
            }
        }
        btn = (Button) conteentView.findViewById(R.id.btn_pass);
        btn.setOnClickListener(this);
        return conteentView;
    }

    @Override
    public void onPatterChange(String passWordS) {
        this.passwordStr = passWordS;


            if(getArguments() != null){
                if(TYPE_CHECK.equals(getArguments().get(ARG_TYPE))){
                    SharedPreferences sp  = getActivity().getSharedPreferences("sp",getActivity().MODE_PRIVATE);
                    Toast.makeText(getActivity(), sp.getString("password",""), Toast.LENGTH_SHORT).show();
                   if(sp.getString("password","").equals( passWordS)){
                        getActivity().startActivity(new Intent(getActivity(),MainActivity.class));
                       getActivity().finish();
                    }else{
                       Toast.makeText(getActivity(),"密码错误",Toast.LENGTH_SHORT).show();
                       mLockPatternView.resetPoint();
                   }
                }else{
                   if(TextUtils.isEmpty(passWordS)){
                        Toast.makeText(getActivity(), "至少5个节点", Toast.LENGTH_SHORT).show();
                   }else{
                        Toast.makeText(getActivity(),"设置成功:密码为"+passWordS,Toast.LENGTH_SHORT).show();
                   }
                }
            }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btn_pass:
                SharedPreferences sp =getActivity().getSharedPreferences("sp",getActivity().MODE_PRIVATE);
                sp.edit().putString("password",passwordStr).commit();
                getActivity().startActivity(new Intent(getActivity(),TestActivity.class));
                getActivity().finish();
                break;
        }
    }
}
