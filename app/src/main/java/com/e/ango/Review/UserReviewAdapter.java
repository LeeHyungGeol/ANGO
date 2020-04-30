package com.e.ango.Review;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.e.ango.R;
import com.e.ango.Response.ReviewDto;

import java.util.ArrayList;

public class UserReviewAdapter extends RecyclerView.Adapter<UserReviewAdapter.ViewHolder>  {


    ArrayList<ReviewDto> items = new ArrayList<>();



    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.user_review_items, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        ReviewDto item = items.get(position);
        holder.setItem(item);

        //holder.chkSelected.setChecked(items.get(position).isSelected());
        holder.chkSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox cb = (CheckBox) v;
                ReviewDto review = (ReviewDto) cb.getTag();
                review.setSelected(cb.isChecked());
                items.get(position).setSelected(cb.isChecked());
//                Toast.makeText(
//                        v.getContext(),
//                        "Clicked on Checkbox: " + cb.getText() + " is "
//                                + cb.isChecked(), Toast.LENGTH_LONG).show();


            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }




    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView text_id;
        TextView text_review;
        TextView text_title;
        TextView text_date;
        RatingBar ratingBar;
        CheckBox chkSelected;

        public ViewHolder(View itemView) {
            super(itemView);

            text_id = itemView.findViewById(R.id.text_id);
            text_review = itemView.findViewById(R.id.text_review);
            text_title = itemView.findViewById(R.id.text_title);
            ratingBar = itemView.findViewById(R.id.ratingBar);
            text_date = itemView.findViewById(R.id.text_date);
            chkSelected = itemView.findViewById(R.id.ckSelect);


        }

        public void setItem(ReviewDto item) {
            text_id.setText(item.getReview_id());
            text_review.setText(item.getReview_text());
            ratingBar.setRating(item.getReview_score());
            text_date.setText(item.getTime());
            text_title.setText(item.getTitle());
            chkSelected.setSelected(item.isSelected());
            chkSelected.setTag(item);
        }


    }

    public void addItem(ReviewDto item) {
        items.add(item);
    }

    public void setItems(ArrayList<ReviewDto> items) {
        this.items = items;
        System.out.println("adapter : " + items.toString());
    }

    public ArrayList<ReviewDto> getItems() {
        return items;
    }

    public ReviewDto getItem(int position) {
        return items.get(position);
    }

    public void setItem(int position, ReviewDto item) {
        items.set(position, item);
    }

}