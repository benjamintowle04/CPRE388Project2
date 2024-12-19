package com.example.getfit.util;

import android.util.Log;

import com.example.getfit.R;
import com.example.getfit.models.DiningCenter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility class for fetching dining center data from an external API and parsing it into a list of DiningCenter objects.
 * This class makes an HTTP request to retrieve dining center information and converts the JSON response into usable data.
 */
public class DiningCenterFetcher {

    // The API endpoint to fetch the dining center data
    private static final String API_URL = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-locations/?time=";

    /**
     * Fetches a list of dining centers from the API and returns them as a list of DiningCenter objects.
     *
     * @return a list of DiningCenter objects representing the available dining centers
     */
    public static List<DiningCenter> fetchDiningCenters() {
        List<DiningCenter> diningCenters = new ArrayList<>();
        OkHttpClient client = new OkHttpClient();

        // Build the HTTP request to the API
        Request request = new Request.Builder()
                .url(API_URL)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                String responseData = response.body().string();
                // Parse the JSON response into DiningCenter objects
                diningCenters = parseDiningCenters(responseData);
            } else {
                Log.e("DiningCenterFetcher", "Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return diningCenters;
    }

    /**
     * Parses the JSON response string and converts it into a list of DiningCenter objects.
     *
     * @param responseData the raw JSON string response from the API
     * @return a list of DiningCenter objects representing the dining centers
     */
    public static List<DiningCenter> parseDiningCenters(String responseData) {
        List<DiningCenter> diningCenters = new ArrayList<>();

        try {
            // Parse the JSON array of dining centers
            JSONArray diningArray = new JSONArray(responseData);

            // Iterate over each dining center in the JSON array
            for (int i = 0; i < diningArray.length(); i++) {
                JSONObject diningObject = diningArray.getJSONObject(i);

                // Extract the title and slug of the dining center
                String title = diningObject.getString("title");
                String slug = diningObject.getString("slug");

                // Extract the location type from the locationType array
                JSONArray locationTypeArray = diningObject.getJSONArray("locationType");
                String locationType = locationTypeArray.length() > 0 ? locationTypeArray.getString(0) : "No location type available";

                // Create a new DiningCenter object and add it to the list
                DiningCenter diningCenter = new DiningCenter(title, locationType, R.drawable.isudining, slug);
                diningCenters.add(diningCenter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return diningCenters;
    }
}
