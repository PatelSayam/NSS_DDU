package com.example.nss_ddu.fragments;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nss_ddu.R;
import com.example.nss_ddu.adapters.EventAdapter;
import com.example.nss_ddu.network.DynamoDBHelper;
import com.example.nss_ddu.models.Event;

import java.util.List;

public class homeFragment extends Fragment {

    private RecyclerView recyclerView;
    private EventAdapter eventAdapter;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Initialize event adapter
        eventAdapter = new EventAdapter();
        recyclerView.setAdapter(eventAdapter);

        // Fetch events asynchronously
        DynamoDBHelper dynamoDBHelper = new DynamoDBHelper(getContext());
        dynamoDBHelper.getEvents(new DynamoDBHelper.Callback() {
            @Override
            public void onSuccess(List<Event> events) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        // Update RecyclerView with fetched events
                        eventAdapter.setEvents(events);
                    });
                }
            }

            @Override
            public void onFailure(Exception exception) {
                if (getActivity() != null) {
                    getActivity().runOnUiThread(() -> {
                        Toast.makeText(getContext(), "Failed to fetch events", Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });
    }
}
