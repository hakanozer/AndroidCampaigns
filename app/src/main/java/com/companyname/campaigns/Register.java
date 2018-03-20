package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Register extends AppCompatActivity {

    EditText txtRName, txtRSurname, txtRTel, txtRMail, txtRPass;
    Button btnRRegister;
    SharedPreferences sha;
    SharedPreferences.Editor edit;
    userPro up = new userPro();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sha = getSharedPreferences("pro", MODE_PRIVATE);
        edit = sha.edit();

        txtRName = findViewById(R.id.txtRName);
        txtRSurname = findViewById(R.id.txtRSurname);
        txtRTel = findViewById(R.id.txtRTel);
        txtRMail = findViewById(R.id.txtRMail);
        txtRPass = findViewById(R.id.txtRPass);
        btnRRegister = findViewById(R.id.btnRRegister);
        btnRRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String n = txtRName.getText().toString().trim();
                boolean nTF = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE).matcher(n).find();
                String s = txtRSurname.getText().toString().trim();
                boolean sTF = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE).matcher(s).find();
                String t = txtRTel.getText().toString().trim();
                boolean tTF = Pattern.compile("\\d{10}", Pattern.CASE_INSENSITIVE).matcher(t).find();
                String m = txtRMail.getText().toString().trim();
                boolean mTF = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(m).find();
                String p = txtRPass.getText().toString().trim();

                if (n.equals("")) {
                    Toast.makeText(Register.this, "Lutfen isim alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                } else if (s.equals("")) {
                    Toast.makeText(Register.this, "Lutfen soyisim alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                } else if (t.equals("")) {
                    Toast.makeText(Register.this, "Lutfen telefon alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                } else if (m.equals("")) {
                    Toast.makeText(Register.this, "Lutfen mail alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                } else if (p.equals("")) {
                    Toast.makeText(Register.this, "Lutfen sifre alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                } else if (!nTF) {
                    Toast.makeText(Register.this, "Isim alani sadece harflerden olusmalidir!", Toast.LENGTH_SHORT).show();
                } else if (!sTF) {
                    Toast.makeText(Register.this, "Soyisim alani sadece harflerden olusmalidir!", Toast.LENGTH_SHORT).show();
                } else if (!tTF) {
                    Toast.makeText(Register.this, "Telefon alani sadece rakamlardan olusmalidir ve 10 haneli olmalidir!", Toast.LENGTH_SHORT).show();
                } else if (!mTF) {
                    Toast.makeText(Register.this, "Lutfen email`inizi dogru giriniz!", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://jsonbulut.com/json/userRegister.php";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("ref", "ce7f46683b56cb84131405b848678c51");
                    hm.put("userName", n);
                    hm.put("userSurname", s);
                    hm.put("userPhone", t);
                    hm.put("userMail", m);
                    hm.put("userPass", p);

                    up.setUserName(n);
                    up.setUserSurname(s);
                    up.setUserEmail(m);
                    up.setUserPhone(t);

                    new jsonData(Register.this, url, hm).execute();
                }
            }
        });
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
                    boolean durum = jobj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                    if (durum) {
                        int kullaniciId = jobj.getJSONArray("user").getJSONObject(0).getInt("kullaniciId");

                        edit.putInt("userId", kullaniciId);
                        edit.putString("userName", hm.get("userName"));
                        edit.putString("userSurname", hm.get("userSurname"));
                        edit.putString("userEmail", hm.get("userEmail"));
                        edit.putString("userPhone", hm.get("userPhone"));
                        edit.commit();

                        up.setUserId(kullaniciId);
                        MainActivity.userInf = up;

                        Intent i = new Intent(cnx, MainActivity.class);
                        startActivity(i);
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
