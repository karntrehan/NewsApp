package com.example.janekxyz.newsapp.Search;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.janekxyz.newsapp.News.NewsPageFragmentListener;
import com.example.janekxyz.newsapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment{


    private static final String LOG_TAG = SearchFragment.class.getSimpleName();

    private EditText date;

    Calendar myCalendar = Calendar.getInstance();

    static NewsPageFragmentListener newsPageFragmentListener;


    public SearchFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        Bundle bundle = getArguments();
        newsPageFragmentListener = (NewsPageFragmentListener) bundle.getSerializable("listener");

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_search, container, false);

        final EditText title = (EditText) view.findViewById(R.id.title);
        final EditText category = (EditText) view.findViewById(R.id.category);
        date = (EditText) view.findViewById(R.id.date);

        final DatePickerDialog.OnDateSetListener dat = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }
        };

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(getActivity(), dat, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        Button search = (Button) view.findViewById(R.id.search);
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String t, c, d;
                if(title.getText().toString().isEmpty()) {
                    t = null;
                } else {
                    t = title.getText().toString();
                }

                if(category.getText().toString().isEmpty()){
                    c = null;
                } else {
                    c = category.getText().toString();
                }

                if(date.getText().toString().isEmpty()){
                    d = null;
                } else {
                    d = changeDateOrder(date.getText().toString());
                }

                Search searchObject = new Search(t,c,d);
                ViewPager viewPager = (ViewPager) getActivity().findViewById(R.id.viewPager);

                newsPageFragmentListener.onSwitchToNews();

                viewPager.setCurrentItem(0);
            }
        });
        return view;
    }

    private void updateLabel() {
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        date.setText(simpleDateFormat.format(myCalendar.getTime()));
    }

    private String changeDateOrder(String firstDate){
        SimpleDateFormat in = new SimpleDateFormat("MM-dd-yyyy");
        SimpleDateFormat out = new SimpleDateFormat("yyyy-MM-dd");

        System.out.println("FIRST DATE: "+firstDate);

        String output = null;
        Date date = null;

        try {
            date = in.parse(firstDate);
            output = out.format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "Problem with parsing date");
        }

        System.out.println("OUTPUT: "+output);
        return output;
    }

}
