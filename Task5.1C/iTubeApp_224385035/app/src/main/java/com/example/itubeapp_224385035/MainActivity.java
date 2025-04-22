package com.example.itubeapp_224385035;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.itubeapp_224385035.database.AppDatabase;
import com.example.itubeapp_224385035.database.PlaylistItem;
import com.example.itubeapp_224385035.utils.SessionManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends AppCompatActivity {
    private EditText editTextYoutubeUrl;
    private Button buttonPlay, buttonAddToPlaylist, buttonMyPlaylist, buttonLogout;
    private SessionManager sessionManager;
    private ExecutorService executorService;
    
    // Default YouTube video URL for testing
    private static final String DEFAULT_YOUTUBE_URL = "https://www.youtube.com/watch?v=dQw4w9WgXcQ";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize session manager and thread pool
        sessionManager = new SessionManager(this);
        executorService = Executors.newSingleThreadExecutor();

        // Check if user is logged in, redirect to login page if not
        if (!sessionManager.isLoggedIn()) {
            navigateToLoginActivity();
            finish();
            return;
        }

        // Initialize view components
        editTextYoutubeUrl = findViewById(R.id.editTextYoutubeUrl);
        buttonPlay = findViewById(R.id.buttonPlay);
        buttonAddToPlaylist = findViewById(R.id.buttonAddToPlaylist);
        buttonMyPlaylist = findViewById(R.id.buttonMyPlaylist);
        buttonLogout = findViewById(R.id.buttonLogout);
        
        // Set default example URL
        editTextYoutubeUrl.setText(DEFAULT_YOUTUBE_URL);

        // Set play button click event
        buttonPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = editTextYoutubeUrl.getText().toString().trim();
                if (youtubeUrl.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidYoutubeUrl(youtubeUrl)) {
                    Toast.makeText(MainActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                playVideo(youtubeUrl);
            }
        });

        // Set add to playlist button click event
        buttonAddToPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String youtubeUrl = editTextYoutubeUrl.getText().toString().trim();
                if (youtubeUrl.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please enter YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!isValidYoutubeUrl(youtubeUrl)) {
                    Toast.makeText(MainActivity.this, "Invalid YouTube URL", Toast.LENGTH_SHORT).show();
                    return;
                }

                addToPlaylist(youtubeUrl);
            }
        });

        // Set my playlist button click event
        buttonMyPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPlaylist();
            }
        });

        // Set logout button click event
        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
    }

    private boolean isValidYoutubeUrl(String url) {
        // Simplified validation, just check if contains youtube.com or youtu.be
        return url.contains("youtube.com") || url.contains("youtu.be");
    }

    private void playVideo(String youtubeUrl) {
        Intent intent = new Intent(MainActivity.this, PlayerActivity.class);
        intent.putExtra("youtube_url", youtubeUrl);
        startActivity(intent);
    }

    private void addToPlaylist(String youtubeUrl) {
        int userId = sessionManager.getUserId();
        
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                PlaylistItem playlistItem = new PlaylistItem(userId, youtubeUrl);
                long result = db.playlistItemDao().insert(playlistItem);
                
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (result > 0) {
                            Toast.makeText(MainActivity.this, "Added to playlist", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(MainActivity.this, "Failed to add, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }

    private void openPlaylist() {
        Intent intent = new Intent(MainActivity.this, PlaylistActivity.class);
        startActivity(intent);
    }

    private void navigateToLoginActivity() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
    }

    // Logout function
    private void logout() {
        // Clear session data
        sessionManager.logout();
        
        // Show toast message
        Toast.makeText(MainActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
        
        // Navigate to login screen
        navigateToLoginActivity();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (executorService != null) {
            executorService.shutdown();
        }
    }
}