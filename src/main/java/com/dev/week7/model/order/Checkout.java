package com.dev.week7.model.order;

import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.model.product.Product;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Checkout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "customer_id")
    private Customer customer;
    @OneToMany(mappedBy = "checkout",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<CheckoutProduct> products = new ArrayList<>();
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "payment_method_id")
    private PaymentMethod paymentMethod;
    private Integer zipCode;

    public Checkout(Customer customer, List<CheckoutProduct> products, PaymentMethod paymentMethod, Integer zipCode) {
        this.customer = customer;
        this.products = products;
        this.paymentMethod = paymentMethod;
        this.zipCode = zipCode;
    }

    public void addProduct(Product product, Integer quantity) {
        CheckoutProduct checkoutProduct = new CheckoutProduct(this, product, quantity);
        products.add(checkoutProduct);

    }

    public void removeProduct(Product product) {
        int indexToRemove = 0;

        for (CheckoutProduct checkoutProduct : products) {
            if (checkoutProduct.getProduct().equals(product)) {
                indexToRemove = products.indexOf(checkoutProduct);
            }
        }

        products.remove(indexToRemove);

    }

}
