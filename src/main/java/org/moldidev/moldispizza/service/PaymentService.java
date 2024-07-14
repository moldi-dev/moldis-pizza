package org.moldidev.moldispizza.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    @Value("${frontend.url}")
    private String frontendUrl;

    public String createPaymentLink(OrderDTO order) {
        Stripe.apiKey = stripeSecretKey;

        try {
            List<SessionCreateParams.LineItem> lineItems = order.pizzas().stream().map(pizza -> {
                long unitAmount = Math.round(pizza.price() * 100);

                return SessionCreateParams.LineItem.builder()
                        .setQuantity(1L)
                        .setPriceData(
                                SessionCreateParams.LineItem.PriceData.builder()
                                        .setCurrency("usd")
                                        .setUnitAmount(unitAmount)
                                        .setProductData(
                                                SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                        .setName(pizza.name())
                                                        .build()
                                        )
                                        .build()
                        )
                        .build();
            }).toList();

            SessionCreateParams.Builder paramsBuilder = SessionCreateParams.builder()
                    .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                    .setMode(SessionCreateParams.Mode.PAYMENT)
                    .setSuccessUrl(frontendUrl + "/payment-success?id=" + order.orderId())
                    .setCancelUrl(frontendUrl + "/payment-cancel");

            for (SessionCreateParams.LineItem lineItem : lineItems) {
                paramsBuilder.addLineItem(lineItem);
            }

            SessionCreateParams params = paramsBuilder.build();
            Session session = Session.create(params);

            return session.getUrl();
        }

        catch (StripeException e) {
            throw new RuntimeException("Cannot create the Stripe payment");
        }
    }
}
