package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

import java.util.ArrayList;
import java.util.HashMap;

public class ContentList extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ContentList() {

    }

    public static ContentList newInstance(String param1, String param2) {
        ContentList fragment = new ContentList();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }


    ListView listcontent;
    BaseAdapter urunBaseAdapter;
    LayoutInflater urunLayoutInflater;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_content_list, container, false);
        listcontent = v.findViewById(R.id.contList);

        String url = "http://jsonbulut.com/json/contentShow.php";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
        hm.put("start", "0");
        hm.put("count", "20");
        new jsonData(v.getContext(), url, hm).execute();

        return v;
    }


    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


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
                    boolean durum = jobj.getJSONArray("contents").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("contents").getJSONObject(0).getString("mesaj");
                    if (durum) {
                        final JSONArray arr = jobj.getJSONArray("contents").getJSONObject(0).getJSONArray("bilgiler");
                        ArrayList<String> icrk = new ArrayList<String>();
                        for (int i = 0; i < arr.length(); i++) {
                            String title = arr.getJSONObject(i).getString("title");
                            icrk.add(title);
                            Log.d("title", title);
                        }

                        ArrayAdapter<String> dd = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, icrk);
                        listcontent.setAdapter(dd);
                        listcontent.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                try {
                                    ContentDetail.contentDT = arr.getJSONObject(i);
                                    Intent it = new Intent(getContext(), ContentDetail.class);
                                    startActivity(it);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });


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
