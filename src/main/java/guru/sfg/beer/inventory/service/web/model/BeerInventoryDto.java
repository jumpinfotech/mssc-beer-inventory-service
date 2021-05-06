package guru.sfg.beer.inventory.service.web.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BeerInventoryDto {
    private UUID id;
    private OffsetDateTime createdDate;
    private OffsetDateTime lastModifiedDate;
    private UUID beerId;
    // upc added, when returning inventory to a consumer they will received beerId, upc + quantityOnHand. A real world inventory service would have many more properties.
    private String upc;
    private Integer quantityOnHand;
}
