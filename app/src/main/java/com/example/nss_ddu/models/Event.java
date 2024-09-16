package com.example.nss_ddu.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Event implements Parcelable {
    private String title;
    private String description;
    private String registrationLink;
    private String date;
    private String venue;
    private String time;

    public Event(String title, String description, String registrationLink, String date, String venue, String time) {
        this.title = title;
        this.description = description;
        this.registrationLink = registrationLink;
        this.date = date;
        this.venue = venue;
        this.time = time;
    }

    protected Event(Parcel in) {
        title = in.readString();
        description = in.readString();
        registrationLink = in.readString();
        date = in.readString();
        venue = in.readString();
        time = in.readString();
    }

    public static final Creator<Event> CREATOR = new Creator<Event>() {
        @Override
        public Event createFromParcel(Parcel in) {
            return new Event(in);
        }

        @Override
        public Event[] newArray(int size) {
            return new Event[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(title);
        dest.writeString(description);
        dest.writeString(registrationLink);
        dest.writeString(date);
        dest.writeString(venue);
        dest.writeString(time);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Getters and Setters
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getRegistrationLink() { return registrationLink; }
    public String getDate() { return date; }
    public String getVenue() { return venue; }
    public String getTime() { return time; }
}
