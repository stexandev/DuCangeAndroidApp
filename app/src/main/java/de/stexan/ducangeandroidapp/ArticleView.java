package de.stexan.ducangeandroidapp;

import android.content.Context;
import android.util.AttributeSet;
import android.webkit.WebView;

/**
 * Created by stefan on 07.05.17.
 */

public class ArticleView extends WebView {
    //Constructors initialize super-class to serve compatibility with WebView
    public ArticleView(Context context) {
        super(context);
    }
    public ArticleView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }
    public ArticleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void loadArticle(String article) {
        /* TODO:
        * add header (CSS etc.),
        * remove images (<a xmlns="" ...>...</a>
        * remove copyright (<div class="rights")
        * handle links
        */
        this.loadDataWithBaseURL("file://android_asset/", article, "text/html", "UTF-8", null);
    }


}
