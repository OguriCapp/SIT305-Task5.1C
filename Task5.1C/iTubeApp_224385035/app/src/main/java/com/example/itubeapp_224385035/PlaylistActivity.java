package com.example.itubeapp_224385035;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp_224385035.adapters.PlaylistAdapter;
import com.example.itubeapp_224385035.database.AppDatabase;
import com.example.itubeapp_224385035.database.PlaylistItem;
import com.example.itubeapp_224385035.utils.SessionManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlaylistActivity extends AppCompatActivity {
    private RecyclerView recyclerViewPlaylist;
    private PlaylistAdapter playlistAdapter;
    private SessionManager sessionManager;
    private ExecutorService executorService;
    private Button buttonLogout, buttonBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        // Initialize session manager and thread pool
        sessionManager = new SessionManager(this);
        executorService = Executors.newSingleThreadExecutor();

        // Initialize views
        recyclerViewPlaylist = findViewById(R.id.recyclerViewPlaylist);
        buttonLogout = findViewById(R.id.buttonLogout);
        buttonBack = findViewById(R.id.buttonBack);
        
        recyclerViewPlaylist.setLayoutManager(new LinearLayoutManager(this));

        // Initialize adapter
        playlistAdapter = new PlaylistAdapter(this, new ArrayList<>());
        recyclerViewPlaylist.setAdapter(playlistAdapter);

        // Load playlist data
        loadPlaylistData();
        
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

    private void loadPlaylistData() {
        int userId = sessionManager.getUserId();

        executorService.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase db = AppDatabase.getInstance(getApplicationContext());
                List<PlaylistItem> playlistItems = db.playlistItemDao().getPlaylistForUserSync(userId);

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        playlistAdapter.updatePlaylist(playlistItems);
                    }
                });
            }
        });
    }
    
    // Logout function
    private void logout() {
        // Clear session data
        sessionManager.logout();
        
        // Show toast message
        Toast.makeText(PlaylistActivity.this, "You have been logged out", Toast.LENGTH_SHORT).show();
        
        // Navigate to login screen
        Intent intent = new Intent(PlaylistActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
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