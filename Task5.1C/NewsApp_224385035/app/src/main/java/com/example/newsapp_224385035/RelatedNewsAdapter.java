package com.example.newsapp_224385035;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.List;

public class RelatedNewsAdapter extends RecyclerView.Adapter<RelatedNewsAdapter.RelatedNewsViewHolder> {
    private List<News> relatedNewsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(News news);
    }

    public RelatedNewsAdapter(List<News> relatedNewsList, OnItemClickListener listener) {
        this.relatedNewsList = relatedNewsList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public RelatedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_related_news, parent, false);
        return new RelatedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsViewHolder holder, int position) {
        News news = relatedNewsList.get(position);
        holder.titleTextView.setText(news.getTitle());
        holder.dateTextView.setText(news.getPublishDate());
        
        Glide.with(holder.itemView.getContext())
                .load(news.getImageUrl())
                .centerCrop()
                .into(holder.imageView);

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(news);
            }
        });
    }

    @Override
    public int getItemCount() {
        return relatedNewsList.size();
    }

    static class RelatedNewsViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView dateTextView;

        public RelatedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.relatedNewsImage);
            titleTextView = itemView.findViewById(R.id.relatedNewsTitle);
            dateTextView = itemView.findViewById(R.id.relatedNewsDate);
        }
    }
} 