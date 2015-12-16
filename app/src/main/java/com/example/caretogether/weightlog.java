package com.example.caretogether;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.util.CurrentTime;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class weightlog extends Activity
{


	List<ParseObject> w_list;

	String m_email;
	String m_name;
	/** Called when the activity is first created. */
	////////////////////////////////////////////////////
	// MyWeightList : DTO, DB���� �о������.
	///////////////////////////////////////////////////
	ArrayList<MyWeightList> myweight;
	Button write, send, revise, closewrw, closerevisew, closemw, logout;
	TextView memotxt;
	EditText d,w,m, rd, rw, rm;
	FrameLayout frame, writew,revisew, memow ;
	MyWeightAdapter adapter;
	MyWeightList mlist ;
	String mainmemo;
	TextView myName;
	TextView widText;
	static int windex =0;
	static Bundle wsave = null;

	@Override
	public void onCreate(final Bundle savedInstanceState)
	{

		// ������Ʈ �� ����
		wsave = savedInstanceState;
		/////////////////////////////////////
		// ���̾ƿ� ����
		/////////////////////////////////////
		super.onCreate(savedInstanceState);
		setContentView(R.layout.weightlg);


		////////////////////////////////////////
		// ��üȭ
		///////////////////////////////////////
		write = (Button)findViewById(R.id.write); // �ۼ���ư-> �ۼ�������
		send = (Button)findViewById(R.id.send);    // �ۼ���ư->�ۼ��Ϸ�. DB����
		revise = (Button)findViewById(R.id.revise);    // �ۼ���ư->�ۼ��Ϸ�. DB����
		logout = (Button)findViewById(R.id.logout); //�α׾ƿ� ��ư
		closerevisew = (Button)findViewById(R.id.closerevisew); // ���� �ݱ�
		closewrw = (Button)findViewById(R.id.closewrw); // �ݱ�
		closemw = (Button)findViewById(R.id.closemw); // �ݱ�
		d = (EditText)findViewById(R.id.d);  // ��¥
		w = (EditText)findViewById(R.id.w); // ü��
		m = (EditText)findViewById(R.id.m); // �޸�
		rd = (EditText)findViewById(R.id.rd);  // ������¥
		rw = (EditText)findViewById(R.id.rw); // ����ü��
		rm = (EditText)findViewById(R.id.rm); // �����޸�
		frame = (FrameLayout)findViewById(R.id.frame); // ù ������
		writew = (FrameLayout)findViewById(R.id.writew); // �ۼ��ϱ� ��
		revisew = (FrameLayout)findViewById(R.id.revisew); // �����ϱ� ��
		memow = (FrameLayout)findViewById(R.id.memow);  // �޸�Ȯ���ؼ� ����

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
		///////////////////////////////////////////////

		ParseQuery<ParseObject> query2 = ParseQuery.getQuery("weight_record");
		query2.whereEqualTo("m_email", email);
		query2.orderByDescending("wr_date");
		try {
			w_list = query2.find();
		} catch (ParseException e) {
			e.printStackTrace();
		}

		myweight = new ArrayList<MyWeightList>();

		for(int i=0;i<w_list.size();i++)
		{
			myweight.add(new MyWeightList(
					w_list.get(i).getString("wr_date").substring(0,2) + "월 " + w_list.get(i).getString("wr_date").substring(2,4) + "일"
					,w_list.get(i).getInt("wr_weight")
					,w_list.get(i).getString("wr_memo")
					,i));
		}


		// R.layout.listitem�� ���̾ƿ��� myweight�� �����͸� ���.
		adapter = new MyWeightAdapter(this, R.layout.listitem, myweight);


		//  ��¥, ������, �޸� list
		ListView list;
		list = (ListView)findViewById(R.id.list);
		list.setAdapter(adapter);
		memotxt = (TextView)findViewById(R.id.memotxt);
		list.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		list.setOnItemClickListener(li);


		//////////////////////////////////////////////////////
		// �ۼ� ��ư. ���빰 üũ �� DB INSERT �� ȭ�� ������Ʈ
		/////////////////////////////////////////////////////
		send.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				int weight = Integer.parseInt(w.getText().toString());
				String date = d.getText().toString();
				String memo = m.getText().toString();

				ParseObject w = new ParseObject("weight_record");
				w.put("m_email", m_email);
				w.put("wr_date", date);
				w.put("wr_time", CurrentTime.getCurrentTime());
				w.put("wr_weight", weight);
				w.put("wr_memo", memo);

				w.saveInBackground();

				Log.i("db", "insert OK!!");

				// ȭ�������Ʈ �ϴ� ���
				weightlog.this.onCreate(savedInstanceState);
				//writew.setVisibility(View.GONE);
				//frame.setVisibility(View.VISIBLE);
			}
		});

		//////////////////////////////////////////////////////
		// ���� ��ư. ���빰 üũ �� DB INSERT �� ȭ�� ������Ʈ
		/////////////////////////////////////////////////////

		//////////////////////////////////////////////////////
		// ���� ��ư. ���빰 üũ �� DB DELETE �� ȭ�� ������Ʈ
		/////////////////////////////////////////////////////
		/*
		revise.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v)
			{

				dbAdapter.delete_weight(windex);
				Log.i("db", "delete OK!!");

				// ȭ�������Ʈ �ϴ� ���
				weightlog.this.onCreate(savedInstanceState);
				//writew.setVisibility(View.GONE);
				//frame.setVisibility(View.VISIBLE);
			}
		});
*/
		// �α׾ƿ�. ���� ��Ƽ��Ƽ��.
		logout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				onBackPressed();

			}
		});
		// �ۼ� ��ư. �ۼ� �� ����
		write.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				writew.setVisibility(View.VISIBLE);
				/////////////////////////////////////////
				// �ۼ� ȭ�� �ڿ� frame ȭ�� disable�ϴ� ���ã��
				/////////////////////////////////////////
			}
		});

		// �ۼ� �� �ݱ�
		closewrw.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub				
				writew.setVisibility(View.GONE);

			}
		});

		// ���� �� �ݱ�
		closerevisew.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				revisew.setVisibility(View.GONE);
				frame.setVisibility(View.VISIBLE);

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

	//////////////////////////////
	// list���� ���
	/////////////////////////////
	public class MyWeightList
	{
		private String date;
		private int weight;
		private String memo;
		private int w_id;

		MyWeightList(String date, int weight, String memo, int w_id)
		{
			this.date = date;
			this.weight = weight;
			this.memo = memo;
			this.w_id = w_id;
		}

		public String getDate() {
			return date;
		}

		public void setDate(String date) {
			this.date = date;
		}

		public int getWeight() {
			return weight;
		}

		public void setWeight(int weight) {
			this.weight = weight;
		}

		public String getMemo() {
			return memo;
		}

		public void setMemo(String memo) {
			this.memo = memo;
		}

		public int getW_id() {
			return w_id;
		}
		public void setW_id(int w_id) {
			this.w_id = w_id;
		}
	}

	public class MyWeightAdapter extends BaseAdapter {

		Context con;
		LayoutInflater inflater;
		ArrayList<MyWeightList> mwl;
		int layout;

		public MyWeightAdapter(Context context, int alayout, ArrayList<MyWeightList> mywl){
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
			return mwl.get(position).date;
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

			CharSequence name[] = {"   " + mwl.get(position).date, "          "+mwl.get(position).weight + "kg",mwl.get(position).memo };

			TextView datelog = (TextView)convertView.findViewById(R.id.dlog);
			datelog.setText(name[0]);
			TextView weightlog = (TextView)convertView.findViewById(R.id.wlog);
			weightlog.setText(name[1]);
			//TextView memolog = (TextView)convertView.findViewById(R.id.mlog);
			//TextView wlog = (TextView)convertView.findViewById(R.id.wid);

			//wlog.setText(""+mwl.get(position).getW_id());
			// index�� �޾ƿ���
			//windex = Integer.parseInt(wlog.getText().toString());


			//

			return convertView;
		}
	}

	// �޸�Ȯ���ؼ� �����ִ� ���.
	OnItemClickListener li = new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,long id) {

			AlertDialog.Builder alertDlg = new AlertDialog.Builder(view.getContext());
			alertDlg.setTitle("Select");

			// '����' ��ư�� Ŭ���Ǹ�
			alertDlg.setPositiveButton("Revise", new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					// ���� ä���
					// �ۼ� ��ư. �ۼ� �� ����
					memow.setVisibility(View.INVISIBLE);
					//TextView wlog = (TextView)view.findViewById(R.id.wid);
					//	revisew.setTag(1, wlog.getText().toString());
					// �ܼ��� �����ִ°Ծƴ϶�, revisew �Ķ���͸� �Ѱܾߵ�.
					revisew.setVisibility(View.VISIBLE);
					frame.setVisibility(View.INVISIBLE);
					/////////////////////////////////////////
					// �ۼ� ȭ�� �ڿ� frame ȭ�� disable�ϴ� ���ã��
					////////////////////////////////////////
				}
			});

			// '����' ��ư�� Ŭ���Ǹ�
			alertDlg.setNegativeButton("delete", new DialogInterface.OnClickListener()
			{
				@Override
				public void onClick( DialogInterface dialog, int which ) {
					//dbAdapter.delete_weight(windex);
					weightlog.this.onCreate(wsave);
				}
			});

			/*
			alertDlg.setMessage( String.format( getString(R.string.alert_msg_delete),
					items.get(position)) );
			alertDlg.show();
*/

			alertDlg.show();
			// TODO Auto-generated method stub
			Log.i("inin", "setting..txt");
			memow.setVisibility(View.VISIBLE);
			Log.i("inin", "setting..gogogo");
			memotxt.setText(myweight.get(position).memo);
			Log.i("inin", "setting..txtout");
		}
	};
}



