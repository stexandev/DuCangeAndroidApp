package de.stexan.ducangeandroidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


/**
 * Created by stexandev on 31.03.17.
 */

public class DatabaseAccess extends SQLiteOpenHelper {
    private final Context appContext;
    private static final String DB_NAME = "ducange.sqlite";
    private static final int MAX_RETURNS = 10;
    private String db_path;
    final String pathToDatabaseFile;


    public DatabaseAccess (Context context) throws IOException {
        super(context, DB_NAME, null, 1);
        this.appContext = context;

        db_path = appContext.getFilesDir().getPath();


        pathToDatabaseFile = db_path + "/" + DB_NAME;

        if (! checkIfCopied()) {
            copyDatabase();
        }
    }

    /* return table row from table “entry” */
    public String[] accessEntry(String id) {
        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery("SELECT * FROM entry WHERE id = \"" + id + "\"", null);

        String[] entryRow = new String[5];

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();
            entryRow[0] = cursor.getString(0); //id
            entryRow[1] = cursor.getString(1); //cb
            entryRow[2] = cursor.getString(2); //label
            entryRow[3] = cursor.getString(3); //body
            entryRow[4] = cursor.getString(4); //head
            cursor.close();
        } else {
            entryRow = null;
        }
        db.close();

        return entryRow;
    }

    /* return array of table rows from table “form”  */
    public String[][] entryList(String input) {
        SQLiteDatabase db = openDb();
        Cursor cursor = db.rawQuery("SELECT * FROM form WHERE norm LIKE \"" + input + "%\"", null);

        String[][] formRow = new String[MAX_RETURNS][5];
        int i=0;

        if (cursor.moveToFirst()) {
            do {
                cursor.moveToNext();
                formRow[i][0] = cursor.getString(0); //rend
                formRow[i][1] = cursor.getString(1); //text
                formRow[i][2] = cursor.getString(2); //norm
                formRow[i][3] = cursor.getString(3); //id
                formRow[i][4] = cursor.getString(4); //anchor
                i++;
            } while (cursor.moveToNext() && i < MAX_RETURNS);
        } else {
            formRow = null;
        }

        cursor.close();
        db.close();

        return formRow;
    }

    private SQLiteDatabase openDb() {
        return SQLiteDatabase.openDatabase(pathToDatabaseFile, null, SQLiteDatabase.OPEN_READONLY);
    }

    private void copyDatabase() throws IOException {
        InputStream myInput;
        OutputStream myOutput;

        /* check if Asset in place */
        //String[] listAssets = appContext.getAssets().list("");

        /* open file stored in assets as InputStream */
        myInput = appContext.getAssets().open(DB_NAME);

        /* open local database file(handler) as OutputStream */
        myOutput = new FileOutputStream(pathToDatabaseFile);

        /* copy */
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
