package com.boco.jersuendome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;



public class WelcomActivity extends FragmentActivity {

    private Context mContext = this;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sp = getSharedPreferences("sp", Context.MODE_PRIVATE);
                String passWord =sp.getString("password","");
                if (TextUtils.isEmpty(passWord)){
                    //设置密码
                    Intent mIntent  = new Intent();
                    mIntent.setClass(WelcomActivity.this,MainActivity.class);
                    startActivity(mIntent);
                }else{
                    //验证密码
                    PassWordFragment passWordFragment = PassWordFragment.newInstance(mContext, PassWordFragment.TYPE_CHECK);
                    getSupportFragmentManager().beginTransaction()
                            .replace(android.R.id.content, passWordFragment)
                            .commit();
                }
            }
        }, 2000);
    }


}
