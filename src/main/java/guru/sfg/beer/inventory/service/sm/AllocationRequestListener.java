package guru.sfg.beer.inventory.service.sm;


import guru.sfg.beer.inventory.service.config.JmsConfig;
import guru.sfg.beer.inventory.service.services.AllocationService;
import guru.sfg.brewery.model.BeerOrderDto;
import guru.sfg.brewery.model.events.AllocateBeerOrderRequest;
import guru.sfg.brewery.model.events.AllocateBeerOrderResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component
@RequiredArgsConstructor
@Slf4j
public class AllocationRequestListener
{
    private final AllocationService allocationService;
    private final JmsTemplate jmsTemplate;

    @JmsListener( destination = JmsConfig.ALLOCATE_ORDER_QUEUE)
    public void listen( AllocateBeerOrderRequest event){

        UUID beerOrderId = event.getBeerOrderDto().getId();
        BeerOrderDto beerOrder = event.getBeerOrderDto();

        Boolean pendingInventory = false;
        Boolean allocationErrors = false;
        try{
            Boolean allocationSucceeded = allocationService.allocateOrder( beerOrder );
            if(allocationSucceeded){

            }else{
                pendingInventory = true;
            }
        }catch ( Exception e ){
            log.error( "Allocation failed for order: " + beerOrderId );
            allocationErrors = true;
        }

        jmsTemplate.convertAndSend( JmsConfig.ALLOCATE_ORDER_RESPONSE_QUEUE, AllocateBeerOrderResult.builder()
                                                                                                    .hasAllocationErrors( allocationErrors )
                                                                                                    .hasPendingInventory( pendingInventory )
                                                                                                    .beerOrderDto( beerOrder )
                                                                                                    .build() );
    }
}
