// OpenWeatherResponse.java
package com.example.travelbuddy;

import java.util.List;

public class OpenWeatherResponse {
    private Main main;
    private List<Weather> weather;
    private Wind wind;
    private String name;
    private int cod;

    // Getters
    public Main getMain() {
        return main;
    }

    public List<Weather> getWeather() {
        return weather;
    }

    public Wind getWind() {
        return wind;
    }

    public String getName() {
        return name;
    }

    public int getCod() {
        return cod;
    }

    // Setters
    public void setMain(Main main) {
        this.main = main;
    }

    public void setWeather(List<Weather> weather) {
        this.weather = weather;
    }

    public void setWind(Wind wind) {
        this.wind = wind;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCod(int cod) {
        this.cod = cod;
    }

    // Inner class for Main weather data
    public static class Main {
        private double temp;
        private double feels_like;
        private double temp_min;
        private double temp_max;
        private int pressure;
        private int humidity;

        // Getters
        public double getTemp() {
            return temp;
        }

        public double getFeelsLike() {
            return feels_like;
        }

        public double getTempMin() {
            return temp_min;
        }

        public double getTempMax() {
            return temp_max;
        }

        public int getPressure() {
            return pressure;
        }

        public int getHumidity() {
            return humidity;
        }

        // Setters
        public void setTemp(double temp) {
            this.temp = temp;
        }

        public void setFeelsLike(double feels_like) {
            this.feels_like = feels_like;
        }

        public void setTempMin(double temp_min) {
            this.temp_min = temp_min;
        }

        public void setTempMax(double temp_max) {
            this.temp_max = temp_max;
        }

        public void setPressure(int pressure) {
            this.pressure = pressure;
        }

        public void setHumidity(int humidity) {
            this.humidity = humidity;
        }
    }

    // Inner class for Weather description
    public static class Weather {
        private int id;
        private String main;
        private String description;
        private String icon;

        // Getters
        public int getId() {
            return id;
        }

        public String getMain() {
            return main;
        }

        public String getDescription() {
            return description;
        }

        public String getIcon() {
            return icon;
        }

        // Setters
        public void setId(int id) {
            this.id = id;
        }

        public void setMain(String main) {
            this.main = main;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }
    }

    // Inner class for Wind data
    public static class Wind {
        private double speed;
        private int deg;
        private double gust;

        // Getters
        public double getSpeed() {
            return speed;
        }

        public int getDeg() {
            return deg;
        }

        public double getGust() {
            return gust;
        }

        // Setters
        public void setSpeed(double speed) {
            this.speed = speed;
        }

        public void setDeg(int deg) {
            this.deg = deg;
        }

        public void setGust(double gust) {
            this.gust = gust;
        }
    }
}