/*
 *  DuCange Android App – A Latin Glossary
    Copyright (C) 2017  Stefan Hynek

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package de.stexan.ducangeandroidapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.SearchView;

import java.util.List;
import java.util.ListIterator;


public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar appBar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(appBar);
        handleIntent(getIntent());




        /* TESTS
        //Eintrag nach id ausgeben
        String[] entry = db.accessEntryRow("A1");
        textBoxView.setText(entry[3]);
        //Tabellenzeile als Array of Arrays, mit (maximaler) Länge MAX_RETURNS
        String[][] form = db.entryList("ab");
        textBoxView.setText(form[0][2]);
        //alternative Funktion, die Tabellenzeilen als Liste von String-Arrays zurückgibt, Reihenfolge und Anzahl der Zeilen ist laufzeitabhängig!
        List<String[]> form = db.entryListNew("ab");
        textBoxView.setText(form.get(0)[2]);
        */
    }

    @Override
    protected void onStart() {
        super.onStart();
        /*
        DatabaseAccess db = new DatabaseAccess(this);

        if (! db.readable) {
            Intent intent = new Intent(this, DatabaseActivity.class);
            startActivity(intent);
            // TODO Change from activity to fragment?
            //DialogFragment newFragment = DatabaseFileFragment.newInstance();
            //newFragment.show(getFragmentManager(), "check db");
            //
        } else {
            ArticleView articleView = (ArticleView) findViewById(R.id.articleView);
            articleView.setWebViewClient(new ArticleViewClient());
            //articleView.getSettings().setJavaScriptEnabled(true);
            //articleView.addJavascriptInterface(new ArticleViewJavaScriptInterface(), "LinkGetter");

            String example = db.accessEntry("A1");
            //String links = db.accessLinks("A1");
            //System.out.println(links);
            articleView.loadArticle(example);
        }
        */
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        handleIntent(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        // Inflate menu to add items to action bar if it is present.
        inflater.inflate(R.menu.appbar, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

        return true;
    }
    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String form = intent.getStringExtra(SearchManager.QUERY);
            DatabaseAccess db = new DatabaseAccess(this);

            if (! db.readable) {
                Intent i = new Intent(this, DatabaseActivity.class);
                startActivity(i);

            } else {
                ArticleView aView = (ArticleView) findViewById(R.id.articleView);
                aView.setWebViewClient(new ArticleViewClient());
                List<String> ids = db.queryForm(form);
                StringBuilder articles = new StringBuilder();
                if ( ! ids.isEmpty()) {
                    ListIterator<String> it = ids.listIterator();
                    while ( it.hasNext() ) {
                        articles.append( db.accessEntry(it.next()) );
                    }
                aView.loadArticle(articles.toString());
                } else {
                    aView.loadData( getString( R.string.search_no_result ), "text/html", "UTF-8");
                }


            }
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()) {
            case R.id.action_settings:

                break;
            case R.id.action_imprint:
                Intent intent = new Intent(this, ImprintActivity.class);
                this.startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }
}
