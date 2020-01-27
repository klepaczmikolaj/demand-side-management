package pl.wut.wsd.dsm.agent.customerHandler.domain.model;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Entity
@Table(name = "obligation")
public class Obligation extends Identifiable<Long> {

    @NonNull
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;

    @NonNull
    @Column(name = "state")
    @Enumerated(EnumType.STRING)
    private CustomerObligationState state;

    /**
     * Obligation size in kiloWatts.
     */
    @NonNull
    @Column(name = "size")
    private double sizeKws;

    /**
     * How many percents of obligation were kept. 100% for KEPT.
     */
    @Getter
    @Setter
    @Column(name = "percentage_kept")
    private double perecentageKept;

    @NonNull
    @OneToOne
    @JoinColumn(name = "offer_id")
    private Offer relatedOffer;

    public void setPercentageKept(final double percentageKept) {
        this.perecentageKept = percentageKept;
        if (percentageKept >= 100.0) {
            this.state = CustomerObligationState.KEPT;
        } else {
            this.state = CustomerObligationState.FAILED;
        }
    }

    public static Obligation newObligation(final Customer customer, final double acceptedKws, final Offer relatedOffer) {
        final Obligation obligation = new Obligation(customer, CustomerObligationState.DURING_EVALUATION, acceptedKws, relatedOffer);
        relatedOffer.setObligation(obligation);

        return obligation;
    }


}
