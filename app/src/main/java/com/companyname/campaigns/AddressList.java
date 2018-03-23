package com.companyname.campaigns;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link AddressList.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link AddressList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AddressList extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public AddressList() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AddressList.
     */
    // TODO: Rename and change types and number of parameters
    public static AddressList newInstance(String param1, String param2) {
        AddressList fragment = new AddressList();
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

    FloatingActionButton btnAdd;
    ListView aListView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_address_list, container, false);

        aListView = v.findViewById(R.id.aListView);

        btnAdd = v.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), AddressAdd.class);
                startActivity(i);
            }
        });

        userPro up = MainActivity.userInf;

        String url = "http://jsonbulut.com/json/addressList.php";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
        hm.put("musterilerID", "" + up.getUserId());
        new jsonData(v.getContext(), url, hm).execute();

        return v;
    }
    static ArrayList<String> liste = new ArrayList<>();
    static ArrayAdapter<String> adp = null;

    String musterilerID = null;
    String id = null;
    String il = null;
    String ilce = null;
    String Mahalle = null;
    String adres = null;
    String kapiNo = null;
    String not = null;
    String tarih = null;

    // json data export
     class jsonData extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String, String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";

        // ProgressDialog pro;
        public jsonData(Context cnx, String url, HashMap<String, String> hm) {
            liste.clear();
            adp = null;
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
                    final JSONArray jarr = jobj.getJSONArray("announcements").getJSONArray(0);
                    if (jarr.length() > 0) {
                        for (int i = 0; i < jarr.length(); i++) {
                            JSONObject jdata = jarr.getJSONObject(i);
                            id = jdata.getString("id");
                            musterilerID = jdata.getString("musterilerID");
                            il = jdata.getString("il");
                            ilce = jdata.getString("ilce");
                            Mahalle = jdata.getString("Mahalle");
                            adres = jdata.getString("adres");
                            kapiNo = jdata.getString("kapiNo");
                            not = jdata.getString("not");
                            tarih = jdata.getString("tarih");

                            liste.add(not);

                        }

                        adp = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, android.R.id.text1, liste);
                        aListView.setAdapter(adp);

                        aListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                AlertDialog.Builder uyari = new AlertDialog.Builder(getContext());
                                uyari.setTitle("Adres Detayı");
                                try {
                                    uyari.setMessage("İl:"+jarr.getJSONObject(i).getString("il")+"\n"+"İlçe:"+jarr.getJSONObject(i).getString("ilce")+"\n"+"Mahalle: " +jarr.getJSONObject(i).getString("Mahalle")+"\n"+"adres: "+jarr.getJSONObject(i).getString("adres")+"\n"+"kapiNo: "+jarr.getJSONObject(i).getString("kapiNo")+"\n"+"not: "+jarr.getJSONObject(i).getString("not")+"\n"+"tarih: "+jarr.getJSONObject(i).getString("tarih")+"\n");
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                uyari.setCancelable(false);
                                //uyari.setIcon(R.mipmap.uyari);

                                //yes button install
                                uyari.setPositiveButton("Kapat", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //Toast.makeText(getContext(), "Evet Tıklandı", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                //uyari.create().show();
                                AlertDialog dialog = uyari.create();
                                dialog.show();
                            }
                        });

                        aListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                            @Override
                            public boolean onItemLongClick(AdapterView<?> adapterView, View view, final int i, long l) {
                                AlertDialog.Builder uyari = new AlertDialog.Builder(getContext());
                                uyari.setTitle("Adres Silme");
                                uyari.setMessage("Adresi silmek istediğinize emin misiniz?");
                                uyari.setCancelable(false);
                                //uyari.setIcon(R.mipmap.);

                                //yes button install
                                uyari.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int a) {

                                        String url = "http://jsonbulut.com/json/addressDelete.php";
                                        HashMap<String, String> hm = new HashMap<>();
                                        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
                                        try {
                                            hm.put("adresID", "" + jarr.getJSONObject(i).getString("id"));
                                            hm.put("musterilerID", "" + jarr.getJSONObject(i).getString("musterilerID"));
                                        } catch (JSONException e) {
                                            e.printStackTrace();
                                        }

                                        new jsonDataDelete(getContext(), url, hm).execute();
                                    }
                                });

                                //no button install
                                uyari.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        Toast.makeText(getContext(), "Silme işlemi iptal edildi", Toast.LENGTH_SHORT).show();
                                    }
                                });


                                //uyari.create().show();
                                AlertDialog dialog = uyari.create();
                                dialog.show();
                                return false;
                            }
                        });


                    } else {
                        Toast.makeText(cnx, "JSON getirme hatası", Toast.LENGTH_SHORT).show();
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

    class jsonDataDelete extends AsyncTask<Void, Void, Void> {

        String url = "";
        HashMap<String, String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";

        public jsonDataDelete(Context cnx, String url, HashMap<String, String> hm) {
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
                    boolean durum = jobj.getJSONArray("announcements").getBoolean(0);
                    Log.e("durum", "durum geldi");
                    if (durum) {
                        Log.e("girdi", "durum girdi");
                        Toast.makeText(cnx, "Adres silme başarılı!", Toast.LENGTH_SHORT).show();

                        String url = "http://jsonbulut.com/json/addressList.php";
                        HashMap<String, String> hm = new HashMap<>();
                        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
                        hm.put("musterilerID", "" + musterilerID);
                        new jsonData(getContext(), url, hm).execute();
                    } else {
                        Toast.makeText(cnx, "Adres silinemedi!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(cnx, "Json Pars Silme Hatası", Toast.LENGTH_SHORT).show();
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

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
