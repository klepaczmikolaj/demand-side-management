package pl.wut.wsd.dsm.agent.customer_handler.model;

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
import javax.persistence.OneToOne;
import javax.persistence.Table;


@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor
@Entity
@Table(name = "obligation")
public class Obligation extends Identifiable<Long> {

    @NonNull
    @Column(name = "customer_id")
    private Long customerId;

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

    public static Obligation newObligation(final Long customerId, final double acceptedKws, final Offer relatedOffer) {
        final Obligation obligation = new Obligation(customerId, CustomerObligationState.DURING_EVALUATION, acceptedKws, relatedOffer);
        relatedOffer.setObligation(obligation);

        return obligation;
    }

}
