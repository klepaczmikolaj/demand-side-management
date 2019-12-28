package pl.wut.wsd.dsm.agent.network_advisor.weather.model;

import com.google.gson.annotations.SerializedName;
import lombok.Data;

@Data
public class Cloudiness {

    @SerializedName("all")
    private double percentage;
}
