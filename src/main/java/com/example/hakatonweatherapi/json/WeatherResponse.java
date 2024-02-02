package com.example.hakatonweatherapi.json;

import lombok.Data;

import java.util.List;

@Data
public class WeatherResponse {
    private String message;
    private String cod;
    private int cityId;
    private double calctime;
    private int cnt;
    private List<WeatherData> list;
}
