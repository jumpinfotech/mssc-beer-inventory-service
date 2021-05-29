package guru.sfg.beer.inventory.service.services;

import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.BeerOrderLineDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jt on 2019-09-09.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class AllocationServiceImpl implements AllocationService {

    private final BeerInventoryRepository beerInventoryRepository;

    @Override
    public Boolean allocateOrder(BeerOrderDto beerOrderDto) { // BeerOrderDto was added to guru.sfg.brewery.model
        log.debug("Allocating OrderId: " + beerOrderDto.getId());

        AtomicInteger totalOrdered = new AtomicInteger();
        AtomicInteger totalAllocated = new AtomicInteger();
        
        // BeerOrderDto has a set of BeerOrderLines on it, for each BeerOrderLine if we need to allocate it then we attempt to allocate>simplistic allocation 
        beerOrderDto.getBeerOrderLines().forEach(beerOrderLine -> {
            if ((((beerOrderLine.getOrderQuantity() != null ? beerOrderLine.getOrderQuantity() : 0)
                    - (beerOrderLine.getQuantityAllocated() != null ? beerOrderLine.getQuantityAllocated() : 0)) > 0)) {
                // we need to allocate the BeerOrderLine 
                allocateBeerOrderLine(beerOrderLine);
            }
            totalOrdered.set(totalOrdered.get() + beerOrderLine.getOrderQuantity());
            totalAllocated.set(totalAllocated.get() + (beerOrderLine.getQuantityAllocated() != null ? beerOrderLine.getQuantityAllocated() : 0));
        });

        log.debug("Total Ordered: " + totalOrdered.get() + " Total Allocated: " + totalAllocated.get());

        // if totalOrdered + totalAllocated are the same return back true> means we had a complete allocation > this is for complete or partial allocation logic 
        return totalOrdered.get() == totalAllocated.get();
    }

    // allocate + we also track the amount that we've ordered + allocated - a simple allocation 
    private void allocateBeerOrderLine(BeerOrderLineDto beerOrderLine) {
        List<BeerInventory> beerInventoryList = beerInventoryRepository.findAllByUpc(beerOrderLine.getUpc());
        // get a list of inventory records>we can have more than 1 inventory record for a given beer + our service. 
        // This method updates those inventory records>as the stock level lowers we delete + remove records (if we completely deplete the stock from it).
        // We're assigning inventory to the order + simultaneously removing inventory from our database.

        beerInventoryList.forEach(beerInventory -> {
            int inventory = (beerInventory.getQuantityOnHand() == null) ? 0 : beerInventory.getQuantityOnHand();
            int orderQty = (beerOrderLine.getOrderQuantity() == null) ? 0 : beerOrderLine.getOrderQuantity(); // BeerOrderLine how many we have ordered
            int allocatedQty = (beerOrderLine.getQuantityAllocated() == null) ? 0 : beerOrderLine.getQuantityAllocated(); // BeerOrderLine how many we already have allocated
            int qtyToAllocate = orderQty - allocatedQty; // beers still needed to meet our order quantity

            if (inventory >= qtyToAllocate) { // full allocation - we can allocate everything
                inventory = inventory - qtyToAllocate; // inventory stock now has our ordered beers removed from it
                beerOrderLine.setQuantityAllocated(orderQty); // our order is allocated
                beerInventory.setQuantityOnHand(inventory); // update the beerInventory, it will be less because of our order

                beerInventoryRepository.save(beerInventory);
            } else if (inventory > 0) { //partial allocation, we can't fully meet the order
                beerOrderLine.setQuantityAllocated(allocatedQty + inventory); // take all available inventory, our orderQty wasn't met
                beerInventory.setQuantityOnHand(0); // there is no more inventory

                beerInventoryRepository.delete(beerInventory); // inventory is 0, so delete beerInventory record 
            }
        });

    }
}
