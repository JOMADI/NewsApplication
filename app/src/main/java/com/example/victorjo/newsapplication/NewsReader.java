package com.example.victorjo.newsapplication;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import java.util.Objects;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class NewsReader extends AppCompatActivity {

    private static final String TAG = "NewsReader";
    private WebView webView;
    private ProgressBar progressBar;
    private static Uri mUri;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.news_reader);
        webView = findViewById(R.id.web_view);
        progressBar = findViewById(R.id.progress_bar);

        progressBar.setMax(100);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    progressBar.setVisibility(View.GONE);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    progressBar.setProgress(newProgress);
                }
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                Objects.requireNonNull(getActionBar()).setSubtitle(mUri.getPath());
            }
        });
        webView.setWebViewClient(new WebViewClient());
        if (mUri.getScheme().equals("https") || mUri.getScheme().equals("http"))
            webView.loadUrl(mUri.toString());
        else
            openLink(mUri);

        webView.setOnKeyListener((view1, i, keyEvent) -> {
            if (i == KeyEvent.KEYCODE_BACK && keyEvent.getAction() == MotionEvent.ACTION_UP && webView.canGoBack()) {
                webView.goBack();
                return true;
            }
            return false;
        });

    }

    private void openLink(Uri mUri) {
        Intent intent = new Intent(Intent.ACTION_VIEW, mUri);
        startActivity(intent);
    }

    public static Intent newIntent(Context context, Uri uri) {
        mUri = uri;
        Intent i = new Intent(context, NewsReader.class);
        i.setData(uri);
        return i;
    }

}
