package de.stexan.ducangeandroidapp;

import android.app.IntentService;
import android.content.Intent;


/**
 * Created by stefan on 21.04.17.
 */

public class DatabaseFileService extends IntentService {
    private final String copyActionName = "de.stexan.ducangeandroidapp.COPY";
    //private final String dlActionName = getString(R.string.dl_action_name);
    //private final String unzipActionName = getString(R.string.unzip_action_name);
    private final String copyCompleteActionName = "de.stexan.ducangeandroidapp.COPY_COMPLETE";
    //private final String unzipCompleteActionName = getString(R.string.unzip_complete_action_name);


    public DatabaseFileService() {
        super("DatabaseFileService");
    }

    @Override
    protected void onHandleIntent(Intent i) {
        DatabaseFile dbFile = new DatabaseFile(this);

        if ( i.getAction().equals(copyActionName) ) {
            dbFile.copyDatabaseFromAssets();
            broadcastAction(copyCompleteActionName);
        //} else if ( i.getAction().equals(dlActionName) ) {
        //    dbFile.downloadDatabase();
        //} else if (i.getAction().equals(unzipActionName)) {
        //    dbFile.unzipDatabaseFromDownloads();
        //    broadcastAction(unzipCompleteActionName);
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
