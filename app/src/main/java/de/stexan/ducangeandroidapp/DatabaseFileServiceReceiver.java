package de.stexan.ducangeandroidapp;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by stefan on 21.04.17.
 */

public class DatabaseFileServiceReceiver extends BroadcastReceiver {
    private static final String copyCompleteActionName = String.valueOf(R.string.copy_complete_action_name);
    private static final String dlCompleteActionName = String.valueOf(R.string.dl_complete_action_name);

    @Override
    public void onReceive(Context context, Intent intent) {
        StringBuilder sb = new StringBuilder();
        sb.append("Action: " + intent.getAction() + "\n");
        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
        String log = sb.toString();
        System.out.println(log);

        String action = intent.getAction();
        if ( action.equals(copyCompleteActionName) ) {
            Toast.makeText(context, copyCompleteActionName, Toast.LENGTH_SHORT).show();
        } else if (action.equals(dlCompleteActionName)) {
            Toast.makeText(context, dlCompleteActionName, Toast.LENGTH_SHORT).show();
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
