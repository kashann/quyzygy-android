package com.victor.oprica.quyzygy20;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.victor.oprica.quyzygy20.entities.QuizResult;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    private QuizResult[] quizResults;
    private BarChart chart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_chart);

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            quizResults = null;
        } else {
            quizResults = (QuizResult[])extras.getSerializable("quizzes");
        }
        chart = findViewById(R.id.barChart);

        ArrayList<String> labels = new ArrayList<>();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for(int i = 0; i < quizResults.length; i++){
            labels.add(quizResults[i].QuizName);
            float grade = quizResults[i].Value;
            grade /= quizResults[i].Total;
            grade *= 100;
            entries.add(new BarEntry(grade, i));
        }
        BarDataSet barDataSet = new BarDataSet(entries, "Procentual Grades");
        BarData data = new BarData(labels, barDataSet);
        chart.setData(data);
        chart.setDescription("");
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        chart.animateY(2000);
    }
}