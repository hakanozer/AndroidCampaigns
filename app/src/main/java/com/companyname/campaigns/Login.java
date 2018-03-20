package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

public class Login extends AppCompatActivity {

    Button btnLogin, btnRegister;
    EditText txtLMail, txtLPass;
    SharedPreferences sha;
    SharedPreferences.Editor edit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        sha = getSharedPreferences("pro", MODE_PRIVATE);
        edit = sha.edit();

        // user login control

        if (sha.getInt("userId", -1) != -1 ) {

            userPro up = new userPro();
            up.setUserId(sha.getInt("userId", -1));
            up.setUserName(sha.getString("userName",""));
            up.setUserSurname(sha.getString("userSurname",""));
            up.setUserEmail(sha.getString("userEmail",""));
            up.setUserPhone(sha.getString("userPhone",""));
            MainActivity.userInf=up;

            Intent i = new Intent(Login.this, MainActivity.class);
            startActivity(i);
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        txtLMail = findViewById(R.id.txtLMail);
        txtLPass = findViewById(R.id.txtLPass);
        // login button action
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String m = txtLMail.getText().toString().trim();
                String p = txtLPass.getText().toString().trim();

                if(!m.equals("") && !p.equals("")){
                    String url = "http://jsonbulut.com/json/userLogin.php";
                    HashMap<String, String> hm = new HashMap<>();
                    hm.put("ref","ce7f46683b56cb84131405b848678c51");
                    hm.put("userEmail", m);
                    hm.put("userPass", p);
                    hm.put("face", "no");
                    new Login.jsonData(Login.this, url, hm ).execute();
                }else if (m.equals("")){
                    txtLMail.requestFocus();
                    Toast.makeText(Login.this, "Lutfen mail alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                }else if (p.equals("")){
                    txtLPass.requestFocus();
                    Toast.makeText(Login.this, "Lutfen sifre alanini doldurunuz!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this , Register.class);
                startActivity(i);
            }
        });



        // setSupportActionBar(toolbar);
        // Snackbar.make(this, "Replace with your own action", Snackbar.LENGTH_LONG).setAction("Action", null).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String,String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";
        // ProgressDialog pro;
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
                    boolean durum = jobj.getJSONArray("user").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("user").getJSONObject(0).getString("mesaj");
                    if(durum) {
                        int userId =jobj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getInt("userId");
                        String userName = jobj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userName");
                        String userSurname = jobj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userSurname");
                        String userEmail = jobj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userEmail");
                        String userPhone = jobj.getJSONArray("user").getJSONObject(0).getJSONObject("bilgiler").getString("userPhone");
                        userPro up = new userPro();
                        up.setUserId(userId);
                        up.setUserName(userName);
                        up.setUserSurname(userSurname);
                        up.setUserEmail(userEmail);
                        up.setUserPhone(userPhone);
                        MainActivity.userInf=up;

                        edit.putInt("userId", up.getUserId());
                        edit.putString("userName", up.getUserName());
                        edit.putString("userSurname", up.getUserSurname());
                        edit.putString("userEmail", up.getUserEmail());
                        edit.putString("userPhone", up.getUserPhone());
                        edit.commit();

                        Intent i = new Intent(cnx, MainActivity.class);
                        startActivity(i);
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
}
