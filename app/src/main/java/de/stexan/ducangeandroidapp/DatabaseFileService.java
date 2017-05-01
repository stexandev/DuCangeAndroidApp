package de.stexan.ducangeandroidapp;

import android.app.IntentService;
import android.content.Intent;






/**
 * Created by stefan on 21.04.17.
 */

public class DatabaseFileService extends IntentService {
    private static final String copyActionName = String.valueOf(R.string.copy_action_name);
    private static final String dlActionName = String.valueOf(R.string.dl_action_name);
    private static final String unzipActionName = String.valueOf(R.string.unzip_action_name);
    private static final String copyCompleteActionName = String.valueOf(R.string.copy_complete_action_name);
    private static final String unzipCompleteActionName = String.valueOf(R.string.unzip_complete_action_name);


    public DatabaseFileService() {
        super("DatabaseFileService");
    }

    @Override
    protected void onHandleIntent(Intent i) {
        DatabaseFile dbFile = new DatabaseFile(this);

        if ( i.getAction().equals(copyActionName) ) {
            dbFile.copyDatabaseFromAssets();
            broadcastAction(copyCompleteActionName);
        } else if ( i.getAction().equals(dlActionName) ) {
            dbFile.downloadDatabase();
        } else if (i.getAction().equals(unzipActionName)) {
            dbFile.unzipDatabaseFromDownloads();
            broadcastAction(unzipCompleteActionName);
        } else {
            System.out.println(i.getAction());
        }
    }

    private void broadcastAction(String action) {
        Intent i = new Intent();
        i.setAction(action);
        sendBroadcast(i);
    }



}
