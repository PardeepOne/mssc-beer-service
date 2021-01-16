package guru.springframework.beer.common.events;

import guru.springframework.beer.msscbeerservice.web.model.BeerDto;
import lombok.*;

import java.io.Serializable;

@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class BeerEvent implements Serializable {
    private static final long serialVersionUID = 3545736275716912663L;
    private BeerDto beerDto;
}
