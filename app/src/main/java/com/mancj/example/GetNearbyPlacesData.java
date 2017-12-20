package com.mancj.example;

/**
 * Created by huika on 11/29/2017.
 */

import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GetNearbyPlacesData extends AsyncTask<Object, String, String> {

    String googlePlacesData;
    GoogleMap mMap;
    String url;

    public List<HashMap<String, String>> nearbyPlacesList;

    @Override
    protected String doInBackground(Object... params) {
        try {

            Log.d("GetNearbyPlacesData", "doInBackground entered");

            mMap = (GoogleMap) params[0];

            url = (String) params[1];

            DownloadUrl downloadUrl = new DownloadUrl();

            googlePlacesData = downloadUrl.readUrl(url);

            Log.d("GooglePlacesReadTask", "doInBackground Exit");

        } catch (Exception e) {

            Log.d("GooglePlacesReadTask", e.toString());
        }
        return googlePlacesData;
    }

    @Override
    protected void onPostExecute(String result) {

        Log.d("GooglePlacesReadTask", "onPostExecute Entered");

        nearbyPlacesList = null;

        DataParser dataParser = new DataParser();

        nearbyPlacesList =  dataParser.parse(result);

        saveNearbyPlaces(nearbyPlacesList);

        ShowNearbyPlaces(nearbyPlacesList);

        Log.d("GooglePlacesReadTask", "onPostExecute Exit");
    }

    private void saveNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList){

        DatabaseReference myRootDBref = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < nearbyPlacesList.size(); i++){

            Log.d("onPostExecute","Saving location " + i );

            Map<String, String> values = new HashMap<>();

            Map<String, String> waittime = new HashMap<>();

            //dummy waittime
            waittime.put("Waittime" , "10" );

            values = nearbyPlacesList.get(i);

            //an if statement should encapsulate the creation of new entry store
            //if(values.get("vicinity") !=  myRootDBref.child(values.get("vicinity")).getKey() )
              //  if(values.get("place_name") != myRootDBref.child(values.get("vicinity")).child(values.get("place_name")).getKey())
                    myRootDBref.child(values.get("place_name")).child(values.get("vicinity")).child("10").child("waitime").push().setValue(waittime);
        }

    };

    private void ShowNearbyPlaces(List<HashMap<String, String>> nearbyPlacesList) {

        DatabaseReference myRootDBref = FirebaseDatabase.getInstance().getReference();

        for (int i = 0; i < nearbyPlacesList.size(); i++) {

            Log.d("onPostExecute","Entered into showing locations");

            final MarkerOptions markerOptions = new MarkerOptions();

            final HashMap<String, String> googlePlace = nearbyPlacesList.get(i);

            double lat = Double.parseDouble(googlePlace.get("lat"));

            double lng = Double.parseDouble(googlePlace.get("lng"));

            final String placeName = googlePlace.get("place_name");

            final String vicinity = googlePlace.get("vicinity");

            MapsActivity.singleton.AddToList(placeName + ", " + vicinity);

            LatLng latLng = new LatLng(lat, lng);

            markerOptions.position(latLng);

            //The wait time will need to go inside parenthesis
            markerOptions.title(placeName + " : " + vicinity
                    /* myRootDBref.child(googlePlace.get("vicinity")).child(googlePlace.get("place_name")).toString()*/ );


            mMap.addMarker(markerOptions);

            markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));

            //move map camera

            mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

            mMap.animateCamera(CameraUpdateFactory.zoomTo(11));
        }
    }
}
