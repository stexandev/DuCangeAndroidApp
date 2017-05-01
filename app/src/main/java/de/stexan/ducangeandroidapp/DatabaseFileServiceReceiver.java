package de.stexan.ducangeandroidapp;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by stefan on 21.04.17.
 */

public class DatabaseFileServiceReceiver extends BroadcastReceiver {
    private static final String copyCompleteActionName = String.valueOf(R.string.copy_complete_action_name);
    private static final String downloadCompleteActionName = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
    private static final String unzipCompleteActionName = String.valueOf(R.string.unzip_complete_action_name);


    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();

        if ( action.equals(copyCompleteActionName) ) {
            Toast.makeText(context, copyCompleteActionName, Toast.LENGTH_SHORT).show();
            System.out.println(copyCompleteActionName);
        } else if (action.equals(downloadCompleteActionName)) {
            Toast.makeText(context, downloadCompleteActionName, Toast.LENGTH_SHORT).show();
            System.out.println(downloadCompleteActionName);
        } else if (action.equals(unzipCompleteActionName)) {
            Toast.makeText(context, unzipCompleteActionName, Toast.LENGTH_SHORT).show();
            System.out.println(unzipCompleteActionName);
        }

        /*
        DatabaseAccess db = new DatabaseAccess(appContext);
        if (! db.readable) {
            Intent i = new Intent(appContext, MainActivity.class);
            appContext.startActivity(i);

        }
        */
    }
}
