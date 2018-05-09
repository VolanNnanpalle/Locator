package edu.bloomu.volan.reticketmaster;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This method presents the user with the login activity to perform various tasks
 * Created by Volan on 11/20/17.
 */

public class LoggedIn extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap googleMap;
    GoogleApiClient googleApiClient;
    LocationRequest locationRequest;
    Location lastLocation;
    Marker currentLocationmMarker;
    public static final int REQUEST_LOCATION_CODE = 99;
    int PROXIMITY_RADIUS = 5000;
    double latitude  = 0,longitude =0;
    static final int CAMERA_REQUEST = 1;
    private final int REQ_CODE_SPEECH_INPUT = 200;
    EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.logged_in);

        editText = findViewById(R.id.search_EditText);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            checkLocationPermission();

        }
        //Check if Google Play Services Available or not
        if(googleServicesAvailable())
        {
            Toast.makeText(this,"Connected to play services",
                    Toast.LENGTH_SHORT).show();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


    }


    /**
     * This method will manipulate the map once it is available. Here, markers and lines are added,
     * listeners or movement of the camera are also added. The Google Play services is check if
     * installed, if not then the user will be prompted to install it inside the SupportMapFragment.
     * For this method to be triggered, the Google Play services must be installed.
     */
    @Override
    public void onMapReady(GoogleMap mMap) {
        googleMap = mMap;
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        //Initialize Google Play Services
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED)
            {
                bulidGoogleApiClient();
                googleMap.setMyLocationEnabled(true);
            }
        }
        else
        {
            bulidGoogleApiClient();
            googleMap.setMyLocationEnabled(true);
        }
    }

    /**
     * This method will request for the users permission to use the feature which allow the
     * application to work properly
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        switch(requestCode)
        {
            case REQUEST_LOCATION_CODE:
                // If request is cancelled, the result arrays are empty.
                if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    // permission was granted. Do the
                    // contacts-related task you need to do.
                    if(ContextCompat.checkSelfPermission(this,
                            Manifest.permission.ACCESS_FINE_LOCATION) !=
                            PackageManager.PERMISSION_GRANTED)
                    {
                        if(googleApiClient == null)
                        {
                            bulidGoogleApiClient();
                        }
                        googleMap.setMyLocationEnabled(true);

                    }
                }
                else
                {
                    // Permission denied, Disable the functionality that depends on this permission.
                    Toast.makeText(this,"Permission Denied" , Toast.LENGTH_LONG).show();
                }
        }
    }



    protected synchronized void bulidGoogleApiClient() {
        googleApiClient = new GoogleApiClient.Builder(
                this).addConnectionCallbacks(this).addOnConnectionFailedListener
                (this).addApi(LocationServices.API).build();
        googleApiClient.connect();

    }

    /**
     * This method is called when the location has changed to remove the marker and place a marker
     * at the new location
     * @param location
     */
    @Override
    public void onLocationChanged(Location location) {
        lastLocation = location;
        if(currentLocationmMarker != null)
        {
            currentLocationmMarker.remove();

        }
        //Place current location marker
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        LatLng latLng = new LatLng(location.getLatitude() , location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("Current Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA));
        currentLocationmMarker = googleMap.addMarker(markerOptions);

        //move map camera
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        googleMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        //stop location updates
        if(googleApiClient != null)
        {
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,this);
        }
    }

    /**
     * This method will allow the user to search for nearby barbershops, take pictures and
     * convert their speech to text
     * @param view
     */
    public void onClick(View view)
    {
        Object dataTransfer[] = new Object[2];

        GetNearByBarberShopInformation getNearByBarberShopInformation =
                new GetNearByBarberShopInformation();
        Toast.makeText(LoggedIn.this, "here",
                Toast.LENGTH_SHORT).show();
        switch(view.getId())
        {

            case R.id.search_button:
                EditText tf_location = findViewById(R.id.search_EditText);
                String location = tf_location.getText().toString();
                List<Address> addressList = null;
                MarkerOptions markerOptions = new MarkerOptions();

                if(!location.equals(""))
                {
                    Geocoder geocoder = new Geocoder(this);

                    try {
                        addressList = geocoder.getFromLocationName(location, 5);

                        if(addressList != null)
                        {
                            for(int i = 0;i<addressList.size();i++)
                            {
                                //Address address = addressList.get(i);
                                LatLng latLng = new LatLng(addressList.get(i).getLatitude() ,
                                        addressList.get(i).getLongitude());
                                markerOptions.position(latLng);
                                markerOptions.title(location);
                                googleMap.addMarker(markerOptions);
                                googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                                googleMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case R.id.find_barbershop_button:
                googleMap.clear();
                String barbershop = "barbershop";
                String url = getUrl(latitude, longitude, barbershop);
                dataTransfer[0] = googleMap;
                dataTransfer[1] = url;
                getNearByBarberShopInformation.execute(dataTransfer);
                Toast.makeText(LoggedIn.this, "Showing Nearby BarberShops",
                        Toast.LENGTH_SHORT).show();
                break;
            case R.id.capture:
                //To open camera
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(cameraIntent,CAMERA_REQUEST);
                break;
            case R.id.speech:
                //To recognize speech
                Intent speechIntent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                speechIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.ENGLISH);
                try {
                    startActivityForResult(speechIntent, 200);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                break;

        }
    }

    /**
     *This method gets the url which is passed by the map
     * @param latitude
     * @param longitude
     * @param nearbyPlace
     * @return
     */
    private String getUrl(double latitude , double longitude , String nearbyPlace)
    {

        StringBuilder googlePlaceUrl = new StringBuilder("https://maps.googleapis.com/maps/api/" +
                "place/nearbysearch/json?");
        googlePlaceUrl.append("location="+latitude+","+longitude);
        googlePlaceUrl.append("&radius="+PROXIMITY_RADIUS);
        googlePlaceUrl.append("&type="+nearbyPlace);
        googlePlaceUrl.append("&sensor=true");
        googlePlaceUrl.append("&key="+"AIzaSyCH6t02G97dA0s29LLi6Q7A7BK-dWI85Ds");

        return googlePlaceUrl.toString();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(100);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);


        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)
        {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient,
                    locationRequest, this);
        }
    }

    /**
     * This method check if the user has allowed location permission
     * @return
     */
    public boolean checkLocationPermission()
    {
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED )
        {

            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,Manifest.permission.ACCESS_FINE_LOCATION))
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            else
            {
                ActivityCompat.requestPermissions(this,new String[]
                        {Manifest.permission.ACCESS_FINE_LOCATION },REQUEST_LOCATION_CODE);
            }
            return false;

        }
        else
            return true;
    }

    /**
     * This method is to check if the application is connected to Google Play Services
     * @return
     */
    public boolean googleServicesAvailable()
    {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int isAvailable = apiAvailability.isGooglePlayServicesAvailable(this);
        if(isAvailable == ConnectionResult.SUCCESS)
        {
            return true;
        }
        else if(apiAvailability.isUserResolvableError(isAvailable)){
            Dialog dialog = apiAvailability.getErrorDialog(this, isAvailable,0);
            dialog.show();

        }
        else
        {
            Toast.makeText(this,"Cannot connect to play services",
                    Toast.LENGTH_SHORT).show();
        }

        return false;
    }

    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editText.setText(result.get(0));
                }
                break;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }


}
