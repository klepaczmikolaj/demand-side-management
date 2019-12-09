package pl.wut.wsd.dsm.agent.trust_factor.persistence.model;

import lombok.Data;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "OBLIGATION")
public class Obligation extends Identifiable<Long> {

    @Column(name = "CUSTOMER_ID")
    private Long customerId;

    @Column(name = "STATE")
    @Enumerated(EnumType.STRING)
    private CustomerObligationState state;

    /**
     * Obligation size in kiloWatts.
     */
    @Column(name = "SIZE")
    private double sizeKws;

    /**
     * How many percents of obligation were kept. 100% for KEPT.
     */
    @Column(name = "PERCENTAGE_KEPT")
    private double perecentageKept;

    /**
     * Obligation period start time.
     */
    @Column(name = "SINCE")
    private ZonedDateTime since;

    /**
     * Obligation period end time.
     */
    @Column(name = "until")
    private ZonedDateTime until;
}
