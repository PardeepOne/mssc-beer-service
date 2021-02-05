package guru.springframework.msscbeerservice.services.order;

import guru.sfg.brewery.model.events.ValidateOrderRequest;
import guru.sfg.brewery.model.events.ValidateOrderResult;
import guru.springframework.msscbeerservice.config.JmsConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BeerOrderValidationListener {

    //it will check if the beer to validate exists in the DB or not
    private final BeerOrderValidator validator;

    private final JmsTemplate jmsTemplate;

    /**
     * JMS listener listening over the queue JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE for incoming validation requests
     * @param validateOrderRequest
     */
    @JmsListener(destination = JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE)
    public void listen(ValidateOrderRequest validateOrderRequest) {
        //validation of the beer already present in the queue
        Boolean isValid = validator.validateOrder(validateOrderRequest.getBeerOrderDto());

        jmsTemplate.convertAndSend(JmsConfig.VALIDATE_ORDER_RESPONSE_QUEUE,
                ValidateOrderResult.builder().isValid(isValid).orderId(validateOrderRequest.getBeerOrderDto().getId()).build());
    }
}
