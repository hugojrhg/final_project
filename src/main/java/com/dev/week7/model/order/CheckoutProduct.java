package com.dev.week7.model.order;

import com.dev.week7.exceptions.SoldOutProductException;
import com.dev.week7.model.product.Product;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "checkout_product")
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
public class CheckoutProduct {

    @EmbeddedId
    private CheckoutProductId id;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("checkoutId")
    private Checkout checkout;
    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("productId")
    private Product product;
    @Column
    private Integer quantity;

    public CheckoutProduct(Checkout checkout, Product product, Integer productQuantity) {
        this.checkout = checkout;
        this.product = product;
        this.quantity = productQuantity;
        this.id = new CheckoutProductId(checkout.getId(), product.getId());
    }

    public Double getTotalValue(){
        return this.product.getPrice() * this.quantity;
    }

    public void validProductQuantityInStock(){
        if (this.quantity > this.product.getQuantity()){
            throw new SoldOutProductException();
        }
    }

}
