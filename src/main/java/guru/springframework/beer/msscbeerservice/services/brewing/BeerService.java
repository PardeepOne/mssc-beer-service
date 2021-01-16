package guru.springframework.beer.msscbeerservice.services.brewing;

import guru.springframework.beer.msscbeerservice.web.model.BeerDto;
import guru.springframework.beer.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.beer.msscbeerservice.web.model.BeerStyleEnum;
import org.springframework.data.domain.PageRequest;

import java.util.UUID;

public interface BeerService {
    BeerDto getById(UUID beerId,boolean showInventoryOnHand);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto saveNewBeer(BeerDto beerDto);

    BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest of,boolean showInventoryOnHand);

    BeerDto getByUpc(String upc);
}
