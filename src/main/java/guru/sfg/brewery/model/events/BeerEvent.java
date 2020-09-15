package guru.sfg.brewery.model.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;


@Data
@AllArgsConstructor
@Builder
//we had to add this noargs constructor because there was an error claiming that it could not instantiate
//the object, so we had to add it to the classes that depend on it and the BeerDto
@NoArgsConstructor
public class BeerEvent
      implements Serializable
{

    static final long serialVersionUID = -2142317658102172056L;
    //when using no args constructor the properties cannot be final
    private BeerDto beerDto;
}
