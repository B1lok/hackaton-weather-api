package com.example.hakatonweatherapi.service.impl;

import com.example.hakatonweatherapi.json.WeatherData;
import com.example.hakatonweatherapi.json.WeatherResponse;
import com.example.hakatonweatherapi.service.PredictionService;
import com.example.hakatonweatherapi.web.dto.PredictResponseDto;
import com.google.gson.Gson;
import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;
import lombok.RequiredArgsConstructor;
import org.apache.commons.math.stat.regression.SimpleRegression;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PredictionServiceImpl implements PredictionService {

    private final WebClient webClient;
    @Value("${weather.api.key}")
    private String apiKey;
    private final String type = "hour";
    private final int P = 1;
    private final int D = 1;
    private final int Q = 0;
    private final int m = 0;

    @Override
    public PredictResponseDto predict(double lat, double lon) {
        String type = "hour";
        Long start = LocalDateTime.now().minusDays(360).toEpochSecond(ZoneOffset.UTC);
        Long end = LocalDateTime.now().toEpochSecond(ZoneOffset.UTC);
        Gson gson = new Gson();
        String jsonResponse = webClient.get()
                .uri("/city?lat={lat}&lon={lon}&type={type}&start={start}&end={end}&appid={apiKey}",
                lat, lon, type, start, end, apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
        WeatherResponse weatherResponse = gson.fromJson(jsonResponse, WeatherResponse.class);
        List<Double> tempParams = new ArrayList<>();
        List<Double> humidityParams = new ArrayList<>();
        List<Double> windParams = new ArrayList<>();
        for (WeatherData data : weatherResponse.getList()){
            tempParams.add(data.getMain().getTemp());
            windParams.add(data.getWind().getSpeed());
            humidityParams.add(data.getMain().getHumidity());
        }
        double calculatedTemperature = calculatePrediction(tempParams.stream().mapToDouble(Double::doubleValue).toArray()) - 273.15;
        double calculatedHumidity = calculatePrediction(humidityParams.stream().mapToDouble(Double::doubleValue).toArray());
        double calculatedWind = calculatePrediction(windParams.stream().mapToDouble(Double::doubleValue).toArray());
        return new PredictResponseDto(calculatedTemperature, calculatedHumidity, calculatedWind);
    }

    public double calculatePrediction(double[] data){
        double[] differencedSeries = difference(data);

        int[] arimaParams = getARIMAParameters(differencedSeries);
        int forecastSize = 24;
        ArimaParams params = new ArimaParams(arimaParams[0], arimaParams[1], arimaParams[2],P,D,Q,m);
        ForecastResult forecastResult = Arima.forecast_arima(data, forecastSize, params);
        return forecastResult.getForecast()[forecastSize/2];
    }

    private static double[] difference(double[] series) {
        double[] differencedSeries = new double[series.length - 1];
        for (int i = 1; i < series.length; i++) {
            differencedSeries[i - 1] = series[i] - series[i - 1];
        }
        return differencedSeries;
    }

    private static int[] getARIMAParameters(double[] series) {
        int bestP = 0, bestD = 0, bestQ = 0;
        double bestAIC = Double.MAX_VALUE;
        for (int p = 1; p <= 3; p++) {
            for (int d = 0; d <= 1; d++) {
                for (int q = 1; q <= 3; q++) {
                    double aic = calculateAIC(series, p, d, q);
                    if (aic < bestAIC) {
                        bestAIC = aic;
                        bestP = p;
                        bestD = d;
                        bestQ = q;
                    }
                }
            }
        }

        return new int[]{bestP, bestD, bestQ};
    }

    private static double calculateAIC(double[] series, int p, int d, int q) {
        SimpleRegression regression = new SimpleRegression();
        for (int i = 0; i < series.length; i++) {
            regression.addData(i, series[i]);
        }
        int n = series.length;
        double residuals = regression.getSumSquaredErrors();
        double aic = n * Math.log(residuals / n) + 2 * (p + d + q + 1);

        return aic;
    }
}