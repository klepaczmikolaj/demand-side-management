package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class BasicWeatherIndicators {

    @SerializedName("temp")
    private double temperature;

    /**
     * Minimum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises
     */
    @SerializedName("temp_min")
    private double minTemperature;

    /**
     * Maximum temperature at the moment of calculation. This is deviation from 'temp' that is possible for large cities and megalopolises
     */
    @SerializedName("temp_max")
    private double maxTemperature;


}
