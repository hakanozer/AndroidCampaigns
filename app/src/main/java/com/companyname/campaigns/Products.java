package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class Products extends AppCompatActivity {

    ListView pListView;
    BaseAdapter urunBaseAdapter;
    LayoutInflater urunLayoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products);

        pListView = findViewById(R.id.pListView);
        urunLayoutInflater = LayoutInflater.from(this);

        String url = "http://jsonbulut.com/json/product.php";
        HashMap<String,String> hm = new HashMap<>();
        hm.put("ref","ce7f46683b56cb84131405b848678c51");
        hm.put("start","0");
        hm.put("count","100");
        hm.put("categoryId","1683");
        new jsonData(Products.this, url, hm).execute();

    }


    // json data export
    class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String, String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";

        // ProgressDialog pro;
        public jsonData(Context cnx, String url, HashMap<String, String> hm) {
            this.cnx = cnx;
            this.url = url;
            this.hm = hm;
            //pro = new ProgressDialog(cnx);
            // pro.setMessage("Yükleniyor Lütfen Bekleyiniz..");
            // pro.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
                //  pro.hide();
            } catch (Exception ex) {
                // pro.hide();
                Toast.makeText(cnx, "İşlem Başarısız Oldu", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // grafiksel işlemler bu gövdede yer alır.
            if (!jsonString.equals("")) {
                try {
                    JSONObject jobj = new JSONObject(jsonString);
                    boolean durum = jobj.getJSONArray("Products").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("Products").getJSONObject(0).getString("mesaj");
                    if (durum) {
                        final JSONArray arr = jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");
                        if(arr.length() > 0 ) {
                            urunBaseAdapter = new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return arr.length();
                                }

                                @Override
                                public Object getItem(int i) {
                                    return null;
                                }

                                @Override
                                public long getItemId(int i) {
                                    return 0;
                                }

                                @Override
                                public View getView(int i, View view, ViewGroup viewGroup) {

                                    if (view == null) {
                                        view = urunLayoutInflater.inflate(R.layout.custom_proitem, null);
                                    }

                                    try {
                                        JSONObject pdata = arr.getJSONObject(i);
                                        String title = pdata.getString("productName");
                                        String price = pdata.getString("price");
                                        TextView tText = view.findViewById(R.id.proitemTitle);
                                        TextView pText = view.findViewById(R.id.proitemPrice);
                                        tText.setText(title);
                                        pText.setText(price);

                                        // image control
                                        ImageView pImage = view.findViewById(R.id.proitemImage);
                                        boolean imageStatu = pdata.getBoolean("image");
                                        if (imageStatu) {
                                            String iUrl = pdata.getJSONArray("images").getJSONObject(0).getString("normal");
                                            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                                            Picasso.with(getApplicationContext()).load(iUrl).into(pImage);

                                        }

                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return view;
                                }
                            };
                            pListView.setAdapter(urunBaseAdapter);
                            pListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                    try {
                                        ProductDetail.proDt = arr.getJSONObject(i);
                                        Intent it = new Intent(Products.this, ProductDetail.class);
                                        startActivity(it);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(cnx, "Bu Kategoride ürün yok !", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(cnx, "Json Pars Hatası", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(cnx, "Sunucu Hatası Oluştur.. ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }

    }

}
