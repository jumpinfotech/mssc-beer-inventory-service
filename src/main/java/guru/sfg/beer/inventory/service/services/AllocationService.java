package guru.sfg.beer.inventory.service.services;


import guru.sfg.brewery.model.BeerOrderDto;

/**
 * Created by jt on 2019-09-09.
 */
// this service is responsible for allocating inventory to our beer orders.
public interface AllocationService {

    // return true if we allocate everything, false if we are short on allocation 
    Boolean allocateOrder(BeerOrderDto beerOrderDto);
}
