package com.codepath.nytimessearch;


import android.graphics.Point;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class FilterDialogFragment extends DialogFragment {

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
    public static SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
    public static String beginDate;

    public FragmentManager fm;
    public DatePickerFragment newFragment;

    public FilterDialogFragment() {
        // Empty constructor is required for DialogFragment
        // Make sure not to add arguments to the constructor
        // Use `newInstance` instead as shown below
    }

    public static FilterDialogFragment newInstance(String title) {
        FilterDialogFragment frag = new FilterDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //return inflater.inflate(R.layout.fragment_filter, container);
        return getActivity().getLayoutInflater().inflate(R.layout.fragment_filter, container);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        sortOrder = (Spinner) view.findViewById(R.id.sortOrder);
        sortOrder.setSelection(sortOrderIndex);

        dateView = (TextView) view.findViewById(R.id.tvDate);
        calendar = Calendar.getInstance();

        if (year == 0 && month == 0 && day == 0) {
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        }

        cbArts = (CheckBox) view.findViewById(R.id.cbArts);
        cbFashion = (CheckBox) view.findViewById(R.id.cbFashion);
        cbSports = (CheckBox) view.findViewById(R.id.cbSports);

        cbArts.setChecked(bArts);
        cbFashion.setChecked(bFashion);
        cbSports.setChecked(bSports);

        showDate(year, month + 1, day);

        newFragment = new DatePickerFragment();
        fm = getFragmentManager();

        showDatePickerDialog();
    }

    public void onResume() {
        // Store access variables for window and blank point
        Window window = getDialog().getWindow();
        Point size = new Point();
        // Store dimensions of the screen in `size`
        Display display = window.getWindowManager().getDefaultDisplay();
        display.getSize(size);
        // Set the width of the dialog proportional to 100% of the screen width
        window.setLayout((int) (size.x), WindowManager.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        // Call super onResume after sizing
        super.onResume();
    }

    public void showDate(int year, int month, int day) {
        dateView.setText(new StringBuilder().append(day).append("/")
                .append(month).append("/").append(year));
    }


    // Attach to an onclick handler to show the date picker
    public void showDatePickerDialog() {

        dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // SETS the target fragment for use later when sending results
                newFragment.setTargetFragment(FilterDialogFragment.this, 300);
                newFragment.show(fm, "datePicker");
            }
        });
    }

    public void onSave(View view) {

        sortOrderIndex = sortOrder.getSelectedItemPosition();

        this.year = c.get(Calendar.YEAR);
        this.month = c.get(Calendar.MONTH);
        this.day = c.get(Calendar.DAY_OF_MONTH);;

        bArts = cbArts.isChecked();
        bFashion= cbFashion.isChecked();
        bSports = cbSports.isChecked();

        // Close the dialog and return back to the parent activity
        dismiss();
    }
}
