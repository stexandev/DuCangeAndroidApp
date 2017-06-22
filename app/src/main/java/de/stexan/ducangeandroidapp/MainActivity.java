package de.stexan.ducangeandroidapp;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.SearchView;
import android.widget.TextView;



public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Toolbar theToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(theToolbar);




        /* TESTS
        //Eintrag nach id ausgeben
        String[] entry = db.accessEntry("A1");
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
        DatabaseAccess db = new DatabaseAccess(this);

        if (! db.readable) {
            Intent intent = new Intent(this, CheckDatabaseActivity.class);
            startActivity(intent);
            /* TODO Change from activity to fragment?
            DialogFragment newFragment = DatabaseFileFragment.newInstance();
            newFragment.show(getFragmentManager(), "check db");
            */
        } else {
            ArticleView articleView = (ArticleView) findViewById(R.id.articleView);
            articleView.setWebViewClient(new ArticleViewClient());
            String[] example = db.accessEntry("A1");
            articleView.loadArticle(example[3]);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
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

        /* Habe deinen Teil an die Toolbar angeschlossen. TextChange und TextSubmit klappen.
         * Wäre besser, wenn du diesen Teil in die Seach.java auslagerst und den SearchManager benutzt,
         * damit die Ergebnisse nicht in einer extra Liste ausgegeben werden müssen, sondern in den
         * Suchvorschlägen des SearchWidgets. Das Grundgerüst habe ich schon vorgegeben.
         */
        searchView.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {

                        System.out.println("textSubmit");

                        ( findViewById (R.id.action_search)).clearFocus();

                        (( TextView) findViewById( R.id.searchViewResult ) ).setText("sdfsdf");

                        return true;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        System.out.println("textChange");
                        return true;
                    }

                    public void search(String query) {
                        // reset loader, swap cursor, etc.
                    }

                }
        );

        return true;
    }
}
