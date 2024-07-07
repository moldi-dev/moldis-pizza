package org.moldidev.moldispizza.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import org.moldidev.moldispizza.dto.OrderDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import com.stripe.param.checkout.SessionCreateParams;

@Service
public class PaymentService {

    @Value("${stripe.api.key}")
    private String stripeSecretKey;

    public String createPaymentLink(OrderDTO order) throws StripeException {
        Stripe.apiKey = stripeSecretKey;

        long unitAmout = Math.round(order.totalPrice()) * 100;

        SessionCreateParams.LineItem lineItem = SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(
                        SessionCreateParams.LineItem.PriceData.builder()
                                .setCurrency("usd")
                                .setUnitAmount(unitAmout)
                                .setProductData(
                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                .setName("Moldi's Pizza")
                                                .build()
                                )
                                .build()
                )
                .build();

        SessionCreateParams params = SessionCreateParams
                .builder()
                .addPaymentMethodType(SessionCreateParams.PaymentMethodType.CARD)
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl("http://localhost:3000/payment-success?id=" + order.orderId())
                .setCancelUrl("http://localhost:3000/payment-cancel?id=" + order.orderId())
                .addLineItem(lineItem)
                .build();

        Session session = Session.create(params);

        return session.getUrl();
    }
}
