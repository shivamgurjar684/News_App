package com.example.newsapp1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements CategoryRVAdapter.CategoryClickInterface {

    // 0c41a3cccae741e68514b8610a3d6817

    private RecyclerView newsRV, categoryRV ;
    private ProgressBar loadingPB ;
    private ArrayList<Articles> articlesArrayList ;
    private ArrayList<CategoryRVModal> categoryRVModalArrayList ;
    private CategoryRVAdapter categoryRVAdapter ;
    private NewsRVAdapter newsRVAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        newsRV = findViewById(R.id.idRVNews);
        categoryRV = findViewById(R.id.idRVCategories);
        loadingPB = findViewById(R.id.idPBLoading);
        articlesArrayList = new ArrayList<>() ;
        categoryRVModalArrayList = new ArrayList<>() ;
        newsRVAdapter = new NewsRVAdapter(articlesArrayList, this);
        categoryRVAdapter = new CategoryRVAdapter(categoryRVModalArrayList, this, this::onCategoryClick) ;
        newsRV.setLayoutManager(new LinearLayoutManager(this));
        newsRV.setAdapter(newsRVAdapter);
        categoryRV.setAdapter(categoryRVAdapter);
        getCategories();
        getNews("All");
        newsRVAdapter.notifyDataSetChanged();

    }

    private void getCategories(){
        categoryRVModalArrayList.add(new CategoryRVModal("All","https://plus.unsplash.com/premium_photo-1675826626404-682d8d6ab001?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2072&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Technology","https://images.unsplash.com/photo-1487058792275-0ad4aaf24ca7?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Science","https://images.unsplash.com/photo-1614935151651-0bea6508db6b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1850&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Sports","https://images.unsplash.com/photo-1517649763962-0c623066013b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("General","https://images.unsplash.com/photo-1494059980473-813e73ee784b?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2069&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Business","https://images.unsplash.com/photo-1664575602276-acd073f104c1?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Entertainment","https://images.unsplash.com/photo-1603190287605-e6ade32fa852?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=2070&q=80")) ;
        categoryRVModalArrayList.add(new CategoryRVModal("Health","https://images.unsplash.com/photo-1506126613408-eca07ce68773?ixlib=rb-4.0.3&ixid=M3wxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8fA%3D%3D&auto=format&fit=crop&w=1999&q=80")) ;

        categoryRVAdapter.notifyDataSetChanged();
    }

    private void getNews(String category){
        loadingPB.setVisibility(View.VISIBLE);
        articlesArrayList.clear();
        String categoryURL = "https://newsapi.org/v2/top-headlines?country=in&category=" + category + "&apiKey=0c41a3cccae741e68514b8610a3d6817" ;
        String url = "https://newsapi.org/v2/top-headlines?country=in&excludeDomains=stackoverflow.com&sortBy=publishedAt&language=en&apiKey=0c41a3cccae741e68514b8610a3d6817" ;
        String BASE_URL = "https://newsapi.org/" ;
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build() ;

        RetrofitAPI retrofitAPI = retrofit.create(RetrofitAPI.class) ;
        Call<NewsModal> call ;
        if (category.equals("All")){
            call = retrofitAPI.getAllNews(url)  ;
        }
        else{
            call = retrofitAPI.getNewsByCategory(categoryURL) ;
        }

        call.enqueue(new Callback<NewsModal>() {
            @Override
            public void onResponse(Call<NewsModal> call, Response<NewsModal> response) {
                NewsModal newsModal = response.body() ;
                loadingPB.setVisibility(View.GONE);
                ArrayList<Articles> articles = newsModal.getArticles() ;
                for (int i = 0; i < articles.size(); i++) {
                    articlesArrayList.add(new Articles(articles.get(i).getTitle(), articles.get(i).getDescription(),articles.get(i).getUrlToImage(),
                            articles.get(i).getUrl(), articles.get(i).getContent()))  ;
                }
                newsRVAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<NewsModal> call, Throwable t) {
                Toast.makeText(MainActivity.this, "Fail to get News ", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onCategoryClick(int position) {
        String category = categoryRVModalArrayList.get(position).getCategory() ;
        getNews(category);
    }

}