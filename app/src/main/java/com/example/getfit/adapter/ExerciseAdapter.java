package com.example.getfit.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.getfit.R;
import com.example.getfit.models.Exercise;
import com.example.getfit.models.UserStats;

import java.util.ArrayList;

/**
 * The {@link ExerciseAdapter} is a RecyclerView adapter responsible for displaying a list of
 * {@link Exercise} items in a RecyclerView. It binds the exercise data to the respective views
 * and handles user interaction, such as adding points to the user's stats when an exercise is clicked.
 */
public class ExerciseAdapter extends RecyclerView.Adapter<ExerciseAdapter.WorkoutViewHolder> {

    private ArrayList<Exercise> exerciseList;
    private Context context;
    private UserStats userStats;

    /**
     * Constructor to initialize the adapter with the context and a list of exercises.
     *
     * @param context      the context used for showing Toast messages and inflating views
     * @param exerciseList the list of exercises to be displayed in the RecyclerView
     */
    public ExerciseAdapter(Context context, ArrayList<Exercise> exerciseList) {
        this.context = context;
        this.exerciseList = exerciseList;
        this.userStats = new UserStats(context); // Initialize the UserStats
    }

    /**
     * Called when a new view holder is created. This method inflates the layout for each exercise item.
     *
     * @param parent   the parent view that will contain the created item view
     * @param viewType the type of the view
     * @return a new {@link WorkoutViewHolder} instance
     */
    @Override
    public WorkoutViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_workout, parent, false);
        return new WorkoutViewHolder(view);
    }

    /**
     * Called to bind data to a view holder. This method sets the exercise data to the respective TextViews.
     *
     * @param holder   the view holder to bind the data to
     * @param position the position of the exercise item in the list
     */
    @Override
    public void onBindViewHolder(WorkoutViewHolder holder, int position) {
        Exercise exercise = exerciseList.get(position);
        holder.nameTextView.setText(exercise.getName());
        holder.muscleGroupTextView.setText(exercise.getMuscleGroup());
        holder.typeTextView.setText(exercise.getType());
        holder.setsTextView.setText("Sets: " + exercise.getSets());
        holder.repsTextView.setText("Reps: " + exercise.getReps());
        holder.difficultyTextView.setText("Difficulty: " + exercise.getDifficulty());

        // Set OnClickListener on the item to add points
        holder.itemView.setOnClickListener(v -> {
            // Add a point when the exercise item is clicked
            userStats.addPoints(1);
            // Display a message to the user
            Toast.makeText(context, "1 point added to your stats!", Toast.LENGTH_SHORT).show();
        });
    }

    /**
     * Returns the size of the exercise list. This is used by the RecyclerView to determine
     * how many items are in the data set.
     *
     * @return the size of the exercise list
     */
    @Override
    public int getItemCount() {
        return exerciseList.size();
    }

    /**
     * Updates the exercise list with a new list of exercises. This method is used to refresh
     * the data displayed by the adapter.
     *
     * @param newExerciseList the new list of exercises to display
     */
    public void updateExerciseList(ArrayList<Exercise> newExerciseList) {
        this.exerciseList = newExerciseList;
        notifyDataSetChanged();  // Notify the adapter that the data has changed
    }

    /**
     * ViewHolder class to hold the views for each exercise item. This helps in recycling
     * views and improving performance.
     */
    public static class WorkoutViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, muscleGroupTextView, typeTextView, setsTextView, repsTextView, difficultyTextView;

        /**
         * Constructor that initializes the view references for each exercise item.
         *
         * @param itemView the view representing a single item in the RecyclerView
         */
        public WorkoutViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.exerciseName);
            muscleGroupTextView = itemView.findViewById(R.id.exerciseMuscleGroup);
            typeTextView = itemView.findViewById(R.id.exerciseType);
            setsTextView = itemView.findViewById(R.id.exerciseSets);
            repsTextView = itemView.findViewById(R.id.exerciseReps);
            difficultyTextView = itemView.findViewById(R.id.exerciseDifficulty);
        }
    }
}
