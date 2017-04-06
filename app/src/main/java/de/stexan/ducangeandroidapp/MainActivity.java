package de.stexan.ducangeandroidapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
}
