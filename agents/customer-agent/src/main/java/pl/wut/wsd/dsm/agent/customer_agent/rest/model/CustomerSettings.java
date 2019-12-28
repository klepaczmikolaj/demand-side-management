package pl.wut.wsd.dsm.agent.customer_agent.rest.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CustomerSettings {
    private Double minimalProfit;
    private String notificationsKey;
}
