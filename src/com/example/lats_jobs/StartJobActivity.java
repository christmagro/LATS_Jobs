package com.example.lats_jobs;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class StartJobActivity extends Activity {
	
	private static final String jobid = "jobid";
	protected LocationManager locationManager;

    LocationListener locationlistener;
	private static String url = "http://christmagro.no-ip.biz:8080/lats/json/testpost2";
	Button btnStopGps;   
	Button btnStartGps; 
	    
	    
	   
	    
	    

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		 this.setContentView(R.layout.start_job);
		 btnStopGps = (Button) findViewById(R.id.buttonStop);
		 btnStartGps = (Button) findViewById(R.id.buttonStart);
		 locationlistener = new MyLocationListener();
		
		 Intent i = getIntent();

		    String newjobid = i.getStringExtra(jobid);
		    Log.d("result", newjobid);
		    
		   
		    
		    
		    TextView txtjobid = (TextView)findViewById(R.id.jobid);
		    txtjobid.setText(newjobid);
		    int jobid = Integer.parseInt(newjobid);
		    
		    
		    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		    
		   
		    
	        locationManager.requestLocationUpdates(
	                LocationManager.GPS_PROVIDER, 
	                0, 
	                0,
	                locationlistener
	             
	        );
	        
	        
	        btnStartGps.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {		
				      
			        showCurrentLocation(5);
					
					
				}
			});
	       
	        
	        btnStopGps.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {		
				      
			       locationManager.removeUpdates(locationlistener);
			     
					
					
				}
			});
		    
	  
		   
		    
	}
	
	
	
	
	
	
	
	 protected void sendJson(final int jobid, final double longitude, final double latitude) {
	        Thread t = new Thread()
	       
	        {

	            public void run() {
	            	 
	                Looper.prepare(); //For Preparing Message Pool for the child Thread
	                HttpClient client = new DefaultHttpClient();
	                HttpConnectionParams.setConnectionTimeout(client.getParams(), 10000); //Timeout Limit
	                HttpResponse response;
	                JSONObject json = new JSONObject();

	                try {
	                    HttpPost post = new HttpPost(url);
	                    json.put("test1", jobid);
	                    json.put("test2", longitude);
	                    json.put("test3", latitude);
	                    
	                    StringEntity se = new StringEntity( json.toString());  
	                    se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
	                    post.setEntity(se);
	                    response = client.execute(post);


	                } catch(Exception e) {
	                    e.printStackTrace();
//                    createDialog("Error", "Cannot Estabilish Connection");
	                }

	                Looper.loop(); //Loop in the message queue
	            }
	        };

	        t.start();      
	    }
	 
	 protected void showCurrentLocation(int jobid) {
		 
		
	        Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	        

	        if (location != null) {
	            String message = String.format(
	                    "New Location \n Longitude: %1$s \n Latitude: %2$s",
	                    location.getLongitude(), location.getLatitude()
	                    
	            );
	            
	            
	            sendJson(jobid,location.getLongitude(),location.getLatitude());
	            Toast.makeText(StartJobActivity.this, message,
	                    Toast.LENGTH_LONG).show();
	        }

	    }   
	 
	
	 
     
	

		private class MyLocationListener implements LocationListener {

	        public void onLocationChanged(Location location) {
	        	 Intent i = getIntent();

	 		    String newjobid = i.getStringExtra(jobid);
	 		   int jobid = Integer.parseInt(newjobid);
	        	
	            String message = String.format(
	                    "Changed Location \n Longitude: %1$s \n Latitude: %2$s",
	                    location.getLongitude(), location.getLatitude()
	                    
	            );
	            Toast.makeText(StartJobActivity.this, message, Toast.LENGTH_LONG).show();
	             sendJson(jobid,location.getLongitude(),location.getLatitude());
	            
	        }

	        public void onStatusChanged(String s, int i, Bundle b) {
	            Toast.makeText(StartJobActivity.this, "Provider status changed",
	                    Toast.LENGTH_LONG).show();
	        }

	        public void onProviderDisabled(String s) {
	            Toast.makeText(StartJobActivity.this,
	                    "Provider disabled by the user. GPS turned off",
	                    Toast.LENGTH_LONG).show();
	        }

	        public void onProviderEnabled(String s) {
	            Toast.makeText(StartJobActivity.this,
	                    "Provider enabled by the user. GPS turned on",
	                    Toast.LENGTH_LONG).show();
	        }
	        
	     
	   	 }
	        

	

}
