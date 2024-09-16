package com.example.nss_ddu.network;

import android.content.Context;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.ScanRequest;
import com.amazonaws.services.dynamodbv2.model.ScanResult;
import com.example.nss_ddu.models.Event;

import java.util.ArrayList;
import java.util.List;

public class DynamoDBHelper {

    private static final String TAG = "DynamoDBHelper";
    private AmazonDynamoDB dynamoDBClient;
    private Context context;

    public DynamoDBHelper(Context context) {
        this.context = context;
        initializeDynamoDBClient();
    }

    private void initializeDynamoDBClient() {
        // Initialize CognitoCredentialsProvider for guest access
        CognitoCachingCredentialsProvider credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                "us-east-1:1e67feee-1c06-4bd0-8a7f-3cde44f0c6b0",
                Regions.US_EAST_1 // Replace with your region
        );

        dynamoDBClient = new AmazonDynamoDBClient(credentialsProvider);
    }

    public List<Event> getEvents() {
        List<Event> eventList = new ArrayList<>();

        try {
            ScanRequest scanRequest = new ScanRequest()
                    .withTableName("nssddu_events");

            ScanResult result = dynamoDBClient.scan(scanRequest);
            // Process the results here and populate the eventList

        } catch (Exception e) {
            Log.e(TAG, "Error fetching events", e);
        }

        return eventList;
    }

}
