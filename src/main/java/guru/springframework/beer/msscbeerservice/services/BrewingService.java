package guru.springframework.beer.msscbeerservice.services;

import guru.springframework.beer.msscbeerservice.config.JmsConfig;
import guru.springframework.beer.msscbeerservice.domain.Beer;
import guru.springframework.beer.common.events.BrewBeerEvent;
import guru.springframework.beer.msscbeerservice.repositories.BeerRepository;
import guru.springframework.beer.msscbeerservice.services.inventory.BeerInventoryService;
import guru.springframework.beer.msscbeerservice.web.mappers.BeerMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class BrewingService {
    private final BeerRepository beerRepository;
    private final BeerInventoryService beerInventoryService;
    private final JmsTemplate jmsTemplate;
    private final BeerMapper beerMapper;

    @Scheduled(fixedRate = 5000)//5 seconds
    public void checkForLowInventory(){
        List<Beer> beers = beerRepository.findAll();

        beers.forEach(beer -> {
            Integer inventoryQOH = beerInventoryService.getOnhandInventory(beer.getId());
            log.debug("Min Onhand is: "+beer.getMinOnHand());
            log.debug("Inventory is: "+inventoryQOH);

            if(beer.getMinOnHand() >= inventoryQOH){
                jmsTemplate.convertAndSend(JmsConfig.BREWING_REQUEST_QUEUE,new BrewBeerEvent(beerMapper.beerToBeerDto(beer)));
            }
        });
    }
}
