package com.example.go4lunch.ui.recyclerView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.example.go4lunch.ui.recyclerView.viewHolder.RestaurantsViewHolder;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private static final String TAG = RestaurantsAdapter.class.getSimpleName();
    public final List<RestaurantModel> listRestaurants;
    private final FragmentActivity activity;

    public RestaurantsAdapter(List<RestaurantModel> listRestaurants, PlacesClient placesClient, FragmentActivity activity) {
        this.activity = activity;
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

        if (restaurantModel.isOpen().equals(activity.getResources().getString(R.string.place_open))) {
            holder.itemRestaurantOpen.setTextColor(activity.getResources().getColor(R.color.colorGreen));
        } else {
            holder.itemRestaurantOpen.setTextColor(activity.getResources().getColor(R.color.colorError));
        }

        holder.restaurantName.setText(restaurantModel.getName());
        holder.imageViewRestaurant.setImageBitmap(restaurantModel.getBitmap());
        holder.textViewAddressRestaurant.setText(restaurantModel.getAddress());
        holder.itemRatingBar.setRating(restaurantModel.getRating());
        holder.itemUserRatingTotal.setText("( " + restaurantModel.getUserRatingTotal() + " )");
        holder.itemDistance.setText(restaurantModel.getUserDistance() + " m ");
        holder.itemRestaurantOpen.setText(restaurantModel.isOpen());
        holder.itemView.setOnClickListener(v -> ((HomeScreenActivity) activity).startActivity(restaurantModel.getPlaceId()));
    }

    @Override
    public int getItemCount() {
        return getNumberOfRestaurants();
    }

    public int getNumberOfRestaurants() {
        return listRestaurants.size();
    }
}
