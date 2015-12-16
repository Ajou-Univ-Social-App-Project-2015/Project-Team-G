package com.example.caretogether;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.util.CurrentTime;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

public class replyAct extends Activity {

    List<ParseObject> r_list;
    ArrayList<ReplyList> replyList;
    listadptr adtr;
    ListView listReply;
    Button reSend;
    EditText reText;
    String m_name;
    String m_email;
    String r_m_name;
    String t_id;

    Button REback;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reply);

        replyList = new ArrayList<ReplyList>();
        adtr = new listadptr(this, R.layout.list_re,replyList);

        reText = (EditText)findViewById(R.id.reText); // 내 댓글
        reSend =(Button)findViewById(R.id.reSend); // 댓글 입력 버튼

        listReply = (ListView)findViewById(R.id.reList); // 댓글 보여 줄 리스트 뷰
        listReply.setAdapter(adtr);

        Intent intent = getIntent();
        String email = intent.getExtras().getString("email");
        t_id = intent.getExtras().getString("t_id");

        final ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
        query.whereEqualTo("m_email", email);

        REback =(Button)findViewById(R.id.REback); // 댓글 입력 버튼


        try {
            m_name = query.getFirst().getString("m_name");
            m_email = query.getFirst().getString("m_email");

        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery<ParseObject> reply = ParseQuery.getQuery("reply");
        reply.whereEqualTo("t_id", t_id);
        reply.orderByDescending("createdAt");
        try {
            r_list = reply.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        replyList =  new ArrayList<ReplyList>();

        for(int i=0;i<r_list.size();i++)
        {
            ParseQuery<ParseObject> m_name = ParseQuery.getQuery("member");
            m_name.whereEqualTo("m_email", r_list.get(i).getString("m_email"));

            try {
                r_m_name = m_name.getFirst().getString("m_name");


            } catch (ParseException e) {
                e.printStackTrace();
            }

            replyList.add(new ReplyList(

                    (r_list.get(i).getUpdatedAt().getMonth()+1) +"/"+r_list.get(i).getUpdatedAt().getDate() ,
                    r_list.get(i).getString("r_content"),
                    r_m_name
            ));
        }

        adtr = new listadptr(this, R.layout.list_re,replyList);


        listReply = (ListView)findViewById(R.id.reList);
        listReply.setAdapter(adtr);


        reSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Toast toast = Toast.makeText(getApplicationContext(),"작성 중..",Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL, 0,0);
                toast.show();

                String r_content = reText.getText().toString();
                ParseObject reply = new ParseObject("reply");

                ParseQuery<ParseObject> timeline = ParseQuery.getQuery("timeline");
                //timeline.whereEqualTo("t_id",t_id);
                timeline.getInBackground(t_id, new GetCallback<ParseObject>() {
                    @Override
                    public void done(ParseObject parseObject, ParseException e) {

                        int t_comment_num = parseObject.getInt("t_comment_num");
                        parseObject.put("t_comment_num", t_comment_num + 1);
                        parseObject.saveInBackground();
                    }
                });


                reply.put("m_email", m_email);
                reply.put("r_content", r_content);
                reply.put("r_date", CurrentTime.getCurrentTime());
                reply.put("t_id", t_id);


                reply.saveInBackground();

                adtr.notifyDataSetChanged();

                Intent intent = new Intent(replyAct.this, replyAct.class);
                intent.putExtra("email", m_email);
                intent.putExtra("t_id",t_id) ;
                startActivity(intent);


            }
        });

        REback.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                Intent intent = new Intent(replyAct.this, sns.class);
                intent.putExtra("email",m_email);
                startActivity(intent);


            }
        });

        for(int i=0; i<replyList.size();i++){
            Log.v("내용: ",replyList.get(i).r_content);
        }
        Log.v(t_id,m_name);


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




    public class listadptr extends BaseAdapter {

        Context con;
        LayoutInflater inflater;
        ArrayList<ReplyList> mwl;


        int layout;


        public listadptr(Context context, int alayout, ArrayList<ReplyList> mywl){
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
            return mwl.get(position).r_content;
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







            TextView reshow = (TextView)convertView.findViewById(R.id.re); // 리스트에 보여질 댓글들
            TextView id = (TextView)convertView.findViewById(R.id.id); // 리스트에 보여질 댓글들
            TextView redate = (TextView)convertView.findViewById(R.id.redate); // 리스트에 보여질 댓글들

            reshow.setText(mwl.get(position).r_content);
            id.setText(mwl.get(position).m_name);
            redate.setText(mwl.get(position).r_date);





            return convertView;
        }



    }

    class ReplyList{
        String r_date;
        String r_content;
        String m_name;

        ReplyList(String r_date, String r_content, String m_name){
            this.r_content = r_content;
            this.r_date =r_date;
            this.m_name = m_name;
        }
    }
}