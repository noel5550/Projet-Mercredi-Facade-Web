package com.example.fatouaicha.projectwebview;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {


    ProgressBar superProgressBar;
    ImageView superImageView;
    WebView superWebView;
    LinearLayout superLinearLayout;
    SwipeRefreshLayout superSwipeLayout;
    String myCurrentUrl;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        superSwipeLayout = findViewById(R.id.mySwipeLayout);
        superLinearLayout = findViewById(R.id.myLinearLayout);
        superProgressBar = findViewById(R.id.MyProgressBar);
        superImageView = findViewById(R.id.MyImageView);
        superWebView = findViewById(R.id.MyWebView);

        superProgressBar.setMax(100);

        superWebView.loadUrl("http://www.univ-nantes.fr/");
        superWebView.getSettings().setJavaScriptEnabled(true);
        superWebView.getSettings().setBuiltInZoomControls(true);
        superWebView.setWebViewClient(new WebViewClient(){


            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                superLinearLayout.setVisibility(View.VISIBLE);
                super.onPageStarted(view, url, favicon);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                superLinearLayout.setVisibility(View.GONE);
                superSwipeLayout.setRefreshing(false);
                super.onPageFinished(view, url);
                myCurrentUrl = url;
            }
        });

        superWebView.setWebChromeClient(new WebChromeClient(){


            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                superProgressBar.setProgress(newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                getSupportActionBar().setTitle(title);
            }

            @Override
            public void onReceivedIcon(WebView view, Bitmap icon) {
                super.onReceivedIcon(view, icon);
                superImageView.setImageBitmap(icon);
            }
        });


        superSwipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                superWebView.reload();
            }
        });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.super_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.menu_back:
                onBackPressed();
                break;

            case R.id.menu_forward:
                onForwardPressed();
                break;

            case R.id.menu_refresh:
                superWebView.reload();
                break;

            case R.id.menu_share:
               Intent shareIntent = new Intent(Intent.ACTION_SEND);
               shareIntent.setType("text/plain");
               shareIntent.putExtra(Intent.EXTRA_TEXT,myCurrentUrl);
               shareIntent.putExtra(Intent.EXTRA_SUBJECT, "COPIED URL");
               startActivity(Intent.createChooser(shareIntent, "Share url with friends"));


        }
        return super.onOptionsItemSelected(item);
    }

    private void onForwardPressed(){
        if (superWebView.canGoForward()){
            superWebView.goForward();
        } else {
            Toast.makeText(this, "Vous ne pouvez pas retourner plus loin", Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onBackPressed() {
        if (superWebView.canGoBack()){
            superWebView.goBack();
        }
        else {
            finish();
        }
    }
}
