package org.example.model;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import javax.swing.*;
import java.awt.*;
import java.net.MalformedURLException;

public class BookCoverFetcher {

    private static final String API_URL = "https://bookcover.longitood.com/bookcover";

    public static String getBookCoverURL(String bookTitle, String authorName) {
        String urlString = API_URL + "?book_title=" + bookTitle.replace(" ", "+") + "&author_name=" + authorName.replace(" ", "+");
        try {
            // Create the URL object
            URL url = new URL(urlString);

            // Open connection
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            // Check for a successful response
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                // Read the response
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                String inputLine;
                StringBuilder response = new StringBuilder();

                while ((inputLine = in.readLine()) != null) {
                    response.append(inputLine);
                }
                in.close();

                // Extract the URL from the JSON response
                String jsonResponse = response.toString();
                String imageUrl = jsonResponse.split("\"url\":\"")[1].split("\"")[0];
                return imageUrl;

            } else {
                System.out.println("GET request failed. Response code: " + responseCode);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


}
