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

public class bsugartmplog extends Activity
{
	// database


	List<ParseObject> bs_list;

	String m_email;
	String m_name;

	ArrayList<MyBloodSugarRecordList> myBloodSugarRecordList;
    Button write, send, closewrw, closemw, logout;
    TextView memotxt;
    EditText d,w,m;
    FrameLayout frame, writew, memow;
    MyBloodSugarRecordAdapter adapter;
	MyBloodSugarRecordList mlist ;
    String mainmemo;
	TextView myName;
	// write form
	EditText br_amount, br_situation, br_type, br_date,br_memo;
	RadioGroup radio;
	RadioButton type1;
	RadioButton type2;
	RadioGroup radio2;
	RadioButton situ1;
	RadioButton situ2;
	RadioButton situ3;
	RadioButton situ4;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{
		//////////////////////////////////////
		// ���̾ƿ� ����
		//////////////////////////////////////
	    super.onCreate(savedInstanceState);
	    setContentView(R.layout.bsugartmplg);

		//////////////////////////////////////
		// ��üȭ
		//////////////////////////////////////
		type1 = (RadioButton)findViewById(R.id.type1);
		type2 = (RadioButton)findViewById(R.id.type2);
		radio = (RadioGroup)findViewById(R.id.radiog);
		radio2 = (RadioGroup)findViewById(R.id.radiog2);
		situ1 = (RadioButton)findViewById(R.id.situ1);
		situ2 = (RadioButton)findViewById(R.id.situ2);
		situ3 = (RadioButton)findViewById(R.id.situ3);
		situ4 = (RadioButton)findViewById(R.id.situ4);


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

		//////////////
		// write form
		/////////////
		br_amount = (EditText)findViewById(R.id.br_amount);


		br_date = (EditText)findViewById(R.id.br_date);
		br_memo = (EditText)findViewById(R.id.br_memo);


		/////////////////////////////
		// �α��ε� ȸ�� ���� ��������
		///////////////////////////
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
		/////////////////////////////////////
		// DB���� �о����
		//////////////////////////////////////
		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("bloodsugar_record");
		query2.whereEqualTo("m_email", email);
		query2.orderByDescending("br_date");
		try {
			bs_list = query2.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		myBloodSugarRecordList = new ArrayList<MyBloodSugarRecordList>();

		for(int i=0;i<bs_list.size();i++)
		{
			myBloodSugarRecordList.add(new MyBloodSugarRecordList(
					bs_list.get(i).getString("br_date").substring(0,2) + "월 " +bs_list.get(i).getString("br_date").substring(2,4) + "일" ,
					bs_list.get(i).getInt("br_amount"),
					bs_list.get(i).getString("br_situation"),
					bs_list.get(i).getString("br_type"),
					bs_list.get(i).getString("br_memo")
			));
		}


	    adapter = new MyBloodSugarRecordAdapter(this, R.layout.listitem_bloodsugarrecord, myBloodSugarRecordList);

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

		/////////////////////////////////////
		// ���� ���� �Է� �� ���� Ȯ��
		///////////////////////////////////////
	    send.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v)
			{

				// br_amount, br_situation, br_type, br_date, br_memo;
				int amount= Integer.parseInt(br_amount.getText().toString());
				String situation;
				if(radio2.getCheckedRadioButtonId()==situ1.getId()){
					situation = "식사 전";}

				else if(radio2.getCheckedRadioButtonId()==situ2.getId()) {
					situation = "식사 후";
				}

				else if(radio2.getCheckedRadioButtonId()==situ3.getId()) {
					situation = "취침 전";
				}

				else{
					situation = "운동 후";
				}


				String type;
				if(radio.getCheckedRadioButtonId()==type1.getId()){
					type = "혈장";}

				else{
					type = "전혈";
				}
				String date = br_date.getText().toString();
				String memo = br_memo.getText().toString();

				ParseObject br = new ParseObject("bloodsugar_record");
				br.put("m_email", m_email);
				br.put("br_amount", amount);
				br.put("br_situation", situation);
				br.put("br_type", type);
				br.put("br_date", date);
				br.put("br_time", CurrentTime.getCurrentTime());
				br.put("br_memo", memo);

				br.saveInBackground();


				Log.i("db", "insert OK!!");

				// ȭ�������Ʈ �ϴ� ���
				bsugartmplog.this.onCreate(savedInstanceState);


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

	public class MyBloodSugarRecordList
	{
		private int br_amount;
		private String br_situation;
		private String br_type;
		private String br_date;
		private String br_memo;

		MyBloodSugarRecordList(String br_date, int br_amount, String br_situation, String br_type,  String br_memo)
		{
			this.br_amount=br_amount;
			this.br_situation=br_situation;
			this.br_type=br_type;
			this.br_date=br_date;
			this.br_memo=br_memo;
		}

		public int getBr_amount() {
			return br_amount;
		}

		public void setBr_amount(int br_amount) {
			this.br_amount = br_amount;
		}

		public String getBr_situation() {
			return br_situation;
		}

		public void setBr_situation(String br_situation) {
			this.br_situation = br_situation;
		}

		public String getBr_type() {
			return br_type;
		}

		public void setBr_type(String br_type) {
			this.br_type = br_type;
		}

		public String getBr_date() {
			return br_date;
		}

		public void setBr_date(String br_date) {
			this.br_date = br_date;
		}

		public String getBr_memo() {
			return br_memo;
		}

		public void setBr_memo(String br_memo) {
			this.br_memo = br_memo;
		}
	}
	
	public class MyBloodSugarRecordAdapter extends BaseAdapter {
		
		Context con;
		LayoutInflater inflater;
		ArrayList<MyBloodSugarRecordList> mwl;
		int layout;
		
		public MyBloodSugarRecordAdapter(Context context, int alayout, ArrayList<MyBloodSugarRecordList> mywl){
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

			if (convertView == null){
				convertView = inflater.inflate(layout, parent, false);
			}
			
			CharSequence name[] = {"" + mwl.get(position).getBr_date(), ""+mwl.get(position).br_amount,""+mwl.get(position).getBr_situation() ,""+mwl.get(position).getBr_type() , mwl.get(position).getBr_memo()};

			TextView br_date = (TextView)convertView.findViewById(R.id.br_date);
			TextView br_amount = (TextView)convertView.findViewById(R.id.br_amount);
			TextView br_situation = (TextView)convertView.findViewById(R.id.br_situation);
			TextView br_type = (TextView)convertView.findViewById(R.id.br_type);

			br_date.setText(name[0]);
			br_amount.setText(name[1]);
			br_situation.setText(name[2]);
			br_type.setText(name[3]);
			
			return convertView;
		}
		
		

	}
	
	OnItemClickListener li = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id)
		{

			memow.setVisibility(View.VISIBLE);
			memotxt.setText(myBloodSugarRecordList.get(position).getBr_memo());
		}
		
	};

}