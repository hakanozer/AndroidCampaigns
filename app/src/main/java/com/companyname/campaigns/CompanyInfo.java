package com.companyname.campaigns;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.squareup.picasso.Picasso;

public class CompanyInfo extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    TextView txtCompanyName,txtCompanyPhone,txtCompanyAdress,txtCompanyMail;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_company_info);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map1);
        mapFragment.getMapAsync(this);
        txtCompanyAdress=findViewById(R.id.txtCompanyAdress);
        txtCompanyName=findViewById(R.id.txtCompanyName);
        txtCompanyPhone=findViewById(R.id.txtCompanyPhone);

        txtCompanyName.setText(MainActivity.cp.getCompanyName());
        txtCompanyPhone.setText(MainActivity.cp.getCompanyPhone());
        setTitle(MainActivity.cp.getCompanyName());
        ImageView imgView = (ImageView) findViewById(R.id.imageView);
        Picasso.with(this)
                .load(MainActivity.cp.getCompanylogo())
                .into(imgView);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        LatLng location= new LatLng(Double.valueOf(MainActivity.cp.getLatitude()),Double.valueOf(MainActivity.cp.getLongitude()));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location,15));
        mMap.addMarker(new MarkerOptions().position(location).title(MainActivity.cp.getCompanyAddress()));
        Log.d("Map","Yükleme Tamalandı");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home :
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


}