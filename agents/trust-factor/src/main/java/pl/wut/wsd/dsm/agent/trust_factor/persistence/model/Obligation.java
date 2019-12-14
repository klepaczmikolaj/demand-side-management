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
@Table(name = "obligation_view")
public class Obligation extends Identifiable<Long> {

    @Column(name = "customer_id")
    private Long customerId;

    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CustomerObligationState state;

    /**
     * Obligation size in kiloWatts.
     */
    @Column(name = "size")
    private double sizeKws;

    /**
     * How many percents of obligation were kept. 100% for KEPT.
     */
    @Column(name = "percentage_kept")
    private double perecentageKept;

    /**
     * Obligation period start time.
     */
    @Column(name = "since")
    private ZonedDateTime since;

    /**
     * Obligation period end time.
     */
    @Column(name = "until")
    private ZonedDateTime until;
}
