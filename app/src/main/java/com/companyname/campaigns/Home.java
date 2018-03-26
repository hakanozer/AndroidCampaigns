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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Home.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Home#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Home extends Fragment implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Home() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Home.
     */
    // TODO: Rename and change types and number of parameters
    public static Home newInstance(String param1, String param2) {
        Home fragment = new Home();
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
    SliderLayout homeSlider;
    BaseAdapter homeBaseAdapter;
    LayoutInflater homeLayoutInflater;
    ListView homeList;
    static JSONObject job=null;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        homeSlider=view.findViewById(R.id.slider);
        homeList=view.findViewById(R.id.homeList);
        homeLayoutInflater=LayoutInflater.from(getContext());
        String url="http://jsonbulut.com/json/product.php";
        HashMap<String, String>hm=new HashMap<>();
        hm.put("ref","ce7f46683b56cb84131405b848678c51");
        hm.put("start","0");
        hm.put("count","3");
       hm.put("order","asc");
     new jsonData(getContext(),url,hm).execute();
        return view;
    }
    // TODO: Rename method, update argument and hook method into UI event

    //json inner class
    class jsonData extends AsyncTask<Void,Void,Void>  implements BaseSliderView.OnSliderClickListener,ViewPagerEx.OnPageChangeListener {
        String url = "";
        HashMap<String, String> hm = new HashMap<>();
        Context cnx = null;
        String jsonString = "";
        //progress dialog
        public jsonData(Context cnx, String url,HashMap<String,String>hm){
            this.cnx = cnx;
            this.url = url;
            this.hm = hm;
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                jsonString = Jsoup.connect(url).data(hm).timeout(30000).ignoreContentType(true).get().body().text();
            } catch (IOException e) {
                Log.d("İşlem Başarısız Oldu", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            //graphically operations
            if (!jsonString.equals("")){
                try {
                    JSONObject jobj=new JSONObject(jsonString);
                    boolean state=jobj.getJSONArray("Products").getJSONObject(0).getBoolean("durum");
                    String message = jobj.getJSONArray("Products").getJSONObject(0).getString("mesaj");
                    if (state){
                        final JSONArray arr = jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");


                        homeBaseAdapter= new BaseAdapter() {
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
                                if (view==null){
                                    view= homeLayoutInflater.inflate(R.layout.custom_proitem,null);
                                }
                                try {
                                    JSONObject json=arr.getJSONObject(i);
                                    job=json;
                                    String title = json.getString("productName");
                                    String price = json.getString("price");
                                    TextView tText = view.findViewById(R.id.proitemTitle);
                                    TextView pText = view.findViewById(R.id.proitemPrice);
                                    tText.setText(title);
                                    pText.setText(price);

                                    //image exist control
                                    ImageView pImage = view.findViewById(R.id.proitemImage);
                                    boolean imageState = json.getBoolean("image");
                                    if (imageState){
                                        String imageUrl = json.getJSONArray("images").getJSONObject(0).getString("normal");
                                        Picasso.with(cnx).load(imageUrl).into(pImage);
                                    }
                                }catch (Exception ex){
                                    Log.d("inflater hatası", ex.toString());
                                }
                                return view;
                            }
                        };
                        homeList.setAdapter(homeBaseAdapter);



                        try {
                            boolean imgControl=jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler").getJSONObject(0).getBoolean("image");
                            String title=jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler").getJSONObject(0).getString("productName");
                            if (imgControl){
                                JSONArray jar=jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler");
                                for (int k = 0; k <jar.length() ; k++) {
                                JSONArray ja=jobj.getJSONArray("Products").getJSONObject(0).getJSONArray("bilgiler").getJSONObject(k).getJSONArray("images");
                                HashMap<String, String> hmi = new HashMap<>();
                                for (int i = 0; i <ja.length() ; i++) {
                                    String img=ja.getJSONObject(i).getString("normal");
                                    hmi.put(title+i,img);
                                }
                                for (String sliding: hmi.keySet()) {
                                    TextSliderView tsv=new TextSliderView(getContext());
                                    //initializing
                                    tsv.description(sliding)
                                            .image(hmi.get(sliding))
                                            .setScaleType(BaseSliderView.ScaleType.Fit)
                                            .setOnSliderClickListener(this);
                                    //extra info
                                    tsv.bundle(new Bundle());

                                    tsv.getBundle()
                                            .putString("extra", sliding);
                                    homeSlider.addSlider(tsv);

                                }
                                homeSlider.setPresetTransformer(SliderLayout.Transformer.Background2Foreground);
                                homeSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Top);
                                homeSlider.setCustomAnimation(new DescriptionAnimation());
                                homeSlider.setDuration(100);
                                homeSlider.addOnPageChangeListener(this);
                                }
                            }else {
                                Toast.makeText(getContext(), "Gösterilecek Veri Bulunamadı!", Toast.LENGTH_SHORT).show();
                            }

                        }catch (Exception ex){
                            Log.d("Slider hatasi",ex.toString());
                        }
                    }
                }catch (Exception ex){
                    Log.d("JSON hatasi",ex.toString());
                }
            }
            super.onPostExecute(aVoid);
        }

        @Override
        public void onSliderClick(BaseSliderView slider) {

        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
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

    @Override
    public void onSliderClick(BaseSliderView slider) {
        Toast.makeText(getContext(),slider.getBundle().get("extra") + "",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

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
