package edu.stanford.cs108.mobiledraw;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button updateB = (Button) findViewById(R.id.update);
        customView customView = (customView) findViewById(R.id.customView);
        updateB.setOnClickListener(customView);
    }

}
