package de.stexan.ducangeandroidapp;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.zip.GZIPInputStream;

import static android.content.Context.DOWNLOAD_SERVICE;
import static android.os.Environment.DIRECTORY_DOWNLOADS;

/**
 * Created by stefan on 15.04.17.
 */

public class DatabaseFile  {
    private static final String ASSET_FILE_NAME = "ducange.sqlite";
    private static final String DL_FILE_NAME = "ducange.sqlite.gz";
    private static final String DEST_FILE_NAME = "ducange.db";
    private static final String DOWNLOADSOURCE_ONE = "https://github.com/stexandev/DuCangeAndroidApp/raw/master/app/src/main/assets/ducange.sqlite.gz";
    private static final String DB_FILE_MD5 = "8527a762266695efc573246be44a9e0e";
    private static final String DB_FILE_GZ_MD5 = "dc3196077ebc61e37d46f513b81998db";
    private final Context appContext;
    private final String pathToDatabaseFile;

    public DatabaseFile (Context context) {
        this.appContext = context;
        this.pathToDatabaseFile = appContext.getFilesDir().getPath() + "/" + DEST_FILE_NAME;
    }


    public void downloadDatabase() {
        //implement file download
        DownloadManager dm = (DownloadManager) appContext.getSystemService(DOWNLOAD_SERVICE);
            Uri sourceUri = Uri.parse(DOWNLOADSOURCE_ONE);
            Uri destUri = Uri.parse(String.valueOf(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toURI()) + DL_FILE_NAME);

        DownloadManager.Request request = new DownloadManager.Request(sourceUri);
        request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE);
        request.setAllowedOverRoaming(false);
        request.setTitle("@string/db_download_title");
        request.setDescription("@string/db_download_description");
        request.setDestinationUri(destUri);

        dm.enqueue(request);
    }

    public boolean checkAssets() throws IOException {
        return Arrays.asList(appContext.getAssets().list("")).contains(ASSET_FILE_NAME);
    }

    public String getPathToDatabaseFile() {
        return pathToDatabaseFile;
    }


    public void unzipDatabaseFromDownloads() throws IOException {
        /* open downloaded gzip-file stored in download folder as InputStream */
        GZIPInputStream is = new GZIPInputStream( new FileInputStream(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString() + DL_FILE_NAME));
        /* open local database file(handler) as OutputStream */
        OutputStream os = new FileOutputStream(pathToDatabaseFile);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //decorate inputStream is to calculate md5 sum
        DigestInputStream dis = new DigestInputStream(is, md);

        /* copy */
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = dis.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        //chekc md5sum
        checkMD5(md, DB_FILE_MD5);


        /* close Streams */
        os.flush();
        os.close();
        is.close();
        dis.close();

    }

    public void copyDatabaseFromAssets() throws IOException {
        /* open file stored in assets as InputStream */
        InputStream is = appContext.getAssets().open(ASSET_FILE_NAME);
        /* open local database file(handler) as OutputStream */
        OutputStream os = new FileOutputStream(pathToDatabaseFile);

        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        //decorate inputStream is to calculate md5 sum
        DigestInputStream dis = new DigestInputStream(is, md);

        /* copy */
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = dis.read(buffer)) > 0) {
            os.write(buffer, 0, length);
        }

        //chekc md5sum
        checkMD5(md, DB_FILE_MD5);


        /* close Streams */
        os.flush();
        os.close();
        is.close();
        dis.close();
    }

    private boolean checkMD5(MessageDigest md, String dbFileMd5) {
        byte[] md5sum = md.digest();
        BigInteger bigInt = new BigInteger(1, md5sum);
        String output = bigInt.toString(16);
        output = String.format("%32s", output).replace(' ', '0');
        return output.equals(dbFileMd5);
    }


    /* Check if already copied */
    public boolean checkLocalDbFile() {
        boolean alreadyCopied;
        boolean notCorrupted = true; //mock-up... implemet HASH check

        File dbFile = new File(pathToDatabaseFile);
        alreadyCopied = dbFile.exists();

        return (alreadyCopied & notCorrupted);
    }

    /* Check if already downloaded */
    public boolean checkIfDownloaded() {
        boolean alreadyDownloaded;
        boolean notCorrupted = true; //mock-up... implemet HASH check

        File dbGzFile = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOWNLOADS).toString() + DL_FILE_NAME);
        alreadyDownloaded = dbGzFile.exists();

        return (alreadyDownloaded & notCorrupted);
    }

}
