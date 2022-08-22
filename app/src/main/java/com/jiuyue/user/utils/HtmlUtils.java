package com.jiuyue.user.utils;

import android.text.Html;

public class HtmlUtils {
    public static CharSequence fromHtml(String content) {
        if (content != null && content.length() > 0) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                return Html.fromHtml(content, Html.FROM_HTML_MODE_LEGACY);
            } else {
                return Html.fromHtml(content);
            }
        }
        return "";
    }
}
