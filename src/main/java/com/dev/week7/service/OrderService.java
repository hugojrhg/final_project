package com.dev.week7.service;

import com.dev.week7.exceptions.IncompleteCheckoutException;
import com.dev.week7.exceptions.OrderNotFoundException;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.Orders;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    @Autowired
    CheckoutService checkoutService;

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Orders getOrdersById(Long id) {
        return ordersRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Orders createOrders(Orders order) {
        Checkout checkout = checkoutService.getCheckoutById(order.getCheckout().getId());

        if (checkout.getPaymentMethod() == null || checkout.getZipCode() == null){
            throw new IncompleteCheckoutException();
        }

        if (processOrderPayment(order)){
            checkout.getPaymentMethod().payDebt();
        }

        order.setTotalValue(checkoutService.sumOfProductsValues(checkout));

        return ordersRepository.save(order);
    }

    public void deleteOrders(Long id) {
        ordersRepository.delete(getOrdersById(id));
    }

    public boolean processOrderPayment(Orders order) {
        Checkout checkout = checkoutService.getCheckoutById(order.getCheckout().getId());
        PaymentMethod paymentMethod = checkout.getPaymentMethod();

        Double orderPrice = checkoutService.sumOfProductsValues(checkout);

        paymentMethod.reserveFunds(orderPrice);

        return true;
    }

}
