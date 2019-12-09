package pl.wut.wsd.dsm.agent.trust_factor.persistence.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "customer_trust")
public class CustomerTrust extends Identifiable<Long> {


    @Column(name = "customer_id", unique = true)
    private Long customerId;

    @Setter
    @Column(name = "current_value")
    private Double currentValue;

    @Setter
    @Column(name = "kws_processed")
    private Double kwsProcessed;

    public static CustomerTrust forCustomer(final Long customerId) {
        return new CustomerTrust(customerId, 0.0, 0.0);
    }
}
