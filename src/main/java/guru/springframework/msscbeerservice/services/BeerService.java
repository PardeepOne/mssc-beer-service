package guru.springframework.msscbeerservice.services;

import guru.springframework.msscbeerservice.web.model.BeerDto;

import java.util.UUID;

public interface BeerService {
    BeerDto getById(UUID beerId);

    BeerDto updateBeer(UUID beerId, BeerDto beerDto);

    BeerDto saveNewBeer(BeerDto beerDto);
}
