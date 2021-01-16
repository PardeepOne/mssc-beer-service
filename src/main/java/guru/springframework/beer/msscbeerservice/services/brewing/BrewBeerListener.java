package guru.springframework.beer.msscbeerservice.services.brewing;

import guru.springframework.beer.msscbeerservice.config.JmsConfig;
import guru.springframework.beer.msscbeerservice.domain.Beer;
import guru.springframework.beer.common.events.BrewBeerEvent;
import guru.springframework.beer.common.events.NewInventoryEvent;
import guru.springframework.beer.msscbeerservice.repositories.BeerRepository;
import guru.springframework.beer.msscbeerservice.web.model.BeerDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class BrewBeerListener {

    private final BeerRepository beerRepository;
    private final JmsTemplate jmsTemplate;

    @Transactional
    @JmsListener(destination = JmsConfig.BREWING_REQUEST_QUEUE)
    public void listen(BrewBeerEvent event){
        BeerDto beerDto = event.getBeerDto();
        Beer beer = beerRepository.getOne(beerDto.getId());
        beerDto.setQuantityOnHand(beer.getQuantityToBrew());
        NewInventoryEvent newInventoryEvent = new NewInventoryEvent(beerDto);

        log.debug("Brewed beer ("+beer.getMinOnHand()+") => QOH: "+beerDto.getQuantityOnHand());

        //sending message to inventory service
        jmsTemplate.convertAndSend(JmsConfig.NEW_INVENTORY_QUEUE,newInventoryEvent);
    }
}
