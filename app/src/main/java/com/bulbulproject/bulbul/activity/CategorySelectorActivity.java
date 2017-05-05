package com.bulbulproject.bulbul.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import com.bulbulproject.bulbul.R;
import com.bulbulproject.bulbul.adapter.CategoryAdapter;
import com.bulbulproject.bulbul.model.Category;

import java.util.ArrayList;
import java.util.List;

public class CategorySelectorActivity extends AppCompatActivity {
    private GridView mGrid;
    private List<Category> categoryList;
    private View mProgressView;

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

        initDummyData();
        mGrid = (GridView) findViewById(R.id.grid_layout);
        mGrid.setAdapter(new CategoryAdapter(categoryList, CategorySelectorActivity.this));
        mGrid.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        ((Button) findViewById(R.id.button_next)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startArtistSelectorActivity();
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

    private List<Integer> getSelectedCategoryIds() {
        List<Integer> list = new ArrayList<Integer>();
        for (Category category : categoryList) {
            if (category.isSelected()) list.add(category.getId());
        }
        return list;
    }

    void startArtistSelectorActivity() {
        Intent intent = new Intent(CategorySelectorActivity.this, ArtistSelectorActivity.class);
        intent.putIntegerArrayListExtra("category_ids", (ArrayList<Integer>) getSelectedCategoryIds());
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
