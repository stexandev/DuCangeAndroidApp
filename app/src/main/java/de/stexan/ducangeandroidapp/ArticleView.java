/*
 * DuCange Android App – A Latin Glossary
 * Copyright (C) 2017  Stefan Hynek
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

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
        StringBuilder html = new StringBuilder();
        //html.append("<?xml version=\"1.0\" encoding=\"UTF-8\" ?><!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">");
        html.append("<head><link rel=\"stylesheet\" type=\"text/css\" href=\"ducange.css\" /><link rel=\"stylesheet\" type=\"text/css\" href=\"article.css\" /></head>");
        html.append(article);
        //html.append("</body></html>");
        this.loadDataWithBaseURL("file:///android_asset/", html.toString(), "text/html", "UTF-8", null);
    }




}
