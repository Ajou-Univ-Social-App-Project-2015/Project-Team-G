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
import android.util.Log;


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
public class week_bloodpressure extends AbstractDemoChart {

  List<ParseObject> bpr_list;
  String m_name;
  String m_email;

  ArrayList<MyBloodPressureRecordList> myBloodPressureRecordList;

  /**
   * Returns the chart name.
   *
   * @return the chart name
   */
  public String getName() {
    return "주간 혈압 그래프";
  }

  /**
   * Returns the chart description.
   *
   * @return the chart description
   */
  public String getDesc() {
    return "주간 혈압 그래프입니다.";
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
    {Log.v("혈당", bpr_list.get(i).getString("brp_date"));
      myBloodPressureRecordList.add(new MyBloodPressureRecordList(

              bpr_list.get(i).getString("brp_date"),
              bpr_list.get(i).getInt("brp_high_pressure"),
              bpr_list.get(i).getInt("brp_low_pressure"),
              bpr_list.get(i).getInt("brp_pulse"),
              bpr_list.get(i).getString("brp_irregular_pulse"),
              bpr_list.get(i).getString("brp_memo")

      ));
    }

    String[] titles = new String[] { "혈압","기준치" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] {  Integer.parseInt(myBloodPressureRecordList.get(0).getBr_date()),Integer.parseInt(myBloodPressureRecordList.get(1).getBr_date()),Integer.parseInt(myBloodPressureRecordList.get(2).getBr_date()),
              Integer.parseInt(myBloodPressureRecordList.get(3).getBr_date()),Integer.parseInt(myBloodPressureRecordList.get(4).getBr_date()),Integer.parseInt(myBloodPressureRecordList.get(5).getBr_date()),
              Integer.parseInt(myBloodPressureRecordList.get(6).getBr_date()) });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { myBloodPressureRecordList.get(0).getBrp_high_pressure(), myBloodPressureRecordList.get(1).getBrp_high_pressure(),myBloodPressureRecordList.get(2).getBrp_high_pressure(),myBloodPressureRecordList.get(3).getBrp_high_pressure(),
            myBloodPressureRecordList.get(4).getBrp_high_pressure(), myBloodPressureRecordList.get(5).getBrp_high_pressure(), myBloodPressureRecordList.get(6).getBrp_high_pressure() });
    values.add(new double[]{120.0,120.0,120.0,120.0,120.0,120.0,120.0});

    int[] colors = new int[] { Color.BLUE, Color.GREEN };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE, PointStyle.DIAMOND };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "주간 혈압 그래프", "날짜", "수치",
            Integer.parseInt(myBloodPressureRecordList.get(0).getBr_date())-1, Integer.parseInt(myBloodPressureRecordList.get(6).getBr_date())+1, 50, 200,
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
            "주간 혈압 차트");
    return intent;
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
}