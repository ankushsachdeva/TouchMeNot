package com.example.hacku1;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.hacku1.model.GeoCodeResult;
import com.example.hacku1.services.GeoCoder;

public class MainActivity extends Activity {
	
    
    private static final long MINIMUM_DISTANCE_CHANGE_FOR_UPDATES = 1; // in Meters
    private static final long MINIMUM_TIME_BETWEEN_UPDATES = 1000; // in Milliseconds
    
    private GeoCoder geoCoder = new GeoCoder();
    
    protected LocationManager locationManager;
    protected Location currentLocation;

    protected Button retrieveLocationButton;
    protected Button reverseGeocodingButton;
    public double oldlat=0.00;
    public double oldlong=0.00;
    DatabaseHandler dbh=new DatabaseHandler(this);
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        retrieveLocationButton = (Button) findViewById(R.id.retrieve_location_button);
        reverseGeocodingButton = (Button) findViewById(R.id.reverse_geocoding_button);
        
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        
        locationManager.requestLocationUpdates(
                LocationManager.GPS_PROVIDER, 
                MINIMUM_TIME_BETWEEN_UPDATES, 
                MINIMUM_DISTANCE_CHANGE_FOR_UPDATES,
                new MyLocationListener()
        );
        
        retrieveLocationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showCurrentLocation();
            }
        });
        
        reverseGeocodingButton.setOnClickListener(new OnClickListener() {            
            @Override
            public void onClick(View v) {                
                performReverseGeocodingInBackground();
            }
        });
        
    }    
    
    protected void performReverseGeocodingInBackground() {
        showCurrentLocation();
        new ReverseGeocodeLookupTask().execute((Void[])null);
    }

    protected void showCurrentLocation() {
    	
        currentLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

        if (currentLocation != null ) {
            String message = String.format(
                    "Current Location \n Longitude: %1$s \n Latitude: %2$s",
                    currentLocation.getLongitude(), currentLocation.getLatitude()
            );
            Context context = getApplicationContext();
            Toast.makeText(context, message,
                    Toast.LENGTH_LONG).show();
        }
        
        
        	
    }   

    private class MyLocationListener implements LocationListener {

    	
        public void onLocationChanged(Location location) {
      
        	String message = String.format(
                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
                    location.getLongitude(), location.getLatitude()
            );
            Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG).show();
            if(Math.abs(oldlat-location.getLatitude())+Math.abs(oldlong-location.getLongitude()) >0);
            oldlat=location.getLatitude();
            oldlong=location.getLongitude();
            
            dbh.addPlace(oldlat,oldlong,2.2);
            Toast.makeText(MainActivity.this, "Newer", Toast.LENGTH_LONG).show();
            List<location> contacts = dbh.getAllplaces();
            location  newc=new location();
            newc.setlong("6.9");
            newc.setlat("9.6");
            newc.ID=5;
            newc.radius=2.0;
            dbh.deleteplace(newc);
            for (location cn : contacts) {
            	Toast.makeText(MainActivity.this, ""+cn.getlat(), Toast.LENGTH_LONG).show();
        }
      }

        public void onStatusChanged(String s, int i, Bundle b) {
            Toast.makeText(MainActivity.this, "Provider status changed",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderDisabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider disabled by the user. GPS turned off",
                    Toast.LENGTH_LONG).show();
        }

        public void onProviderEnabled(String s) {
            Toast.makeText(MainActivity.this,
                    "Provider enabled by the user. GPS turned on",
                    Toast.LENGTH_LONG).show();
        }

    }
    
    public class ReverseGeocodeLookupTask extends AsyncTask <Void, Void, GeoCodeResult> {
        
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            this.progressDialog = ProgressDialog.show(
                    MainActivity.this,
                    "Please wait...contacting Yahoo!", // title
                    "Requesting reverse geocode lookup", // message
                    true // indeterminate
            );
        }

        @Override
        protected GeoCodeResult doInBackground(Void... params) {
            return geoCoder.reverseGeoCode(currentLocation.getLatitude(), currentLocation.getLongitude());
        }

        @Override
        protected void onPostExecute(GeoCodeResult result) {
            this.progressDialog.cancel();
            Toast.makeText(MainActivity.this, result.toString(), Toast.LENGTH_LONG).show();            
        }
        
    }
    
}
