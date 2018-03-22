package com.companyname.campaigns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import org.json.JSONObject;

public class ContentDetail extends AppCompatActivity {
    static JSONObject contentDT = null;

    TextView ttitle, tdesc, tdate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        WebView html = (WebView)findViewById(R.id.html);
        html.getSettings().setJavaScriptEnabled(true);


        ttitle = findViewById(R.id.ttitle);

        tdate = findViewById(R.id.tdate);


        try {
            String title = contentDT.getString("title");
            String desc = contentDT.getString("details");
            String date = contentDT.getString("date");
            String status = contentDT.getString("status");

            ttitle.setText(title+" - AKTİF");

            tdate.setText(date);
            html.loadData(desc,"text/html;charset=UTF-8",null );


        }catch (Exception ex)
        {
            ex.printStackTrace();

        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
