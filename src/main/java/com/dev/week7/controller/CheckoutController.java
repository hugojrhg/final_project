package com.dev.week7.controller;

import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.CheckoutDTO;
import com.dev.week7.model.product.Product;
import com.dev.week7.service.CheckoutService;
import com.dev.week7.service.ProductService;
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

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/checkouts")
public class CheckoutController {

    @Autowired
    CheckoutService checkoutService;

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<CheckoutDTO>> getAllCheckouts() {
        List<CheckoutDTO> checkoutsDTO =
                checkoutService.getAllCheckouts()
                        .stream()
                        .map(checkout -> mapper.map(checkout, CheckoutDTO.class))
                        .toList();

        return new ResponseEntity<>(checkoutsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Checkout> getCheckoutById(@PathVariable Long id) {
        return new ResponseEntity<>(checkoutService.getCheckoutById(id), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CheckoutDTO> createCheckout(@Valid @RequestBody CheckoutDTO checkoutDTO) {
        Checkout checkout = new Checkout(checkoutDTO.getCustomer(), new ArrayList<>(), null, null);

        checkoutService.createCheckout(checkout);
        checkoutService.addProductToCheckout(checkoutDTO.getProduct(), checkoutDTO.getQuantity(), checkout.getId());

        return new ResponseEntity<>(checkoutDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CheckoutDTO> deleteCheckout(@PathVariable Long id) {
        checkoutService.deleteCheckout(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CheckoutDTO> updateCheckout(@Valid @RequestBody CheckoutDTO checkoutDTO, @PathVariable Long id) {
        Checkout checkout = mapper.map(checkoutDTO, Checkout.class);
        checkoutService.updateCheckout(checkout, id);
        return new ResponseEntity<>(checkoutDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CheckoutDTO> updateCheckoutFields(@PathVariable Long id, @RequestBody CheckoutDTO checkoutDTO) {
        Checkout checkout = mapper.map(checkoutDTO, Checkout.class);

        if (checkoutService.updateCheckoutFields(checkout, id) == null) {
            return new ResponseEntity<>(checkoutDTO, HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity<>(checkoutDTO, HttpStatus.OK);
    }

    @PatchMapping("/addProduct/{productId}/{quantity}/toCheckout/{checkoutId}")
    public ResponseEntity<CheckoutDTO> addProductToCheckout(@PathVariable Long productId, @PathVariable Integer quantity, @PathVariable Long checkoutId) {
        Product product = productService.getProductById(productId);

        checkoutService.addProductToCheckout(product, quantity, checkoutId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/removeProduct/{productId}/fromCheckout/{checkoutId}")
    public ResponseEntity<CheckoutDTO> removeProductFromCheckout(@PathVariable Long productId, @PathVariable Long checkoutId) {
        Product product = productService.getProductById(productId);

        checkoutService.removeProductFromCheckout(product, checkoutId);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @PatchMapping("/{checkoutId}/newProductQuantity/{quantity}/toProduct/{productId}")
    public ResponseEntity<CheckoutDTO> updateProductQuantityInCheckout(@PathVariable Integer quantity, @PathVariable Long checkoutId, @PathVariable Long productId) {
        checkoutService.updateProductQuantityInCheckout(checkoutId, productId, quantity);
        return new ResponseEntity<>(HttpStatus.OK);
    }

}
