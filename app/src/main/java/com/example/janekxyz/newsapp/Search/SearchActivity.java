package com.example.janekxyz.newsapp.Search;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.janekxyz.newsapp.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class SearchActivity extends AppCompatActivity {

    private static final String LOG_TAG = SearchActivity.class.getSimpleName();

    private String defaultJsonQuery = null;

    private EditText title;
    private EditText category;
    private EditText date;

    private String tit;
    private String cat;
    private String dat;

    Calendar myCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        Intent intent = getIntent();
        defaultJsonQuery = intent.getStringExtra("query");


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        title = (EditText) findViewById(R.id.title);
        category = (EditText) findViewById(R.id.category);
        date = (EditText) findViewById(R.id.date);


        // date picker for searching
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
                new DatePickerDialog(SearchActivity.this,
                        dat,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = new Intent();
        switch (item.getItemId()) {
            case R.id.submit_search:
                /* when button is clicked default request should be restarted
                 because otherwise it will remember the last state of search*/
                defaultJsonQuery = "https://content.guardianapis.com/search?api-key=47a30750-4041-4cce-9e99-4e602fdeff81";

                setObject();
                /* put extra to intent to extract them from MainActivity and use
                 them to make query */
                intent.putExtra("title", tit);
                intent.putExtra("category", cat);
                intent.putExtra("date", dat);
                intent.putExtra("query", defaultJsonQuery);
                // I need to set result because intent was started with waiting for the results
                setResult(Activity.RESULT_OK, intent);
                finish();

                return true;
            case R.id.home:
                /* when button back is clicked I need to do nothing
                   so query should not change*/
                NavUtils.navigateUpFromSameTask(this);
                intent.putExtra("query", defaultJsonQuery);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /*
        Check if searching objects (title, category, date) was added
     */
    private void setObject(){
        if(title.getText().toString().isEmpty()) {
            tit = null;
        } else {
            tit = title.getText().toString();
        }

        if(category.getText().toString().isEmpty()){
            cat = null;
        } else {
            cat = category.getText().toString().toLowerCase();
        }

        if(date.getText().toString().isEmpty()){
            dat = null;
        } else {
            dat = changeDateOrder(date.getText().toString());
        }
    }

    // change date format from calendar to edit text
    // for example 20 Jan 2017 will be changed to 20-07-2017
    private void updateLabel() {
        String myFormat = "MM-dd-yyyy";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(myFormat, Locale.ENGLISH);

        date.setText(simpleDateFormat.format(myCalendar.getTime()));
    }


    // change date order from for example 05-20-2017 to 2017-05-20
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
