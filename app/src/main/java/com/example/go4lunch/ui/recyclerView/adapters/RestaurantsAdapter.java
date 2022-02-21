package com.example.go4lunch.ui.recyclerView.adapters;

import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;
import com.example.go4lunch.model.RestaurantModel;
import com.example.go4lunch.ui.activity.HomeScreenActivity;
import com.example.go4lunch.ui.fragments.DetailsRestaurantsFragment;
import com.example.go4lunch.ui.recyclerView.viewHolder.RestaurantsViewHolder;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.model.PhotoMetadata;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FetchPhotoRequest;
import com.google.android.libraries.places.api.net.FetchPlaceRequest;
import com.google.android.libraries.places.api.net.FetchPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.util.Arrays;
import java.util.List;

public class RestaurantsAdapter extends RecyclerView.Adapter<RestaurantsViewHolder> {

    private static final String TAG = RestaurantsAdapter.class.getSimpleName();
    private PlacesClient placeClient;
    public List<RestaurantModel> listRestaurants;
    public List<RestaurantModel> testListRestaurant = Arrays.asList(
            new RestaurantModel("zeze", "tosca", "8 rue de la tuilerie", null, null), new RestaurantModel("zeze", "tosca", "8 rue de la tuilerie", null, null));
    private FragmentActivity activity;

    public RestaurantsAdapter(List<RestaurantModel> listRestaurants) {
        this.listRestaurants = listRestaurants;
    }

    public RestaurantsAdapter(List<RestaurantModel> listRestaurants, PlacesClient placesClient) {
        this.listRestaurants = listRestaurants;
        this.placeClient = placesClient;
    }

    public RestaurantsAdapter(List<RestaurantModel> listRestaurants, PlacesClient placesClient, FragmentActivity activity) {
        this.activity = activity;
        this.listRestaurants = listRestaurants;
        this.placeClient = placesClient;
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

        final PhotoMetadata photoMetadata = restaurantModel.getPhotoMetadata().get(0);
        final String attribution = photoMetadata.getAttributions();

        final FetchPhotoRequest fetchPlaceRequest = FetchPhotoRequest.builder(photoMetadata)
                .setMaxHeight(80)
                .setMaxWidth(80)
                .build();

        placeClient.fetchPhoto(fetchPlaceRequest).addOnSuccessListener(fetchPhotoResponse -> {
            Bitmap bitmap = fetchPhotoResponse.getBitmap();
            holder.imageViewRestaurant.setImageBitmap(bitmap);
        }).addOnFailureListener(exception -> {
            if (exception instanceof ApiException) {
                final ApiException apiException = (ApiException) exception;
                Log.e(TAG, "Place not found: " + exception.getMessage());
                final int statusCode = apiException.getStatusCode();
            }
        });

        final String placeId = restaurantModel.getPlaceId();
        final List<Place.Field> placeField = Arrays.asList(Place.Field.OPENING_HOURS, Place.Field.PHONE_NUMBER);
        final FetchPlaceRequest placeRequest = FetchPlaceRequest.newInstance(placeId, placeField);

        placeClient.fetchPlace(placeRequest).addOnSuccessListener(new OnSuccessListener<FetchPlaceResponse>() {
            @Override
            public void onSuccess(FetchPlaceResponse fetchPlaceResponse) {
                Place place = fetchPlaceResponse.getPlace();
                //Log.i("place", "Place found: " + place.getOpeningHours().getWeekdayText());
                place.getOpeningHours().getPeriods().get(0).getClose().getTime();
            }
        });

        holder.restaurantName.setText(restaurantModel.getName());
        holder.textViewAddressRestaurant.setText(restaurantModel.getAddress());

        holder.itemView.setOnClickListener(v -> {
            ((HomeScreenActivity)activity).displayFragment(DetailsRestaurantsFragment.newInstance("test","test"));
        });
    }

    @Override
    public int getItemCount() {
        return getNumberOfRestaurants();
    }

    public int getNumberOfRestaurants() {
        return listRestaurants.size();
    }
}
