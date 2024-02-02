package com.example.hakatonweatherapi.web.controller;

import com.example.hakatonweatherapi.service.PredictionService;
import com.example.hakatonweatherapi.web.dto.PredictResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@CrossOrigin
@RequiredArgsConstructor
@RequestMapping("/predict")
public class PredictionController {

    private final PredictionService predictionService;

    @PostMapping("/{lat}/{lon}")
    public ResponseEntity<PredictResponseDto> predictWeather(@PathVariable double lat, @PathVariable double lon){
        return ResponseEntity.status(HttpStatus.OK).body(predictionService.predict(lat, lon));
    }

}
