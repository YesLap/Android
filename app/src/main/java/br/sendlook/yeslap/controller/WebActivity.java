package br.sendlook.yeslap.controller;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import br.sendlook.yeslap.R;
import br.sendlook.yeslap.view.Utils;

public class WebActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private WebView webRecovey;
    private String URL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            URL = bundle.getString(Utils.URL);
        }

        toolbar = (Toolbar) findViewById(R.id.toolbarRecovery);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(getResources().getDrawable(R.drawable.ic_https_white_24dp));
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        webRecovey = (WebView) findViewById(R.id.webRecovery);
        webRecovey.getSettings().setJavaScriptEnabled(true);
        webRecovey.loadUrl(URL);

        webRecovey.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                getSupportActionBar().setTitle(view.getTitle());
                getSupportActionBar().setSubtitle(url);
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                getSupportActionBar().setTitle("Loading ...");
                getSupportActionBar().setSubtitle(null);
            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
