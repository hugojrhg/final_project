package com.dev.week7.model.order;

import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.product.Product;
import lombok.Data;

import java.util.List;

@Data
public class OrdersDTO {

    private Customer customer;
    private List<Product> products;
    private Integer address;

}
