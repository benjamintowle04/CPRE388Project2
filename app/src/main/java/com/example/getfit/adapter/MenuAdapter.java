package com.example.getfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.R;
import com.example.getfit.models.MenuItem;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.MenuViewHolder> {

    private ArrayList<MenuItem> menuItems;
    private Context context;
    private OnItemClickListener onItemClickListener; // Listener to handle click events

    // Constructor to set the context, menuItems and listener
    public MenuAdapter(Context context, ArrayList<MenuItem> menuItems, OnItemClickListener onItemClickListener) {
        this.menuItems = menuItems;
        this.context = context;
        this.onItemClickListener = onItemClickListener; // Set the listener
    }

    @Override
    public MenuViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_card, parent, false);
        return new MenuViewHolder(view); // Pass the view to the ViewHolder
    }

    @Override
    public void onBindViewHolder(MenuViewHolder holder, int position) {
        MenuItem menuItem = menuItems.get(position);
        holder.nameTextView.setText(menuItem.getName());
        holder.caloriesTextView.setText("Calories: " + menuItem.getTotalCal());

        // Set the MenuItem object to the holder's view
        holder.itemView.setOnClickListener(v -> {
            // When clicked, trigger the listener with the current MenuItem
            onItemClickListener.onItemClick(menuItem);
        });
    }

    @Override
    public int getItemCount() {
        return menuItems.size();
    }

    // Method to update the data and notify the adapter
    public void updateMenuItems(ArrayList<MenuItem> newMenuItems) {
        this.menuItems.clear(); // Clear the existing list
        this.menuItems.addAll(newMenuItems); // Add the new data
        notifyDataSetChanged(); // Notify the adapter that the data has changed
    }

    // Interface to handle click events
    public interface OnItemClickListener {
        void onItemClick(MenuItem menuItem); // Define the action for an item click
    }

    public static class MenuViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, caloriesTextView;

        public MenuViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.menuItemName);
            caloriesTextView = itemView.findViewById(R.id.menuItemCalories);
        }
    }
}
