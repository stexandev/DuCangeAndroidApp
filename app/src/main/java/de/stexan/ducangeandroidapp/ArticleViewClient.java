package de.stexan.ducangeandroidapp;

import android.provider.Settings;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;
import java.util.ListIterator;



class ArticleViewClient extends WebViewClient {
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
        /* this is for API 24 or higher
         * parse the request and load new page
         * links look like „A1#A1-10“ or „ABHORRICATIO“ or „ABANDUM#ABANDUM-7“
         * that is „id“ or „id#anchor“, where „anchor“ is „id-n“, where „n“ is a number
         */

        return super.shouldOverrideUrlLoading(view, request);
    }



    /**
     * @param view
     * @param url
     */
    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        //get the html to extract class for latin or french article
        /*
         * example url: „file:///android_asset/ABATIS“ or „file:///android_asset/RESPONSUM“ (while article with id „RESPONSUM“ does not exist but is RESPONSUM1, 2, 3 and 4)
         * now: parse String and load new url into view
         * comment: this is only for browsing inside the webview
         * TODO make this a DatabaseAccess function?
         */
        String form = url.replace("file:///android_asset/", "");
        DatabaseAccess db = new DatabaseAccess(view.getContext());
        ArticleView aView = (ArticleView) view;
        //String id=aView.getID(); //ID of the previously and still loaded article, in which the link has been clicked
        //String links=db.accessLinks(id);

        //could also be PATER#PATER-24 (in PATRILOQUIUM)
        String anchor = "";
        if (form.contains("#")) {
            anchor = form.substring(form.indexOf("#"));
            form = form.substring(0, form.indexOf("#"));
        }
        String norm = normalizeID(form);

        List<String> ids = db.queryForm(norm);
        StringBuilder articles = new StringBuilder();
        if ( ! ids.isEmpty()) {
            ListIterator<String> it = ids.listIterator();
            while ( it.hasNext() ) {
                articles.append( db.accessEntry(it.next()) );
            }
        }

        if ( ! articles.toString().isEmpty()) {
            aView.loadArticle(articles.toString());
            return true;
        } else return false;


    }
    @Override
    public void onPageFinished(WebView view, String url) {
        String form = url.replace("file:///android_asset/", "");
        String anchor = "";
        if (form.contains("#")) { //TODO override loadUrl in ArticleView and handle this? (let loadUrl call loadArticle?)
            view.loadUrl(form.substring(form.indexOf("#")));
        }
        System.out.println(anchor);
    }
    String normalizeID(String form) {
        /* Normalisierung von IDs
         * vgl. die Implementation in DuCange in Datei ./enc/lib/Ducange.php:
         * static $fro_id=array(...);
         * static $lat_id=array(...);
         * assuming the authors of the original code dropped the differentiation of "fro_id" and "lat_id"
         * I wonder why the replacement pattern originally contents small letters although its made uppercase before
         */
        form = form.toUpperCase();
        String[][] replacements = {  {"ç", "c"}, {"Ç", "C"}, {"œ", "oe"}, {"Œ", "OE"}, {"æ", "ae"}, {"Æ", "AE"}, {"é", "e"}, {"É", "E"}, {"è", "e"}, {"È", "E"}, {"ê", "e"}, {"Ê", "E"}, {"ë", "e"}, {"Ë", "E"}, {"ü", "u"}, {"Ü", "U"}, {"û", "u"}, {"Û", "U"}, {" 1", "1"}, {" 2", "2"}, {" 3", "3"}, {" 4", "4"}, {" 5", "5"}, {" 6", "6"}, {" 7", "7"}, {" 8", "8"}, {" 9", "9"}, {" 0", "0"}, {".", ""}, {" ", "-"}};
        for(String[] replacement: replacements) {
            form = form.replace(replacement[0], replacement[1]);
        }
        form = form.replaceFirst("^J", "I");
        form = form.replaceFirst("^[UÜÛ]", "U");


        return form;
    }
}
