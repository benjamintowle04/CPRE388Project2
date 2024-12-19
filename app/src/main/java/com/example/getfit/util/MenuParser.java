package com.example.getfit.util;

import android.util.Log;

import com.example.getfit.models.MenuItem;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Utility class for fetching and parsing menu data from the dining API.
 * <p>
 * This class is responsible for making an HTTP request to the dining service API to fetch
 * menu data for a specific dining center, based on its slug. It parses the JSON response
 * from the API and returns a list of {@link MenuItem} objects containing the menu items and their
 * calorie information.
 * </p>
 */
public class MenuParser {

    /**
     * Fetches the menu data for a specific dining center using its slug.
     * <p>
     * This method constructs the API URL with the provided slug and makes a request to the dining API.
     * It then parses the response to extract relevant menu items and returns them as a list of {@link MenuItem} objects.
     * </p>
     *
     * @param slug the slug representing a specific dining center
     * @return an ArrayList of {@link MenuItem} objects containing the menu items and their calorie information
     */
    public static ArrayList<MenuItem> fetchMenuData(String slug) {
        // The base URL for the dining center menu API
        String url = "https://www.dining.iastate.edu/wp-json/dining/menu-hours/get-single-location/?";

        // List to store the menu items
        ArrayList<MenuItem> menus = new ArrayList<MenuItem>();
        OkHttpClient client = new OkHttpClient();

        Log.d("Slug", "Slug during the API call is: " + slug);

        // Customize the request using the slug string for the specific location
        url = url + "slug=" + slug + "&time=";

        // Build the request
        Request request = new Request.Builder()
                .url(url)
                .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful()) {
                // Parse the JSON response and extract menu items
                String responseData = response.body().string();
                menus = parseMenus(responseData);
            } else {
                Log.e("DiningCenterFetcher", "Request failed with code: " + response.code());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return menus;
    }

    /**
     * Parses the JSON response from the dining API and extracts menu items.
     * <p>
     * This method processes the JSON response to extract the name and calorie count of each menu item
     * from the API's response structure. It iterates through the menu categories and collects the data
     * into {@link MenuItem} objects.
     * </p>
     *
     * @param jsonResponse the raw JSON response from the API
     * @return an ArrayList of {@link MenuItem} objects containing the menu items and their calorie information
     */
    public static ArrayList<MenuItem> parseMenus(String jsonResponse) {
        ArrayList<MenuItem> menuItems = new ArrayList<>();

        try {
            // Parse the JSON response to get the menu data
            JSONArray responseJson = new JSONArray(jsonResponse);
            JSONObject responseJSONObject = responseJson.getJSONObject(0);
            JSONArray menusJson = responseJSONObject.getJSONArray("menus");
            JSONObject selectedMenu = menusJson.getJSONObject(0);
            JSONArray menuDisplays = selectedMenu.getJSONArray("menuDisplays");

            // Iterate through each menu display to extract categories and menu items
            for (int k = 0; k < menuDisplays.length(); k++) {
                JSONObject display = menuDisplays.getJSONObject(k);
                JSONArray categories = display.getJSONArray("categories");

                // Iterate through categories and extract "menuItems" for each category
                for (int i = 0; i < categories.length(); i++) {
                    JSONObject categoryObject = categories.getJSONObject(i);

                    // Extract the menuItems array from the category
                    JSONArray menuItemsArray = categoryObject.getJSONArray("menuItems");

                    // Iterate through each menu item and extract its details
                    for (int j = 0; j < menuItemsArray.length(); j++) {
                        JSONObject menuItemObject = menuItemsArray.getJSONObject(j);
                        String name = menuItemObject.getString("name");
                        int totalCal = menuItemObject.getInt("totalCal");

                        // Create MenuItem object and add to the list
                        MenuItem menuItem = new MenuItem(name, totalCal);
                        menuItems.add(menuItem);
                    }
                }
            }

            return menuItems;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
