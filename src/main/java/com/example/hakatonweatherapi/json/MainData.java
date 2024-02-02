package com.example.hakatonweatherapi.json;

import lombok.Data;

@Data
public class MainData {
    private double temp;
    private double feelsLike;
    private int pressure;
    private double humidity;
    private double tempMin;
    private double tempMax;
}
