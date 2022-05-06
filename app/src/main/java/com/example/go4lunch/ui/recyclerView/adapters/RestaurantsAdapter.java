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

import com.bumptech.glide.Glide;
import com.example.go4lunch.R;
import com.example.go4lunch.model.PlaceModel;
import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.example.go4lunch.ui.recyclerView.viewHolder.RestaurantsViewHolder;

import java.util.ArrayList;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> implements Filterable {

    public final List<PlaceModel> restaurantList;
    private final FragmentActivity activity;
    private List<PlaceModel> restaurantFiltered;

    public RestaurantsAdapter(List<PlaceModel> restaurantList, FragmentActivity activity) {
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
        PlaceModel placeModel = restaurantFiltered.get(position);

        if (placeModel.isOpen().equals(activity.getResources().getString(R.string.place_open))) {
            holder.itemRestaurantOpen.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorGreen));
        } else {
            holder.itemRestaurantOpen.setTextColor(ContextCompat.getColor(activity.getApplicationContext(), R.color.colorError));
        }

        holder.restaurantName.setText(placeModel.getName());

        holder.imageViewRestaurant.setImageBitmap(placeModel.getBitmap());

        Glide.with(activity)
                .load(placeModel.getBytesImage())
                .into(holder.imageViewRestaurant);

        holder.textViewAddressRestaurant.setText(placeModel.getAddress());
        holder.itemRatingBar.setRating(placeModel.getRating());
        holder.itemUserRatingTotal.setText("( " + placeModel.getUserRatingTotal() + " )");
        holder.itemDistance.setText(placeModel.getUserDistance() + " m ");
        holder.itemRestaurantOpen.setText(placeModel.isOpen());
        holder.itemView.setOnClickListener(v -> ((HomeScreenActivity) activity).startRestaurantDetailActivity(placeModel.getPlaceId()));
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
                    List<PlaceModel> filteredList = new ArrayList<>();
                    for (PlaceModel row : restaurantList) {

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
                restaurantFiltered = (ArrayList<PlaceModel>) results.values;
                notifyDataSetChanged();
            }
        };
    }

}
