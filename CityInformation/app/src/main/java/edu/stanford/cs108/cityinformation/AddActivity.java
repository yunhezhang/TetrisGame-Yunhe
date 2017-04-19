package edu.stanford.cs108.cityinformation;

import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddActivity extends AppCompatActivity {

    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        db = openOrCreateDatabase("CityDB", MODE_PRIVATE, null);
    }

    public void onAdd(View view) {
        EditText city = (EditText) findViewById(R.id.addCity);
        EditText continent = (EditText) findViewById(R.id.addContinent);
        EditText popu = (EditText) findViewById(R.id.addPopulation);
        String[] addInfo = {city.getText().toString(), continent.getText().toString(),
                             popu.getText().toString()};
        String cityName = city.getText().toString();
        String addStr = "INSERT INTO cities VALUES (?, ?, ?, NULL)";
        db.execSQL(addStr, addInfo);
        city.setText("");
        continent.setText("");
        popu.setText("");
        Toast toast = Toast.makeText(getApplicationContext(),cityName+" Added",
                Toast.LENGTH_SHORT);
        toast.show();
    }
}
