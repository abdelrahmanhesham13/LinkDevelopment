package com.linkdevelopment.newsfeeds;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.android.volley.NoConnectionError;
import com.android.volley.VolleyError;
import com.linkdevelopment.newsfeeds.adapters.NewsAdapter;
import com.linkdevelopment.newsfeeds.models.NewModel;
import com.linkdevelopment.newsfeeds.networkUtils.Connector;
import com.linkdevelopment.newsfeeds.utils.Helper;

import java.util.ArrayList;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private final String TAG = HomeActivity.class.getSimpleName();
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawer;
    @BindView(R.id.nav_view)
    NavigationView mNavigationView;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;
    @BindView(R.id.news)
    RecyclerView mNewsRecycler;
    @BindView(R.id.progressIndicator)
    ProgressBar mProgressBar;

    ArrayList<NewModel> mNewsModels;

    NewsAdapter mNewsAdapter;

    ActionBarDrawerToggle mToggle;

    Connector mConnector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ButterKnife.bind(this);


        setSupportActionBar(mToolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setHomeButtonEnabled(false);
            getSupportActionBar().setTitle(getString(R.string.link_development));
        }

        mNavigationView.setNavigationItemSelectedListener(this);
        mNavigationView.setLayoutDirection(View.LAYOUT_DIRECTION_LTR);
        mNavigationView.setTextDirection(View.TEXT_DIRECTION_LTR);

        mToggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerSlideAnimationEnabled(false);

        mToolbar.setTitle(getString(R.string.link_development));
        mToolbar.setTitleTextColor(Color.WHITE);

        mNewsModels = new ArrayList<>();

        mNewsAdapter = new NewsAdapter(mNewsModels, this, new NewsAdapter.OnItemClicked() {
            @Override
            public void setOnItemClicked(int position) {
                startActivity(new Intent(HomeActivity.this,NewDetailsActivity.class).putExtra("new",mNewsModels.get(position)));
            }
        });

        mNewsRecycler.setLayoutManager(new LinearLayoutManager(this));
        mNewsRecycler.setHasFixedSize(true);
        mNewsRecycler.setAdapter(mNewsAdapter);

        mConnector = new Connector(this, new Connector.LoadCallback() {
            @Override
            public void onComplete(String tag, String response) {
                mNewsRecycler.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                mNewsModels.addAll(Connector.getNews(response));
                mNewsAdapter.notifyDataSetChanged();
            }
        }, new Connector.ErrorCallback() {
            @Override
            public void onError(VolleyError error) {
                mNewsRecycler.setVisibility(View.VISIBLE);
                mProgressBar.setVisibility(View.GONE);
                if (error instanceof NoConnectionError){
                    Helper.showShortTimeToast(HomeActivity.this,getString(R.string.no_internet));
                } else {
                    Helper.showShortTimeToast(HomeActivity.this,getString(R.string.error));
                }

            }
        });


        mNewsRecycler.setVisibility(View.GONE);
        mProgressBar.setVisibility(View.VISIBLE);
        mConnector.getRequest(TAG,Connector.createUrl());

    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.nav_explore:
                Helper.showShortTimeToast(HomeActivity.this,getString(R.string.explore));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_live_chat:
                Helper.showShortTimeToast(HomeActivity.this,getString(R.string.live_chat));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_gallery:
                Helper.showShortTimeToast(HomeActivity.this,getString(R.string.gallery));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_wish_list:
                Helper.showShortTimeToast(HomeActivity.this,getString(R.string.wish_list));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            case R.id.nav_e_magazine:
                Helper.showShortTimeToast(HomeActivity.this,getString(R.string.e_magazine));
                mDrawer.closeDrawer(GravityCompat.START);
                return true;
            default: return true;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mConnector != null)
            mConnector.cancelAllRequests(TAG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_items,menu);
        return true;
    }
}
