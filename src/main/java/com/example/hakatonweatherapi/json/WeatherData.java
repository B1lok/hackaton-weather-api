package com.example.hakatonweatherapi.json;

import lombok.Data;

import java.util.List;

@Data
public class WeatherData {
    private long dt;
    private MainData main;
    private WindData wind;
    private CloudsData clouds;
    private List<WeatherDetails> weather;

    @Override
    public String toString() {
        return "WeatherData{" +
                "dt=" + dt +
                ", main=" + main +
                ", wind=" + wind +
                ", clouds=" + clouds +
                ", weather=" + weather +
                '}';
    }
}
