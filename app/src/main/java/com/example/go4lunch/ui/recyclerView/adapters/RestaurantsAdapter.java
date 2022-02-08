package com.example.go4lunch.ui.recyclerView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.recyclerView.viewHolder.RestaurantsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    public List<RestaurantModel> listRestaurants;

    public RestaurantsAdapter(List<RestaurantModel> listRestaurants) {
        this.listRestaurants = listRestaurants;
    }

    @NonNull
    @Override
    public RestaurantsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recycler_view_list_restaurant, parent, false);

        return new RestaurantsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RestaurantsViewHolder holder, int position) {
        RestaurantModel restaurantModel = listRestaurants.get(position);
        holder.restaurantName.setText(restaurantModel.getName());

    }

    @Override
    public int getItemCount() {
        return getNumberOfRestaurants();
    }

    public int getNumberOfRestaurants(){
        return listRestaurants.size();
    }
}
