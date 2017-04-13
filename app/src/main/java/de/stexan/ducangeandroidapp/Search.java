package de.stexan.ducangeandroidapp;


import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;

import java.io.IOException;

/**
 * Created by stefan on 12.04.17.
 */

public class Search extends ListActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /* Ab hier kannst du loslegen :)
         *
         */






        // Get the intent, verify the action and get the query
        Intent intent = getIntent();
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            try {
                DatabaseAccess db = new DatabaseAccess(this);
                String[] result = db.accessEntry(query);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
