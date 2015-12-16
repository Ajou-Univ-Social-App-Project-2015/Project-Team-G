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
import android.os.Bundle;

import com.example.caretogether.bloodtmplog;
import com.example.caretogether.bsugartmplog;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.BubbleChart;
import org.achartengine.chart.CubicLineChart;
import org.achartengine.chart.LineChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.model.XYValueSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;




/**
 * Combined temperature demo chart.
 */
public class week_bp_bs_weight extends AbstractDemoChart {
  /**
   * Returns the chart name.
   *
   * @return the chart name
   */

  List<ParseObject> w_list;
  List<ParseObject> bpr_list;
  List<ParseObject> bs_list;

  String m_email;
  String m_name;

  ArrayList<MyBloodSugarRecordList> myBloodSugarRecordList;
  ArrayList<MyBloodPressureRecordList> myBloodPressureRecordList;
  ArrayList<MyWeightRecordList> myWeightRecordList;




  public String getName() {
    return "주간 혈당-혈압-체중 차트";
  }

  /**
   * Returns the chart description.
   *
   * @return the chart description
   */
  public String getDesc() {
    return "주간 혈당-혈압-체중 관리 그래프입니다.";
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
    } catch (ParseException e) {
      e.printStackTrace();
    }


    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("bloodpressure_record");
    query2.whereEqualTo("m_email", email);
    query2.orderByAscending("brp_date");
    try {
      bpr_list = query2.find();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    myBloodPressureRecordList = new ArrayList<MyBloodPressureRecordList>();

    for(int i=0;i<bpr_list.size();i++)
    {
      myBloodPressureRecordList.add(new MyBloodPressureRecordList(
              bpr_list.get(i).getString("brp_date"),
              bpr_list.get(i).getInt("brp_high_pressure"),
              bpr_list.get(i).getInt("brp_low_pressure"),
              bpr_list.get(i).getInt("brp_pulse"),
              bpr_list.get(i).getString("brp_irregular_pulse"),
              bpr_list.get(i).getString("brp_memo")

      ));
    }

    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("bloodsugar_record");
    query3.whereEqualTo("m_email", email);
    query3.orderByAscending("br_date");
    try {
      bs_list = query3.find();
    } catch (ParseException e) {
      e.printStackTrace();
    }

    myBloodSugarRecordList = new ArrayList<MyBloodSugarRecordList>();

    for(int i=0;i<bs_list.size();i++)
    {
      myBloodSugarRecordList.add(new MyBloodSugarRecordList(
              bs_list.get(i).getString("br_date"),
              bs_list.get(i).getInt("br_amount"),
              bs_list.get(i).getString("br_situation"),
              bs_list.get(i).getString("br_type"),
              bs_list.get(i).getString("br_memo")
      ));
    }

    ParseQuery<ParseObject> query4 = ParseQuery.getQuery("weight_record");
    query4.whereEqualTo("m_email", email);
    try {
      w_list = query4.find();
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



    //점선 그래프
    String[] titles = new String[] { "혈당", "혈압" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] { Integer.parseInt(myBloodSugarRecordList.get(0).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(1).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(2).getBr_date()),
              Integer.parseInt(myBloodSugarRecordList.get(3).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(4).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(5).getBr_date()),
              Integer.parseInt(myBloodSugarRecordList.get(6).getBr_date())});
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { myBloodSugarRecordList.get(0).getBr_amount(), myBloodSugarRecordList.get(1).getBr_amount(),myBloodSugarRecordList.get(2).getBr_amount(),myBloodSugarRecordList.get(3).getBr_amount(),
            myBloodSugarRecordList.get(4).getBr_amount(), myBloodSugarRecordList.get(5).getBr_amount(), myBloodSugarRecordList.get(6).getBr_amount()});
    values.add(new double[] { myBloodPressureRecordList.get(0).getBrp_high_pressure(), myBloodPressureRecordList.get(1).getBrp_high_pressure(), myBloodPressureRecordList.get(2).getBrp_high_pressure(),
            myBloodPressureRecordList.get(3).getBrp_high_pressure(), myBloodPressureRecordList.get(4).getBrp_high_pressure(), myBloodPressureRecordList.get(5).getBrp_high_pressure(), myBloodPressureRecordList.get(6).getBrp_high_pressure() });
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
    setChartSettings(renderer, "주간 차트", "날짜", "수치",  Integer.parseInt(myBloodSugarRecordList.get(0).getBr_date())-1,  Integer.parseInt(myBloodSugarRecordList.get(6).getBr_date())+1, 0, 200,
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
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(0).getBr_date()), myWeightRecordList.get(0).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(1).getBr_date()), myWeightRecordList.get(1).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(2).getBr_date()), myWeightRecordList.get(2).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(3).getBr_date()), myWeightRecordList.get(3).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(4).getBr_date()), myWeightRecordList.get(4).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(5).getBr_date()), myWeightRecordList.get(5).getWr_weight());
    waterSeries.add(Integer.parseInt(myBloodSugarRecordList.get(6).getBr_date()), myWeightRecordList.get(6).getWr_weight());

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
}