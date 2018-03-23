package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class AddressAdd extends AppCompatActivity {

    EditText aCity, aDistrict, aNeighborhood, aAddress, aDoorNumber, aNote;
    Button btnAddressAdd;
    SharedPreferences sha ;
    SharedPreferences.Editor edit;

    userPro up=MainActivity.userInf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
        setTitle("Adres Ekle");

        sha = getSharedPreferences("pro", MODE_PRIVATE);
        edit = sha.edit();

        aCity = findViewById(R.id.aCity);
        aDistrict = findViewById(R.id.aDistrict);
        aNeighborhood = findViewById(R.id.aNeighborhood);
        aAddress = findViewById(R.id.aAddress);
        aDoorNumber = findViewById(R.id.aDoorNumber);
        aNote = findViewById(R.id.aNote);
        btnAddressAdd = findViewById(R.id.btnAddressAdd);
        btnAddressAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String city = aCity.getText().toString().trim();
                String district = aDistrict.getText().toString().trim();
                String neighborhood = aNeighborhood.getText().toString().trim();
                String address = aAddress.getText().toString().trim();
                String door = aDoorNumber.getText().toString().trim();
                String note = aNote.getText().toString().trim();

                String url = "http://jsonbulut.com/json/addressAdd.php";
                HashMap<String, String> hm = new HashMap<>();
                hm.put("ref","ce7f46683b56cb84131405b848678c51");
                hm.put("musterilerID", ""+ up.getUserId());
                hm.put("il", city);
                hm.put("ilce", district);
                hm.put("Mahalle", neighborhood);
                hm.put("adres", address);
                hm.put("kapiNo", door);
                hm.put("notBilgi", note);

                new jsonData(AddressAdd.this, url, hm ).execute();

            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String,String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";
        public jsonData(Context cnx ,String url,HashMap<String,String> hm){
            this.cnx = cnx;
            this.url = url;
            this.hm = hm;
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
                        boolean durum = jobj.getJSONArray("user").getBoolean(0);
                        if(durum) {
                            Toast.makeText(cnx, "Adres ekleme başarılı!", Toast.LENGTH_SHORT).show();
                            AddressList.liste.add(hm.get("notBilgi"));
                            AddressList.adp.notifyDataSetChanged();
                            finish();
                    }else {
                        Toast.makeText(cnx, "Adres eklenemedi!", Toast.LENGTH_SHORT).show();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}