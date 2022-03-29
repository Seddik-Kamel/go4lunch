package com.example.go4lunch.ui.recyclerView.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.go4lunch.R;
import com.example.go4lunch.model.WorkmateModel;
import com.example.go4lunch.ui.recyclerView.viewHolder.WorkmateLikedRestaurantViewHolder;

import java.util.List;

public class WorkmateLikedRestaurantAdapter extends RecyclerView.Adapter<WorkmateLikedRestaurantViewHolder> {

    private final List<WorkmateModel> workmateModelList;

    public WorkmateLikedRestaurantAdapter(List<WorkmateModel> workmateModelList) {
        this.workmateModelList = workmateModelList;
    }

    @NonNull
    @Override
    public WorkmateLikedRestaurantViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.items_recycler_view_list_workmate_liked_restaurant, parent, false);

        return new WorkmateLikedRestaurantViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull WorkmateLikedRestaurantViewHolder holder, int position) {

        WorkmateModel workmateModel = workmateModelList.get(position);

        holder.workmateDisplayName.setText(workmateModel.getUserName());
        Glide.with(holder.workmateImage.getContext())
                .load(workmateModel.getUrlPicture())
                .apply(RequestOptions.circleCropTransform())
                .into(holder.workmateImage);
    }

    @Override
    public int getItemCount() {
        return getNumberOfWorkmates();
    }

    private int getNumberOfWorkmates(){
        return workmateModelList.size();
    }
}
