package com.example.go4lunch.ui.recyclerView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.example.go4lunch.ui.recyclerView.viewHolder.RestaurantsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> implements Filterable {

    public final List<RestaurantModel> restaurantList;
    private final FragmentActivity activity;
    private List<RestaurantModel> restaurantFiltered;

    public RestaurantsAdapter(List<RestaurantModel> restaurantList, FragmentActivity activity) {
        this.activity = activity;
        this.restaurantList = restaurantList;
        this.restaurantFiltered = restaurantList;
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
        RestaurantModel restaurantModel = restaurantFiltered.get(position);

        if (restaurantModel.isOpen().equals(activity.getResources().getString(R.string.place_open))) {
            holder.itemRestaurantOpen.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorGreen));
        } else {
            holder.itemRestaurantOpen.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorError));
        }

        holder.restaurantName.setText(restaurantModel.getName());
        holder.imageViewRestaurant.setImageBitmap(restaurantModel.getBitmap());
        holder.textViewAddressRestaurant.setText(restaurantModel.getAddress());
        holder.itemRatingBar.setRating(restaurantModel.getRating());
        holder.itemUserRatingTotal.setText("( " + restaurantModel.getUserRatingTotal() + " )");
        holder.itemDistance.setText(restaurantModel.getUserDistance() + " m ");
        holder.itemRestaurantOpen.setText(restaurantModel.isOpen());
        holder.itemView.setOnClickListener(v -> ((HomeScreenActivity) activity).startRestaurantDetailActivity(restaurantModel.getPlaceId()));
    }

    @Override
    public int getItemCount() {
        return getNumberOfRestaurants();
    }

    public int getNumberOfRestaurants() {
        return restaurantFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    restaurantFiltered = restaurantList;
                } else {
                    List<RestaurantModel> filteredList = new ArrayList<>();
                    for (RestaurantModel row : restaurantList) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase())) {
                            filteredList.add(row);
                        }
                    }

                    restaurantFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = restaurantFiltered;

                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                restaurantFiltered = (ArrayList<RestaurantModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
