package edu.bloomu.volan.reticketmaster;

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * This class will locate near by barbershops and other places
 * Created by Volan on 11/22/17.
 */
public class GetNearByBarberShopInformation extends AsyncTask<Object, String, String> {

   String googleBarbershopData;
    GoogleMap googleMap;
    String url;
    //JSONObject googleBarbershopJson;

    @Override
    protected String doInBackground(Object... objects) {

        googleMap = (GoogleMap) objects[0];
        url = (String) objects[1];
        Retrieve retrieve = new Retrieve();
        try {
            googleBarbershopData =  retrieve.readUrl(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return googleBarbershopData;

    }

    @Override
    protected void onPostExecute(String result) {
        List<HashMap<String, String>> nearbyBarberShopList = null;
        DataParser dataParser = new DataParser();
        nearbyBarberShopList = dataParser.parseBarberShopList(result);
        showNearbyBarberShops(nearbyBarberShopList);

    }

    /**
     * This method will display near by barbershops and other locations in the list
     * @param nearbyBarberShopList
     */
    public void showNearbyBarberShops(List<HashMap<String, String>> nearbyBarberShopList)
    {

        Log.i("GetNearbyBarberShop","in show nearby method");
        for (int i = 0; i < nearbyBarberShopList.size() ; i++)
        {
            MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googleBarberShop = nearbyBarberShopList.get(i);

            String barberShopName = googleBarberShop.get("place_name");
            String vicinity = googleBarberShop.get("vicinity");
            double latitude = Double.parseDouble(googleBarberShop.get("lat"));
            double longitude = Double.parseDouble(googleBarberShop.get("lng"));

            LatLng latLng = new LatLng(latitude, longitude);
            markerOptions.position(latLng);
            markerOptions.title(barberShopName+" : "+vicinity);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(
                    BitmapDescriptorFactory.HUE_BLUE));
            googleMap.addMarker(markerOptions);

            //move map camera
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));

        }
    }
}
