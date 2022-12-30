package com.dev.week7.service;

import com.dev.week7.exceptions.OrderNotFoundException;
import com.dev.week7.model.order.Orders;
import com.dev.week7.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {

    @Autowired
    OrdersRepository ordersRepository;

    public List<Orders> getAllOrders() {
        return ordersRepository.findAll();
    }

    public Orders getOrdersById(Long id) {
        return ordersRepository.findById(id).orElseThrow(OrderNotFoundException::new);
    }

    public Orders createOrders(Orders order) {
        return ordersRepository.save(order);
    }

    public void deleteOrders(Long id) {
        ordersRepository.delete(getOrdersById(id));
    }

    public Orders updateOrders(Orders orders, Long id) {
        Orders oldOrders = getOrdersById(id);


        oldOrders.setAddress(orders.getAddress());

        return ordersRepository.save(oldOrders);
    }

    public Orders updateOrdersFields(Orders newOrders, Long id) {
        Orders oldOrders = getOrdersById(id);


        if (newOrders.getAddress() != null) {
            oldOrders.setAddress(newOrders.getAddress());
        }

        return ordersRepository.save(oldOrders);
    }


}
