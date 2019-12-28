package pl.wut.wsd.dsm.agent.trust_factor.persistence.model;

import lombok.Data;
import pl.wut.wsd.dsm.infrastructure.persistence.model.Identifiable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Data
@Entity
@Table(name = "refresh_details")
public class CustomerTrustRefreshDetails extends Identifiable<Long> {

    @Column(name = "last_refresh")
    private ZonedDateTime lastRefreshed;
}
