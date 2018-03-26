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
import java.util.regex.Pattern;

public class UserSetting extends AppCompatActivity {

    EditText txtUserName;
    EditText txtUserSurname;
    EditText txtUserMail;
    EditText txtUserPhone;
    EditText txtUserPass;
    Button btnUserEdit;

    SharedPreferences sha;
    SharedPreferences.Editor edit;

    userPro user = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_setting);

        user = MainActivity.userInf;
        txtUserName = findViewById(R.id.txtUserName);
        txtUserSurname = findViewById(R.id.txtUserSurname);
        txtUserMail = findViewById(R.id.txtUserMail);
        txtUserPhone = findViewById(R.id.txtUserPhone);
        txtUserPass = findViewById(R.id.txtUserPass);
        btnUserEdit = findViewById(R.id.btnUserEdit);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Kullanıcı Ayarları");

        sha = getSharedPreferences("pro", MODE_PRIVATE);
        edit = sha.edit();

        txtUserName.setText(user.getUserName());
        txtUserSurname.setText(user.getUserSurname());
        txtUserMail.setText(user.getUserEmail());
        txtUserPhone.setText(user.getUserPhone());

        btnUserEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = txtUserName.getText().toString().trim();
                boolean nTF = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE).matcher(n).find();
                String s = txtUserSurname.getText().toString().trim();
                boolean sTF = Pattern.compile("^[\\p{L} .'-]+$", Pattern.CASE_INSENSITIVE).matcher(s).find();
                String t = txtUserPhone.getText().toString().trim();
                boolean tTF = Pattern.compile("\\d{10}", Pattern.CASE_INSENSITIVE).matcher(t).find();
                String m = txtUserMail.getText().toString().trim();
                boolean mTF = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE).matcher(m).find();
                String p = txtUserPass.getText().toString().trim();
                int usr = user.getUserId();

                if (n.equals("")) {
                    Toast.makeText(UserSetting.this, "Lütfen isim giriniz!", Toast.LENGTH_SHORT).show();
                } else if (s.equals("")) {
                    Toast.makeText(UserSetting.this, "Lütfen soyadı giriniz!", Toast.LENGTH_SHORT).show();
                } else if (t.equals("")) {
                    Toast.makeText(UserSetting.this, "Lütfen telefon numarası giriniz!", Toast.LENGTH_SHORT).show();
                } else if (m.equals("")) {
                    Toast.makeText(UserSetting.this, "Lütfen mail adresi giriniz!", Toast.LENGTH_SHORT).show();
                }else if (p.equals("")) {
                    Toast.makeText(UserSetting.this, "Lütfen şifre giriniz!", Toast.LENGTH_SHORT).show();
                } else if (!nTF) {
                    Toast.makeText(UserSetting.this, "İsim sadece harf içermelidir!", Toast.LENGTH_SHORT).show();
                } else if (!sTF) {
                    Toast.makeText(UserSetting.this, "Soyadı sadece harf içermelidir!", Toast.LENGTH_SHORT).show();
                } else if (!mTF) {
                    Toast.makeText(UserSetting.this, "Doğru mail formatı giriniz!", Toast.LENGTH_SHORT).show();
                } else if (!tTF) {
                    Toast.makeText(UserSetting.this, "Doğru telefon numarası formatı giriniz!", Toast.LENGTH_SHORT).show();
                } else {
                    String url = "http://jsonbulut.com/json/userSettings.php";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("ref", "ce7f46683b56cb84131405b848678c51");
                    hm.put("userName", n);
                    hm.put("userSurname", s);
                    hm.put("userPhone", t);
                    hm.put("userMail", m);
                    hm.put("userPass", p);
                    hm.put("userId", usr + "");

                    MainActivity.userInf.setUserName(n);
                    MainActivity.userInf.setUserSurname(s);
                    MainActivity.userInf.setUserEmail(m);

                    Intent i = new Intent(UserSetting.this, MainActivity.class);
                    startActivity(i);
                    finish();

                    new jsonData(UserSetting.this, url, hm).execute();

                }
            }
        });
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
        protected void onPostExecute(Void aVoid) {

            if (!jsonString.equals("")){
                try {
                    JSONObject jobj = new JSONObject(jsonString);
                    boolean durum = jobj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                    if(durum) {
                        Toast.makeText(cnx, "İşlem Başarıyla Tamamlandı.", Toast.LENGTH_SHORT).show();
                    }else {
                        Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    }
                }catch (Exception ex) {
                    Toast.makeText(cnx, "Json Pars Hatası", Toast.LENGTH_SHORT).show();
                }
            }else {
                Toast.makeText(cnx, "Sunucu Hatası Oluştu. ", Toast.LENGTH_SHORT).show();
            }
            super.onPostExecute(aVoid);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
            }catch (Exception ex) {
                Toast.makeText(cnx, "İşlem Başarısız!", Toast.LENGTH_SHORT).show();
            }
            return null;
        }
    }
}
