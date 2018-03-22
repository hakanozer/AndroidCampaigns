package com.companyname.campaigns;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link Favorites.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link Favorites#newInstance} factory method to
 * create an instance of this fragment.
 */
public class Favorites extends Fragment {


    static FavProList pr=null;

    ListView listFovorites;
    ArrayList<String> titles=new ArrayList<>();
    ArrayList<FavProList> prls=new ArrayList<>();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public Favorites() {
        // Required empty public constructor
    }



    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment Favorites.
     */
    // TODO: Rename and change types and number of parameters
    public static Favorites newInstance(String param1, String param2) {
        Favorites fragment = new Favorites();
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
        View view=inflater.inflate(R.layout.fragment_favorites,container,false);
        listFovorites=view.findViewById(R.id.listFovorites);
        listData(view);

        listFovorites.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(final AdapterView<?> adapterView, final View view, int i, long l) {
                AlertDialog.Builder uyari=new AlertDialog.Builder(view.getContext());
                uyari.setTitle("Uyarı Başlığı");
                uyari.setTitle("Uyarı Mesajı");
                uyari.setCancelable(false);
                uyari.setIcon(R.drawable.ic_launcher_background);
                uyari.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        DB db=new DB(getContext());
                        SQLiteDatabase sil=db.getWritableDatabase();
                        int sDurum=sil.delete("liste","favorid" + "=" + prls.get(0).getFavorid(),null);
                        if(sDurum>0){
                            Toast.makeText(getContext(), "Silme işlemi başarılı", Toast.LENGTH_SHORT).show();
                            listData(view);
                        }
                    }
                });
                uyari.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(getContext(), "Silme işlemi başarısız", Toast.LENGTH_SHORT).show();
                    }
                });
                uyari.create().show();
            }
        });
        return view;
    }




    public void listData(View view){
        DB db=new DB(view.getContext());
        SQLiteDatabase oku=db.getReadableDatabase();
        Cursor cr=oku.query("liste",null,null,null,null,null,null);
        titles.clear();
        prls.clear();
        while(cr.moveToNext()){
            String title=cr.getString(cr.getColumnIndex("fProductTitle"));
            titles.add(title);
            FavProList pr=new FavProList();
            pr.setFavorid(cr.getInt(cr.getColumnIndex("favorid")));
            pr.setFuserid(cr.getInt(cr.getColumnIndex("fuserid")));
            pr.setFproductid(cr.getInt(cr.getColumnIndex("fproductid")));
            pr.setfProductTitle(cr.getString(cr.getColumnIndex("fProductTitle")));
            pr.setfProductMoney(cr.getString(cr.getColumnIndex("fProductMoney")));
            prls.add(pr);
        }
        final ArrayAdapter<String> adp=new ArrayAdapter<String>(view.getContext(),android.R.layout.simple_list_item_1,android.R.id.text1,titles);
        listFovorites.setAdapter(adp);
        oku.close();
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
