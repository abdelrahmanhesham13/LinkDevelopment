package com.linkdevelopment.newsfeeds.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.linkdevelopment.newsfeeds.R;
import com.linkdevelopment.newsfeeds.models.NewModel;
import com.linkdevelopment.newsfeeds.utils.Helper;
import com.squareup.picasso.Picasso;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewViewHolder> {

    ArrayList<NewModel> newsModels;
    Context context;
    OnItemClicked onItemClicked;

    public NewsAdapter(ArrayList<NewModel> newsModels, Context context, OnItemClicked onItemClicked) {
        this.newsModels = newsModels;
        this.context = context;
        this.onItemClicked = onItemClicked;
    }

    @NonNull
    @Override
    public NewViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.new_item, viewGroup, false);
        return new NewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewViewHolder newViewHolder, int i) {
        newViewHolder.author.setText(String.format("By %s", newsModels.get(i).getAuthor()));
        newViewHolder.date.setText(Helper.parseDate(newsModels.get(i).getPublishedAt()));
        newViewHolder.title.setText(newsModels.get(i).getTitle());
        if (!newsModels.get(i).getUrlToImage().isEmpty())
            Picasso.get().load(newsModels.get(i).getUrlToImage()).error(R.drawable.im_placeholder).placeholder(R.drawable.im_placeholder).into(newViewHolder.image);
    }

    @Override
    public int getItemCount() {
        return newsModels.size();
    }


    public class NewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.image)
        ImageView image;
        @BindView(R.id.author)
        TextView author;
        @BindView(R.id.title)
        TextView title;
        @BindView(R.id.date)
        TextView date;

        NewViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            onItemClicked.setOnItemClicked(getAdapterPosition());
        }

    }

    public interface OnItemClicked {
        void setOnItemClicked(int position);
    }


}
