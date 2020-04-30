package com.e.ango.Review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ango.R;
import com.e.ango.Response.ReviewDto;

import java.util.ArrayList;

public class ReviewDTOAdapter extends RecyclerView.Adapter<ReviewDTOAdapter.ViewHolder>{


    ArrayList<ReviewDto> items = new ArrayList<>();

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.review_item, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ReviewDto item = items.get(position);
        holder.setItem(item);

    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        TextView textView2;
        RatingBar ratingBar;
        TextView textView4;

        public ViewHolder(View itemView) {
            super(itemView);

            textView = itemView.findViewById(R.id.text_id);
            textView2 = itemView.findViewById(R.id.text_title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            textView4 = itemView.findViewById(R.id.text_date);
        }

        public void setItem(ReviewDto item) {
            textView.setText(item.getReview_id());
            textView2.setText(item.getReview_text());
            ratingBar.setRating(item.getReview_score());
            textView4.setText(item.getTime());
        }


    }

    public void addItem(ReviewDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<ReviewDto> items) {
        this.items = items;
    }

    public ReviewDto getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ReviewDto item) {
        items.set(position, item);
    }

}
