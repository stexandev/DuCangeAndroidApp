package de.stexan.ducangeandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView textBoxView = (TextView)findViewById(R.id.textBox);

        try {
            DatabaseAccess db = new DatabaseAccess(this);
            textBoxView.setText(db.getDatabaseName());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
