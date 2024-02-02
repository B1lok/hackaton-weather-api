package com.example.hakatonweatherapi.json;

import lombok.Data;

@Data
public class WindData {
    private double speed;
    private int deg;
    private double gust;
}
