package edu.stanford.cs108.cityinformation;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class LookupActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lookup);
        db = openOrCreateDatabase("CityDB", MODE_PRIVATE, null);
    }

    public void onSearch(View view) {
        EditText city = (EditText) findViewById(R.id.city);
        EditText continent = (EditText) findViewById(R.id.continent);
        EditText population = (EditText) findViewById(R.id.population);
        RadioGroup radio = (RadioGroup) findViewById(R.id.choiceGroup);

        String cityString = city.getText().toString();
        String continentString = continent.getText().toString();
        cityString = "%" + cityString + "%";
        continentString = "%" + continentString + "%";
        String popuString = population.getText().toString();
        if(popuString.equals("")) {
            String[] queryParameters = {cityString, continentString};

            Cursor cursor = db.rawQuery("SELECT * FROM cities WHERE city LIKE ?" +
                    "AND continent LIKE ? ", queryParameters);
            String[] fromArray = {"city","continent","population"};
            int[] toArray = {R.id.custom_text_1, R.id.custom_text_2, R.id.custom_text_3};
            ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.custom_list_item_3,
                      cursor, fromArray, toArray, 0);
            ListView listView = (ListView) findViewById(R.id.loopUpView);
            listView.setAdapter(adapter);
        }
        else {
            int currentCheck = radio.getCheckedRadioButtonId();
            String[] queryParameters = {cityString, continentString, popuString};
            String[] fromArray = {"city","continent","population"};
            int[] toArray = {R.id.custom_text_1, R.id.custom_text_2, R.id.custom_text_3};
            Cursor cursor = null;
            switch (currentCheck) {
                case R.id.greater:
                    cursor = db.rawQuery("SELECT * FROM cities WHERE city LIKE ? AND continent" +
                            " LIKE ? AND population >= ?", queryParameters);
                    break;
                case R.id.less:
                    cursor = db.rawQuery("SELECT * FROM cities WHERE city LIKE ? AND continent" +
                            " LIKE ? AND population < ?", queryParameters);
            }
            ListAdapter adapter = new SimpleCursorAdapter(this, R.layout.custom_list_item_3,
                    cursor, fromArray, toArray, 0);
            ListView listView = (ListView) findViewById(R.id.loopUpView);
            listView.setAdapter(adapter);
        }
    }
}
