package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.apollographql.apollo.ApolloCall;
import com.apollographql.apollo.api.Response;
import com.apollographql.apollo.exception.ApolloException;
import com.bulbulproject.GenreQuery;
import com.bulbulproject.bulbul.App;
import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.CategorySelectorAdapter;
import com.bulbulproject.bulbul.interfaces.AdapterCallbackInterface;
import com.bulbulproject.bulbul.model.Category;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

public class CategorySelectorActivity extends AppCompatActivity {
    private GridView mGrid;
    private List<Category> categoryList;
    private View mProgressView;
    private BaseAdapter mAdapter;
    Button button_next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        categoryList = new ArrayList<Category>();
        setContentView(R.layout.activity_category_selector);
        mProgressView = findViewById(R.id.progress);
        mProgressView.setVisibility(View.VISIBLE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }
        button_next = (Button) findViewById(R.id.button_next);

//        initDummyData();
        fetchGenres();
        mAdapter = new CategorySelectorAdapter(categoryList, CategorySelectorActivity.this, new AdapterCallbackInterface() {
            @Override
            public void onSelectedItemCountChanged(int selectedItemCount) {
                if(selectedItemCount < 2)
                    button_next.setEnabled(false);
                else
                    button_next.setEnabled(true);
            }
        });
        mGrid = (GridView) findViewById(R.id.grid_layout);
        mGrid.setAdapter(mAdapter);
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        button_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startArtistSelectorActivity();
            }
        });
    }

    void fetchGenres() {
        ((App) getApplication()).apolloClient().newCall(GenreQuery.builder().build()).enqueue(new ApolloCall.Callback<GenreQuery.Data>() {
            @Override
            public void onResponse(@Nonnull Response<GenreQuery.Data> response) {
                if (response.isSuccessful()) {
                    if (response.data().genres() != null) {
                        for (GenreQuery.Data.Genre genre : response.data().genres()) {
                            categoryList.add(new Category(genre.name(), genre.icon_url(), genre.id()));
                        }

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                mAdapter.notifyDataSetChanged();
                                mProgressView.setVisibility(View.GONE);
                            }
                        });
                    }
                }
            }

            @Override
            public void onFailure(@Nonnull ApolloException e) {
                final String text = e.getMessage();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(CategorySelectorActivity.this, text, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    void initDummyData() {
        categoryList.add(new Category("Pattis", "http://symbolsnet.com/thumbs/g8Z10hUtLUHjJTXD8DkZXDx8YNfyL_dpZI6BKH-BAW5u2CJuXBa6UeyEXyW9ft1btW-tNjIwtfWnU3c4TyEEnQ.jpg", 1));
        categoryList.add(new Category("Domatis", "http://symbolsnet.com/thumbs/g8Z10hUtLUHjJTXD8DkZXDx8YNfyL_dpZI6BKH-BAW5u2CJuXBa6UeyEXyW9ft1btW-tNjIwtfWnU3c4TyEEnQ.jpg", 2));
        categoryList.add(new Category("Sogan", "http://symbolsnet.com/thumbs/g8Z10hUtLUHjJTXD8DkZXDx8YNfyL_dpZI6BKH-BAW5u2CJuXBa6UeyEXyW9ft1btW-tNjIwtfWnU3c4TyEEnQ.jpg", 3));
        categoryList.add(new Category("Patlican", "http://symbolsnet.com/thumbs/g8Z10hUtLUHjJTXD8DkZXDx8YNfyL_dpZI6BKH-BAW5u2CJuXBa6UeyEXyW9ft1btW-tNjIwtfWnU3c4TyEEnQ.jpg", 4));
        categoryList.add(new Category("Biber", "http://symbolsnet.com/thumbs/g8Z10hUtLUHjJTXD8DkZXDx8YNfyL_dpZI6BKH-BAW5u2CJuXBa6UeyEXyW9ft1btW-tNjIwtfWnU3c4TyEEnQ.jpg", 5));
        mProgressView.setVisibility(View.GONE);
    }

    private ArrayList<Integer> getSelectedCategoryIds() {
        ArrayList<Integer> list = new ArrayList<Integer>();
        for (Category category : categoryList) {
            if (category.isSelected()) list.add(category.getId());
        }
        return list;
    }

    void startArtistSelectorActivity() {
        Intent intent = new Intent(CategorySelectorActivity.this, ArtistSelectorActivity.class);
        intent.putIntegerArrayListExtra("category_ids", getSelectedCategoryIds());
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(CategorySelectorActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

}
