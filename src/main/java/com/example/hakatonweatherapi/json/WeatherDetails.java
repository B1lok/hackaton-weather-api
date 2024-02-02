package com.example.hakatonweatherapi.json;

import lombok.Data;

@Data
public class WeatherDetails {
    private int id;
    private String main;
    private String description;
    private String icon;
}
