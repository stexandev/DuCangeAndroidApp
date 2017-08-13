package de.stexan.ducangeandroidapp;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.util.AbstractSequentialList;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.ListIterator;


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
    //TODO wrap this with function querying table form for “norm” and return concatenation of multiple articles, see example RESPONSUM
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
    String accessLinks(String id) {
        Document doc = Jsoup.parse( accessEntry(id) );
        Elements links = doc.getElementsByTag("a");
        return links.toString();
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
            cursor = db.rawQuery("SELECT * FROM form WHERE norm LIKE \"" + input + "%\" LIMIT " + MAX_RETURNS, null);
        } catch (android.database.sqlite.SQLiteDatabaseCorruptException e) {
            cursor = null;
        }
        List<String[]> formRowList = new LinkedList<>();

        if (cursor.moveToFirst()) {
            do {
                String[] formRow = new String[5];
                formRow[0] = cursor.getString(0); //rend
                formRow[1] = cursor.getString(1); //text
                formRow[2] = cursor.getString(2); //norm
                formRow[3] = cursor.getString(3); //id
                formRow[4] = cursor.getString(4); //anchor
                formRowList.add(formRow);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();

        return formRowList;
    }
    String normalize(String form) {
        /* Normalisierung von Nutzereingaben
         * vgl. die Implementation in DuCange in Datei ./enc/lib/Ducange.php:
         * public static function norm($form) {...};
         */
        form = form.toLowerCase();
        String[][] replacements = { {"à", "a"}, {"á", "a"}, {"â", "a"}, {"ã", "a"}, {"ä", "a"}, {"å", "a"}, {"À", "A"}, {"Á", "A"}, {"Â", "A"}, {"Ã", "A"}, {"Ä", "A"}, {"Å", "A"}, {"aa", "a"}, {"æ" , "e"}, {"ae", "e"}, {"Æ" , "E"}, {"AE", "E"}, {"ai", "e"}, {"aia", "aia"}, {"aie", "aie"}, {"aio", "aio"}, {"aiu", "aiu"}, {"AI", "E"}, {"AU", "O"}, {"au", "o"}, {"ay", "e"}, {"aya", "aia"}, {"ayo", "aio"}, {"ayu", "aiu"}, {"AY", "E"}, {"bb", "b"}, {"ß", "ss"}, {"þ", "th"}, {"ça", "ssa"}, {"çe", "ce"}, {"chi", "chi"}, {"che", "che"}, {"çi", "ci"}, {"ço", "sso"}, {"çu", "ssu"}, {"cc", "c"}, {"cce", "xe"}, {"cci", "xi"}, {"dd", "d"}, {"Ð", "D"}, {"ð", "d"}, {"ee", "e"}, {"é" , "e"}, {"è" , "e"}, {"ê" , "e"}, {"ë" , "e"}, {"ę", "e"}, {"È", "E"}, {"É", "E"}, {"Ê", "E"}, {"Ë", "E"}, {"Ę" , "E"}, {"eia", "eia"}, {"eio", "eio"}, {"eiu", "eiu"}, {"ei" , "e"}, {"eya", "eya"}, {"eyo", "eyo"}, {"eyu", "eyu"}, {"ey" , "e"}, {"ff", "f"}, {"ﬀ", "f"}, {"ﬃ", "fi"}, {"ﬄ", "fl"}, {"ﬁ", "fi"}, {"ﬂ", "fl"}, {"ﬅ", "ft"}, {"gg", "g"}, {"h", ""}, {"Ì", "I"}, {"Í", "I"}, {"Î", "I"}, {"Ï", "I"}, {"ì", "i"}, {"í", "i"}, {"î", "i"}, {"ï", "i"}, {"ij", "i"}, {"ñ", "n"}, {"Ñ", "N"}, {"ò", "o"}, {"ó", "o"}, {"ô", "o"}, {"õ", "o"}, {"ö", "o"}, {"ø", "o"}, {"Ò", "O"}, {"Ó", "O"}, {"Ô", "O"}, {"Õ", "O"}, {"Ö", "O"}, {"Ø", "O"}, {"œ" , "e"}, {"Œ", "E"}, {"ph", "f"}, {"PH", "F"}, {"ﬆ", "st"}, {"ù", "u"}, {"ú", "u"}, {"û", "u"}, {"ü", "u"}, {"Ù", "U"}, {"Ú", "U"}, {"Û", "U"}, {"Ü", "U"}, {"v", "u"}, {"U", "U"}, {"y", "i"}, {"ÿ", "i"}, {"Y", "I"}, {"Ÿ", "I"}, {"0", ""}, {"1", ""}, {"2", ""}, {"3", ""}, {"4", ""}, {"5", ""}, {"6", ""}, {"7", ""}, {"8", ""}, {"9", ""}, {".", ""}};
        for(String[] replacement: replacements) {
            form = form.replace(replacement[0], replacement[1]);
        }
        return form;
    }


    // query a form and return a List of IDs
    List<String> queryForm(String form) {
        // Hier muss normalisiert werden, bevor Spalte „norm“ abgefragt wird, d.h. bb>b, ch>h, h>, uswusf.
        String norm = normalize(form);
        List<String[]> formRowList = queryFormRow(norm);
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
        return SQLiteDatabase.openDatabase(pathToDatabaseFile, null, SQLiteDatabase.OPEN_READONLY+SQLiteDatabase.NO_LOCALIZED_COLLATORS);
    }
}
