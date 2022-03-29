package com.example.go4lunch.ui.recyclerView.viewHolder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.go4lunch.R;

public class WorkmateLikedRestaurantViewHolder extends RecyclerView.ViewHolder {


    public final TextView workmateDisplayName;
    public final ImageView workmateImage;

    public WorkmateLikedRestaurantViewHolder(@NonNull View itemView) {
        super(itemView);

        workmateDisplayName = itemView.findViewById(R.id.item_workmate_display_name);
        workmateImage = itemView.findViewById(R.id.item_workmate_image);
    }
}
