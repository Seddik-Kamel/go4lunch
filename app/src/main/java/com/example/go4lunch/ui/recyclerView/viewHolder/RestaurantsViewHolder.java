package com.example.go4lunch.ui.recyclerView.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {


    public final TextView restaurantName;
    public final ImageView imageViewRestaurant;
    public final TextView textViewAddressRestaurant;
    public final TextView itemRestaurantOpen;
    public final RatingBar itemRatingBar;
    public final TextView itemUserRatingTotal;
    public final TextView itemDistance;


    public RestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
        restaurantName = itemView.findViewById(R.id.item_restaurant_name);
        imageViewRestaurant = itemView.findViewById(R.id.item_workmate_image);
        textViewAddressRestaurant = itemView.findViewById(R.id.item_restaurant_address);
        itemRestaurantOpen = itemView.findViewById(R.id.item_restaurant_open);
        itemRatingBar = itemView.findViewById(R.id.restaurant_ratingBar);
        itemUserRatingTotal = itemView.findViewById(R.id.item_restaurant_user_rating_total);
        itemDistance = itemView.findViewById(R.id.item_restaurant_distance);
    }
}
