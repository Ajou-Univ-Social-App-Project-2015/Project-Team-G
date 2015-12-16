/**
 * Copyright (C) 2009 - 2013 SC 4ViewSoft SRL
 *  
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *      http://www.apache.org/licenses/LICENSE-2.0
 *  
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.caretogether.chart;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


/**
 * Combined temperature demo chart.
 */
public class week_food_exercise_weight extends AbstractDemoChart {
  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  List<ParseObject> w_list;
  List<ParseObject> f_list;
  List<ParseObject> e_list;

  String m_email;
  String m_name;
  int m_weight;

  ArrayList<MyExerciseList> myExerciseList;
  ArrayList<MyWeightRecordList> myWeightRecordList;
  ArrayList<MyFoodRecordList> myFoodRecordList;




  public String getName() {
    return "주간 음식-운동량-체중 차트";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "주간 음식-운동량-체중 관리 그래프입니다.";
  }

  /**
   * Executes the chart demo.
   * 
   * @param context the context
   * @return the built intent
   */

  public Intent execute(Context context,String email) {

    ParseQuery<ParseObject> query = ParseQuery.getQuery("member");
    query.whereEqualTo("m_email", email);

    try {
      m_name = query.getFirst().getString("m_name");
      m_email = query.getFirst().getString("m_email");
      m_weight = query.getFirst().getInt("m_weight");

    } catch (ParseException e) {
      e.printStackTrace();
    }


    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("exercise_record");
    query2.whereEqualTo("m_email", email);
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
              e_list.get(i).getString("er_date")
      ));
    }



    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("weight_record");
    query3.whereEqualTo("m_email", email);
    try {
      w_list = query3.find();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    myWeightRecordList = new ArrayList<MyWeightRecordList>();

    for(int i=0;i<w_list.size();i++)
    {
      myWeightRecordList.add(new MyWeightRecordList(
              w_list.get(i).getString("wr_date"),
              w_list.get(i).getInt("wr_weight")
      ));
    }

    ParseQuery<ParseObject> query4 = ParseQuery.getQuery("food_record");
    query4.whereEqualTo("m_email", email);
    try {
      f_list = query4.find();
    } catch (ParseException e) {
      e.printStackTrace();
    }


    myFoodRecordList = new ArrayList<MyFoodRecordList>();

    for(int i=0;i<f_list.size();i++)
    {
      myFoodRecordList.add(new MyFoodRecordList(
              f_list.get(i).getString("fr_date"),
              f_list.get(i).getDouble("fr_kal")


      ));
    }
    //점선 그래프
    String[] titles = new String[] { "음식", "운동량" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] { Integer.parseInt(myFoodRecordList.get(0).getFr_date()),Integer.parseInt(myFoodRecordList.get(1).getFr_date()),Integer.parseInt(myFoodRecordList.get(2).getFr_date()),
              Integer.parseInt(myFoodRecordList.get(3).getFr_date()),Integer.parseInt(myFoodRecordList.get(4).getFr_date()),Integer.parseInt(myFoodRecordList.get(5).getFr_date()),
              Integer.parseInt(myFoodRecordList.get(6).getFr_date())});
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { myFoodRecordList.get(0).getFr_kal(), myFoodRecordList.get(1).getFr_kal(),myFoodRecordList.get(2).getFr_kal(),myFoodRecordList.get(3).getFr_kal(),
            myFoodRecordList.get(4).getFr_kal(), myFoodRecordList.get(5).getFr_kal(), myFoodRecordList.get(6).getFr_kal()});
    values.add(new double[] { myExerciseList.get(0).getEr_kal(), myExerciseList.get(1).getEr_kal(), myExerciseList.get(2).getEr_kal(),
            myExerciseList.get(3).getEr_kal(), myExerciseList.get(4).getEr_kal(), myExerciseList.get(5).getEr_kal(), myExerciseList.get(6).getEr_kal() });
    int[] colors = new int[] { Color.RED, Color.rgb(200, 150, 0) };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };


    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    renderer.setPointSize(7.5f);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      XYSeriesRenderer r = (XYSeriesRenderer) renderer.getSeriesRendererAt(i);
      r.setLineWidth(5);
      r.setFillPoints(false);
    }
    setChartSettings(renderer, "주간 차트", "날짜", "수치", Integer.parseInt(myFoodRecordList.get(0).getFr_date())-1, Integer.parseInt(myFoodRecordList.get(6).getFr_date())+1, 0, 800,
            Color.LTGRAY, Color.LTGRAY);

    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[]{-10, 20, -10, 40});
    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

//    XYValueSeries sunSeries = new XYValueSeries("Sunshine hours");
//    sunSeries.add(1f, 35, 4.3);
//    sunSeries.add(2f, 35, 4.9);
//    sunSeries.add(3f, 35, 5.9);
//    sunSeries.add(4f, 35, 8.8);
//    sunSeries.add(5f, 35, 10.8);
//    sunSeries.add(6f, 35, 11.9);
//    sunSeries.add(7f, 35, 13.6);
//    sunSeries.add(8f, 35, 12.8);
//    sunSeries.add(9f, 35, 11.4);
//    sunSeries.add(10f, 35, 9.5);
//    sunSeries.add(11f, 35, 7.5);
//    sunSeries.add(12f, 35, 5.5);
//    XYSeriesRenderer lightRenderer = new XYSeriesRenderer();
//    lightRenderer.setColor(Color.YELLOW);

    XYSeries waterSeries = new XYSeries("체중");
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(0).getFr_date()), myWeightRecordList.get(0).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(1).getFr_date()), myWeightRecordList.get(1).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(2).getFr_date()), myWeightRecordList.get(2).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(3).getFr_date()), myWeightRecordList.get(3).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(4).getFr_date()), myWeightRecordList.get(4).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(5).getFr_date()), myWeightRecordList.get(5).getWr_weight());
    waterSeries.add(Integer.parseInt(myFoodRecordList.get(6).getFr_date()), myWeightRecordList.get(6).getWr_weight());

    renderer.setBarSpacing(1);
    XYSeriesRenderer waterRenderer = new XYSeriesRenderer();
    waterRenderer.setColor(Color.argb(250, 0, 210, 250));

    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
   // dataset.addSeries(0, sunSeries);
    dataset.addSeries(0, waterSeries);
   // renderer.addSeriesRenderer(0, lightRenderer);
   renderer.addSeriesRenderer(0, waterRenderer);
    waterRenderer.setDisplayChartValues(true);
    waterRenderer.setChartValuesTextSize(20);

    String[] types = new String[] { BarChart.TYPE,  LineChart.TYPE,
        CubicLineChart.TYPE };/*BarChart.TYPE,*/
    Intent intent = ChartFactory.getCombinedXYChartIntent(context, dataset, renderer, types,
            "주간 관리 그래프");
    return intent;
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
    private double fr_kal;

    MyFoodRecordList(String fr_date, double fr_kal){
      this.fr_date = fr_date;
      this.fr_kal = fr_kal;

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
  }
}
