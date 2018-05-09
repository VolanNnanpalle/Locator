package edu.bloomu.volan.reticketmaster;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This method will parse all the location information
 * Created by Volan on 11/22/17.
 */

public class DataParser {

    private HashMap<String, String> getBarbershop(JSONObject googleBarbershopJson)
    {
        HashMap<String, String> googleBarberShopMap = new HashMap<>();

        //store all parameters
        String barberShopName= "--NA--";
        String vicinity = "--NA--";
        String latitude = "";
        String longitude  = "";
        String reference = "";
        try {
            if (!googleBarbershopJson.isNull("name")) {
                barberShopName = googleBarbershopJson.getString("name");

            }
            if (!googleBarbershopJson.isNull("vicinity")) {
                vicinity = googleBarbershopJson.getString("vicinity");
            }

            latitude = googleBarbershopJson.getJSONObject("geometry").getJSONObject("loc" +
                    "ation").getString("lat");
            longitude = googleBarbershopJson.getJSONObject("geometry").getJSONObject("loc" +
                    "ation").getString("lng");

            reference = googleBarbershopJson.getString("reference");

            googleBarberShopMap.put("place_name",barberShopName);
            googleBarberShopMap.put("vicinity",vicinity);
            googleBarberShopMap.put("lat",latitude);
            googleBarberShopMap.put("lng",longitude);
            googleBarberShopMap.put("reference",reference);

        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        return googleBarberShopMap;
    }

    /**
     * This method will fetch each element in the JSON array of each place and store the locations
     * in an array list
     * @param jsonArray
     * @return
     */
    private List<HashMap<String, String>> getBarbershops(JSONArray jsonArray)
    {
        int barberShopCount = jsonArray.length();

        List<HashMap<String, String>> barberShopList = new ArrayList<>();
        HashMap<String,String> barberShopMap = null;

        for (int i =0; i < barberShopCount; i++)
        {
            try {
                barberShopMap = getBarbershop((JSONObject) jsonArray.get(i));
                barberShopList.add(barberShopMap);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return barberShopList;
    }

    /**
     * This method will parse the JSON data
     * @param jsonData
     * @return
     */
    public List<HashMap<String, String>> parseBarberShopList(String jsonData)
    {
        JSONArray jsonArray = null;
        JSONObject jsonObject;

        try {
            jsonObject = new JSONObject(jsonData);
            jsonArray = jsonObject.getJSONArray("results");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return getBarbershops(jsonArray);
    }

}
