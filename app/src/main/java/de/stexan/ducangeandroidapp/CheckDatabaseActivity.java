package de.stexan.ducangeandroidapp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class CheckDatabaseActivity extends AppCompatActivity {
    private static final String copyActionName = String.valueOf(R.string.copy_action_name);
    private static final String dlActionName = String.valueOf(R.string.dl_action_name);
    private BroadcastReceiver br;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_database);

    }

    @Override
    public void onStart() {
        super.onStart();
        TextView checkDbText = (TextView) findViewById(R.id.checkDbText);
        ProgressBar progressBar = (ProgressBar) findViewById(R.id.checkDbProgess);

        DatabaseFile dbFile = new DatabaseFile(this);
        Intent i = new Intent(this, DatabaseFileService.class);
        if (dbFile.checkAssets()) {
            checkDbText.setText(R.string.copy_db_from_assets);
            i.setAction(copyActionName);
        } else {
            checkDbText.setText(R.string.dl_db_from_web);
            i.setAction(dlActionName);
        }
        progressBar.setVisibility(View.VISIBLE);


        br = new DatabaseFileServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(copyActionName);
        filter.addAction(dlActionName);
        this.registerReceiver(br, filter);

        startService(i);
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(br);
    }



    public void check(View v){
        DatabaseFile dbFile = new DatabaseFile(this);

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

    }
    public void returnToMain(View v){
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
