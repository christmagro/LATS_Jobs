package com.example.lats_jobs;

import java.util.ArrayList;
import java.util.HashMap;
 
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

public class MainActivity extends ListActivity {
	
	 private ProgressDialog pDialog;
	 
	    // URL to get contacts JSON
	    private static String url = "http://christmagro.no-ip.biz:8080/lats/json/listJobs?imei=";
	   
	    // JSON Node names
	    private static final String TAG_JOBID = "jobid";
	    private static final String TAG_CLIENTNAME = "clientName";
	    private static final String TAG_CLIENTADDRESS = "clientAddress";
	    private static final String TAG_APPOINTMENTDATE = "appointmentDate";
	    private static final String TAG_JOBDESCRIPTION = "jobDescription";
	    private static final String TAG_JOBREMARKS = "jobRemarks";
	    private static final String TAG_JOBSTATUS = "jobStatus";
	
	 
	    // contacts JSONArray
	    JSONArray contacts = null;
	 
	    // Hashmap for ListView
	    ArrayList<HashMap<String, String>> joblist;
	 
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.activity_main);
	        
	    
	 
	 
	        joblist = new ArrayList<HashMap<String, String>>();
	 
	        ListView lv = getListView();
	        
	     // Listview on item click listener
	        lv.setOnItemClickListener(new OnItemClickListener() {
	 
	            @Override
	            public void onItemClick(AdapterView<?> parent, View view,
	                    int position, long id) {
	                // getting values from selected ListItem
	                String jobid = ((TextView) view.findViewById(R.id.jobid))
	                        .getText().toString();
	                String clientName = ((TextView) view.findViewById(R.id.clientName))
	                        .getText().toString();
	                String clientAddress = ((TextView) view.findViewById(R.id.clientAddress))
	                        .getText().toString();
	                String appointmentDate = ((TextView) view.findViewById(R.id.appointementDate))
	                        .getText().toString();
	                String jobDescription = ((TextView) view.findViewById(R.id.jobDescription))
	                        .getText().toString();
	                String jobRemarks = ((TextView) view.findViewById(R.id.jobRemarks))
	                        .getText().toString();
	                String jobStatus = ((TextView) view.findViewById(R.id.jobStatus))
	                        .getText().toString();
	                
	                Intent in = new Intent(MainActivity.this, ViewJobActivity.class);

	                // Starting single contact activity

	                in.putExtra(TAG_JOBID, jobid);
	                in.putExtra(TAG_CLIENTNAME, clientName);
	                in.putExtra(TAG_CLIENTADDRESS, clientAddress);
	                in.putExtra(TAG_APPOINTMENTDATE, appointmentDate);
	                in.putExtra(TAG_JOBDESCRIPTION, jobDescription);
	                in.putExtra(TAG_JOBREMARKS, jobRemarks);
	                in.putExtra(TAG_JOBSTATUS, jobStatus);
	                startActivity(in);
	 
	            }
	        });
	 
	    
	        
	        
	     // Calling async task to get json
	        new GetJobs().execute();
	    }
	    /**
	     * Async task class to get json by making HTTP call
	     * */
	    private class GetJobs extends AsyncTask<Void, Void, Void> {
	 
	        @Override
	        protected void onPreExecute() {
	            super.onPreExecute();
	            // Showing progress dialog
	            pDialog = new ProgressDialog(MainActivity.this);
	            pDialog.setMessage("Please wait...");
	            pDialog.setCancelable(false);
	            pDialog.show();
	 
	        }
	 
	        @Override
	        protected Void doInBackground(Void... arg0) {
	            // Creating service handler class instance
	            ServiceHandler sh = new ServiceHandler();
	            
	            TelephonyManager telephonyManager = (TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);
		        String imei = telephonyManager.getDeviceId();
		        Log.d("imei", imei);
		       final String urlimei = url+imei;
	           
	            // Making a request to url and getting response
	            String jsonStr = sh.makeServiceCall(urlimei, ServiceHandler.GET);
	 
	            Log.d("Response: ", "> " + jsonStr);
	 
	            if (jsonStr != null) {
	                try {
	                   // JSONObject jsonObj = new JSONObject(jsonStr);
	                    JSONArray Jarray = new JSONArray(jsonStr);
	                    if(Jarray.length()<=0){

	                        String jobid = "No Jobs are assigned";
	                        String clientName = "";
	                        String clientAddress = "";
	                        String appointementDate = "";
	                        String jobDescription = "";
	                        String jobRemarks = "";
	                        String jobStatus = "";
	                        // tmp hashmap for single contact
	                        HashMap<String, String> job = new HashMap<String, String>();

	                        // adding each child node to HashMap key => value
	                        job.put(TAG_JOBID, jobid);
	                        job.put(TAG_CLIENTNAME, clientName);
	                        job.put(TAG_CLIENTADDRESS, clientAddress);
	                        job.put(TAG_APPOINTMENTDATE, appointementDate);
	                        job.put(TAG_JOBDESCRIPTION, jobDescription);
	                        job.put(TAG_JOBREMARKS, jobRemarks);
	                        job.put(TAG_JOBSTATUS, jobStatus);
	                        joblist.add(job);
	                    	
	                    }else
	                     
	                  
	                    for (int i = 0; i < Jarray.length(); i++) {
	                        JSONObject c = Jarray.getJSONObject(i);
	                         
	                        String jobid = c.getString(TAG_JOBID);
	                        String clientName = c.getString(TAG_CLIENTNAME);
	                        String clientAddress = c.getString(TAG_CLIENTADDRESS);
	                        String appointementDate = c.getString(TAG_APPOINTMENTDATE);
	                        String jobDescription = c.getString(TAG_JOBDESCRIPTION);
	                        String jobRemarks = c.getString(TAG_JOBREMARKS);
	                        String jobStatus = c.getString(TAG_JOBSTATUS);
	                        // tmp hashmap for single contact
	                        HashMap<String, String> job = new HashMap<String, String>();
	 
	                        // adding each child node to HashMap key => value
	                        job.put(TAG_JOBID, jobid);
	                        job.put(TAG_CLIENTNAME, clientName);
	                        job.put(TAG_CLIENTADDRESS, clientAddress);
	                        job.put(TAG_APPOINTMENTDATE, appointementDate);
	                        job.put(TAG_JOBDESCRIPTION, jobDescription);
	                        job.put(TAG_JOBREMARKS, jobRemarks);
	                        job.put(TAG_JOBSTATUS, jobStatus);
	 
	                        // adding contact to contact list
	                        joblist.add(job);
	                    }
	                } catch (JSONException e) {
	                    e.printStackTrace();
	                }
	            } else {
	                Log.e("ServiceHandler", "Couldn't get any data from the url");
	                String jobid = "Connection not established,";
                    String clientName = "check Network/Wifi connectivity";
                    String clientAddress = "if problem perists refer to administrator ";
                    String appointementDate = "";
                    String jobDescription = "";
                    String jobRemarks = "";
                    String jobStatus = "";
                    // tmp hashmap for single contact
                    HashMap<String, String> job = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    job.put(TAG_JOBID, jobid);
                    job.put(TAG_CLIENTNAME, clientName);
                    job.put(TAG_CLIENTADDRESS, clientAddress);
                    job.put(TAG_APPOINTMENTDATE, appointementDate);
                    job.put(TAG_JOBDESCRIPTION, jobDescription);
                    job.put(TAG_JOBREMARKS, jobRemarks);
                    job.put(TAG_JOBSTATUS, jobStatus);
                    joblist.add(job);
	                
	            }
	 
	            return null;
	        }
	 
	        @Override
	        protected void onPostExecute(Void result) {
	            super.onPostExecute(result);
	            // Dismiss the progress dialog
	            if (pDialog.isShowing())
	                pDialog.dismiss();
	            /**
	             * Updating parsed JSON data into ListView
	             * */
	            
	        
	            ListAdapter adapter = new SimpleAdapter(
	                    MainActivity.this, joblist,
	                    R.layout.list_item, new String[] { TAG_JOBID, TAG_CLIENTNAME,
	                    		TAG_CLIENTADDRESS, TAG_APPOINTMENTDATE, TAG_JOBDESCRIPTION, TAG_JOBREMARKS, TAG_JOBSTATUS }, new int[] { R.id.jobid,
	                            R.id.clientName, R.id.clientAddress, R.id.appointementDate, R.id.jobDescription, R.id.jobRemarks, R.id.jobStatus });
	            
	            
	            
	            
	            setListAdapter(adapter);
	            }
	       
	 
	    }


}
