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
package com.example.caretogether;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;


import com.example.caretogether.chart.week_bloodpressure;
import com.example.caretogether.chart.week_bloodsugar;
import com.example.caretogether.chart.week_bp_bs_weight;
import com.example.caretogether.chart.IDemoChart;
import com.example.caretogether.chart.week_exercise;
import com.example.caretogether.chart.week_food_exercise_weight;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Chart extends ListActivity {
  private IDemoChart[] mCharts = new IDemoChart[] { new week_bloodsugar(),
          new week_bloodpressure(),new week_exercise(),new week_bp_bs_weight(), new week_food_exercise_weight(),


       };

  private String[] mMenuText;

  private String[] mMenuSummary;

  String m_name;
  String m_email;
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) {



    super.onCreate(savedInstanceState);


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

    int length = mCharts.length;
    mMenuText = new String[length];
    mMenuSummary = new String[length];
    for (int i = 0; i < length; i++) {
      mMenuText[i] = mCharts[i].getName();
      mMenuSummary[i] = mCharts[i].getDesc();
    }
    setListAdapter(new SimpleAdapter(this, getListValues(), android.R.layout.simple_list_item_2,
        new String[] { IDemoChart.NAME, IDemoChart.DESC }, new int[] { android.R.id.text1,
            android.R.id.text2 }));
  }

  private List<Map<String, String>> getListValues() {
    List<Map<String, String>> values = new ArrayList<Map<String, String>>();
    int length = mMenuText.length;
    for (int i = 0; i < length; i++) {
      Map<String, String> v = new HashMap<String, String>();
      v.put(IDemoChart.NAME, mMenuText[i]);
      v.put(IDemoChart.DESC, mMenuSummary[i]);
      values.add(v);
    }
    return values;
  }

  @Override
  protected void onListItemClick(ListView l, View v, int position, long id) {
    super.onListItemClick(l, v, position, id);
    Intent intent = null;
   if (position <= mCharts.length ) {

      intent = mCharts[position].execute(this,m_email);
    }
    startActivity(intent);
  }
}