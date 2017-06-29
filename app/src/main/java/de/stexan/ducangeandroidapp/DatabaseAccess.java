package de.stexan.ducangeandroidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;


/**
 * Created by stexandev on 31.03.17.
 * TBD:
 * -- store db-file compressed and access via vfs? view: https://commons.apache.org/proper/commons-vfs/filesystems.html#gzip_and_bzip2
 * -- implement online query?
 * -- performing a full-text search using FTS3, rather than a LIKE query
 * -- handle E/SQLiteLog: (11) database corruption at line 51841 of [00bb9c9ce4]
 * -- handle E/SQLiteLog: (11) database disk image is malformed
 */

class DatabaseAccess {
    private static final int MAX_RETURNS = 10;
    private final Context appContext;
    final String pathToDatabaseFile;
    DatabaseFile dbFile;
    boolean readable;

    // Constructor
    DatabaseAccess(Context context) {
        this.appContext = context;
        this.dbFile = new DatabaseFile(appContext);
        this.pathToDatabaseFile = dbFile.getPathToDatabaseFile();
        this.readable = dbFile.checkLocalDbFile();
    }


    /* return table row from table “entry” */
    //TODO wrap this with function quierying table form for “norm” and return concatenation of multiple articles, see example RESPONSUM
    String[] accessEntryRow(String id) {
        SQLiteDatabase db = openDb();
        Cursor cursor; //TODO prepare a function taking a query, returning the cursor and handling the exceptions
        try {
            cursor = db.rawQuery("SELECT * FROM entry WHERE id = \"" + id + "\"", null);
        } catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
            cursor = null;
        }

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
    String accessEntry(String id) {
        return accessEntryRow(id)[3];
    }
    

    /** DuCange-Online behaviour (JavaScript)
     * Behavior of the main input for suggestion
     * function qKey(input) {
     * // no modification of content, do nothing
     * if (input.last == input.value) return false;
     * o=document.getElementById('fulltext');
     * if (o && o.checked) return false;
     * // keep track of last value
     * input.last=input.value;
     * var iframe=document.getElementById('side');
     * if (!iframe) return true;
     * iframe.src='suggest.php?q='+input.value;
     * return true;
     * }
     * returns a list of <a href="id+anchor" class="sc">text</a>
     * where id, anchor and text are the names of the table columns in table "form"
     */
    /* return array of table rows from table “form”  */
    public String[][] entryList(String input) {
        SQLiteDatabase db = openDb();
        Cursor cursor;
        try {
            cursor = db.rawQuery("SELECT * FROM form WHERE norm LIKE \"" + input + "%\"", null);
        } catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
            cursor = null;
        }


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

    /* return list of table rows from table “form”  */
    List<String[]> queryFormRow(String input) {
        SQLiteDatabase db = openDb();
        Cursor cursor;
        try {
            cursor = db.rawQuery("SELECT * FROM form WHERE norm LIKE \"" + input + "%\"", null);
        } catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
            cursor = null;
        }
        List<String[]> formRowList = new LinkedList<>();
        String[] formRow = new String[5];

        if (cursor.moveToFirst()) {
            do {
                formRow[0] = cursor.getString(0); //rend
                formRow[1] = cursor.getString(1); //text
                formRow[2] = cursor.getString(2); //norm
                formRow[3] = cursor.getString(3); //id
                formRow[4] = cursor.getString(4); //anchor
                formRowList.add(formRow);
            } while (cursor.moveToNext());
        } else {
            formRowList = null;
        }

        cursor.close();
        db.close();

        return formRowList;
    }
    // query a form and return a List of IDs
    List<String> queryForm(String form) {
        /* Hier muss normalisiert werden, bevor Spalte „norm“ abgefragt wird, d.h. bb>b, ch>h, h>, uswusf.
         * vgl. die Implementation in DuCange in Datei ./enc/lib/lat.tr
         * Nutze String replace(CharSequence target, CharSequence replacement)
         */
        List<String[]> formRowList = queryFormRow(form);
        //List<String> ids = new LinkedList<>();
        //use set to avoid duplicate entries
        Set<String> ids = new LinkedHashSet<>();
        if ( ! formRowList.isEmpty()) {
            ListIterator<String[]> it = formRowList.listIterator();
            while ( it.hasNext() ) {
                ids.add(it.next()[3]);
            }
        }
        //create list from set
        return new LinkedList<String>(ids);
    }



    private SQLiteDatabase openDb() {
        return SQLiteDatabase.openDatabase(pathToDatabaseFile, null, SQLiteDatabase.OPEN_READONLY);
    }
}
