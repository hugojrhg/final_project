package com.dev.week7.model.order;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CheckoutProductId implements Serializable {

    @Column
    private Long checkoutId;
    @Column
    private Long productId;
    @Override
    public boolean equals(Object o) {
        if (this == o){
            return true;
        }

        if (o == null || getClass() != o.getClass()){
            return false;
        }


        CheckoutProductId that = (CheckoutProductId) o;
        return Objects.equals(checkoutId, that.checkoutId) &&
                Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(checkoutId, productId);
    }

}
