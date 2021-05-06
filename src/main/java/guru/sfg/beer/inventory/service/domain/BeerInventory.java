package guru.sfg.beer.inventory.service.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Entity
public class BeerInventory extends BaseEntity{

    @Builder
    public BeerInventory(UUID id, Long version, Timestamp createdDate, Timestamp lastModifiedDate, UUID beerId,
                         String upc, Integer quantityOnHand) {
        super(id, version, createdDate, lastModifiedDate);
        this.beerId = beerId;
        this.upc = upc;
        this.quantityOnHand = quantityOnHand;
    }
// Takes in upc + beerId>key elements for a unique beer record, avoiding carrying in data existing in other microservices.
// To find BeerInventory - some users use beerId + others will use upc. Assumption - upc + beerId uniquely identify a beer. 
// BeerId is a unique identifier that we have within our control - within our services. 
// upc is a unique ID not within our control. So it's similar to a surrogate ID used in databases - this surrogate ID should have no business logic + it's unique for us to identify a specific object in the system. 

    private UUID beerId;
    private String upc;
    private Integer quantityOnHand = 0;
}
