package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Wind {

    /**
     * Wind direction, degrees (meteorological).
     */
    @SerializedName("deg")
    private double degree;

    /**
     * Wind speed. Unit Default: meter/sec, Metric: meter/sec, Imperial: miles/hour.
     */
    private double speed;
}
