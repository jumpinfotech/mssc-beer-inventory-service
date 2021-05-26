package guru.sfg.beer.inventory.service;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

//  Spring Boot does a ComponentScan from this package + down.
//  We have components in a different package guru.sfg.common.events>can cause problems>for us it's OK - they aren't annotated Spring components.
@SpringBootApplication 
public class BeerInventoryApplication {

    // He deleted BeerInventoryBootstrap class, no longer needed
    public static void main(String[] args) {
        SpringApplication.run(BeerInventoryApplication.class, args);
    }

//  starts mssc-beer-inventory-service 
//  then starts mssc-beer-service>wake up>checks the inventory>didn't find any>generate new inventory records
//  Search for beer inventory records:-
//  SELECT * FROM beerinventoryservice.beer_inventory;
//  MySQLWorkbench>right click on table>Truncate Table...
//  We have no inventory(beer)>mssc-beer-service>I've no inventory (beer)> brew more> we brew more fresh beers>3 NewInventoryEvent's are received.
//  We see the 3 new beers:- SELECT * FROM beerinventoryservice.beer_inventory;
//  Have 2 Spring Boot services + MySQL + ActiveMQ running + interacting.
}
