package com.example.caretogether;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;


import com.example.util.CurrentTime;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class foodlog extends Activity implements AdapterView.OnItemSelectedListener
{


	List<ParseObject> fi_list;
	List<ParseObject> f_list;


	String m_email;
	String m_name;


	ArrayList<MyFoodRecordList> myFoodList;
	Button write, send, closewrw, closemw, logout;
	TextView memotxt;
	EditText d,w,m;
	FrameLayout frame, writew, memow;
	MyFoodRecordAdapter adapter;
	TextView myName;
	// write form
	EditText fr_amount, fr_kal, fr_date, fr_memo;
	RadioGroup radio2;
	RadioButton type1;
	RadioButton type2;
	RadioButton type3;
	RadioButton type4;

	// spinner
	private ArrayAdapter<String> dsAdapter;
	private String selectedValue;
	private String[] items;
	// ?????? ????????

	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		//////////////////////////////////////
		// 레이아웃 셋팅
		//////////////////////////////////////
		super.onCreate(savedInstanceState);
		setContentView(R.layout.foodlg);

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

		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("food_info");
		try {
			fi_list = query2.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		/////////////////////////////////////
		// DB에서 음식 종류 SELECT
		//////////////////////////////////////


		items = new String[fi_list.size()];
		for(int i=0;i<fi_list.size();i++)
		{
			items[i]=fi_list.get(i).getString("fi_name");
		}

		/////////////////////////////////////
		// spinner
		///////////////////////////////////////
		Spinner foodSpinner = (Spinner)findViewById(R.id.foodType);
		foodSpinner.setOnItemSelectedListener(this); // Spinner ?????? ???.
		dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		foodSpinner.setAdapter(dsAdapter);
		selectedValue = items[0];


		//////////////////////////////////////
		// 객체화
		//////////////////////////////////////
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
		radio2 = (RadioGroup)findViewById(R.id.radiog2);
		type1 = (RadioButton)findViewById(R.id.type1);
		type2 = (RadioButton)findViewById(R.id.type2);
		type3 = (RadioButton)findViewById(R.id.type3);
		type4 = (RadioButton)findViewById(R.id.type4);
		//////////////
		// write form
		/////////////
		//fi_name = (EditText)findViewById(R.id.fi_name);

		fr_kal = (EditText)findViewById(R.id.fr_kal);
		fr_date = (EditText)findViewById(R.id.fr_date);
		fr_memo = (EditText)findViewById(R.id.fr_memo);

		///////////////////////////
		// 갯수 변하면 칼로리 자동 계산
		///////////////////////////
		radio2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
				double amount;
				if (R.id.type1 == checkedId) {
					amount = 0.5;
				} else if (R.id.type2 == checkedId){
					amount = 1;
				} else if (R.id.type3 == checkedId){
					amount = 1.5;
				}else{
					amount = 2;
				}
					double kcal = 0;
				for (int j = 0; j < fi_list.size(); j++) {

					if (fi_list.get(j).getString("fi_name").equals(selectedValue)) {
						kcal = fi_list.get(j).getDouble("fi_kcal");
						break;
					}
				}
				double tot_kcal = amount * kcal;
				fr_kal.setText("" + tot_kcal);
			}
		});


		ParseQuery<ParseObject> query3 = ParseQuery.getQuery("food_record");
		query3.whereEqualTo("m_email", email);
		query3.orderByDescending("fr_date");
		try {
			f_list = query3.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}


		myFoodList = new ArrayList<MyFoodRecordList>();

		for(int i=0;i<f_list.size();i++)
		{
			myFoodList.add(new MyFoodRecordList(
					f_list.get(i).getString("fr_date").substring(0,2) + "월 "+f_list.get(i).getString("fr_date").substring(2,4) + "일",
					f_list.get(i).getString("fi_name"),
					f_list.get(i).getDouble("fr_amount") +"인분",
					f_list.get(i).getDouble("fr_kal"),
					f_list.get(i).getString("fr_memo")

			));
		}

		adapter = new MyFoodRecordAdapter(this, R.layout.listitem_foodrecord, myFoodList);

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

		//////////////////////////////////////
		// 음식 기록 후 최종 확인
		///////////////////////////////////////
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{
				// fi_name, fr_amount, fr_kal, fr_date, fr_memo;
				String name= selectedValue;
				double  amount;
				if(radio2.getCheckedRadioButtonId()==type1.getId()){
					amount = 0.5;}

				else if(radio2.getCheckedRadioButtonId()==type2.getId()) {
					amount = 1.0;
				}

				else if(radio2.getCheckedRadioButtonId()==type3.getId()) {
					amount = 1.5;
				}

				else{
					amount = 2.0;
				}
				double  kal = Double.parseDouble(fr_kal.getText().toString());
				String date = fr_date.getText().toString();
				String memo = fr_memo.getText().toString();

				ParseObject fr = new ParseObject("food_record");
				fr.put("m_email", m_email);
				fr.put("fi_name", name);
				fr.put("fr_time", CurrentTime.getCurrentTime());
				fr.put("fr_amount", amount);
				fr.put("fr_date", date);
				fr.put("fr_memo", memo);
				fr.put("fr_kal", kal);

				fr.saveInBackground();


				Log.i("db", "insert OK!!");

				// 화면업데이트 하는 방법
				foodlog.this.onCreate(savedInstanceState);

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

	public class MyFoodRecordList
	{
		private String fr_date;
		private String fi_name;
		private String fr_amount;
		private double fr_kal;
		private String fr_memo;


		MyFoodRecordList(String fr_date, String fi_name, String fr_amount,double fr_kal, String fr_memo)
		{
			this.fr_date = fr_date;
			this.fi_name = fi_name;
			this.fr_amount=fr_amount;
			this.fr_kal=fr_kal;
			this.fr_memo=fr_memo;
		}

		public String getFr_date() {
			return fr_date;
		}

		public void setFr_date(String fr_date) {
			this.fr_date = fr_date;
		}

		public String getFi_name() {
			return fi_name;
		}

		public void setFi_name(String fi_name) {
			this.fi_name = fi_name;
		}

		public String getFr_amount() {
			return fr_amount;
		}

		public void setFr_amount(String fr_amount) {
			this.fr_amount = fr_amount;
		}

		public double getFr_kal() {
			return fr_kal;
		}

		public void setFr_kal(double fr_kal) {
			this.fr_kal = fr_kal;
		}

		public String getFr_memo() {
			return fr_memo;
		}

		public void setFr_memo(String fr_memo) {
			this.fr_memo = fr_memo;
		}
	}

	public class MyFoodRecordAdapter extends BaseAdapter {

		Context con;
		LayoutInflater inflater;
		ArrayList<MyFoodRecordList> mwl;
		int layout;

		public MyFoodRecordAdapter(Context context, int alayout, ArrayList<MyFoodRecordList> mywl){
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
			return mwl.get(position).getFr_date();
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

			CharSequence name[] = {"" + mwl.get(position).getFr_date(), ""+mwl.get(position).getFi_name(),""+ mwl.get(position).getFr_amount(),""+mwl.get(position).getFr_kal(),""+mwl.get(position).getFr_memo()};

			TextView fi_name = (TextView)convertView.findViewById(R.id.fi_name);
			TextView fr_amount = (TextView)convertView.findViewById(R.id.fr_amount);
			TextView fr_kal = (TextView)convertView.findViewById(R.id.fr_kal);
			TextView fr_date = (TextView)convertView.findViewById(R.id.fr_date);
			TextView fr_memo = (TextView)convertView.findViewById(R.id.fr_memo);

			fr_date.setText(name[0]);
			fi_name.setText(name[1]);
			fr_amount.setText(name[2]);
			fr_kal.setText(name[3]);
			// fr_memo.setText(name[4]);

			return convertView;
		}



	}

	OnItemClickListener li = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{
			memow.setVisibility(View.VISIBLE);
			memotxt.setText(myFoodList.get(position).getFr_memo());
		}

	};


	///////////////////////////////////////
	///////// spinner//////////
	///////////////////////////////////////
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		selectedValue = dsAdapter.getItem(position);
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{

	}
}
