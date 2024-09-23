package com.example.nss_ddu.models;

public class Event {
    private String title;
    private String description;
    private String registrationLink;
    private String date;
    private String venue;
    private String time;

    // Constructor
    public Event(String title, String description, String date, String venue, String time) {
        this.title = this.title;
        this.description = this.description;
        this.registrationLink = registrationLink;
        this.date = this.date;
        this.venue = this.venue;
        this.time = this.time;
    }

    // Getters
    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getRegistrationLink() {
        return registrationLink;
    }

    public String getDate() {
        return date;
    }

    public String getVenue() {
        return venue;
    }

    public String getTime() {
        return time;
    }

    // toString for debugging
    @Override
    public String toString() {
        return "Event{" +
                "title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", registrationLink='" + registrationLink + '\'' +
                ", date='" + date + '\'' +
                ", venue='" + venue + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
