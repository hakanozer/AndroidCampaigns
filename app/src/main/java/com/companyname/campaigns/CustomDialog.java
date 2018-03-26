package com.companyname.campaigns;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.HashMap;

/**
 * Created by java_oglen on 22.3.2018.
 */

public class CustomDialog extends AppCompatDialogFragment {
    private EditText editDesc;
    SharedPreferences sha;
    SharedPreferences.Editor edit;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder=new AlertDialog.Builder(getActivity());
        Button btnAddCart;



        LayoutInflater inflater=getActivity().getLayoutInflater();
        final View view = inflater.inflate(R.layout.custom_cartdialog,null);
        editDesc = view.findViewById(R.id.edtDescription);

        final userPro up = MainActivity.userInf;
        final String customerId =up.getUserId()+"";
        String productId = "";
        try {
            productId = ProductDetail.proDt.getString("productId");
        } catch (JSONException e) {
            e.printStackTrace();
        }


        final String finalProductId = productId;
        builder.setView(view)
                .setTitle("Accept Cart")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                })
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String url = "http://jsonbulut.com/json/orderForm.php";
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
                        hm.put("customerId", customerId);
                        hm.put("productId", finalProductId);
                        hm.put("html", editDesc.getText().toString().trim());
                        new jsonData(getContext(),url,hm).execute();

                    }
                });
        editDesc = view.findViewById(R.id.edtDescription);
        return  builder.create();
    }

    // json data export
    class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String,String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";
        public jsonData(Context cnx , String url, HashMap<String,String> hm){
            this.cnx = cnx;
            this.url = url;
            this.hm = hm;

        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();

            }catch (Exception ex) {

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
                    boolean durum = jobj.getJSONArray("order").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("order").getJSONObject(0).getString("mesaj");
                    if(durum) {
                        Toast.makeText(cnx, "Order is successfull", Toast.LENGTH_SHORT).show();
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
