package com.boco.jersuendome;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;


public class MainActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PassWordFragment passWordFragment = PassWordFragment.newInstance(this, PassWordFragment.TYPE_SETTING);

        getSupportFragmentManager().beginTransaction()
                .replace(android.R.id.content, passWordFragment)
                .commit();
    }

}
