package com.example.newsapp_224385035;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private List<News> topStories;
    private List<News> newsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize sample data
        initializeData();

        // Setup top stories RecyclerView
        RecyclerView topStoriesRecyclerView = findViewById(R.id.topStoriesRecyclerView);
        topStoriesRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        TopStoriesAdapter topStoriesAdapter = new TopStoriesAdapter(topStories, this::showNewsDetail);
        topStoriesRecyclerView.setAdapter(topStoriesAdapter);

        // Setup the four news items in the grid
        setupNewsGrid();
    }

    private void setupNewsGrid() {
        if (newsList.size() >= 4) {
            setupNewsItem(R.id.newsItem1, newsList.get(0));
            setupNewsItem(R.id.newsItem2, newsList.get(1));
            setupNewsItem(R.id.newsItem3, newsList.get(2));
            setupNewsItem(R.id.newsItem4, newsList.get(3));
        }
    }
    
    private void setupNewsItem(int viewId, News news) {
        CardView cardView = findViewById(viewId);
        if (cardView != null) {
            ImageView newsImage = cardView.findViewById(R.id.newsImage);
            TextView newsTitle = cardView.findViewById(R.id.newsTitle);
            TextView newsDescription = cardView.findViewById(R.id.newsDescription);
            
            newsTitle.setText(news.getTitle());
            newsDescription.setText(news.getDescription());
            
            Glide.with(this)
                 .load(news.getImageUrl())
                 .centerCrop()
                 .into(newsImage);
                 
            cardView.setOnClickListener(v -> showNewsDetail(news));
        }
    }

    private void initializeData() {
        // Top Stories
        topStories = new ArrayList<>();
        topStories.add(new News(
                "health1", 
                "Food insecurity fuels disordered eating habits", 
                "Food insecurity stemming from cost-of-living crisis can fuel disordered eating habits",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image1, 
                "As expenses skyrocket and pantry supplies dwindle, Rebecca Buckley is getting creative to keep food on the table during the cost-of-living crisis.", 
                "2025-03-23",
                "https://www.abc.net.au/news/2025-03-23/food-insecurity-due-to-living-costs-can-cause-disordered-eating/104776474"));
        
        topStories.add(new News(
                "health2", 
                "NSW Health doctors announce strike", 
                "NSW Health doctors announce industrial action from Tuesday for three days",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image2, 
                "NSW Health doctors will strike for three days this week, claiming the state government is ignoring their concerns about staff shortages and unsafe working conditions.", 
                "2025-04-02",
                "https://www.abc.net.au/news/2025-04-02/nsw-health-doctors-three-day-strike/105125372"));
        
        topStories.add(new News(
                "health3", 
                "Heart-healthy cooking tips", 
                "Heart Foundation provides essential cooking tips for heart health",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image3, 
                "Learn how to prepare heart-healthy meals with these expert tips from the Heart Foundation.", 
                "2025-03-15",
                "https://www.heartfoundation.org.au/healthy-living/healthy-eating/skills/heart-healthy-cooking-tips"));

        newsList = new ArrayList<>();
        
        newsList.add(new News(
                "breakfast1", 
                "Healthy Breakfast Recipes", 
                "Collection of healthy breakfast recipes for every day",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image4, 
                "Start your day right with these nutritious breakfast ideas that are both delicious and good for you.", 
                "2025-03-10",
                "https://www.taste.com.au/search-recipes/?q=healthy+breakfast"));

        newsList.add(new News(
                "nutrition1", 
                "Less fruit, more discretionary food by 2030", 
                "Study shows Australians consuming less fruit and vegetables, more discretionary food by 2030",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image5, 
                "New research indicates a concerning trend in Australian eating habits with fewer fruits and vegetables and more processed foods in diets by 2030.", 
                "2025-03-19",
                "https://www.abc.net.au/news/2025-03-19/less-fruit-vegetables-more-discretionary-food-by-2030-study/105061998"));

        newsList.add(new News(
                "meal1", 
                "Heart-healthy meal planning", 
                "Guide to planning heart-healthy meals for your family",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image6, 
                "Learn how to plan nutritious meals that promote heart health with these tips from the Heart Foundation.", 
                "2025-02-28",
                "https://www.heartfoundation.org.au/healthy-living/healthy-eating/skills/heart-healthy-meal-planning"));

        newsList.add(new News(
                "science1", 
                "Translating nutrition science", 
                "Translating the science behind eating well and staying healthy",
                "android.resource://" + getPackageName() + "/" + R.drawable.news_background_image7, 
                "Making sense of nutrition science and how to apply it to everyday eating for better health outcomes.", 
                "2025-03-05",
                "https://www.eatforhealth.gov.au/news-media/translating-science-behind-eating-well-and-staying-healthy"));
    }

    public List<News> getTopStories() {
        return topStories;
    }
    
    public List<News> getNewsList() {
        return newsList;
    }

    public void showNewsDetail(News news) {
        // Hide main content
        findViewById(R.id.mainContent).setVisibility(View.GONE);

        // Create and show detail Fragment
        Fragment detailFragment = NewsDetailFragment.newInstance(
            news.getTitle(),
            news.getContent(),
            news.getImageUrl(),
            news.getUrl()
        );
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, detailFragment)
                .addToBackStack(null)
                .commit();
                
        // Show Fragment container
        findViewById(R.id.fragmentContainer).setVisibility(View.VISIBLE);
    }
    
    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
            // If the back stack is empty, show main content and hide Fragment container
            if (getSupportFragmentManager().getBackStackEntryCount() == 1) {
                findViewById(R.id.fragmentContainer).setVisibility(View.GONE);
                findViewById(R.id.mainContent).setVisibility(View.VISIBLE);
            }
        } else {
            super.onBackPressed();
        }
    }
}
