package de.stexan.ducangeandroidapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;

public class CheckDatabaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_database);

        TextView dbNameView = (TextView)findViewById(R.id.dbName);
        DatabaseFile dbFile = new DatabaseFile(this);
        dbNameView.setText(dbFile.getPathToDatabaseFile());


        //move out of onCreate; implement button to start check/copy/download

    }
    public void check(View v){
        DatabaseFile dbFile = new DatabaseFile(this);
        try {
            if (dbFile.checkAssets()) {
                dbFile.copyDatabaseFromAssets();
            } else {
                //check for downloaded file
                if (! dbFile.checkIfDownloaded()) {
                    dbFile.downloadDatabase();
                } else {
                    //unzip or copy, depending on impelementation of compressed virtual filesystem
                    dbFile.unzipDatabaseFromDownloads();
                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void returnToMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
