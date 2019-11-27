package pl.wut.wsd.dsm.ontology.device;

import lombok.Data;

import java.util.List;

@Data
public class GetCurrentConsumptionResponse {
    private double totalConsumtion;
    private List<ElectricDevice> devices;
}
