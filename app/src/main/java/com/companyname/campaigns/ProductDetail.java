package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class ProductDetail extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener {
    RatingBar ratingBar;



    static JSONObject proDt = null;
    private SliderLayout mDemoSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        mDemoSlider = findViewById(R.id.slider);
        ratingBar =  findViewById(R.id.ratingBar);

        // image control
        HashMap<String,String> url_maps = new HashMap<String, String>();

        try {
            boolean imgControl = proDt.getBoolean("image");
            String title = proDt.getString("productName");
            if(imgControl) {
                JSONArray iArr = proDt.getJSONArray("images");
                for(int i = 0; i<iArr.length(); i++) {
                    String iur = iArr.getJSONObject(i).getString("normal");
                    url_maps.put(title+i, iur);
                }

                for(String name : url_maps.keySet()){
                    TextSliderView textSliderView = new TextSliderView(this);
                    // initialize a SliderLayout
                    textSliderView
                            .description(name)
                            .image(url_maps.get(name))
                            .setScaleType(BaseSliderView.ScaleType.Fit)
                            .setOnSliderClickListener(this);

                    //add your extra information
                    textSliderView.bundle(new Bundle());
                    textSliderView.getBundle()
                            .putString("extra",name);

                    mDemoSlider.addSlider(textSliderView);
                }
                mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Default);
                mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
                mDemoSlider.setCustomAnimation(new DescriptionAnimation());
                mDemoSlider.setDuration(4000);
                mDemoSlider.addOnPageChangeListener(this);


            }else {
                // gösterilecek resim yok
            }
        }catch (Exception ex) {

        }
        String url="http://jsonbulut.com/json/likeManagement.php";
        raiting(ProductDetail.this,ratingBar,5,606,url);
    }

    @Override
    protected void onStop() {
        // To prevent a memory leak on rotation, make sure to call stopAutoCycle() on the slider before activity or fragment is destroyed
        mDemoSlider.stopAutoCycle();
        super.onStop();
    }

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(this,slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        Log.d("Slider Demo", "Page Changed: " + position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }



    // json raiting  data export edit:Muharrem
    class jsonDataRaiting extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String,String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";
        // ProgressDialog pro;
        public jsonDataRaiting(Context cnx,String url,HashMap<String,String> hm){

            this.url = url;
            this.hm = hm;
            this.cnx=cnx;
            //pro = new ProgressDialog(cnx);
            // pro.setMessage("Yükleniyor Lütfen Bekleyiniz..");
            // pro.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
                //  pro.hide();
            }catch (Exception ex) {
                // pro.hide();
                Toast.makeText(cnx, "İşlem Başarısız Oldu", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // grafiksel işlemler bu gövdede yer alır.
            if (!jsonString.equals("")){
                try {
                    JSONObject jobj = new JSONObject(jsonString);
                    boolean durum = jobj.getJSONArray("votes").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("votes").getJSONObject(0).getString("mesaj");
                    if(durum) {


                    }else {
                        Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex) {
                    Toast.makeText(cnx, "Json Pars Hatası", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(cnx, "Sunucu Hatası Oluştur.. ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }

    }
    public void raiting(final Context rcnx, RatingBar ratingBar, final int cid, final int pid, final String url){

        final HashMap<String,String> hm=new HashMap<>();
        hm.put("ref","ce7f46683b56cb84131405b848678c51");
        hm.put("productId", String.valueOf(pid));
        hm.put("customerId",String.valueOf(cid));

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float v, boolean b) {
                Log.d("RATING ", "onRatingChanged: "+v);

                hm.put("vote",String.valueOf(v));
                new jsonDataRaiting(rcnx,url, hm ).execute();


            }
        });

    }

}
