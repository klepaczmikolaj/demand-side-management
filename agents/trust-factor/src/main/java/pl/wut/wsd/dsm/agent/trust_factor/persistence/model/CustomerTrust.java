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
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor(access = AccessLevel.PACKAGE)
@Entity
@Table(name = "customer_trust")
public class CustomerTrust extends Identifiable<Long> {

    @OneToOne
    @JoinColumn(name = "customer_id", unique = true)
    private Customer customer;

    @Setter
    @Column(name = "current_value")
    private Double currentValue;

    @Setter
    @Column(name = "kws_processed")
    private Double kwsProcessed;

    public static CustomerTrust forCustomer(final Customer customer) {
        return new CustomerTrust(customer, 0.0, 0.0);
    }
}
