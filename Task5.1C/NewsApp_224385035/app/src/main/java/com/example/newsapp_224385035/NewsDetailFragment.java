package com.example.newsapp_224385035;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class NewsDetailFragment extends Fragment {
    private static final String ARG_TITLE = "title";
    private static final String ARG_CONTENT = "content";
    private static final String ARG_IMAGE_URL = "image_url";
    private static final String ARG_NEWS_URL = "news_url";

    private String title;
    private String content;
    private String imageUrl;
    private String newsUrl;
    private List<News> relatedNews;

    public static NewsDetailFragment newInstance(String title, String content, String imageUrl, String newsUrl) {
        NewsDetailFragment fragment = new NewsDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_TITLE, title);
        args.putString(ARG_CONTENT, content);
        args.putString(ARG_IMAGE_URL, imageUrl);
        args.putString(ARG_NEWS_URL, newsUrl);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            title = getArguments().getString(ARG_TITLE);
            content = getArguments().getString(ARG_CONTENT);
            imageUrl = getArguments().getString(ARG_IMAGE_URL);
            newsUrl = getArguments().getString(ARG_NEWS_URL);
        }
        
        // Get related news
        relatedNews = getRelatedNews();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_news_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        TextView titleTextView = view.findViewById(R.id.news_title);
        TextView contentTextView = view.findViewById(R.id.news_content);
        ImageView imageView = view.findViewById(R.id.news_image);
        Button viewOriginalButton = view.findViewById(R.id.view_original_button);

        titleTextView.setText(title);
        contentTextView.setText(content);
        
        // Load image with Glide
        Glide.with(this)
                .load(imageUrl)
                .into(imageView);
                
        // Set read more button click listener
        viewOriginalButton.setOnClickListener(v -> {
            if (newsUrl != null && !newsUrl.isEmpty()) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(newsUrl));
                startActivity(intent);
            }
        });
        
        // Setup related news
        setupRelatedNews(view);
    }
    
    private void setupRelatedNews(View view) {
        if (relatedNews.size() >= 1) {
            CardView relatedNews1Card = view.findViewById(R.id.related_news_1);
            TextView relatedNewsTitle1 = view.findViewById(R.id.related_news_title_1);
            TextView relatedNewsDesc1 = view.findViewById(R.id.related_news_desc_1);
            ImageView relatedNewsImage1 = view.findViewById(R.id.related_news_image_1);
            
            News news1 = relatedNews.get(0);
            relatedNewsTitle1.setText(news1.getTitle());
            relatedNewsDesc1.setText(news1.getDescription());
            
            Glide.with(this)
                    .load(news1.getImageUrl())
                    .into(relatedNewsImage1);
                    
            relatedNews1Card.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showNewsDetail(news1);
                }
            });
        }
        
        if (relatedNews.size() >= 2) {
            CardView relatedNews2Card = view.findViewById(R.id.related_news_2);
            TextView relatedNewsTitle2 = view.findViewById(R.id.related_news_title_2);
            TextView relatedNewsDesc2 = view.findViewById(R.id.related_news_desc_2);
            ImageView relatedNewsImage2 = view.findViewById(R.id.related_news_image_2);
            
            News news2 = relatedNews.get(1);
            relatedNewsTitle2.setText(news2.getTitle());
            relatedNewsDesc2.setText(news2.getDescription());
            
            Glide.with(this)
                    .load(news2.getImageUrl())
                    .into(relatedNewsImage2);
                    
            relatedNews2Card.setOnClickListener(v -> {
                if (getActivity() instanceof MainActivity) {
                    ((MainActivity) getActivity()).showNewsDetail(news2);
                }
            });
        }
    }
    
    private List<News> getRelatedNews() {
        List<News> result = new ArrayList<>();
        
        if (getActivity() instanceof MainActivity) {
            MainActivity activity = (MainActivity) getActivity();
            List<News> allNews = new ArrayList<>();
            allNews.addAll(activity.getTopStories());
            allNews.addAll(activity.getNewsList());
            
            // Find 2 related news (excluding current news)
            for (News news : allNews) {
                if (!news.getTitle().equals(title)) {
                    result.add(news);
                    if (result.size() >= 2) {
                        break;
                    }
                }
            }
        }
        
        return result;
    }
} 

