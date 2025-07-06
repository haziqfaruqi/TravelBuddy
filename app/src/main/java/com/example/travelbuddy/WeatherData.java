package com.example.travelbuddy;

public class WeatherData {
    private String location;
    private double temperature;
    private String weatherType;
    private String description;
    private int humidity;
    private double windSpeed;
    private long timestamp;

    public WeatherData() {}

    public WeatherData(String location, double temperature, String weatherType,
                       String description, int humidity, double windSpeed) {
        this.location = location;
        this.temperature = temperature;
        this.weatherType = weatherType;
        this.description = description;
        this.humidity = humidity;
        this.windSpeed = windSpeed;
        this.timestamp = System.currentTimeMillis();
    }

    // Getters and setters
    public String getLocation() { return location; }
    public void setLocation(String location) { this.location = location; }

    public double getTemperature() { return temperature; }
    public void setTemperature(double temperature) { this.temperature = temperature; }

    public String getWeatherType() { return weatherType; }
    public void setWeatherType(String weatherType) { this.weatherType = weatherType; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public int getHumidity() { return humidity; }
    public void setHumidity(int humidity) { this.humidity = humidity; }

    public double getWindSpeed() { return windSpeed; }
    public void setWindSpeed(double windSpeed) { this.windSpeed = windSpeed; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public String getFormattedTemperature() {
        return Math.round(temperature) + "°C";
    }
}