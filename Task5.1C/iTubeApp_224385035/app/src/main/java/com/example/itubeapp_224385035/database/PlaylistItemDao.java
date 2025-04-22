package com.example.itubeapp_224385035.database;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PlaylistItemDao {
    @Insert
    long insert(PlaylistItem playlistItem);

    @Query("SELECT * FROM playlist_items WHERE userId = :userId")
    LiveData<List<PlaylistItem>> getPlaylistForUser(int userId);

    @Query("SELECT * FROM playlist_items WHERE userId = :userId")
    List<PlaylistItem> getPlaylistForUserSync(int userId);

    @Delete
    void delete(PlaylistItem playlistItem);
} 