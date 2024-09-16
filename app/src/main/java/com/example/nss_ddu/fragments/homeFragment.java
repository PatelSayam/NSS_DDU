package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.example.nss_ddu.network.DynamoDBHelper;
import com.example.nss_ddu.models.Event;
import com.example.nss_ddu.databinding.FragmentHomeBinding;
import java.util.List;

public class homeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private DynamoDBHelper dynamoDBHelper;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Initialize DynamoDBHelper
        dynamoDBHelper = new DynamoDBHelper(getContext());

        // Fetch events from DynamoDB
        List<Event> events = dynamoDBHelper.getEvents(); // Assuming this method does not require a callback
        if (events.isEmpty()) {
            Toast.makeText(getContext(), "No events found", Toast.LENGTH_SHORT).show();
        } else {
            // Update UI with the list of events
            // Example: binding.eventList.setAdapter(new EventAdapter(events));
            Toast.makeText(getContext(), "Events fetched successfully", Toast.LENGTH_SHORT).show();
        }
    }
}
