package de.stexan.ducangeandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    TextView textBox = (TextView)findViewById(R.id.textBox);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            DatabaseAccess db = new DatabaseAccess(this);
            textBox.setText(db.getDatabaseName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
