package de.stexan.ducangeandroidapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class CheckDatabaseActivity extends AppCompatActivity {
    private static final String copyActionName = String.valueOf(R.string.copy_action_name);
    private static final String dlActionName = String.valueOf(R.string.dl_action_name);
    private static final String unzipActionName = String.valueOf(R.string.unzip_action_name);
    private static final String copyCompleteActionName = String.valueOf(R.string.copy_complete_action_name);
    private static final String unzipCompleteActionName = String.valueOf(R.string.unzip_complete_action_name);

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

        DatabaseAccess db = new DatabaseAccess(this);
        if ( db.readable) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }

        DatabaseFile dbFile = new DatabaseFile(this);
        Intent i = new Intent(this, DatabaseFileService.class);
        if (dbFile.checkAssets()) {
            checkDbText.setText(R.string.copy_db_from_assets);
            i.setAction(copyActionName);
        } else {
            if (dbFile.checkIfDownloaded()) {
                checkDbText.setText(R.string.unzip_db);
                i.setAction(unzipActionName);
            } else {
                checkDbText.setText(R.string.dl_db_from_web);
                i.setAction(dlActionName);
            }

        }

        br = new DatabaseFileServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(copyCompleteActionName);
        filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        filter.addAction(unzipCompleteActionName);
        this.registerReceiver(br, filter);

        startService(i);
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(br);
    }
}
