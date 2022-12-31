package com.dev.week7.controller;

import com.dev.week7.model.order.Orders;
import com.dev.week7.model.order.OrdersDTO;
import com.dev.week7.service.OrderService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/orders")
public class OrderController {

    @Autowired
    OrderService orderService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<OrdersDTO>> getAllOrders() {
        List<OrdersDTO> ordersDTO =
                orderService.getAllOrders()
                        .stream()
                        .map(order -> mapper.map(order, OrdersDTO.class))
                        .toList();

        return new ResponseEntity<>(ordersDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrdersDTO> getOrderById(@PathVariable Long id) {
        OrdersDTO orderDTO = mapper.map(orderService.getOrdersById(id), OrdersDTO.class);
        return new ResponseEntity<>(orderDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<OrdersDTO> createOrder(@Valid @RequestBody OrdersDTO orderDTO) {
        Orders order = mapper.map(orderDTO, Orders.class);
        orderService.createOrders(order);
        return new ResponseEntity<>(orderDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<OrdersDTO> deleteOrder(@PathVariable Long id) {
        orderService.deleteOrders(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
