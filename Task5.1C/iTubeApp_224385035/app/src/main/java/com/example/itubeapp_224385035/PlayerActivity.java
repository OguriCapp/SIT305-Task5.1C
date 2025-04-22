package com.example.itubeapp_224385035;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.itubeapp_224385035.utils.SessionManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PlayerActivity extends AppCompatActivity {
    private WebView webView;
    private String youtubeUrl;
    private Button buttonLogout, buttonBack;
    private SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Initialize session manager
        sessionManager = new SessionManager(this);

        // Initialize WebView and buttons
        webView = findViewById(R.id.webView);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonBack = findViewById(R.id.buttonBack);
        
        setupWebView();

        // Get YouTube URL from intent
        youtubeUrl = getIntent().getStringExtra("youtube_url");
        
        if (youtubeUrl != null && !youtubeUrl.isEmpty()) {
            playVideo(youtubeUrl);
        } else {
            Toast.makeText(this, "Invalid URL", Toast.LENGTH_SHORT).show();
            finish();
        }
        
        // Set logout button click event
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        
        // Set back button click event
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Go back to previous screen
            }
        });
    }

    private void setupWebView() {
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setMediaPlaybackRequiresUserGesture(false);
        
        webView.setWebChromeClient(new WebChromeClient());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
    }

    private void playVideo(String url) {
        if (url.contains("youtube.com") || url.contains("youtu.be")) {
            webView.loadUrl(url);
        } else {
            Toast.makeText(this, "Not a valid YouTube URL", Toast.LENGTH_SHORT).show();
            finish();
        }
    }
    
    // Logout function
    private void logout() {
        // Clear session data
        sessionManager.logout();
        
        // Show toast message
        Toast.makeText(PlayerActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
        
        // Navigate to login screen
        Intent intent = new Intent(PlayerActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (webView != null) {
            webView.destroy();
        }
    }
    
    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
} 
 