package pl.wut.wsd.dsm.agent.customer_handler.model;

import lombok.AccessLevel;
import lombok.Builder;
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
import javax.persistence.FetchType;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.UUID;

@Entity
@Table(name = "offer")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class Offer extends Identifiable<Long> {

    @NonNull
    @Column(name = "offer_id", nullable = false)
    private UUID offerId;

    @NonNull
    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @NonNull
    @Column(name = "valid_until", nullable = false)
    private ZonedDateTime validUntil;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "state", length = 15, nullable = false)
    private State state;

    @NonNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private Type type;

    @NonNull
    @Column(name = "size", nullable = false)
    private Double kws;

    @NonNull
    @Column(name = "price", nullable = false)
    private BigDecimal pricePerKw;

    @NonNull
    @Column(name = "demand_change_since", nullable = false)
    private ZonedDateTime demandChangeSince;

    @NonNull
    @Column(name = "demand_change_until", nullable = false)
    private ZonedDateTime demandChangeUntil;

    @Setter(AccessLevel.PACKAGE)
    @OneToOne(mappedBy = "relatedOffer", fetch = FetchType.LAZY)
    private Obligation obligation;

    public enum State {
        PENDING, ACCEPTED
    }

    public enum Type {
        INCREASE, REDUCTION
    }

    public static Offer reduction(@NonNull final UUID offerId,
                                  @NonNull final Long customerId,
                                  @NonNull final ZonedDateTime validUntil,
                                  @NonNull final Double kws,
                                  @NonNull final BigDecimal pricePerKw,
                                  @NonNull final ZonedDateTime demandChangeSince,
                                  @NonNull final ZonedDateTime demandChangeUntil) {
        return new Offer(offerId, customerId, validUntil, State.PENDING, Type.REDUCTION, kws, pricePerKw, demandChangeSince, demandChangeUntil);
    }

    @Builder(builderMethodName = "increase", buildMethodName = "increase", access = AccessLevel.PUBLIC)
    public static Offer increase(@NonNull final UUID offerId,
                                 @NonNull final Long customerId,
                                 @NonNull final ZonedDateTime validUntil,
                                 @NonNull final Double kws,
                                 @NonNull final BigDecimal pricePerKw,
                                 @NonNull final ZonedDateTime demandChangeSince,
                                 @NonNull final ZonedDateTime demandChangeUntil) {
        return new Offer(offerId, customerId, validUntil, State.PENDING, Type.INCREASE, kws, pricePerKw, demandChangeSince, demandChangeUntil);
    }
}
