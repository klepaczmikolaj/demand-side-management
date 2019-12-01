package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class WeatherForecastEntry {

    @SerializedName("main")
    private BasicWeatherIndicators base;

    private Wind wind;

    @SerializedName("clouds")
    private Cloudiness cloudiness;
}
