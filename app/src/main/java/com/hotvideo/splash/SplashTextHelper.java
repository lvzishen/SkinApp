package com.hotvideo.splash;

import android.content.Context;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.URLSpan;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by bf on 18-6-8.
 */
public class SplashTextHelper {
    private static final String TAG = "SplashTextHelper";

    public static CharSequence getHtmlString(Context context, String contentStr) {
        Spanned html = Html.fromHtml(contentStr);
        SpannableStringBuilder ssb = new SpannableStringBuilder(html);
        URLSpan[] urlSpans = html.getSpans(0, html.length(), URLSpan.class);
        if (urlSpans != null) {
            for (URLSpan urlSpan : urlSpans) {
                String url = urlSpan.getURL();
                ssb.removeSpan(urlSpan);
                ssb.setSpan(new SplashUrlSpan(context, url), html.getSpanStart(urlSpan), html.getSpanEnd(urlSpan), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        }
        return ssb;
    }

    public static CharSequence process(Context context, String source, String[] urls, View.OnClickListener[] clickListeners) {

        Spanned html = Html.fromHtml(source);
        source = html.toString();

        ArrayList<ClickSpanInfo> clickSpanInfos = new ArrayList<>();
        int startIndex;
        int endIndex = 0;
        int offset = 0;
        for (int i = 0, urlsLength = urls.length; i < urlsLength; i++) {
            String url = urls[i];
            startIndex = source.indexOf("[", endIndex);
            endIndex = source.indexOf("]", startIndex);
            if (startIndex > -1 && endIndex > startIndex) {
                View.OnClickListener clickListener = clickListeners != null ? clickListeners[i] : null;
                clickSpanInfos.add(new ClickSpanInfo(startIndex - offset, endIndex - offset - 1, url, clickListener));
            } else {
                break;
            }
            offset += 2;
        }

        source = source.replaceAll("[\\]|\\[]", "");

        SpannableStringBuilder ssb = new SpannableStringBuilder(source);
        for (ClickSpanInfo clickSpanInfo : clickSpanInfos) {

            ssb.setSpan(new SplashUrlSpan(context, clickSpanInfo.url, clickSpanInfo.clickListener), clickSpanInfo.startIndex, clickSpanInfo.endIndex, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        return ssb;
    }

    public static CharSequence process(Context context, String source, String... urls) {
        return process(context, source, urls, null);
    }

    public static CharSequence processWithQuote(Context context, String source, String... urls) {

        Spanned html = Html.fromHtml(source);
        source = html.toString();


        SpannableStringBuilder ssb = new SpannableStringBuilder(html);
        int startIndex;
        int endIndex = 0;
        if (urls != null) {
            for (String url : urls) {
                startIndex = source.indexOf("\"", endIndex);
                endIndex = source.indexOf("\"", startIndex + 1) + 1;
                if (startIndex > -1 && endIndex > startIndex) {
                    ssb.setSpan(new SplashUrlSpan(context, url, new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                        }
                    }), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    break;
                }
            }
        }
        return ssb;
    }

    public static CharSequence processWithQuote(Context context, String source, String[] urls, View.OnClickListener[] clickListeners) {

        Spanned html = Html.fromHtml(source);
        source = html.toString();

        SpannableStringBuilder ssb = new SpannableStringBuilder(html);
        int startIndex;
        int endIndex = 0;
        if (urls != null) {
            for (int i = 0; i < urls.length; i++) {
                startIndex = source.indexOf("\"", endIndex);
                endIndex = source.indexOf("\"", startIndex + 1) + 1;
                if (startIndex > -1 && endIndex > startIndex) {
                    ssb.setSpan(new SplashUrlSpan(context, urls[i], clickListeners[i]), startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
                } else {
                    break;
                }
            }
        }
        return ssb;
    }

    static class ClickSpanInfo {
        public int startIndex;
        public int endIndex;
        public String url;
        public View.OnClickListener clickListener;

        public ClickSpanInfo(int startIndex, int endIndex, String url, View.OnClickListener clickListener) {
            this.startIndex = startIndex;
            this.endIndex = endIndex;
            this.url = url;
            this.clickListener = clickListener;
        }

        @Override
        public String toString() {
            return "ClickSpanInfo{" +
                    "startIndex=" + startIndex +
                    ", endIndex=" + endIndex +
                    ", url='" + url + '\'' +
                    ", clickListener=" + clickListener +
                    '}';
        }
    }
}
