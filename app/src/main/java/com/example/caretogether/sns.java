package com.example.caretogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.CurrentTime;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class sns extends Activity {


    listadptr adtr = null;


    ListView listsns;
    Button writeBtn;
    Button likeBtn;
    EditText write;


    String m_name;
    String m_email;
    String m_disease;
    String t_m_name;

    FrameLayout timeW;
    FrameLayout reW;

    TextView diseasintro;

    List<ParseObject> t_list;

    ArrayList<TimelineList> timelineList;

    Button SNSback;


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sns);

        diseasintro = (TextView)findViewById(R.id.diseaseintro);
        SNSback = (Button)findViewById(R.id.SNSback);


        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");

        ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
        query.whereEqualTo("m_email", email);

        try {
            m_name = query.getFirst().getString("m_name");
            m_email = query.getFirst().getString("m_email");
            m_disease = query.getFirst().getString("m_disease");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> timeline = ParseQuery.getQuery("timeline");
        timeline.whereEqualTo("t_disease", m_disease);
        timeline.orderByDescending("createdAt");
        try {
            t_list = timeline.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        timelineList =  new ArrayList<TimelineList>();

        for(int i=0;i<t_list.size();i++)
        {
            ParseQuery<ParseObject> m_name = ParseQuery.getQuery("member");
            m_name.whereEqualTo("m_email", t_list.get(i).getString("m_email"));

            try {
                t_m_name = m_name.getFirst().getString("m_name");


            } catch (ParseException e) {
                e.printStackTrace();
            }

            timelineList.add(new TimelineList(
                    t_list.get(i).getString("m_email"),
                    t_list.get(i).getObjectId(),
                    (t_list.get(i).getUpdatedAt().getMonth()+1) +"/"+t_list.get(i).getUpdatedAt().getDate() ,
                    t_list.get(i).getString("t_content"),
                    t_m_name,
                    t_list.get(i).getInt("t_comment_num"),
                    t_list.get(i).getInt("t_like_num")
            ));
        }



        adtr = new listadptr(this, R.layout.list_sns,timelineList);


        listsns = (ListView)findViewById(R.id.list);
        listsns.setAdapter(adtr);


        writeBtn = (Button)findViewById(R.id.writeBtn);
        write = (EditText)findViewById(R.id.write);

        likeBtn = (Button)findViewById(R.id.likeBtn);






        String disease;

        if(m_disease.equals("비만")){
            disease = "비만을";
        }else if(m_disease.equals("당뇨")){
            disease= "당뇨를";
        }else
            disease="고혈압을";

        diseasintro.setText(disease+" 함께하는 친구들");



/*        likeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseQuery<ParseObject> timeline = ParseQuery.getQuery("timeline");
                //timeline.whereEqualTo("t_id",t_id);
                timeline.getInBackground(t_id, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {

                        int t_comment_num = parseObject.getInt("t_comment_num");
                        parseObject.put("t_comment_num", t_comment_num+1);
                        parseObject.saveInBackground();
                    }
                });

                onResume();

            }
        });*/



        writeBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),"작성 중..",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0,0);
                toast.show();

                String t_content = write.getText().toString();
                ParseObject timeline= new ParseObject("timeline");

                timeline.put("m_email", m_email);
                timeline.put("t_content",t_content);
                timeline.put("t_date", CurrentTime.getCurrentTime());
                timeline.put("t_like_num", 0);
                timeline.put("t_comment_num", 0);
                timeline.put("t_disease", m_disease);

                timeline.saveInBackground();

                sns.this.onCreate(savedInstanceState);
                // adtr.notifyDataSetChanged();

               /* Intent intent = new Intent(sns.this, sns.class);
                intent.putExtra("email", m_email);
                startActivity(intent);
*/


                //   write.setText("");





            }
        });

        SNSback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(sns.this, choooose.class);
                intent.putExtra("email",m_email);
                startActivity(intent);


            }
        });

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    class TimelineList {

        String t_id;
        String t_date;
        String t_content;
        String m_name;
        String m_email;
        int t_comment_num;
        int t_like_num;

        TimelineList(String m_email, String t_id, String t_date, String t_content, String m_name, int t_comment_num, int t_like_num){
            this.m_email=m_email;
            this.t_id = t_id;
            this.t_date = t_date;
            this.t_content = t_content;
            this.m_name = m_name;
            this.t_comment_num = t_comment_num;
            this.t_like_num = t_like_num;

        }



    }



    public class listadptr extends BaseAdapter {

        Context con;
        LayoutInflater inflater;
        ArrayList<TimelineList> mwl;


        int layout;


        public listadptr(Context context, int alayout, ArrayList<TimelineList> mywl){
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
            return 0;
        }

        @Override
        public long getItemId(int position) {
            // TODO Auto-generated method stub
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent)
        {

            if (convertView == null){
                convertView = inflater.inflate(layout, parent, false);
            }

            TextView frinames =  (TextView)convertView.findViewById(R.id.frinames);  // 친구 이름
            TextView fritxts = (TextView)convertView.findViewById(R.id.fritxts); // 친구의 타임라인 text글
            TextView date = (TextView)convertView.findViewById(R.id.date);
            TextView likecnt = (TextView)convertView.findViewById(R.id.likecnt);
            TextView recnt = (TextView)convertView.findViewById(R.id.recnt);

            Button reBtn =(Button)convertView.findViewById(R.id.reBtn);  // 댓글보는 곳으로 가는 버튼
            Button likeBtn = (Button)convertView.findViewById(R.id.likeBtn);

            frinames.setText(mwl.get(position).m_name);
            fritxts.setText(mwl.get(position).t_content);
            date.setText(mwl.get(position).t_date);
            likecnt.setText(mwl.get(position).t_like_num + "개");
            recnt.setText(mwl.get(position).t_comment_num + "개");


            frinames.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(sns.this, care_other.class);
                    //intent.putExtra("email",m_email);
                    intent.putExtra("email",mwl.get(position).m_email);
                    startActivity(intent);

                }
            });

            likeBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ParseQuery<ParseObject> timeline = ParseQuery.getQuery("timeline");
                    timeline.getInBackground(mwl.get(position).t_id, new GetCallback<ParseObject>() {
                        @Override
                        public void done(ParseObject parseObject, ParseException e) {

                            int t_like_num = parseObject.getInt("t_like_num");
                            parseObject.put("t_like_num", t_like_num + 1);
                            parseObject.saveInBackground();

                            Intent intent = new Intent(sns.this, sns.class);
                            intent.putExtra("email", m_email);
                            startActivity(intent);
                        }
                    });

                    //sns.this.onCreate(savedInstanceState);
                 /*   adtr.notifyDataSetChanged();
                    Intent intent = new Intent(sns.this, sns.class);
                    intent.putExtra("email", m_email);
                    startActivity(intent);*/

                }
            });

            reBtn.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    adtr.notifyDataSetChanged();

                    Intent intent = new Intent(sns.this, replyAct.class);
                    intent.putExtra("email",m_email);
                    intent.putExtra("t_id",mwl.get(position).t_id);
                    startActivity(intent);




                }
            });




            return convertView;
        }



    }


}
