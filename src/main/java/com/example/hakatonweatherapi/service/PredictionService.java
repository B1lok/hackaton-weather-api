package com.example.hakatonweatherapi.service;

import com.example.hakatonweatherapi.web.dto.PredictResponseDto;

public interface PredictionService {

    PredictResponseDto predict(double lat, double lon);
}
