package com.companyname.campaigns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetail extends AppCompatActivity {

    static JSONObject newsDt = null;
    WebView newsWebView;
    ImageView newsdetailimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        newsWebView = findViewById(R.id.newsWebView);
        newsWebView.getSettings().setJavaScriptEnabled(true);


        try {
            String newstitle = newsDt.getString("title");
            String newsdescription = newsDt.getString("s_description");
            String newscontent = newsDt.getString("l_description");
            // String newsimage=newsDt.getString("picture");
            String newsdate = newsDt.getString("date_time");

           /* Log.d("", newstitle);
            Log.d("",newsdescription);
            Log.d("",newscontent);*/


            // image control
            ImageView newsImage = findViewById(R.id.newsdetailimage);

            String iUrl = newsDt.getString("picture");
            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
            Picasso.with(this).load(iUrl).into(newsImage);
            Log.d("hata", iUrl.toString());

            String htmlData =
                    "<html>\n" +
                            "<head>\n" +
                            "<title>News</title>\n" +
                            "</head>\n" +
                            "<body>\n" +
                            "<h3 align=\"center\"> " + newstitle + " </h3>" +
                            "<p> " + newsdescription + " </p>" +
                            "<p> " + newscontent + " </p>" +
                            "<p> " + newsdate + " </p>" +
                            "</body>\n" +
                            "</html>\n";

            getSupportActionBar().setTitle(newstitle);
            newsWebView.setWebViewClient(new WebViewClient());
            newsWebView.loadData(htmlData, "text/html; charset=UTF-8", null);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
