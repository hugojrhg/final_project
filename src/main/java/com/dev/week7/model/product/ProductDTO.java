package com.dev.week7.model.product;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class ProductDTO {

    @Length(max = 50, message = "Product's name can't be bigger than 50 chars")
    private String name;
    private Double price;
    @PositiveOrZero(message = "Quantity can't be less than 0")
    private Integer quantity;

}
