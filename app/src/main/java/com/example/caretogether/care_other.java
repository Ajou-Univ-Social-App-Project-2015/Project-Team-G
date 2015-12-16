package com.example.caretogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class care_other extends Activity
{
	Button weekchart;

	String m_name;
	String m_email;
	int m_age;
	int m_weight;
	int m_height;
	String m_disease;


	TextView name,email2,disease1,old,cm,kg,bmi;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(final Bundle savedInstanceState)
	{

		super.onCreate(savedInstanceState);
		setContentView(R.layout.chart);

		name = (TextView)findViewById(R.id.name);
		email2 = (TextView)findViewById(R.id.email);
		disease1 = (TextView)findViewById(R.id.disease1);
		old = (TextView)findViewById(R.id.old);
		cm = (TextView)findViewById(R.id.cm);
		kg = (TextView)findViewById(R.id.kg);
		bmi = (TextView)findViewById(R.id.bmi);


		Intent intent = getIntent();
		String email = intent.getExtras().getString("email");

		ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
		query.whereEqualTo("m_email", email);

		try {
			m_name = query.getFirst().getString("m_name");
			m_email = query.getFirst().getString("m_email");
			m_age = query.getFirst().getInt("m_age");
			m_weight = query.getFirst().getInt("m_weight");
			m_height = query.getFirst().getInt("m_height");
			m_disease = query.getFirst().getString("m_disease");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		int birth = m_age/10000 + 1900;
		int age = 2015 - birth;

		float bmi_value = (float)m_weight/((float)m_height/100 * m_height/100);
		bmi_value = (int)(bmi_value * 100);
		bmi_value = bmi_value / 100;


		name.setText(m_name);
		email2.setText(m_email);
		disease1.setText(m_disease);
		old.setText(age + "세");
		cm.setText(m_height + "cm");
		kg.setText(m_weight + "kg");
		bmi.setText(bmi_value+"");



		weekchart = (Button)findViewById(R.id.weekchart);
		weekchart.setText(m_name+"님의 차트보기");

		weekchart.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(care_other.this, Chart_other.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

			}
		});





	}



}
