package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import lombok.Data;
import pl.wut.wsd.dsm.agent.network_advisor.infrastructure.json.WeatherApiJsonDateAdapter;

import java.time.ZonedDateTime;

@Data
public class WeatherForecastEntry {

    @SerializedName("main")
    private BasicWeatherIndicators base;

    private Wind wind;

    @SerializedName("dt_txt")
    @JsonAdapter(WeatherApiJsonDateAdapter.class)
    private ZonedDateTime measurementDate;

    @SerializedName("clouds")
    private Cloudiness cloudiness;
}
