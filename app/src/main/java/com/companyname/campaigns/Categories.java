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
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Categories.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Categories#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Categories extends Fragment {

    ExpandableListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader = new ArrayList<>();
    List<String> listIdHeader = new ArrayList<>();
    HashMap<String, List<String>> listDataChild = new HashMap<>();
    HashMap<String, List<String>> listIdChild = new HashMap<>();
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Categories() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Categories.
     */
    // TODO: Rename and change types and number of parameters
    public static Categories newInstance(String param1, String param2) {
        Categories fragment = new Categories();
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
        View v = inflater.inflate(R.layout.fragment_categories, container, false);

        // get the listview
        expListView =  v.findViewById(R.id.lvExp);



        String url = "http://jsonbulut.com/json/companyCategory.php";
        HashMap<String, String> hm = new HashMap<>();
        hm.put("ref", "ce7f46683b56cb84131405b848678c51");
        new Categories.jsonData(v.getContext(), url, hm).execute();



        return v;
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
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
            } catch (Exception ex) {
                Toast.makeText(cnx, "İşlem Başarısız Oldu", Toast.LENGTH_SHORT).show();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            // grafiksel işlemler bu gövdede yer alır.
            if (!jsonString.equals("")) {
                try {
                    HashMap<String,String> hmx = new HashMap<>();
                    JSONObject jobj = new JSONObject(jsonString);
                    boolean durum = jobj.getJSONArray("Kategoriler").getJSONObject(0).getBoolean("durum");
                    String mesaj = jobj.getJSONArray("Kategoriler").getJSONObject(0).getString("mesaj");
                    if (durum) {
                        JSONArray categoryArray = jobj.getJSONArray("Kategoriler").getJSONObject(0).getJSONArray("Categories");

                        for (int i=0 ; i < categoryArray.length() ; i++){
                                if (categoryArray.getJSONObject(i).getInt("TopCatogryId") == 0){
                                    String head = categoryArray.getJSONObject(i).getString("CatogryName")+"";
                                    String id = categoryArray.getJSONObject(i).getInt("CatogryId")+"";
                                    Log.d("list name",  head);
                                    listDataHeader.add(head);
                                    listIdHeader.add(id);
                                    List<String> cat =  new ArrayList<String>();
                                    List<String> catid =  new ArrayList<String>();
                                    listDataChild.put(head,cat);
                                    listIdChild.put(head,catid);
                                }
                            }
                        for (int j=0;j<categoryArray.length();j++){
                            for (int k=0;k<listIdHeader.size();k++){
                                String catid= categoryArray.getJSONObject(j).getInt("TopCatogryId")+"";
                                String id= categoryArray.getJSONObject(j).getInt("CatogryId")+"";
                                String catName = categoryArray.getJSONObject(j).getString("CatogryName")+"";
                                if(catid.equals(listIdHeader.get(k))){
                                    listDataChild.get(listDataHeader.get(k)).add(catName);
                                    listIdChild.get(listDataHeader.get(k)).add(id);
                                }
                            }
                        }
                        listAdapter = new ExpandableListAdapter(cnx, listDataHeader, listDataChild);
                        // setting list adapter
                        expListView.setAdapter(listAdapter);
                        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
                            @Override
                            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                                Products.productCatId =  listIdChild.get(listDataHeader.get(i)).get(i1);
                                Products.productCatName =  listDataChild.get(listDataHeader.get(i)).get(i1);
                                Intent intent = new Intent(getContext(), Products.class);
                                startActivity(intent);
                                return false;
                            }
                        });
                    } else {
                        Toast.makeText(cnx, mesaj, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception ex) {
                    Toast.makeText(cnx, "Json Pars Hatası", Toast.LENGTH_SHORT).show();
                    Log.e("Json Pars Hatası", ex.toString() );
                }
            } else {
                Toast.makeText(cnx, "Sunucu Hatası Oluştur.. ", Toast.LENGTH_SHORT).show();
            }

            super.onPostExecute(aVoid);
        }

    }
}
