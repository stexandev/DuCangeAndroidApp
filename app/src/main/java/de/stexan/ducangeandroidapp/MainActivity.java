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
            /* TESTS
            textBoxView.setText(db.getDatabaseName());
            String[] entry = db.accessEntry("A1");
            textBoxView.setText(entry[3]);
            */
            String[][] form = db.entryList("ab");
            textBoxView.setText(form[0][2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
