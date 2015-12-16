package com.example.caretogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.List;



public class MainActivity extends Activity implements OnItemSelectedListener, OnCheckedChangeListener {

	Boolean checked = true;
	Boolean unchecked = false;
	String[] items = {"당뇨","고혈압","비만"};
	Button loginbt, joinbt, closelogin, closejoin, reallogin;
	FrameLayout main, login, join;
	RadioGroup radio;
	RadioButton male, female;

	static int check =0;

	// ȸ��������
	EditText name,birth,cemail,weight,tall,cpw,ccpw;

	// �α��� â
	EditText email, pw;

	// ȸ���� ���� �Է� �� Ȯ�� ��ư
	Button ccc;

	// spinner
	ArrayAdapter<String> dsAdapter;
	public String selectedValue;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{

		if(check ==0) {

			Parse.enableLocalDatastore(this);

			Parse.initialize(this, "i9St2G4biFobDe3XqOWHiyXMfgMehFouKeUxlos7", "zXHzpzL4ulwQHufbeGHZvMZXgRWQucCRl2HnoQP4");
			check = 1;
		}








		//////////////////////////////////////////////////////////////////////
		//ȸ���� �κ� ȭ����ȯ�� main���� �ٸ� Frame ���̾ƿ����� �ٲ㰡�鼭 ��ȯ.
		///////////////////////////////////////////////////////////////////////
		// Layout Setting
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Spinner : �������� �Ѱ� ����. html�� select�� ���,�� ���� �����Ҷ� ���. // items : �索, ������, �� �� ����
		Spinner ds = (Spinner)findViewById(R.id.disease);
		ds.setOnItemSelectedListener(this); // Spinner ������ ���.
		dsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, items);
		dsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		ds.setAdapter(dsAdapter);
		selectedValue = items[0];

		// ��ü ����. inflation
		male = (RadioButton)findViewById(R.id.m);
		female = (RadioButton)findViewById(R.id.fm);
		radio = (RadioGroup)findViewById(R.id.radiog);
		main = (FrameLayout)findViewById(R.id.mainw);
		login = (FrameLayout)findViewById(R.id.loginw);
		join = (FrameLayout)findViewById(R.id.joinw);
		loginbt = (Button)findViewById(R.id.loginbt); // �α���
		joinbt = (Button)findViewById(R.id.joinbt);    // ȸ����
		closelogin = (Button)findViewById(R.id.closelogin);
		closejoin = (Button)findViewById(R.id.closejoin);
		reallogin = (Button)findViewById(R.id.login);
		ccc    = (Button)findViewById(R.id.ccc); //  ȸ�� ���� �Է� �� ���� Ȯ�� ��ư
		name =(EditText)findViewById(R.id.name);      // �̸�
		birth =(EditText)findViewById(R.id.birth);    // ������
		cemail =(EditText)findViewById(R.id.cemail);  // �̸���
		weight =(EditText)findViewById(R.id.weight);  // ü��
		tall =(EditText)findViewById(R.id.tall);      // Ű
		cpw =(EditText)findViewById(R.id.cpw);        // ��й�ȣ
		ccpw =(EditText)findViewById(R.id.ccpw);      // ��й�ȣ Ȯ��
		email=(EditText)findViewById(R.id.email);
		pw = (EditText)findViewById(R.id.pw);

		email.setText("");
		pw.setText("");

		/////////////////////////////////////////////////////
		// ȸ���� ���� �Է� �� ���� Ȯ�� ��ư
		////////////////////////////////////////////////////
		ccc.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view)
			{

				ParseObject member = new ParseObject("member");
				member.put("m_name", name.getText().toString());
				member.put("m_age", Integer.parseInt(birth.getText().toString()));
				member.put("m_disease", selectedValue);
				member.put("m_email",cemail.getText().toString());

				if(radio.getCheckedRadioButtonId()==male.getId()){
					member.put("m_gender", "male");}

				else{
					member.put("m_gender", "female");
				}

				member.put("m_height", Integer.parseInt(tall.getText().toString()));
				member.put("m_password", cpw.getText().toString());
				member.put("m_weight",Integer.parseInt(weight.getText().toString()));

				member.saveInBackground();

				Toast toast = Toast.makeText(getApplicationContext(),"User Created!",Toast.LENGTH_SHORT);
				toast.show();

				join.setVisibility(View.GONE);
				main.setVisibility(View.VISIBLE);
			}
		});

/////////////////////////////// ���� ��ư //////////////////////////////////////////   
		radio.setOnCheckedChangeListener(this); // ������ư ������ ���

/////////////////////////////// �α���, ȸ���� �ݱ� ��ư //////////////////////////////////////////
		closelogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login.setVisibility(View.GONE);
				main.setVisibility(View.VISIBLE);

			}
		});

		closejoin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				join.setVisibility(View.GONE);
				main.setVisibility(View.VISIBLE);

			}
		});

/////////////////////////////// �α���, ȸ���� ��ư //////////////////////////////////////////
		loginbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				login.setVisibility(View.VISIBLE);
				main.setVisibility(View.GONE);
			}
		});

		joinbt.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				join.setVisibility(View.VISIBLE);
				main.setVisibility(View.GONE);
			}
		});


		//////////////////////////////////////////////////////////////
		// ���̵� �н����� �Է� �� �α��� , DB��ȸ ��� �ֱ�
		// -> choooose ��Ƽ��Ƽ�� �̵�
		////////////////////////////////////////////////////////////

		reallogin.setOnClickListener(new OnClickListener() {
			String serverPwd = null;
			@Override
			public void onClick(View v) {



				String inputEmail = email.getText().toString();
				String inputPwd = pw.getText().toString();

				ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
				query.whereEqualTo("m_email", inputEmail);
				try {
					serverPwd = query.getFirst().getString("m_password");
				} catch (ParseException e) {
					e.printStackTrace();
				}
   /*         query.getFirstInBackground(new GetCallback<ParseObject>() {
               public void done(ParseObject object, ParseException e) {
                  if (object == null) {
                     Log.d("member", "The getFirst request failed.");
                  } else {
                     serverPwd = object.getString("m_password");


                  }
               }
            });*/

				if(inputPwd.equals(serverPwd))
				{
					Toast toast = Toast.makeText(getApplicationContext(),"Login Success!!",Toast.LENGTH_SHORT);
					toast.show();

					Intent intent = new Intent(MainActivity.this, choooose.class);

					intent.putExtra("email", inputEmail);
					startActivity(intent);


				}
				else
				{
					Toast toast = Toast.makeText(getApplicationContext(),"Login Failed!!"+ serverPwd ,Toast.LENGTH_LONG);
					toast.show();
				}


			}
		});
	}

	///////// ���� �׷� ������//////////
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		if(group == radio)
		{
			if(checkedId == R.id.m)
			{
				male.setChecked(checked);
				female.setChecked(unchecked);
			}

			if(checkedId == R.id.fm)
			{
				male.setChecked(unchecked);
				female.setChecked(checked);
			}
		}
	}

	///////// ���ǳ� ������//////////
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
	{
		selectedValue = dsAdapter.getItem(position);
	}


	@Override
	public void onNothingSelected(AdapterView<?> parent)
	{

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item)
	{
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();
		if (id == R.id.action_settings)
		{
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
}