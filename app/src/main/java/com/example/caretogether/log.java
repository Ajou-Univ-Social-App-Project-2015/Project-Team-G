package com.example.caretogether;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class log extends Activity {

    TextView weightlog, weightdate, Exlog, Exdate, foodlog, fooddate, bldlog, blddate, sugarlog, sugardate, avgbldtmp, avgsugartmp, safeOrwarnn1, safeOrwarnn2;

    List<ParseObject> w_list;
    List<ParseObject> bpr_list;
    List<ParseObject> bad_bpr_list;
    List<ParseObject> bs_list;
    List<ParseObject> bad_bs_list;
    List<ParseObject> f_list;
    List<ParseObject> bad_f_list;
    List<ParseObject> e_list;



    String m_email;
    String m_name;
    int m_weight;


    ArrayList<MyExerciseList> myExerciseList;
    ArrayList<MyWeightRecordList> myWeightRecordList;
    ArrayList<MyFoodRecordList> myFoodRecordList;
    ArrayList<MyFoodRecordList> myFoodRecordList2;
    ArrayList<MyBloodPressureRecordList> myBloodPressureRecordList;
    ArrayList<MyBloodPressureRecordList> myBloodPressureRecordList2;
    ArrayList<MyBloodSugarRecordList> myBloodSugarRecordList;
    ArrayList<MyBloodSugarRecordList> myBloodSugarRecordList2;

    Button LOGback, warnnfood, closeWarnnFood;
    FrameLayout LogW, WarnnfoodW;

    // 혈압 음식, 혈압, 날짜
    TextView warnnbpfood1,   warnnbloodpres1,  warnnbpdate1, warnnbpfood2,   warnnbloodpres2,  warnnbpdate2, warnnbpfood3,   warnnbloodpres3,  warnnbpdate3;

    //혈당 음식, 혈당, 날짜
    TextView warnnfood1,   warnnbloodsugar1,  warnndate1, warnnfood2,   warnnbloodsugar2,  warnndate2, warnnfood3,   warnnbloodsugar3,  warnndate3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        weightlog = (TextView)findViewById(R.id.weightlog);
        weightdate = (TextView)findViewById(R.id.weightdate);
        Exlog = (TextView)findViewById(R.id.Exlog);
        Exdate = (TextView)findViewById(R.id.Exdate);
        foodlog = (TextView)findViewById(R.id.foodlog);
        fooddate = (TextView)findViewById(R.id.fooddate);
        bldlog = (TextView)findViewById(R.id.bldlog);
        blddate = (TextView)findViewById(R.id.blddate);
        sugarlog = (TextView)findViewById(R.id.sugarlog);
        sugardate = (TextView)findViewById(R.id.sugardate);
        avgbldtmp = (TextView)findViewById(R.id.avgbldtmp);
        avgsugartmp = (TextView)findViewById(R.id.avgsugartmp);
        safeOrwarnn1 = (TextView)findViewById(R.id.safeOrWarnn1);
        safeOrwarnn2 = (TextView)findViewById(R.id.safeOrWarnn2);


        ////// 1순위 음식, 혈압, 날짜/////
        warnnbpfood1 = (TextView)findViewById(R.id.warnnfood1);
        warnnbloodpres1 = (TextView)findViewById(R.id.warnnbloodsugar1);
        warnnbpdate1 = (TextView)findViewById(R.id.warnndate1);


        ////// 2순위 음식, 혈압, 날짜/////
        warnnbpfood2 = (TextView)findViewById(R.id.warnnfood2);
        warnnbloodpres2 = (TextView)findViewById(R.id.warnnbloodsugar2);
        warnnbpdate2 = (TextView)findViewById(R.id.warnndate2);


        ////// 3순위 음식, 혈압, 날짜/////
        warnnbpfood3 = (TextView)findViewById(R.id.warnnfood3);
        warnnbloodpres3 = (TextView)findViewById(R.id.warnnbloodsugar3);
        warnnbpdate3 = (TextView)findViewById(R.id.warnndate3);

/////////////////////////////////////////////////////////////////////////////

        ////// 1순위 음식, 혈당, 날짜/////
        warnnfood1 = (TextView)findViewById(R.id.warnnfood11);
        warnnbloodsugar1 = (TextView)findViewById(R.id.warnnbloodsugar11);
        warnndate1 = (TextView)findViewById(R.id.warnndate11);


        ////// 2순위 음식, 혈당, 날짜/////
        warnnfood2 = (TextView)findViewById(R.id.warnnfood22);
        warnnbloodsugar2 = (TextView)findViewById(R.id.warnnbloodsugar22);
        warnndate2 = (TextView)findViewById(R.id.warnndate22);


        ////// 3순위 음식, 혈당, 날짜/////
        warnnfood3 = (TextView)findViewById(R.id.warnnfood33);
        warnnbloodsugar3 = (TextView)findViewById(R.id.warnnbloodsugar33);
        warnndate3 = (TextView)findViewById(R.id.warnndate33);

        ////////////////////////////////////////////////
        LogW = (FrameLayout)findViewById(R.id.LogW);
        WarnnfoodW = (FrameLayout)findViewById(R.id.WarnnfoodW);
        LOGback = (Button)findViewById(R.id.LOGback);
        warnnfood = (Button)findViewById(R.id.warnnfood);
        closeWarnnFood = (Button)findViewById(R.id.closeWarnnFood);




        warnnfood.setOnClickListener(new View.OnClickListener() { // 음식 리스트 보여주는 창 연다.

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                WarnnfoodW.setVisibility(View.VISIBLE);
                LogW.setVisibility(View.GONE);

            }
        });
        closeWarnnFood.setOnClickListener(new View.OnClickListener() {  // 음식 리스트 보여주는 창 닫음
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                WarnnfoodW.setVisibility(View.GONE);
                LogW.setVisibility(View.VISIBLE);

            }
        });

        LOGback.setOnClickListener(new View.OnClickListener() { // 뒤로가기(츄즈 액티비티로 전환)

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub

                Intent intent = new Intent(log.this, choooose.class);
                intent.putExtra("email",m_email);
                startActivity(intent);

            }
        });

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


        ParseQuery<ParseObject> query2 = ParseQuery.getQuery("exercise_record");
        query2.whereEqualTo("m_email", email);
        query2.orderByAscending("er_date");
        try {
            e_list = query2.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myExerciseList = new ArrayList<MyExerciseList>();

        for(int i=0;i<e_list.size();i++)
        {
            myExerciseList.add(new MyExerciseList(
                    e_list.get(i).getString("ei_name"),
                    e_list.get(i).getInt("er_amount"),
                    e_list.get(i).getInt("er_kcal"),
                    e_list.get(i).getString("er_date").substring(0,2) + "월 "+ e_list.get(i).getString("er_date").substring(2,4)+"일"
            ));
        }



        ParseQuery<ParseObject> query3 = ParseQuery.getQuery("weight_record");
        query3.whereEqualTo("m_email", email);
        query3.orderByAscending("wr_date");
        try {
            w_list = query3.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myWeightRecordList = new ArrayList<MyWeightRecordList>();

        for(int i=0;i<w_list.size();i++)
        {
            myWeightRecordList.add(new MyWeightRecordList(
                    w_list.get(i).getString("wr_date").substring(0,2) + "월 " + w_list.get(i).getString("wr_date").substring(2,4) + "일",
                    w_list.get(i).getInt("wr_weight")
            ));
        }

        ParseQuery<ParseObject> query4 = ParseQuery.getQuery("food_record");
        query4.whereEqualTo("m_email", email);
        query4.orderByAscending("fr_date");
        try {
            f_list = query4.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }


        myFoodRecordList = new ArrayList<MyFoodRecordList>();

        for(int i=0;i<f_list.size();i++)
        {
            myFoodRecordList.add(new MyFoodRecordList(
                    f_list.get(i).getString("fr_date").substring(0,2) + "월 "+f_list.get(i).getString("fr_date").substring(2,4) + "일",
                    f_list.get(i).getString("fi_name"),
                    f_list.get(i).getDouble("fr_kal")


            ));
        }

        ParseQuery<ParseObject> query5 = ParseQuery.getQuery("bloodpressure_record");
        query5.whereEqualTo("m_email", email);
        query5.orderByAscending("brp_date");
        try {
            bpr_list = query5.find();
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

        ParseQuery<ParseObject> query6 = ParseQuery.getQuery("bloodsugar_record");
        query6.whereEqualTo("m_email", email);
        query6.orderByAscending("br_date");
        try {
            bs_list = query6.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        myBloodSugarRecordList = new ArrayList<MyBloodSugarRecordList>();

        for(int i=0;i<bs_list.size();i++)
        {
            myBloodSugarRecordList.add(new MyBloodSugarRecordList(
                    bs_list.get(i).getString("br_date").substring(0,2) + "월 " +bs_list.get(i).getString("br_date").substring(2,4) + "일",
                    bs_list.get(i).getInt("br_amount"),
                    bs_list.get(i).getString("br_situation"),
                    bs_list.get(i).getString("br_type"),
                    bs_list.get(i).getString("br_memo")
            ));
        }

        ParseQuery<ParseObject> query7 = ParseQuery.getQuery("bloodsugar_record");
        query7.whereEqualTo("m_email", email);
        query7.orderByDescending("br_amount");
        query7.setLimit(3);
        try {
            bad_bs_list = query7.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        warnndate1.setText(bad_bs_list.get(0).getString("br_date").substring(0, 2) + "월 " + bad_bs_list.get(0).getString("br_date").substring(2, 4) + "일");
        warnndate2.setText(bad_bs_list.get(1).getString("br_date").substring(0, 2) + "월 " + bad_bs_list.get(1).getString("br_date").substring(2, 4) + "일");
        warnndate3.setText(bad_bs_list.get(2).getString("br_date").substring(0, 2) + "월 " + bad_bs_list.get(2).getString("br_date").substring(2, 4) + "일");

        warnnbloodsugar1.setText(bad_bs_list.get(0).getInt("br_amount")+"mg/dl");
        warnnbloodsugar2.setText(bad_bs_list.get(1).getInt("br_amount")+"mg/dl");
        warnnbloodsugar3.setText(bad_bs_list.get(2).getInt("br_amount")+"mg/dl");

        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnndate1.getText().toString())){
                warnnfood1.setText(myFoodRecordList.get(i).getFi_name());
            }
        }
        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnndate2.getText().toString())){
                warnnfood2.setText(myFoodRecordList.get(i).getFi_name());
            }
        }
        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnndate3.getText().toString())){
                warnnfood3.setText(myFoodRecordList.get(i).getFi_name());
            }
        }


        ParseQuery<ParseObject> query8 = ParseQuery.getQuery("bloodpressure_record");
        query8.whereEqualTo("m_email", email);
        query8.orderByDescending("brp_high_pressure");
        query8.setLimit(3);
        try {
            bad_bpr_list = query8.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        warnnbpdate1.setText(bad_bpr_list.get(0).getString("brp_date").substring(0, 2) + "월 " + bad_bpr_list.get(0).getString("brp_date").substring(2, 4) + "일");
        warnnbpdate2.setText(bad_bpr_list.get(1).getString("brp_date").substring(0, 2) + "월 " + bad_bpr_list.get(1).getString("brp_date").substring(2, 4) + "일");
        warnnbpdate3.setText(bad_bpr_list.get(2).getString("brp_date").substring(0, 2) + "월 " + bad_bpr_list.get(2).getString("brp_date").substring(2, 4) + "일");

        warnnbloodpres1.setText(bad_bpr_list.get(0).getInt("brp_high_pressure")+"mmHg");
        warnnbloodpres2.setText(bad_bpr_list.get(1).getInt("brp_high_pressure")+"mmHg");
        warnnbloodpres3.setText(bad_bpr_list.get(2).getInt("brp_high_pressure")+"mmHg");

        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnnbpdate1.getText().toString())){
                warnnbpfood1.setText(myFoodRecordList.get(i).getFi_name());
            }
        }
        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnnbpdate2.getText().toString())){
                warnnbpfood2.setText(myFoodRecordList.get(i).getFi_name());
            }
        }
        for(int i=0; i<myFoodRecordList.size();i++){
            if(myFoodRecordList.get(i).getFr_date().equals(warnnbpdate3.getText().toString())){
                warnnbpfood3.setText(myFoodRecordList.get(i).getFi_name());
            }
        }






        /////////////////////////////////////////////////////////////////////
        int tmpw = myWeightRecordList.get(myWeightRecordList.size()-1).getWr_weight();
        weightlog.setText(String.valueOf(tmpw) + "kg");
        weightdate.setText(myWeightRecordList.get(myWeightRecordList.size()-1).getWr_date());

        int tmpe = (int)myExerciseList.get(myExerciseList.size()-1).er_kal;
        Exlog.setText(String.valueOf(tmpe) + "kcal");
        Exdate.setText(myExerciseList.get(myExerciseList.size()-1).er_date);

        int tmpf = (int)myFoodRecordList.get(myFoodRecordList.size()-1).fr_kal;
        foodlog.setText(String.valueOf(tmpf) + "kcal");
        fooddate.setText(myFoodRecordList.get(myFoodRecordList.size()-1).fr_date);

        int tmpbp = myBloodPressureRecordList.get(myBloodPressureRecordList.size()-1).brp_high_pressure;
        bldlog.setText(String.valueOf(tmpbp) + "mmHg");
        blddate.setText(myBloodPressureRecordList.get(myBloodPressureRecordList.size()-1).br_date);

        int tmpbs = myBloodSugarRecordList.get(myBloodSugarRecordList.size()-1).br_amount;
        sugarlog.setText(String.valueOf(tmpbs) + "mg/dL");
        sugardate.setText(myBloodSugarRecordList.get(myBloodSugarRecordList.size()-1).br_date);

        int avgbldp =  myBloodPressureRecordList.get(myBloodPressureRecordList.size()-1).brp_high_pressure +  myBloodPressureRecordList.get(myBloodPressureRecordList.size()-2).brp_high_pressure +  myBloodPressureRecordList.get(myBloodPressureRecordList.size()-3).brp_high_pressure;
        int bldprss = avgbldp/3;
        avgbldtmp.setText(String.valueOf(bldprss) + "mmHg");

        int avgblds = myBloodSugarRecordList.get(myBloodSugarRecordList.size()-1).br_amount +  myBloodSugarRecordList.get(myBloodSugarRecordList.size()-2).br_amount +  myBloodSugarRecordList.get(myBloodSugarRecordList.size()-3).br_amount;
        int bldsugar = avgblds/3;
        avgsugartmp.setText(String.valueOf(bldsugar) + "mg/dL");

        if(bldprss > 120) {
            safeOrwarnn1.setText("현재 3일간의 혈압 수치가 위험합니다.");
            safeOrwarnn1.setTextColor(Color.RED);
        }
        else {
            safeOrwarnn1.setText("현재 3일간의 혈압 수치는 안전합니다.");
            safeOrwarnn1.setTextColor(Color.BLUE);
        }
        if(bldsugar > 110) {
            safeOrwarnn2.setText("현재 3일간의 혈당 수치가 위험합니다.");
            safeOrwarnn2.setTextColor(Color.RED);
        }
        else {
            safeOrwarnn2.setText("현재 3일간의 혈당 수치는 안전합니다.");
            safeOrwarnn2.setTextColor(Color.BLUE);
        }






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


    public class MyExerciseList
    {

        private String ei_name;
        private int er_amount;
        private double er_kal;
        private String er_date;

        MyExerciseList(String ei_name, int er_amount, double er_kal, String er_date){
            this.ei_name = ei_name;
            this.er_amount = er_amount;
            this.er_kal = er_kal;
            this.er_date = er_date;
        }

        public String getEi_name() {
            return ei_name;
        }

        public void setEi_name(String ei_name) {
            this.ei_name = ei_name;
        }

        public int getEr_amount() {
            return er_amount;
        }

        public void setEr_amount(int er_amount) {
            this.er_amount = er_amount;
        }

        public double getEr_kal() {
            return er_kal;
        }

        public void setEr_kal(double er_kal) {
            this.er_kal = er_kal;
        }

        public String getEr_date() {
            return er_date;
        }

        public void setEr_date(String er_date) {
            this.er_date = er_date;
        }


    }



    public class MyWeightRecordList
    {
        private String wr_date;
        private int wr_weight;

        MyWeightRecordList(String wr_date, int wr_weight){
            this.wr_date = wr_date;
            this.wr_weight = wr_weight;

        }


        public String getWr_date() {
            return wr_date;
        }

        public void setWr_date(String wr_date) {
            this.wr_date = wr_date;
        }


        public int getWr_weight() {
            return wr_weight;
        }

        public void setWr_weight(int wr_weight) {
            this.wr_weight = wr_weight;
        }

    }

    public class MyFoodRecordList
    {
        private String fr_date;
        private String fi_name;
        private double fr_kal;

        MyFoodRecordList(String fr_date, String fi_name, double fr_kal){
            this.fr_date = fr_date;
            this.fr_kal = fr_kal;
            this.fi_name = fi_name;

        }

        public String getFr_date() {
            return fr_date;
        }

        public void setFr_date(String fr_date) {
            this.fr_date = fr_date;
        }

        public double getFr_kal() {
            return fr_kal;
        }

        public void setFr_kal(double fr_kal) {
            this.fr_kal = fr_kal;
        }

        public String getFi_name(){
            return fi_name;
        }
        public void setFi_name(String fi_name){
            this.fi_name =fi_name;
        }
    }


    public class MyBloodPressureRecordList
    {
        private String br_date;
        private int brp_high_pressure;
        private int brp_low_pressure;
        private int brp_pulse;
        private String brp_irregular_pulse;
        private String brp_memo;

        MyBloodPressureRecordList(String br_date, int brp_high_pressure, int brp_low_pressure, int brp_pulse
                ,String brp_irregular_pulse, String brp_memo )
        {
            this.br_date = br_date;
            this.brp_high_pressure=brp_high_pressure;
            this.brp_low_pressure = brp_low_pressure;
            this.brp_pulse=brp_pulse;
            this.brp_irregular_pulse=brp_irregular_pulse;
            this.brp_memo=brp_memo;
        }

        public String getBr_date() {
            return br_date;
        }

        public void setBr_date(String br_date) {
            this.br_date = br_date;
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
}