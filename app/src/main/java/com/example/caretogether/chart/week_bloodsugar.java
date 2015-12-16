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
import android.widget.TextView;


import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import org.achartengine.ChartFactory;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Average temperature demo chart.
 */
public class week_bloodsugar extends AbstractDemoChart {
  List<ParseObject> bs_list;

  String m_email;
  String m_name;

  ArrayList<MyBloodSugarRecordList> myBloodSugarRecordList;

  /**
   * Returns the chart name.
   *
   * @return the chart name
   */
  public String getName() {
    return "주간 혈당 그래프";
  }

  /**
   * Returns the chart description.
   *
   * @return the chart description
   */
  public String getDesc() {
    return "주간 혈당 그래프입니다.";
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

    /////////////////////////////////////
    // DB���� �о����
    //////////////////////////////////////
    ParseQuery<ParseObject> query2 = ParseQuery.getQuery("bloodsugar_record");
    query2.whereEqualTo("m_email", email);
    query2.orderByAscending("br_date");
    try {
      bs_list = query2.find();
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

    String[] titles = new String[] { "혈당", "기준치" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] {  Integer.parseInt(myBloodSugarRecordList.get(0).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(1).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(2).getBr_date()),
              Integer.parseInt(myBloodSugarRecordList.get(3).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(4).getBr_date()),Integer.parseInt(myBloodSugarRecordList.get(5).getBr_date()),
              Integer.parseInt(myBloodSugarRecordList.get(6).getBr_date()) });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { myBloodSugarRecordList.get(0).getBr_amount(), myBloodSugarRecordList.get(1).getBr_amount(),myBloodSugarRecordList.get(2).getBr_amount(),myBloodSugarRecordList.get(3).getBr_amount(),
            myBloodSugarRecordList.get(4).getBr_amount(), myBloodSugarRecordList.get(5).getBr_amount(), myBloodSugarRecordList.get(6).getBr_amount() });
    values.add(new double[]{90.0,90.0,90.0,90.0,90.0,90.0,90.0});
    int[] colors = new int[] { Color.RED, Color.GREEN };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "주간 혈당 그래프", "날짜", "수치",
            Integer.parseInt(myBloodSugarRecordList.get(0).getBr_date())-1, Integer.parseInt(myBloodSugarRecordList.get(6).getBr_date())+1, 50, 150,
            Color.LTGRAY, Color.LTGRAY);
    renderer.setXLabels(12);
    renderer.setYLabels(10);
    renderer.setShowGrid(true);
    renderer.setXLabelsAlign(Align.RIGHT);
    renderer.setYLabelsAlign(Align.RIGHT);
    renderer.setZoomButtonsVisible(true);
    renderer.setPanLimits(new double[] { -10, 20, -10, 40 });
    renderer.setZoomLimits(new double[] { -10, 20, -10, 40 });

    XYMultipleSeriesDataset dataset = buildDataset(titles, x, values);
    XYSeries series = dataset.getSeriesAt(0);
    series.addAnnotation("Vacation", 6, 30);
    Intent intent = ChartFactory.getLineChartIntent(context, dataset, renderer,
            "Average temperature");
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
}