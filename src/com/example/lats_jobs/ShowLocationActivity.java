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
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ShowLocationActivity extends Activity implements LocationListener {
  private static final String TAG_JOBID = "jobid";
  private static final String TAG_CLIENTNAME = "clientName";
  private TextView latituteField;
  private TextView longitudeField;
  private TextView jobIdField;
  private TextView clientNameField;
  private LocationManager locationManager;
  private String provider;
  private static String url = "http://christmagro.no-ip.biz:8080/lats/json/testpost2";
  Button btnStop;
  Button btnStart;
  Button btnPause;

  
/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    latituteField = (TextView) findViewById(R.id.TextView02);
    longitudeField = (TextView) findViewById(R.id.TextView04);
    Intent i = getIntent();
    String jobid =i.getStringExtra(TAG_JOBID);
    String clientName = i.getStringExtra(TAG_CLIENTNAME);
    jobIdField = (TextView) findViewById(R.id.jobId);
    clientNameField = (TextView) findViewById(R.id.clientName);
    jobIdField.setText(jobid);
    clientNameField.setText(clientName);
    // Get the location manager
    locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    // Define the criteria how to select the locatioin provider -> use
    // default
    Criteria criteria = new Criteria();
    provider = locationManager.getBestProvider(criteria, false);
    
    final Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
    
 

    // Initialize the location fields
    if (location != null) {
      System.out.println("Provider " + provider + " has been selected.");
      
    } else {
      latituteField.setText("Location not available");
      longitudeField.setText("Location not available");
      
    }
    btnStop = (Button) findViewById(R.id.stopButton);
    btnStop.setOnClickListener(new View.OnClickListener() {
		
  		@Override
  		public void onClick(View arg0) {		
  		      
  	        
  			stopGPS(location);
  			
  		}
  	});
    
//    btnStart = (Button) findViewById(R.id.startButton);
//    btnStart.setOnClickListener(new View.OnClickListener() {
//		
//  		@Override
//  		public void onClick(View arg0) {		
//  		      
//  			onLocationChanged(location);
//  			btnStart.setVisibility(View.GONE);
//  			
//  		}
//  	});
    btnPause = (Button) findViewById(R.id.pauseButton);
    btnPause.setOnClickListener(new View.OnClickListener() {
		
  		@Override
  		public void onClick(View arg0) {		
  		      
  			pauseGPS(location);
  			
  		}
  	});
    
    
  }
  
  
/* Request updates at startup */
  @Override
  protected void onResume() {
    super.onResume();
    locationManager.requestLocationUpdates(locationManager.NETWORK_PROVIDER, 6000, 0, this);
  }

//  /* Remove the locationlistener updates when Activity is paused */
//  @Override
//  protected void onPause() {
//    super.onPause();
//
//  }
  
  protected void pauseGPS(Location location) {
	  
	 
	    locationManager.removeUpdates(this);
	            
	    double lat = (double) (location.getLatitude());
	    double lng = (double) (location.getLongitude());
	    String status = "pause";
	    Intent j = getIntent();
	    String job = j.getStringExtra(TAG_JOBID);
	    int jobId = Integer.parseInt(job);
	    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
	    
	    sendJson(jobId, status, lng,lat, imei);
	    Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
	  }
  
  protected void stopGPS(Location location) {
	 
	    locationManager.removeUpdates(this);
	    double lat = (double) (location.getLatitude());
	    double lng = (double) (location.getLongitude());
	    String status = "stop";
	    Intent j = getIntent();
	    String job = j.getStringExtra(TAG_JOBID);
	    int jobId = Integer.parseInt(job);
	    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
        String imei = telephonyManager.getDeviceId();
	    
	    sendJson(jobId, status, lng,lat, imei);
	    Intent i = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(i);
	  }

  @Override
  public void onLocationChanged(Location location) {
    double lat = (double) (location.getLatitude());
    double lng = (double) (location.getLongitude());
    latituteField.setText(String.valueOf(lat));
    longitudeField.setText(String.valueOf(lng));
    String status = "start";
    Intent j = getIntent();
    String job = j.getStringExtra(TAG_JOBID);
    int jobId = Integer.parseInt(job);
    TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
    String imei = telephonyManager.getDeviceId();
    
    sendJson(jobId, status, lng,lat, imei);
  }

  @Override
  public void onStatusChanged(String provider, int status, Bundle extras) {
    // TODO Auto-generated method stub

  }

  @Override
  public void onProviderEnabled(String provider) {
    Toast.makeText(this, "Enabled new provider " + provider,
        Toast.LENGTH_SHORT).show();

  }

  @Override
  public void onProviderDisabled(String provider) {
    Toast.makeText(this, "Disabled provider " + provider,
        Toast.LENGTH_SHORT).show();
  }
  
  
  
  
  protected void sendJson(final int jobid, final String status, final double longitude, final double latitude, final String imei) {
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
                  json.put("jobid", jobid);
                  json.put("status", status);
                  json.put("longitude", longitude);
                  json.put("latitude", latitude);
                  json.put("imei",imei);
                  
                  StringEntity se = new StringEntity( json.toString());  
                  se.setContentType(new BasicHeader(HTTP.CONTENT_TYPE, "application/json"));
                  post.setEntity(se);
                  response = client.execute(post);


              } catch(Exception e) {
                  e.printStackTrace();
//              createDialog("Error", "Cannot Estabilish Connection");
              }

              Looper.loop(); //Loop in the message queue
          }
      };

      t.start();      
  }
}
