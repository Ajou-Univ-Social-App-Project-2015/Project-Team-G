package com.example.caretogether;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.example.util.CurrentTime;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class bloodtmplog extends Activity
{
	// database


	List<ParseObject> bpr_list;

	String m_email;
	String m_name;


	ArrayList<MyBloodPressureRecordList> myBloodPressureRecordList;
    Button write, send, closewrw, closemw, logout;
    TextView memotxt;
    EditText d,w,m;
    FrameLayout frame, writew, memow;
    MyBloodPressureRecordAdapter adapter;
	MyBloodPressureRecordList mlist ;
    String mainmemo;
	TextView myName;
	// wriet form
	EditText brp_high_pressure, brp_low_pressure, brp_pulse, brp_irregular_pulse,brp_date, brp_memo;
	RadioGroup radio;
	RadioButton type1;
	RadioButton type2;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{

	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.bloodtmplg);

	    write = (Button)findViewById(R.id.write);
	    send = (Button)findViewById(R.id.send);
	    logout = (Button)findViewById(R.id.logout);
	    closewrw = (Button)findViewById(R.id.closewrw);
	    closemw = (Button)findViewById(R.id.closemw);
	    d = (EditText)findViewById(R.id.d);
	    w = (EditText)findViewById(R.id.w);
	    m = (EditText)findViewById(R.id.m);
	    frame = (FrameLayout)findViewById(R.id.frame);
	    writew = (FrameLayout)findViewById(R.id.writew);
	    writew = (FrameLayout)findViewById(R.id.writew);
	    memow = (FrameLayout)findViewById(R.id.memow);
		myName = (TextView)findViewById(R.id.myName);
		type1 = (RadioButton)findViewById(R.id.type1);
		type2 = (RadioButton)findViewById(R.id.type2);
		radio = (RadioGroup)findViewById(R.id.radiog);

		//////////////
		// write form - brp_high_pressure, brp_low_pressure, brp_pulse, brp_irregular_pulse,brp_date, brp_memo;
		/////////////
		brp_high_pressure = (EditText)findViewById(R.id.brp_high_pressure);
		brp_low_pressure = (EditText)findViewById(R.id.brp_low_pressure);
		brp_pulse = (EditText)findViewById(R.id.brp_pulse);
		brp_date = (EditText)findViewById(R.id.brp_date);
		brp_memo = (EditText)findViewById(R.id.brp_memo);




		Intent intent = getIntent();
		String email = intent.getExtras().getString("email");


		ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
		query.whereEqualTo("m_email", email);

		try {
			m_name = query.getFirst().getString("m_name");
			m_email = query.getFirst().getString("m_email");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		myName = (TextView)findViewById(R.id.myName);
		//myName.setText(m_name + " welcome!!");

		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("bloodpressure_record");
		query2.whereEqualTo("m_email", email);
		query2.orderByDescending("brp_date");
		try {
			 bpr_list = query2.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		myBloodPressureRecordList = new ArrayList<MyBloodPressureRecordList>();

		for(int i=0;i<bpr_list.size();i++)
		{
			myBloodPressureRecordList.add(new MyBloodPressureRecordList(
					bpr_list.get(i).getString("brp_date").substring(0,2) + "월 " + bpr_list.get(i).getString("brp_date").substring(2,4) + "일",
					bpr_list.get(i).getInt("brp_high_pressure"),
					bpr_list.get(i).getInt("brp_low_pressure"),
					bpr_list.get(i).getInt("brp_pulse"),
					bpr_list.get(i).getString("brp_irregular_pulse"),
					bpr_list.get(i).getString("brp_memo")

			));
		}


	    adapter = new MyBloodPressureRecordAdapter(this, R.layout.listitem_bloodpressurerecord, myBloodPressureRecordList);
	    ListView list;
	    list = (ListView)findViewById(R.id.list);
	    list.setAdapter(adapter);
	    memotxt = (TextView)findViewById(R.id.memotxt);
	    list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
	    list.setOnItemClickListener(li);


	    logout.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();
				
			}
		});
	    
	    write.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				writew.setVisibility(View.VISIBLE);
						
			}
		});


	    send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v)
			{
				// brp_high_pressure, brp_low_pressure, brp_pulse, brp_irregular_pulse, brp_date, brp_memo;

				int high_pressure= Integer.parseInt(brp_high_pressure.getText().toString());
				int low_pressure= Integer.parseInt(brp_low_pressure.getText().toString());
				int pulse= Integer.parseInt(brp_pulse.getText().toString());
				String irregular_pulse;
				if(radio.getCheckedRadioButtonId()==type1.getId()){
					irregular_pulse = "예";}

				else{
					irregular_pulse = "아니오";
				}
				String date = brp_date.getText().toString();
				String memo = brp_memo.getText().toString();

				ParseObject bpr = new ParseObject("bloodpressure_record");
				bpr.put("m_email", m_email);
				bpr.put("brp_high_pressure", high_pressure);
				bpr.put("brp_low_pressure", low_pressure);
				bpr.put("brp_pulse", pulse);
				bpr.put("brp_irregular_pulse", irregular_pulse);
				bpr.put("brp_date", date);
				bpr.put("brp_time", CurrentTime.getCurrentTime());
				bpr.put("brp_memo", memo);

				bpr.saveInBackground();


				Log.i("db", "insert OK!!");


				bloodtmplog.this.onCreate(savedInstanceState);

			}
		});
	    
	    closewrw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				writew.setVisibility(View.GONE);
											
			}
		});
	    
	    closemw.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub					
				
				memow.setVisibility(View.GONE);								
			}
		});
	    
	}
	
	public class MyBloodPressureRecordList
	{
		private String brp_date;
		private int brp_high_pressure;
		private int brp_low_pressure;
		private int brp_pulse;
		private String brp_irregular_pulse;
		private String brp_memo;

		MyBloodPressureRecordList(String brp_date, int brp_high_pressure, int brp_low_pressure, int brp_pulse
										,String brp_irregular_pulse, String brp_memo )
		{
			this.brp_date = brp_date;
			this.brp_high_pressure=brp_high_pressure;
			this.brp_low_pressure = brp_low_pressure;
			this.brp_pulse=brp_pulse;
			this.brp_irregular_pulse=brp_irregular_pulse;
			this.brp_memo=brp_memo;
		}

		public String getBr_date() {
			return brp_date;
		}

		public void setBr_date(String br_date) {
			this.brp_date = br_date;
		}

		public int getBrp_high_pressure() {
			return brp_high_pressure;
		}

		public void setBrp_high_pressure(int brp_high_pressure) {
			this.brp_high_pressure = brp_high_pressure;
		}

		public int getBrp_low_pressure() {
			return brp_low_pressure;
		}

		public void setBrp_low_pressure(int brp_low_pressure) {
			this.brp_low_pressure = brp_low_pressure;
		}

		public int getBrp_pulse() {
			return brp_pulse;
		}

		public void setBrp_pulse(int brp_pulse) {
			this.brp_pulse = brp_pulse;
		}

		public String getBrp_irregular_pulse() {
			return brp_irregular_pulse;
		}

		public void setBrp_irregular_pulse(String brp_irregular_pulse) {
			this.brp_irregular_pulse = brp_irregular_pulse;
		}

		public String getBrp_memo() {
			return brp_memo;
		}

		public void setBrp_memo(String brp_memo) {
			this.brp_memo = brp_memo;
		}
	}
	
	public class MyBloodPressureRecordAdapter extends BaseAdapter {
		
		Context con;
		LayoutInflater inflater;
		ArrayList<MyBloodPressureRecordList> mwl;
		int layout;
		
		public MyBloodPressureRecordAdapter(Context context, int alayout, ArrayList<MyBloodPressureRecordList> mywl){
			con = context;
			inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			mwl = mywl;
			layout = alayout;	
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mwl.size();
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return mwl.get(position).getBr_date();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent)
		{
			if (convertView == null)
			{
				convertView = inflater.inflate(layout, parent, false);
			}
			
			CharSequence name[] = {"" + mwl.get(position).getBr_date(), ""+mwl.get(position).getBrp_high_pressure()
					, ""+mwl.get(position).getBrp_low_pressure(), ""+mwl.get(position).getBrp_pulse()
					, ""+mwl.get(position).getBrp_irregular_pulse(), mwl.get(position).getBrp_memo() };



			TextView brp_date = (TextView)convertView.findViewById(R.id.brp_date);
			TextView brp_high_pressure = (TextView)convertView.findViewById(R.id.brp_high_pressure);
			TextView brp_low_pressure = (TextView)convertView.findViewById(R.id.brp_low_pressure);
			TextView brp_pulse = (TextView)convertView.findViewById(R.id.brp_pulse);
			TextView brp_irregular_pulse = (TextView)convertView.findViewById(R.id.brp_irregular_pulse);
			TextView brp_memo = (TextView)convertView.findViewById(R.id.brp_memo);


			brp_date.setText(name[0]);
			brp_high_pressure.setText(name[1]);
			brp_low_pressure.setText(name[2]);
			brp_pulse.setText(name[3]);
			brp_irregular_pulse.setText(name[4]);

			return convertView;
		}
		
		

	}
	
	OnItemClickListener li = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{

			memow.setVisibility(View.VISIBLE);
			memotxt.setText(myBloodPressureRecordList.get(position).getBrp_memo());

		}
		
	};

}
