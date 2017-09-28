
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
