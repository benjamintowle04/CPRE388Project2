package com.example.getfit.util;

import com.example.getfit.models.Exercise;
import com.example.getfit.models.User;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * The {@link WorkoutParser} class provides methods to interact with workout data.
 * It includes methods for fetching workout data from a remote server, parsing it into a list of exercises,
 * filtering exercises based on user preferences, and adding new exercises to the system.
 */
public class WorkoutParser {

    // Local list to store user-added exercises
    private static ArrayList<Exercise> localExerciseList = new ArrayList<>();

    /**
     * Filters the list of exercises based on the user's intensity level and workout type (strength or cardio).
     *
     * @param exercises the list of exercises to be filtered
     * @param user      the user whose preferences will be used for filtering
     * @return a filtered list of exercises that match the user's intensity level and workout type
     */
    public static ArrayList<Exercise> filterByIntensity(ArrayList<Exercise> exercises, User user) {
        ArrayList<Exercise> filteredList = new ArrayList<>();

        // Get the user's selected intensity level (Beginner, Moderate, Hard-core)
        String userIntensityLevel = user.getIntensityLevel();

        // Determine whether the user needs to bulk or lose weight based on their target weight
        String workoutType = getWorkoutTypeBasedOnWeight(user);

        // Loop through the exercises and filter them based on intensity level and workout type
        for (Exercise exercise : exercises) {
            // Filter by intensity level first
            if (exercise.getDifficulty().equalsIgnoreCase("easy") && userIntensityLevel.equalsIgnoreCase("Beginner")) {
                filteredList.add(exercise);
            } else if (exercise.getDifficulty().equalsIgnoreCase("medium") && userIntensityLevel.equalsIgnoreCase("Moderate")) {
                filteredList.add(exercise);
            } else if (exercise.getDifficulty().equalsIgnoreCase("hard") && userIntensityLevel.equalsIgnoreCase("Hard-core")) {
                filteredList.add(exercise);
            }

            // Filter by workout type (strength or cardio) based on weight goals
            if (!exercise.getType().equalsIgnoreCase(workoutType)) {
                filteredList.remove(exercise);  // Remove exercises that don't match the type (strength or cardio)
            }
        }

        return filteredList;
    }

    /**
     * Retrieves a list of all exercises, including both remote and locally stored exercises.
     *
     * @return an ArrayList of all exercises, both from the server and locally stored
     */
    public static ArrayList<Exercise> getAllExercises() {
        // Add mock exercises from the server
        ArrayList<Exercise> allExercises = new ArrayList<>();
        fetchWorkouts(new WorkoutCallback() {
            @Override
            public void onSuccess(ArrayList<Exercise> exercises) {
                allExercises.addAll(exercises);
            }

            @Override
            public void onError(String errorMessage) {
                // Handle error (you could show a toast here if needed)
            }
        });

        // Add locally stored exercises to the list
        allExercises.addAll(localExerciseList);
        return allExercises;
    }

    /**
     * Fetches workout data from a remote server and returns it via a callback.
     *
     * @param callback the callback to handle the result of the API request
     */
    public static void fetchWorkouts(final WorkoutCallback callback) {
        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();
        String API_URL = "https://9879705f-b245-492c-8986-8a8535a7e1b4.mock.pstmn.io/exercise";

        // Build the request to the API
        Request request = new Request.Builder()
                .url(API_URL) // Set the URL
                .build();

        // Execute the request asynchronously (to avoid blocking the main thread)
        client.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Handle failure (e.g., network issues)
                e.printStackTrace();
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                // Check if the response is successful
                if (response.isSuccessful()) {
                    // Get the response body as a string
                    String responseBody = response.body().string();

                    try {
                        // Parse the response into a JSON array
                        JSONArray jsonResponse = new JSONArray(responseBody);
                        // Process the JSON data (parse exercises into an ArrayList)
                        ArrayList<Exercise> exercises = parseExercises(jsonResponse);
                        // Pass the resulting list of exercises to the callback
                        callback.onSuccess(exercises);
                    } catch (Exception e) {
                        e.printStackTrace();
                        callback.onError("Failed to parse data.");
                    }
                } else {
                    // Handle unsuccessful response (e.g., server error)
                    System.out.println("Error: " + response.code());
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    /**
     * Determines the type of workout (strength or cardio) based on the user's weight and target weight.
     *
     * @param user the user whose weight and target weight are used to determine workout type
     * @return a string representing the workout type ("strength" or "cardio")
     */
    public static String getWorkoutTypeBasedOnWeight(User user) {
        if (user.getWeight() < user.getTargetWeight()) {
            // If the user's weight is less than the target, they should be on a bulk (strength) plan
            return "strength";
        } else {
            // If the user's weight is greater than or equal to the target, they should focus on cardio
            return "cardio";
        }
    }

    /**
     * Parses a JSON array into a list of {@link Exercise} objects.
     *
     * @param jsonResponse the JSON array containing exercise data
     * @return an ArrayList of parsed {@link Exercise} objects
     */
    private static ArrayList<Exercise> parseExercises(JSONArray jsonResponse) {
        ArrayList<Exercise> exercisesList = new ArrayList<>();
        try {
            for (int i = 0; i < jsonResponse.length(); i++) {
                JSONObject jsonObj = jsonResponse.getJSONObject(i);
                int id = jsonObj.getInt("id");
                String name = jsonObj.getString("name");
                String muscleGroup = jsonObj.getString("muscleGroup");
                String type = jsonObj.getString("type");
                int sets = jsonObj.getInt("sets");
                Object reps = jsonObj.get("reps");
                String difficulty = jsonObj.getString("difficulty");

                Exercise exercise = new Exercise(id, name, muscleGroup, type, sets, reps, difficulty);
                exercisesList.add(exercise);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return exercisesList;
    }

    /**
     * Adds a new exercise to the remote server.
     *
     * @param exercise the exercise to be added
     * @param callback the callback to handle the result of the API request
     */
    public static void addExercise(Exercise exercise, final WorkoutCallback callback) {
        // Define the API URL for adding an exercise
        String API_URL = "https://9879705f-b245-492c-8986-8a8535a7e1b4.mock.pstmn.io/addExercise";

        // Convert the Exercise object to a JSON object
        JSONObject exerciseJson = new JSONObject();
        try {
            exerciseJson.put("id", exercise.getId());
            exerciseJson.put("name", exercise.getName());
            exerciseJson.put("muscleGroup", exercise.getMuscleGroup());
            exerciseJson.put("type", exercise.getType());
            exerciseJson.put("sets", exercise.getSets());
            exerciseJson.put("reps", exercise.getReps());
            exerciseJson.put("difficulty", exercise.getDifficulty());
        } catch (Exception e) {
            e.printStackTrace();
            callback.onError("Failed to create JSON for exercise.");
            return;
        }

        // Create an OkHttpClient instance
        OkHttpClient client = new OkHttpClient();

        // Create the request body
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");
        RequestBody body = RequestBody.create(exerciseJson.toString(), JSON);

        // Build the request to the API (POST request)
        Request request = new Request.Builder()
                .url(API_URL)
                .post(body)  // POST the JSON object
                .build();

        // Execute the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                // Handle failure (e.g., network issues)
                e.printStackTrace();
                callback.onError(e.getMessage());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                if (response.isSuccessful()) {
                    // Handle successful response
                    callback.onSuccess(null);  // You can pass null or an empty list since no data is returned
                } else {
                    // Handle unsuccessful response
                    System.out.println("Error: " + response.code());
                    callback.onError("Error: " + response.code());
                }
            }
        });
    }

    /**
     * Callback interface to handle the result of workout data fetching or adding.
     */
    public interface WorkoutCallback {
        /**
         * Called when the workout data is successfully fetched or added.
         *
         * @param exercises a list of exercises, or null if no data is returned
         */
        void onSuccess(ArrayList<Exercise> exercises);

        /**
         * Called when there is an error in fetching or adding workout data.
         *
         * @param errorMessage the error message describing what went wrong
         */
        void onError(String errorMessage);
    }
}

