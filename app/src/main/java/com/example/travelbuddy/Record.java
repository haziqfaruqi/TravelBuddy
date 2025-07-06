package com.example.travelbuddy;

public class Record {
    private int id;
    private String userId;         // 🔸 new field
    private String imagePath;
    private String dateTime;
    private String locationName;
    private String description;
    private String photoLabel;
    public String latitude;
    public String longitude;

    // Constructor without coordinates (with userId)
    public Record(int id, String userId, String imagePath, String dateTime, String locationName, String description, String photoLabel) {
        this.id = id;
        this.userId = userId;
        this.imagePath = imagePath;
        this.dateTime = dateTime;
        this.locationName = locationName;
        this.description = description;
        this.photoLabel = photoLabel;
        this.latitude = null;
        this.longitude = null;
    }

    // Constructor with coordinates (with userId)
    public Record(int id, String userId, String imagePath, String dateTime, String locationName, String description, String photoLabel, String latitude, String longitude) {
        this.id = id;
        this.userId = userId;
        this.imagePath = imagePath;
        this.dateTime = dateTime;
        this.locationName = locationName;
        this.description = description;
        this.photoLabel = photoLabel;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // Existing constructor for backward compatibility (no userId)
    public Record(int id, String imagePath, String dateTime, String locationName, String description, String photoLabel) {
        this(id, null, imagePath, dateTime, locationName, description, photoLabel);
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDateTime() {
        return dateTime;
    }

    public String getLocationName() {
        return locationName;
    }

    public String getDescription() {
        return description;
    }

    public String getPhotoLabel() {
        return photoLabel;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPhotoLabel(String photoLabel) {
        this.photoLabel = photoLabel;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    // Helper methods for coordinates
    public boolean hasCoordinates() {
        return latitude != null && longitude != null &&
                !latitude.isEmpty() && !longitude.isEmpty() &&
                !latitude.equals("null") && !longitude.equals("null");
    }

    public double getLatitudeAsDouble() {
        if (latitude != null && !latitude.isEmpty() && !latitude.equals("null")) {
            try {
                return Double.parseDouble(latitude);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }

    public double getLongitudeAsDouble() {
        if (longitude != null && !longitude.isEmpty() && !longitude.equals("null")) {
            try {
                return Double.parseDouble(longitude);
            } catch (NumberFormatException e) {
                return 0.0;
            }
        }
        return 0.0;
    }
}
