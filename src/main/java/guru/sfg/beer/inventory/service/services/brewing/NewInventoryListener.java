package guru.sfg.beer.inventory.service.services.brewing;

import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.domain.BeerInventory;
import guru.sfg.brewery.model.events.NewInventoryEvent;
import guru.sfg.beer.inventory.service.repositories.BeerInventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;


@Slf4j
@Component
@RequiredArgsConstructor
public class NewInventoryListener
{

    private final BeerInventoryRepository beerInventoryRepository;

    @JmsListener(destination = JmsConfig.NEW_INVENTORY_QUEUE)
    public void listen(NewInventoryEvent event){

        log.debug("Got Inventory: " + event.toString());
        //here we are saving the event that is coming from the beer service
        beerInventoryRepository.save( BeerInventory.builder()
                                                   .beerId(event.getBeerDto().getId())
                                                   .upc(event.getBeerDto().getUpc())
                                                   .quantityOnHand(event.getBeerDto().getQuantityOnHand())
                                                   .build());
    }
}
