package com.companyname.campaigns;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
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

public class Orders extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Orders() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Orders.
     */
    // TODO: Rename and change types and number of parameters
    public static Orders newInstance(String param1, String param2) {
        Orders fragment = new Orders();
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
    ListView oListView;
    BaseAdapter urunBaseAdapter;
    LayoutInflater urunLayoutInflater;
    static JSONObject jobj=null;
    JSONArray arr = null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_orders, container, false);
        oListView=v.findViewById(R.id.oListView);
        urunLayoutInflater = LayoutInflater.from(getContext());
        userPro up=MainActivity.userInf;
        String id=up.getUserId()+"";
        String url="http://jsonbulut.com/json/orderList.php";
        HashMap<String, String>hm=new HashMap<>();
        hm.put("ref","ce7f46683b56cb84131405b848678c51");
        hm.put("musterilerID",id);
     new jsonData(getContext(), url, hm).execute();
        return v;
    }
    class jsonData extends AsyncTask<Void, Void, Void> {
        String url = "";
        HashMap<String, String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";

        public jsonData(Context cnx, String url, HashMap<String, String> hm) {
            this.cnx = cnx;
            this.url = url;
            this.hm = hm;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
            } catch (Exception ex) {
                Log.d("İşlem Başarısız Oldu", ex.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (!jsonString.equals("")) {
                try {
                   jobj = new JSONObject(jsonString);
                    arr = jobj.getJSONArray("orderList").getJSONArray(0);
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
                                        String userId = pdata.getString("must_id");
                                        String productId=pdata.getString("urun_id");
                                        String productName = pdata.getString("urun_adi");
                                        String photo = pdata.getString("thumb");
                                        String price= pdata.getString("fiyat");
                                        String info= pdata.getString("siparis_bilgisi");

                                        TextView tText = view.findViewById(R.id.proitemTitle);
                                        TextView pText = view.findViewById(R.id.proitemPrice);
                                        tText.setText(productName);
                                        pText.setText(info);

                                        // image control
                                        ImageView pImage = view.findViewById(R.id.proitemImage);
                                        String imageStatu = pdata.getString("thumb");
                                        if (!imageStatu.equals("")) {
                                            String iUrl = pdata.getString("thumb");
                                            Picasso.with(cnx.getApplicationContext()).load(iUrl).into(pImage);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    return view;
                                }
                            };
                            oListView.setAdapter(urunBaseAdapter);
                        }
                   else if (arr.getJSONObject(0)==null){
                        Toast.makeText(cnx, "Bu Kategoride ürün yok !", Toast.LENGTH_SHORT).show();
                    }
                     else {
                        String mesaj=arr.getJSONObject(0).getString("mesaj");
                        Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Log.d("Json Pars Hatası", ex.toString());
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
