package de.stexan.ducangeandroidapp;

import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.List;
import java.util.ListIterator;


/**
 * Created by stefan on 20.06.17.
 */

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
        /*
         * example url: „file:///android_asset/ABATIS“ or „file:///android_asset/RESPONSUM“ (while article with id „RESPONSUM“ does not exist but is RESPONSUM1, 2, 3 and 4)
         * now: parse String and load new url into view
         */
        String form = url.replace("file:///android_asset/", "");
        DatabaseAccess db = new DatabaseAccess(view.getContext());
        List<String> ids = db.queryForm(form);
        StringBuilder articles = new StringBuilder();
        if ( ! ids.isEmpty()) {
            ListIterator<String> it = ids.listIterator();
            while ( it.hasNext() ) {
                articles.append( db.accessEntry(it.next()) );
            }
        }

        ArticleView aView = (ArticleView) view;
        if ( ! articles.toString().isEmpty()) {
            aView.loadArticle(articles.toString());
            return true;
        } else return false;

    }
}
