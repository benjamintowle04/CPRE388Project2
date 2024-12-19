package com.example.getfit.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.MenuActivity;
import com.example.getfit.R;
import com.example.getfit.models.DiningCenter;

import java.util.List;

public class DiningCenterAdapter extends RecyclerView.Adapter<DiningCenterAdapter.ViewHolder> {

    private List<DiningCenter> diningCenters;
    private Context context;

    // Constructor to pass the list of dining centers
    public DiningCenterAdapter(Context context, List<DiningCenter> diningCenters) {
        this.context = context;
        this.diningCenters = diningCenters;
    }

    // ViewHolder class to hold the views for each item
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title, type;
        public ImageView icon;

        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.dining_center_title);
            type = itemView.findViewById(R.id.dining_center_type);
            icon = itemView.findViewById(R.id.dining_center_icon);
        }
    }

    @Override
    public DiningCenterAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the layout for the dining center item
        View view = LayoutInflater.from(context).inflate(R.layout.item_dining_center, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DiningCenter diningCenter = diningCenters.get(position);

        // Set data for the dining center
        holder.title.setText(diningCenter.getName());
        holder.type.setText(diningCenter.getType());
        holder.icon.setImageResource(diningCenter.getIconResId());  // Set the icon resource ID

        // Set a click listener on the itemView to navigate to the MenuActivity
        holder.itemView.setOnClickListener(v -> {
            // Create an intent to navigate to MenuActivity
            Intent intent = new Intent(context, MenuActivity.class);
            // Pass the slug of the dining center to the next activity
            intent.putExtra("slug", diningCenter.getSlug());
            Log.d("Slug", diningCenter.getSlug());
            context.startActivity(intent);  // Start the MenuActivity
        });
    }

    @Override
    public int getItemCount() {
        return diningCenters.size();
    }
}
