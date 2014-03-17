package com.example.lats_jobs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class ViewJobActivity extends Activity {
	// JSON node keys
	    private static final String TAG_JOBID = "jobid";
	    private static final String TAG_CLIENTNAME = "clientName";
	    private static final String TAG_CLIENTADDRESS = "clientAddress";
	    private static final String TAG_APPOINTMENTDATE = "appointmentDate";
	    private static final String TAG_JOBDESCRIPTION = "jobDescription";
	    private static final String TAG_JOBREMARKS = "jobRemarks";
	    private static final String TAG_JOBSTATUS = "jobStatus";
	    private Button button;
	 
	
	 @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        this.setContentView(R.layout.single_list_item_view);
	        addListenerOnButton();
	        
	        Intent i = getIntent();
	        
	        // getting attached intent data
	        String jobid = i.getStringExtra(TAG_JOBID);
	        String clientName = i.getStringExtra(TAG_CLIENTNAME);
	        String clientAddress = i.getStringExtra(TAG_CLIENTADDRESS);
	        String appointmentDate = i.getStringExtra(TAG_APPOINTMENTDATE);
	        String jobDescription= i.getStringExtra(TAG_JOBDESCRIPTION);
	        String jobRemarks = i.getStringExtra(TAG_JOBREMARKS);
	        String jobStatus = i.getStringExtra(TAG_JOBSTATUS);
	     
	        
	        TextView txtjobid = (TextView)findViewById(R.id.jobid);
	   
	        TextView txtclientName = (TextView)findViewById(R.id.clientName);
	        TextView txtclientAddress = (TextView)findViewById(R.id.clientAddress);
	        TextView txtappointmentDate = (TextView)findViewById(R.id.appointmentDate);
	        TextView txtjobDescription = (TextView)findViewById(R.id.jobDescription1);
	        TextView txtjobRemarks = (TextView)findViewById(R.id.jobRemarks1);
	        TextView txtjobStatus = (TextView)findViewById(R.id.jobStatus1);
	      
	      
	        // displaying selected product name
	       
	        txtjobid.setText(jobid);
	        txtclientName.setText(clientName);
	        txtclientAddress.setText(clientAddress);
	        txtappointmentDate.setText(appointmentDate);
	        txtjobDescription.setText(jobDescription);
	        txtjobRemarks.setText(jobRemarks);
	        txtjobStatus.setText(jobStatus);
	 }
	 
	 
	 public void addListenerOnButton() {

	        //Select a specific button to bundle it with the action you want
			button = (Button) findViewById(R.id.button1);

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
				
					
					 TextView txtjobid = ( TextView)findViewById(R.id.jobid);
					 TextView txtclientName = ( TextView)findViewById(R.id.clientName);

							String jobid = txtjobid.getText().toString();
							String clientName = txtclientName.getText().toString();
					
					Intent i = new Intent(getApplicationContext(), ShowLocationActivity.class);
	                i.putExtra("jobid",jobid);
	                i.putExtra("clientName", clientName);
	                startActivity(i);
	               
				}
			});

		}
	 
}