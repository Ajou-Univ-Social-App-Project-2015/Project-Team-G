package com.example.caretogether;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


import com.example.util.CurrentTime;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
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
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class exerciselog extends Activity implements OnItemSelectedListener
{
	// database

	List<ParseObject> ei_list;
	List<ParseObject> e_list;


	String m_email;
	String m_name;
	int m_weight;


	ArrayList<MyExerciseList> myExercise;
	Button write, send, closewrw, logout;
	EditText d,w,m;
	FrameLayout frame, writew;
	MyExerciseAdapter adapter;
	TextView myName;

	// write form
	EditText er_amount, er_kal, er_date;

	// spinner
	private ArrayAdapter<String> dsAdapter;
	private String selectedValue;
	private String[] items;

	// 운동 정보 리스트


	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		//////////////////////////////////////
		// 레이아웃
		//////////////////////////////////////
		super.onCreate(savedInstanceState);
		setContentView(R.layout.exerciselg);


		Intent intent = getIntent();
		String email = intent.getExtras().getString("email");


		ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
		query.whereEqualTo("m_email", email);

		try {
			m_name = query.getFirst().getString("m_name");
			m_email = query.getFirst().getString("m_email");
			m_weight = query.getFirst().getInt("m_weight");

		} catch (ParseException e) {
			e.printStackTrace();
		}

		myName = (TextView)findViewById(R.id.myName);
		//myName.setText(m_name + " welcome!!");

		/////////////////////////////////////////
		// 운동 정보 SELECT
		//////////////////////////////////////////
		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("exercise_info");
		try {
			ei_list = query2.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		items = new String[ei_list.size()];
		for(int i=0;i<ei_list.size();i++)
		{
			items[i]=ei_list.get(i).getString("ei_name");
		}

		/////////////////////////////////////
		// spinner
		///////////////////////////////////////
		Spinner exerciseSpinner = (Spinner)findViewById(R.id.exerciseType);
		exerciseSpinner.setOnItemSelectedListener(this); // Spinner ������ ���.
		dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		exerciseSpinner.setAdapter(dsAdapter);
		selectedValue = items[0];



		//////////////////////////////////////
		// ��üȭ
		//////////////////////////////////////
		write = (Button)findViewById(R.id.write);
		send = (Button)findViewById(R.id.send);
		logout = (Button)findViewById(R.id.logout);
		closewrw = (Button)findViewById(R.id.closewrw);
		d = (EditText)findViewById(R.id.d);
		w = (EditText)findViewById(R.id.w);
		m = (EditText)findViewById(R.id.m);
		frame = (FrameLayout)findViewById(R.id.frame);
		writew = (FrameLayout)findViewById(R.id.writew);
		writew = (FrameLayout)findViewById(R.id.writew);
		myName = (TextView)findViewById(R.id.myName);

		//////////////
		// write form
		/////////////
		er_amount = (EditText)findViewById(R.id.er_amount);
		er_kal = (EditText)findViewById(R.id.er_kal);
		er_date = (EditText)findViewById(R.id.er_date);

		///////////////////////////
		// 갯수 변하면 칼로리 자동 계산
		///////////////////////////
		er_amount.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				if(er_amount.getText().toString().length()!=0)
				{
					int amount = Integer.parseInt(er_amount.getText().toString());
					double eff = 0;
					for(int j=0;j<ei_list.size();j++)
					{

						if(ei_list.get(j).getString("ei_name").equals(selectedValue))
						{
							eff = ei_list.get(j).getDouble("ei_coefficient");
							break;
						}
					}

					int kcal = (int)(eff*(amount/15)*m_weight);

					er_kal.setText(""+kcal);
				}
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});


		ParseQuery<ParseObject> query3 = ParseQuery.getQuery("exercise_record");
		query3.whereEqualTo("m_email", email);
		query3.orderByDescending("er_date");
		try {
			e_list = query3.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		myExercise = new ArrayList<MyExerciseList>();

		for(int i=0;i<e_list.size();i++)
		{
			myExercise.add(new MyExerciseList(
					e_list.get(i).getString("er_date").substring(0, 2) + "월 "+ e_list.get(i).getString("er_date").substring(2,4)+"일",
					e_list.get(i).getString("er_time"),
					e_list.get(i).getString("ei_name"),
					e_list.get(i).getInt("er_amount")+ "분",
					(int)e_list.get(i).getInt("er_kcal") + "kcal"
			));
		}

		// ��¥ � �޸� list
		adapter = new MyExerciseAdapter(this, R.layout.listitem_exercise, myExercise);
		ListView list;
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);



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

		/////////////////////////////////////////
		// � ��� ���� Ȯ�� ��ư
		/////////////////////////////////////////
		send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{

				String  name= selectedValue;
				int  amount = Integer.parseInt(er_amount.getText().toString());
				double  kal = Double.parseDouble(er_kal.getText().toString());
				String date = er_date.getText().toString();

				ParseObject er = new ParseObject("exercise_record");
				er.put("m_email", m_email);
				er.put("ei_name", name);
				er.put("er_amount", amount);
				er.put("er_kcal", kal);
				er.put("er_date", date);
				er.put("er_time", CurrentTime.getCurrentTime());

				er.saveInBackground();


				Log.i("db", "insert OK!!");

				// ȭ�������Ʈ �ϴ� ���
				exerciselog.this.onCreate(savedInstanceState);

			}
		});

		closewrw.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				writew.setVisibility(View.GONE);

			}
		});

	}

	////////////////////////////////////////////////
	// list�� �����ֱ� ���� Ŭ����
	////////////////////////////////////////////////
	public class MyExerciseList {

		private String er_date;
		private String er_time;
		private String ei_name;
		private String er_amount;
		private String er_kal;


		MyExerciseList(String er_date, String er_time, String ei_name, String er_amount, String er_kal)
		{
			this.er_date = er_date;
			this.er_time = er_time;
			this.ei_name = ei_name;
			this.er_amount = er_amount;
			this.er_kal=er_kal;
		}

		public String getEr_date() {
			return er_date;
		}

		public void setEr_date(String er_date) {
			this.er_date = er_date;
		}

		public String getEr_time() {
			return er_time;
		}

		public void setEr_time(String er_time) {
			this.er_time = er_time;
		}

		public String getEi_name() {
			return ei_name;
		}

		public void setEi_name(String ei_name) {
			this.ei_name = ei_name;
		}

		public String getEr_amount() {
			return er_amount;
		}

		public void setEr_amount(String er_amount) {
			this.er_amount = er_amount;
		}

		public String getEr_kal() {
			return er_kal;
		}

		public void setEr_kal(String er_kal) {
			this.er_kal = er_kal;
		}
	}


	public class MyExerciseAdapter extends BaseAdapter {

		Context con;
		LayoutInflater inflater;
		ArrayList<MyExerciseList> mwl;
		int layout;

		public MyExerciseAdapter(Context context, int alayout, ArrayList<MyExerciseList> mywl){
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
			return mwl.get(position).getEr_date();
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			if (convertView == null){
				convertView = inflater.inflate(layout, parent, false);
			}

			CharSequence name[] = {"" + mwl.get(position).getEr_date(), ""+mwl.get(position).getEi_name(),""+ mwl.get(position).getEr_amount(), ""+mwl.get(position).getEr_kal() };

			TextView ei_name = (TextView)convertView.findViewById(R.id.ei_name);
			TextView er_amount = (TextView)convertView.findViewById(R.id.er_amount);
			TextView er_kal = (TextView)convertView.findViewById(R.id.er_kal);
			TextView er_date = (TextView)convertView.findViewById(R.id.er_date);

			er_date.setText(name[0]);
			ei_name.setText(name[1]);
			er_amount.setText(name[2]);
			er_kal.setText(name[3]);

			return convertView;
		}
	}

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