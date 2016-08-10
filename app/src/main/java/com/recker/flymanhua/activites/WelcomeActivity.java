package com.recker.flymanhua.activites;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;

import com.recker.flymanhua.R;


import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by recker on 16/7/14.
 */
public class WelcomeActivity extends AppCompatActivity {

    @Bind(R.id.banner) LinearLayout mBanner;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        ButterKnife.bind(this);

        init();
    }

    private void init() {

    }
}
