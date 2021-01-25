package guru.springframework.msscbeerservice.services.inventory;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Disabled
@SpringBootTest
class BeerInventoryServiceRestTemplateImplTest {
    @Autowired
    BeerInventoryService beerInventoryService;

    @BeforeEach
    void setup(){

    }

    @Test
    void getOnhandInventory(){
        //TODO: to be done with UPC instead of UUID

//        Integer quantityOnhand = beerInventoryService.getOnhandInventory(BeerLoader.BEER_1_UUID);
//
//        System.out.println(quantityOnhand);
    }
}