package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

import java.util.List;

@Data
public class WeatherForecast {

    @SerializedName("cod")
    private String code;

    private int message;

    private int cnt;

    @SerializedName("list")
    private List<WeatherForecastEntry> entries;

}
