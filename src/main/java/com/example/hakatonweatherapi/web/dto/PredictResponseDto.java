package com.example.hakatonweatherapi.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;
@Data
@AllArgsConstructor
public class PredictResponseDto implements Serializable {
    private double temperature;
    private double humidity;
    private double windSpeed;
}
