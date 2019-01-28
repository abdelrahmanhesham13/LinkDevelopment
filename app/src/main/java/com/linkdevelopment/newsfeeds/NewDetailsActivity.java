package com.linkdevelopment.newsfeeds;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkdevelopment.newsfeeds.models.NewModel;
import com.linkdevelopment.newsfeeds.utils.Helper;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewDetailsActivity extends AppCompatActivity {

    @BindView(R.id.title)
    TextView mTitle;
    @BindView(R.id.description)
    TextView mDescription;
    @BindView(R.id.image)
    ImageView mImage;
    @BindView(R.id.date)
    TextView mDate;
    @BindView(R.id.author)
    TextView mAuthor;
    @BindView(R.id.website)
    Button mWebsite;

    NewModel mNewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_details);
        ButterKnife.bind(this);

        setTitle(getString(R.string.link_development));
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        if (getIntent() != null && getIntent().hasExtra("new"))
            mNewModel = (NewModel) getIntent().getSerializableExtra("new");
        else
            finish();

        mTitle.setText(mNewModel.getTitle());
        mAuthor.setText(String.format("By %s", mNewModel.getAuthor()));
        mDescription.setText(mNewModel.getDescription());
        if (!mNewModel.getUrlToImage().isEmpty())
            Picasso.get().load(mNewModel.getUrlToImage()).placeholder(R.drawable.im_placeholder).error(R.drawable.im_placeholder).into(mImage);
        mDate.setText(Helper.parseDate(mNewModel.getPublishedAt()));


        mWebsite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Helper.openWebPage(mNewModel.getUrl(), NewDetailsActivity.this);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return false;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.action_bar_menu_items, menu);
        return true;
    }
}
