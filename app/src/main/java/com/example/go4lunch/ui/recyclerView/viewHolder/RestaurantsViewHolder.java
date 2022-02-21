package com.example.go4lunch.ui.recyclerView.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RestaurantsViewHolder extends RecyclerView.ViewHolder {


    public TextView restaurantName;
    public ImageView imageViewRestaurant;
    public TextView textViewAddressRestaurant;

    public RestaurantsViewHolder(@NonNull View itemView) {
        super(itemView);
        restaurantName = itemView.findViewById(R.id.item_restaurant_name);
        imageViewRestaurant = itemView.findViewById(R.id.item_restaurant_image);
        textViewAddressRestaurant = itemView.findViewById(R.id.item_restaurant_address);
    }
}
