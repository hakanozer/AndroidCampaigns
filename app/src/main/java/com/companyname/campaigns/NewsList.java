package com.companyname.campaigns;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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


public class NewsList extends Fragment {

    ListView newsListView;
    BaseAdapter newsBaseAdapter;
    LayoutInflater newsLayoutInflater;



    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewsList() {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static NewsList newInstance(String param1, String param2) {
        NewsList fragment = new NewsList();
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


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view=inflater.inflate(R.layout.fragment_news_list, container, false);
        newsListView=view.findViewById(R.id.newsListView);
        newsLayoutInflater=LayoutInflater.from(view.getContext());

        //http://jsonbulut.com/json/news.php?ref=ce7f46683b56cb84131405b848678c51&start=0&count=5

        String url = "http://jsonbulut.com/json/news.php";
        HashMap<String,String> hm = new HashMap<>();
        hm.put("ref","ce7f46683b56cb84131405b848678c51");
        hm.put("start","0");
        hm.put("count","100");
       // new Products.jsonData(Products.this, url, hm).execute();
        new jsonData(getContext(),url,hm).execute();


        return view;
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
                    boolean durum = jobj.getJSONArray("News").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("News").getJSONObject(0).getString("mesaj");
                    if (durum) {
                        final JSONArray news = jobj.getJSONArray("News").getJSONObject(0).getJSONArray("Haber_Bilgileri");
                        if(news.length() > 0 ) {
                            newsBaseAdapter = new BaseAdapter() {
                                @Override
                                public int getCount() {
                                    return news.length();
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
                                        view = newsLayoutInflater.inflate(R.layout.custom_pronews, null);
                                    }

                                    try {
                                        JSONObject pdata = news.getJSONObject(i);
                                        String title = pdata.getString("title");
                                        String image=pdata.getString("picture");
                                        Log.d("resim",image);
                                        TextView newsText = view.findViewById(R.id.txtnewstitle);

                                        newsText.setText(title);

                                        // image control
                                        ImageView pImage = view.findViewById(R.id.pronewsimage);

                                            String iUrl = pdata.getString("picture");
                                            //Picasso.get().load("http://i.imgur.com/DvpvklR.png").into(imageView);
                                            Picasso.with(getContext()).load(iUrl).into(pImage);
                                            Log.d("hata",iUrl.toString());


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return view;
                                }
                            };
                            newsListView.setAdapter(newsBaseAdapter);

                            newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                @Override
                                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                   // Intent it = new Intent(getContext(), NewsDetail.class);
                                   // startActivity(it);
                                    try {
                                      NewsDetail.newsDt = news.getJSONObject(i);
                                        Intent it = new Intent(getContext(), NewsDetail.class);
                                        startActivity(it);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }else {
                            Toast.makeText(cnx, "Bu Haber yok !", Toast.LENGTH_SHORT).show();
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

    // TODO: Rename method, update argument and hook method into UI event
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
}
