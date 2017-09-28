/*
 * DuCange Android App – A Latin Glossary
 * Copyright (C) 2017  Stefan Hynek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.stexan.ducangeandroidapp;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;

public class DatabaseActivity extends AppCompatActivity {
    //private final String copyActionName = getString(R.string.copy_action_name);
    private final String copyActionName = "de.stexan.ducangeandroidapp.COPY";
//    private final String dlActionName = getString(R.string.dl_action_name);
//    private final String unzipActionName = getString(R.string.unzip_action_name);
//    private final String copyCompleteActionName = getString(R.string.copy_complete_action_name);
    private final String copyCompleteActionName ="de.stexan.ducangeandroidapp.COPY_COMPLETE";
//    private final String unzipCompleteActionName = getString(R.string.unzip_complete_action_name);

    private BroadcastReceiver br;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

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
        }/* else {
            if (dbFile.checkIfDownloaded()) {
                checkDbText.setText(R.string.unzip_db);
                i.setAction(unzipActionName);
            } else {
                checkDbText.setText(R.string.dl_db_from_web);
                i.setAction(dlActionName);
            }

        }*/

        br = new DatabaseFileServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(copyCompleteActionName);
        //filter.addAction(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
        //filter.addAction(unzipCompleteActionName);
        this.registerReceiver(br, filter);

        startService(i);
    }


    @Override
    protected void onStop() {
        super.onStop();
        this.unregisterReceiver(br);
    }
}
