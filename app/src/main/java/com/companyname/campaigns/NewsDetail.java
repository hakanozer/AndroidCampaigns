package com.companyname.campaigns;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

public class NewsDetail extends AppCompatActivity {

    static JSONObject newsDt=null;
    TextView txtnewsdetailtitle,txtnewscontent,txtnewsdescription,txtnewsdate;
    ImageView newsdetailimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        txtnewsdetailtitle=findViewById(R.id.txtnewsdetailtitle);
        txtnewscontent=findViewById(R.id.txtnewscontent);
        txtnewsdescription=findViewById(R.id.txtnewsdescription);
        txtnewsdate=findViewById(R.id.txtnewsdate);

        try {
            String newstitle=newsDt.getString("title");
            String newsdescription=newsDt.getString("s_description");
            String newscontent=newsDt.getString("l_description");
           // String newsimage=newsDt.getString("picture");
            String newsdate=newsDt.getString("date_time");

           /* Log.d("", newstitle);
            Log.d("",newsdescription);
            Log.d("",newscontent);*/

            txtnewsdetailtitle.setText(newstitle);
            txtnewsdescription.setText(newsdescription);
            txtnewscontent.setText(newscontent);
            txtnewsdate.setText(newsdate);

            // image control
            ImageView newsImage = findViewById(R.id.newsdetailimage);

            String iUrl = newsDt.getString("picture");
            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
            Picasso.with(this).load(iUrl).into(newsImage);
            Log.d("hata",iUrl.toString());




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;

        }
        return super.onOptionsItemSelected(item);
    }

}
