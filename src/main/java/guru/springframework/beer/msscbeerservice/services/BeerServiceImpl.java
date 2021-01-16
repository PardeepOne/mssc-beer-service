package guru.springframework.beer.msscbeerservice.services;

import guru.springframework.beer.msscbeerservice.domain.Beer;
import guru.springframework.beer.msscbeerservice.repositories.BeerRepository;
import guru.springframework.beer.msscbeerservice.services.brewing.BeerService;
import guru.springframework.beer.msscbeerservice.web.controller.NotFoundException;
import guru.springframework.beer.msscbeerservice.web.mappers.BeerMapper;
import guru.springframework.beer.msscbeerservice.web.model.BeerDto;
import guru.springframework.beer.msscbeerservice.web.model.BeerPagedList;
import guru.springframework.beer.msscbeerservice.web.model.BeerStyleEnum;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class BeerServiceImpl implements BeerService {
    private final BeerRepository beerRepository;
    private final BeerMapper beerMapper;

    @Cacheable(cacheNames = "beerCache",key = "#beerId",condition = "#showInventoryOnHand == false")
    @Override
    public BeerDto getById(UUID beerId, boolean showInventoryOnHand) {
        log.info(" ** Called guru.springframework.beer.msscbeerservice.services.BeerServiceImpl.getById **");
        return (showInventoryOnHand) ? beerMapper.beerToBeerDtoWithInventory(beerRepository.findById(beerId).orElseThrow(NotFoundException::new)) : beerMapper.beerToBeerDto(beerRepository.findById(beerId).orElseThrow(NotFoundException::new));
    }

    @Override
    public BeerDto updateBeer(UUID beerId, BeerDto beerDto) {
        Beer beer = beerRepository.findById(beerId).orElseThrow(NotFoundException::new);

        beer.setBeerName(beerDto.getBeerName());
        beer.setBeerStyle(beerDto.getBeerStyle().name());
        beer.setPrice(beerDto.getPrice());
        beer.setUpc(beerDto.getUpc());

        return beerMapper.beerToBeerDto(
                beerRepository.save(beer)
        );
    }

    @Override
    public BeerDto saveNewBeer(BeerDto beerDto) {

        return beerMapper.beerToBeerDto(beerRepository.save(beerMapper.beerDtoToBeer(beerDto)));
    }

    @Cacheable(cacheNames = "beerListCache",condition = "#showInventoryOnHand == false")
    @Override
    public BeerPagedList listBeers(String beerName, BeerStyleEnum beerStyle, PageRequest pageRequest, boolean showInventoryOnHand) {
        log.info(" ** Called guru.springframework.beer.msscbeerservice.services.BeerServiceImpl.listBeers **");
        BeerPagedList beerPagedList;
        Page<Beer> beerPage;

        if (!StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search both
            beerPage = beerRepository.findAllByBeerNameAndBeerStyle(beerName, beerStyle, pageRequest);
        } else if (!StringUtils.isEmpty(beerName) && StringUtils.isEmpty(beerStyle)) {
            //search beer_service name
            beerPage = beerRepository.findAllByBeerName(beerName, pageRequest);
        } else if (StringUtils.isEmpty(beerName) && !StringUtils.isEmpty(beerStyle)) {
            //search by beer style
            beerPage = beerRepository.findAllByBeerStyle(beerStyle, pageRequest);
        } else {
            beerPage = beerRepository.findAll(pageRequest);
        }

        beerPagedList = new BeerPagedList(
                beerPage
                        .getContent()
                        .stream()
                        .map(beer -> (showInventoryOnHand) ? beerMapper.beerToBeerDtoWithInventory(beer) : beerMapper.beerToBeerDto(beer))
                        .collect(Collectors.toList()),
                PageRequest
                        .of(beerPage.getPageable().getPageNumber(),
                                beerPage.getPageable().getPageSize()),
                beerPage.getTotalElements()
        );
        return beerPagedList;
    }

    @Cacheable(cacheNames = "beerUpcCache",key = "#upc")
    @Override
    public BeerDto getByUpc(String upc) {
        log.info(" ** Called guru.springframework.beer.msscbeerservice.services.BeerServiceImpl.getByUpc **");
        return beerMapper.beerToBeerDto(beerRepository.findByUpc(upc).orElseThrow(NotFoundException::new));
    }
}
