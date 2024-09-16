package com.example.nss_ddu.viewmodel;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.nss_ddu.models.Event;

import java.util.ArrayList;
import java.util.List;

public class EventViewModel extends ViewModel {

    private MutableLiveData<List<Event>> events;

    public LiveData<List<Event>> getEvents() {
        if (events == null) {
            events = new MutableLiveData<>();
            loadEvents();
        }
        return events;
    }

    private void loadEvents() {
        // Load events from DynamoDB or other data sources
        List<Event> eventList = new ArrayList<>();
        // Add sample data or load from your database
        events.setValue(eventList);
    }
}
