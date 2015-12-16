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
public class week_exercise extends AbstractDemoChart {
  List<ParseObject> e_list;

  String m_email;
  String m_name;
  int m_weight;

  ArrayList<MyExerciseList> myExerciseList;

  /**
   * Returns the chart name.
   * 
   * @return the chart name
   */
  public String getName() {
    return "주간 운동량 그래프";
  }

  /**
   * Returns the chart description.
   * 
   * @return the chart description
   */
  public String getDesc() {
    return "주간 운동량 그래프입니다.";
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


    ParseQuery<ParseObject> query3 = ParseQuery.getQuery("exercise_record");
    query3.whereEqualTo("m_email", email);
    try {
      e_list = query3.find();
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



    String[] titles = new String[] { "운동량" };
    List<double[]> x = new ArrayList<double[]>();
    for (int i = 0; i < titles.length; i++) {
      x.add(new double[] {  Integer.parseInt(myExerciseList.get(0).getEr_date()),Integer.parseInt(myExerciseList.get(1).getEr_date()),Integer.parseInt(myExerciseList.get(2).getEr_date()),
              Integer.parseInt(myExerciseList.get(3).getEr_date()),Integer.parseInt(myExerciseList.get(4).getEr_date()),Integer.parseInt(myExerciseList.get(5).getEr_date()),
              Integer.parseInt(myExerciseList.get(6).getEr_date()) });
    }
    List<double[]> values = new ArrayList<double[]>();
    values.add(new double[] { myExerciseList.get(0).getEr_kal(), myExerciseList.get(1).getEr_kal(),myExerciseList.get(2).getEr_kal(),myExerciseList.get(3).getEr_kal(),
            myExerciseList.get(4).getEr_kal(), myExerciseList.get(5).getEr_kal(), myExerciseList.get(6).getEr_kal()});

    int[] colors = new int[] { Color.GREEN };
    PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE };
    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
    int length = renderer.getSeriesRendererCount();
    for (int i = 0; i < length; i++) {
      ((XYSeriesRenderer) renderer.getSeriesRendererAt(i)).setFillPoints(true);
    }
    setChartSettings(renderer, "주간 운동량 그래프", "날짜", "칼로리",
            Integer.parseInt(myExerciseList.get(0).getEr_date())-1, Integer.parseInt(myExerciseList.get(6).getEr_date())+1, 0, 1000,
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
}
