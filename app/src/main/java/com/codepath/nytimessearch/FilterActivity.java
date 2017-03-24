package com.codepath.nytimessearch;

import android.app.DatePickerDialog;

import com.codepath.nytimessearch.DatePickerFragment;

import android.app.Dialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.R.attr.data;
import static android.R.attr.format;
import static com.codepath.nytimessearch.R.id.sortOrder;

public class FilterActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    public static final String ID_DATE = "10";
    public static final String ID_SORT_ORDER = "11";
    private DatePicker datePicker;
    private Calendar calendar;
    private TextView dateView;
    public Spinner sortOrder;
    public static int sortOrderIndex;
    public static int year;
    public static int month;
    public static int day;
    CheckBox cbArts, cbFashion, cbSports;
    public static boolean bArts, bFashion, bSports;
    // store the values selected into a Calendar instance
    static final Calendar c = Calendar.getInstance();
    SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String beginDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        sortOrder = (Spinner) findViewById(R.id.sortOrder);
        sortOrder.setSelection(sortOrderIndex);

        dateView = (TextView) findViewById(R.id.tvDate);
        calendar = Calendar.getInstance();

        if (year == 0 && month == 0 && day == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        cbArts = (CheckBox) findViewById(R.id.cbArts);
        cbFashion = (CheckBox) findViewById(R.id.cbFashion);
        cbSports = (CheckBox) findViewById(R.id.cbSports);

        cbArts.setChecked(bArts);
        cbFashion.setChecked(bFashion);
        cbSports.setChecked(bSports);

        showDate(year, month + 1, day);
    }

    private void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    // attach to an onclick handler to show the date picker
    public void showDatePickerDialog(View v) {
        DatePickerFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    // handle the date selected

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, monthOfYear);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        beginDate = format.format(c.getTime());
        showDate(year, monthOfYear + 1, dayOfMonth);
    }

    public void onSave(View view) {

        sortOrderIndex = sortOrder.getSelectedItemPosition();

        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);;

        bArts = cbArts.isChecked();
        bFashion= cbFashion.isChecked();
        bSports = cbSports.isChecked();

        Intent data = new Intent();

        data.putExtra(ID_DATE, format.toPattern());
        data.putExtra(ID_SORT_ORDER, sortOrder.getSelectedItem().toString());

        // Activity finished ok, return the data
        setResult(RESULT_OK, data); // set result code and bundle data for response
        finish();
    }
}
