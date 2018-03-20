package com.companyname.campaigns;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

public class splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        Thread th = new Thread(){
            @Override
            public void run() {
                try {
                    Thread.sleep(3000);
                }catch (Exception e){
                    Log.e("Splash Hatasi", e.toString());
                }finally {
                    Intent i = new Intent(splash.this,Login.class);
                    startActivity(i);
                    finish();
                }
            }
        };
        th.start();
    }
}
