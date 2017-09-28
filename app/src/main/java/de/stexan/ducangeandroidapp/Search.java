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


import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;


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
                DatabaseAccess db = new DatabaseAccess(this);
                String[] result = db.accessEntryRow(query);
        }
    }
}
