package de.stexan.ducangeandroidapp;

import android.content.Context;
import android.content.res.AssetManager;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by stexandev on 31.03.17.
 */

public class DatabaseAccess extends SQLiteOpenHelper {
    private final Context appContext;
    public SQLiteDatabase db; //make private later on
    private static String db_name = "ducange.sqlite";
    private String db_path;
    final String pathToDatabaseFile;


    public DatabaseAccess (Context context) throws IOException {
        super(context, db_name, null, 1);
        this.appContext = context;

        db_path = appContext.getFilesDir().getPath();


        pathToDatabaseFile = db_path + "/" + db_name;

        if (checkIfCopied()) {
            /* already copied */
            db = SQLiteDatabase.openDatabase(pathToDatabaseFile, null, SQLiteDatabase.OPEN_READONLY);
        } else {
            /* not copied yet */
            copyDatabase();
        }
    }

    /* return dictionary entry text from id */
    public String accessEntryText(String id) {
        String entry = "";
        return entry;
    }

    /* return list of entries according to user input */
    public String[] entryList(String input) {
        String[] entries = new String[0];
        return entries;
    }

    /* unnötig?
    private void openDatabase() {

    }*/

    private void copyDatabase() throws IOException {
        InputStream myInput;
        OutputStream myOutput;

        /* check if Asset in place*/
        String[] listAssets = appContext.getAssets().list("");
        /* try to open file stored in assets as InputStream */
        myInput = appContext.getAssets().open(db_name);

        /* try to open local database file(handler) as OutputStream */
        myOutput = new FileOutputStream(pathToDatabaseFile);

        /* try to copy */
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = myInput.read(buffer)) > 0) {
            myOutput.write(buffer, 0, length);
        }

        /* close Streams */
        myOutput.flush();
        myOutput.close();
        myInput.close();
    }


    /* Check if already copied */
    private boolean checkIfCopied() {
        boolean alreadyCopied;

        File dbFile = new File(pathToDatabaseFile);
        alreadyCopied = dbFile.exists();

        return alreadyCopied;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
