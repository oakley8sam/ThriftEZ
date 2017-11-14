package com.oakley8sam.thriftez;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.oakley8sam.thriftez.Model.Budget;
import com.oakley8sam.thriftez.Model.Expenditure;
import com.roomorama.caldroid.CaldroidFragment;

import java.util.Calendar;
import java.util.Date;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;

public class calendarActivity extends AppCompatActivity {
    private Integer max_date;
    private int heatmapColor = Color.RED;
    private Realm realm;
    Calendar calendar = Calendar.getInstance();

    CaldroidFragment heatmap = new CaldroidFragment();

    Bundle dateArgs = new Bundle();

    Date currentDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        realm = Realm.getDefaultInstance();

        //sets up the caldroid
        dateArgs.putInt(CaldroidFragment.MONTH, calendar.get(Calendar.MONTH) + 1);
        dateArgs.putInt(CaldroidFragment.YEAR, calendar.get(Calendar.YEAR));
        heatmap.setArguments(dateArgs);

        android.support.v4.app.FragmentTransaction trans =
                getSupportFragmentManager().beginTransaction();
        trans.replace(R.id.caldroidFrame, heatmap);
        trans.commit();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                RealmResults<Budget> budgets = realm.where(Budget.class).findAll();
                int numBudgets = budgets.size();
                for (int i = 0; i < numBudgets; i++){
                    Log.d("month/year", "WORKING ON BUDGET FOR: " + (budgets.get(i).getMonthCreated() + 1) + "/" + budgets.get(i).getYearCreated());
                    calendar.set(Calendar.MONTH, budgets.get(i).getMonthCreated());
                    calendar.set(Calendar.YEAR, budgets.get(i).getYearCreated());

                    RealmList<Expenditure> masterList = budgets.get(i).getExpenditureMasterList();
                    int masterListSize = masterList.size();

                    for(int j = 0; j < masterListSize; j++){
                        int dailySpending = 0;
                        calendar.set(Calendar.DATE, masterList.get(j).getDay());
                        dailySpending += masterList.get(j).getAmtSpent();
                        while(j != masterListSize -1 && masterList.get(j+1).getDay() == masterList.get(j).getDay() ){
                            dailySpending += masterList.get(j+1).getAmtSpent();
                            j++;
                        }
                        Log.d("Daily Spending", "DAILY SPENDING FOR " +calendar.get(Calendar.DATE) + "/" + calendar.get(Calendar.MONTH) + " IS: " + dailySpending);

                        currentDate = calendar.getTime();


                        ColorDrawable color = new ColorDrawable(heatmapColor);
                        float alphaScalar = dailySpending/budgets.get(i).getTotalMaxFunds();
                        color.setAlpha((int)(alphaScalar* 255));
                        Log.d("alpha", "ALPHA: "+color.getAlpha());
                        Log.d("dailyspending", "DailySpending: " +dailySpending);
                        Log.d("totalMaxFunds", "Total Max Funds: " +(int)budgets.get(i).getTotalMaxFunds());

                        heatmap.setBackgroundDrawableForDate(color, currentDate);

                    }
                }
            }
        });
    }
}
