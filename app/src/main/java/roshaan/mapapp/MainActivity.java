package roshaan.mapapp;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.identity.intents.Address;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;
    Marker marker1;
    Marker marker2;
    Marker marker3;
    SharedPreferences shared;
    SharedPreferences.Editor editor;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        if (googleServiceAvailable()) {

            setContentView(R.layout.activity_main);
            Toast.makeText(this, "Perfeccty working", Toast.LENGTH_SHORT).show();
            initMap();

            shared = getSharedPreferences("markers", Context.MODE_PRIVATE);
            editor = shared.edit();


        } else {
            //No Google Maps Layput
        }
    }

    //this method will checck if the mobile on which application is running haave google play services available
    public boolean googleServiceAvailable() {

        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        //now checking if it is available
        int isAvailable = api.isGooglePlayServicesAvailable(this);

        //if is available
        if (isAvailable == ConnectionResult.SUCCESS) {
            return true;
        }
        //if device is capable of installing play services but somthing happpend which is not allowing
        else if (api.isUserResolvableError(isAvailable)) {

            Dialog dialog = api.getErrorDialog(this, isAvailable, 0);
            dialog.show();
        }
        //play services cant be installed on device
        else {
            Toast.makeText(this, "Cant connect to play services", Toast.LENGTH_LONG).show();

        }
        return false;

    }


    private void initMap() {

        System.out.println("not");
        MapFragment mapFragment = (MapFragment) getFragmentManager().findFragmentById(R.id.mapFragment);
        //load map
        mapFragment.getMapAsync(this);
    }

    //ye he asal method yhn sy strt hga sb
    @Override
    public void onMapReady(GoogleMap googleMap) {
        System.out.println("reay");
        mGoogleMap = googleMap;

        //first checking if there are any markers inside shared preference

//        Map<String,?> keys = shared.getAll();
//        System.out.println(keys.size()==0);

//        for(Map.Entry<String,?> entry : keys.entrySet()){
//            System.out.println("map values"+entry.getKey() + ": " + entry.getValue().toString());
//
//            LatLng ll=new LatLng(Double.parseDouble(entry.getKey()),Double.parseDouble(entry.getValue().toString()));
//            CameraUpdate update=CameraUpdateFactory.newLatLngZoom(ll,15);
//              mGoogleMap.animateCamera(update);
//
//            MarkerOptions options=new MarkerOptions()
//                    .position(ll);
//            Marker marker= mGoogleMap.addMarker(options);
////
//            if(marker1==null){
//                marker1=marker;
//
//            }
//            else if(marker2==null){
//                marker2=marker;
//                drawLine(marker1 , marker2);
//            }
//            else{
//                marker3=marker;
//                marker1=marker2;
//                marker2=null;
//                drawLine(marker1, marker3);
//                marker1=marker3;
//            }
//        }


        for (int i = 1; i <= shared.getAll().size(); i++) {

            // System.out.println("map values"+entry.getKey() + ": " + entry.getValue().toString());
            String latNdLngString = (shared.getString(String.valueOf(i), ""));
            String latNdLng[] = latNdLngString.split(",");
            // System.out.println("hell"+latNdLng[0]+"k"+latNdLng[1]);
            counter = i;
            LatLng ll = new LatLng(Double.parseDouble(latNdLng[0]), Double.parseDouble(latNdLng[1]));
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);
            mGoogleMap.animateCamera(update);


            //to get the name of locality
            Geocoder gc = new Geocoder(this);
            List<android.location.Address> list = null;
            String locality = null;
            try {
                list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                android.location.Address address = list.get(0);
                locality = address.getSubLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //to get locality name ends here///////////


            MarkerOptions options = new MarkerOptions()
                    .title(String.valueOf(i))
                    .position(ll)
                    .snippet(String.valueOf(locality));
            Marker marker = mGoogleMap.addMarker(options);
//
            if (marker1 == null) {
                marker1 = marker;

            } else if (marker2 == null) {
                marker2 = marker;
                drawLine(marker1, marker2);
            } else {
                marker3 = marker;
                marker1 = marker2;
                marker2 = null;
                drawLine(marker1, marker3);
                marker1 = marker3;
            }
        }


        //to open map with latitude and longitude of my choice
        // goToLocationZoom(29.320221, 68.291436,9);
//        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        mGoogleMap.setMyLocationEnabled(true);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();
    }

    private void goToLocation(double v, double i) {
        LatLng ll = new LatLng(v, i);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double v, double i, int zoom) {
        LatLng ll = new LatLng(v, i);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);
    }

    LocationRequest mLocationRequest;

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        mLocationRequest = LocationRequest.create();
        //kis tarhan kiservice chaiye high accuracy zyada battery leti he
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //ktni der k baad location cahiye
        mLocationRequest.setInterval(1000);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    Polyline line;

    //    Marker marker1;
//    Marker marker2;
//    Marker marker3;
    //this method will be called when the location is changed
    //and we will get our new location inside this method
    @Override
    public void onLocationChanged(Location location) {

        if (location == null) {
            Toast.makeText(this, "Cant get current location", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println(location);
            LatLng ll = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, 15);

            //to get the name of locality
            Geocoder gc = new Geocoder(this);
            List<android.location.Address> list = null;
            String locality = null;
            try {
                list = gc.getFromLocation(ll.latitude, ll.longitude, 1);
                android.location.Address address = list.get(0);
                locality = address.getSubLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //to get locality name ends here///////////


            //  mGoogleMap.moveCamera(update);
            //ye animate krega camera
            counter++;
            mGoogleMap.animateCamera(update);
            MarkerOptions options = new MarkerOptions()
                    .title(String.valueOf(counter))
                    .position(ll)
                    .snippet(String.valueOf(locality));
            Marker marker = mGoogleMap.addMarker(options);

//            editor.putString(String.valueOf(ll.latitude),String.valueOf(ll.longitude));
            editor.putString(String.valueOf(counter), String.valueOf(ll.latitude) + "," + String.valueOf(ll.longitude));
            if (marker1 == null) {
                marker1 = marker;

            } else if (marker2 == null) {
                marker2 = marker;
                drawLine(marker1, marker2);
            } else {
                marker3 = marker;
                marker1 = marker2;
                marker2 = null;
                drawLine(marker1, marker3);
                marker1 = marker3;
            }
        }
    }

    private void drawLine(Marker m1, Marker m2) {


        PolylineOptions options = new PolylineOptions()
                .add(m1.getPosition())
                .add(m2.getPosition())
                .color(Color.RED)
                .width(5);

        mGoogleMap.addPolyline(options);
    }

    @Override
    protected void onStop() {
        super.onStop();
        editor.apply();
    }

    public void logoutClicked(View v) {

        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(this, Login.class));
    }
}
