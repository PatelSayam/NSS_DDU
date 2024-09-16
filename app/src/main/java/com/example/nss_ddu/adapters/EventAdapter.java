package com.example.nss_ddu.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.nss_ddu.R;
import com.example.nss_ddu.models.Event;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder> {
    private List<Event> eventList;
    private OnRegisterClickListener onRegisterClickListener;

    // Constructor accepting the list of events
    public EventAdapter(List<Event> eventList, OnRegisterClickListener onRegisterClickListener) {
        this.eventList = eventList;
        this.onRegisterClickListener = onRegisterClickListener;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_event, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = eventList.get(position);
        holder.bind(event);
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public interface OnRegisterClickListener {
        void onRegisterClick(Event event);
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {
        private TextView titleTextView;
        private TextView descriptionTextView;
        private TextView dateTextView;
        private Button registerButton;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.event_title);
            descriptionTextView = itemView.findViewById(R.id.event_description);
            dateTextView = itemView.findViewById(R.id.event_date);
            registerButton = itemView.findViewById(R.id.register_button);

            registerButton.setOnClickListener(v -> {
                if (onRegisterClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        onRegisterClickListener.onRegisterClick(eventList.get(position));
                    }
                }
            });
        }

        public void bind(Event event) {
            titleTextView.setText(event.getTitle());
            descriptionTextView.setText(event.getDescription());
            dateTextView.setText(event.getDate());
        }
    }
}
