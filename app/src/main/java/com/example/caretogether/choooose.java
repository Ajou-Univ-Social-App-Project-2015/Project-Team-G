package com.example.caretogether;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.SQLException;

public class choooose extends Activity
{
	int writeflag;
	Button logout2, write, cpwndlog, dnsehdfid, dmatlr, gufdkq, gufekd ,yes, no;
	FrameLayout frame, writew, sss;
	Animation ani1, ani2;
	TextView myName, loading;



	Button care, log, sns;

	String m_email;
	String m_name;



	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		//////////////////////////////////////
		// layout setting - activity_second
		//////////////////////////////////////
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_second);


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

		// ���̸� ����
		myName = (TextView)findViewById(R.id.myName);
		loading = (TextView)findViewById(R.id.loading);
		myName.setText(m_name+"님, 환영합니다.");

		////////////////////////////////
		// ��ü ����
		////////////////////////////////
		writeflag = 0;
		logout2 = (Button)findViewById(R.id.logout2);
		write = (Button)findViewById(R.id.write);
		cpwndlog = (Button)findViewById(R.id.cpwnd); // ü�߿����..
		dnsehdfid = (Button)findViewById(R.id.dnsehdfid);
		dmatlr = (Button)findViewById(R.id.dmatlr);
		gufdkq = (Button)findViewById(R.id.gufdkq);
		gufekd = (Button)findViewById(R.id.gufekd);

		frame = (FrameLayout)findViewById(R.id.frame);
		writew = (FrameLayout)findViewById(R.id.writew);
		ani1 = AnimationUtils.loadAnimation(this,R.anim.move1);
		ani2 = AnimationUtils.loadAnimation(this,R.anim.move2);
		MySlideAnimationListener slideListener = new MySlideAnimationListener();
		ani1.setAnimationListener(slideListener);
		ani2.setAnimationListener(slideListener);

		sss = (FrameLayout)findViewById(R.id.sss);
		yes = (Button)findViewById(R.id.yes);
		no = (Button)findViewById(R.id.no);

		care = (Button)findViewById(R.id.care);
		log = (Button)findViewById(R.id.log);
		sns = (Button)findViewById(R.id.sns);


		//////////////////////////////////////////////////////////
		// �ۼ��ϱ� ������ ��� - �ۼ��ϱ� Frame ����
		//////////////////////////////////////////////////////////
		write.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if(writeflag == 0)
				{
					writew.setVisibility(View.VISIBLE);
					writew.startAnimation(ani1);
					writeflag = 1;
				}
				else
				{
					writew.startAnimation(ani2);
					writew.setVisibility(View.INVISIBLE);
					writeflag = 0;
				}
			}
		});
		care.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choooose.this, care.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

			}
		});

		log.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(choooose.this, log.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

			}
		});
		sns.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				loading.setVisibility(View.VISIBLE);

				Intent intent = new Intent(choooose.this, sns.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

				loading.setVisibility(View.GONE);

			}
		});
		////////////////////////////////////////////////////////////
		// logout -> MainActivity�� �̵�
		////////////////////////////////////////////////////////////
		logout2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				sss.setVisibility(View.VISIBLE);
				frame.setVisibility(View.GONE);

			}
		});
		no.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sss.setVisibility(View.GONE);
				frame.setVisibility(View.VISIBLE);

			}
		});
		yes.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				sss.setVisibility(View.GONE);
				frame.setVisibility(View.VISIBLE);

				MainActivity.check = 1;

				Intent intent = new Intent(choooose.this, MainActivity.class);
				startActivity(intent);


			}
		});



		////////////////////////////////////////////
		// ü�� ����ϱ� writelog.class �� �̵�
		////////////////////////////////////////////
		cpwndlog.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(choooose.this, weightlog.class);
				intent.putExtra("email",m_email);
				startActivity(intent);
			}
		});

		/////////////////////////////////////////////
		// � ����ϱ� exerciselog.class�� �̵�
		/////////////////////////////////////////////
		dnsehdfid.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(choooose.this, exerciselog.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

			}
		});

		// ���� ����ϱ� . foodlog.class�� �̵�
		dmatlr.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(choooose.this, foodlog.class);
				intent.putExtra("email",m_email);
				startActivity(intent);
			}
		});

		// ���� ����ϱ� bloodtmplog.class�� �̵�
		gufdkq.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				Intent intent = new Intent(choooose.this, bloodtmplog.class);
				intent.putExtra("email",m_email);
				startActivity(intent);

			}
		});

		// ���緮 ��� bsugartmplog.class�� �̵�
		gufekd.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(choooose.this, bsugartmplog.class);
				intent.putExtra("email",m_email);
				startActivity(intent);
			}
		});



	}

	private class MySlideAnimationListener implements AnimationListener{

		@Override
		public void onAnimationStart(Animation animation) { }

		@Override
		public void onAnimationEnd(Animation animation) { }

		@Override
		public void onAnimationRepeat(Animation animation) { }

	}



}