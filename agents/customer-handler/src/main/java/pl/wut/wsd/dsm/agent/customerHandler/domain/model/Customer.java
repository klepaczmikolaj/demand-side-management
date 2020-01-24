package pl.wut.wsd.dsm.agent.customerHandler.domain.model;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.ToString;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@Entity
@Table
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Customer extends Identifiable<Long> {

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(nullable = false, unique = true, name = "customer_id")
    private Long customerId;

    @Column(name = "nominal_usage_watts", unique = true, nullable = false)
    private int nominalUsageInWatts;

    @Builder
    private Customer(@NonNull final String login,
                     @NonNull final String name,
                     @NonNull final Long customerId,
                     @NonNull final int nominalUsageInWatts) {
        this.login = login;
        this.name = name;
        this.customerId = customerId;
        this.nominalUsageInWatts = nominalUsageInWatts;
    }
}
