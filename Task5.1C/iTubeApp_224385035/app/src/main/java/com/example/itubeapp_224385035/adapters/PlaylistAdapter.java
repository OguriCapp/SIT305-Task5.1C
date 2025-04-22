package com.example.itubeapp_224385035.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.itubeapp_224385035.PlayerActivity;
import com.example.itubeapp_224385035.R;
import com.example.itubeapp_224385035.database.PlaylistItem;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.PlaylistViewHolder> {
    private List<PlaylistItem> playlistItems;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onDeleteClick(PlaylistItem playlistItem);
    }

    public PlaylistAdapter(Context context, List<PlaylistItem> playlistItems) {
        this.context = context;
        this.playlistItems = playlistItems;
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public void updatePlaylist(List<PlaylistItem> newPlaylistItems) {
        this.playlistItems = newPlaylistItems;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        PlaylistItem playlistItem = playlistItems.get(position);
        holder.textViewVideoUrl.setText(playlistItem.getYoutubeUrl());

        holder.buttonPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = playlistItem.getYoutubeUrl();
                // Ensure URL is a YouTube URL before trying to play
                if (url.contains("youtube.com") || url.contains("youtu.be")) {
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("youtube_url", url);
                    context.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return playlistItems == null ? 0 : playlistItems.size();
    }

    public class PlaylistViewHolder extends RecyclerView.ViewHolder {
        TextView textViewVideoUrl;
        ImageButton buttonPlayVideo;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewVideoUrl = itemView.findViewById(R.id.textViewVideoUrl);
            buttonPlayVideo = itemView.findViewById(R.id.buttonPlayVideo);
        }
    }
} 