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



    @Override
    public void onReceive(Context appContext, Intent intent) {
        String copyCompleteActionName = appContext.getString(R.string.copy_complete_action_name);
        //String downloadCompleteActionName = DownloadManager.ACTION_DOWNLOAD_COMPLETE;
        String unzipCompleteActionName = appContext.getString(R.string.unzip_complete_action_name);
        String action = intent.getAction();

        Toast.makeText(appContext, action, Toast.LENGTH_SHORT).show();
        System.out.println(action);

         if (action.equals(unzipCompleteActionName) || action.equals(copyCompleteActionName)) {
             Intent i = new Intent(appContext, MainActivity.class);
             appContext.startActivity(i);
        }



    }
}
