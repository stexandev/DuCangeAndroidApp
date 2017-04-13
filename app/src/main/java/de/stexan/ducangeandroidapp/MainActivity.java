package de.stexan.ducangeandroidapp;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(myToolbar);
        TextView textBoxView = (TextView)findViewById(R.id.textBox);

        try {
            DatabaseAccess db = new DatabaseAccess(this);
            /* TESTS
            //Datenbankname ausgeben
            textBoxView.setText(db.getDatabaseName());
            //Eintrag nach id ausgeben
            String[] entry = db.accessEntry("A1");
            textBoxView.setText(entry[3]);
            //Tabellenzeile als Array of Arrays, mit (maximaler) Länge MAX_RETURNS
            String[][] form = db.entryList("ab");
            textBoxView.setText(form[0][2]);
            */
            //alternative Funktion, die Tabellenzeilen als Liste von String-Arrays zurückgibt, Reihenfolge und Anzahl der Zeilen ist laufzeitabhängig!
            List<String[]> form = db.entryListNew("ab");
            textBoxView.setText(form.get(0)[2]);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
}
