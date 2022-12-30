package com.dev.week7.service;

import com.dev.week7.exceptions.CheckoutNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.order.Checkout;
import com.dev.week7.model.order.CheckoutProduct;
import com.dev.week7.model.product.Product;
import com.dev.week7.repository.CheckoutRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CheckoutService {

    @Autowired
    CheckoutRepository checkoutRepository;

    @Autowired
    ProductService productService;

    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }

    public Checkout getCheckoutById(Long id) {
        return checkoutRepository.findById(id).orElseThrow(CheckoutNotFoundException::new);
    }

    public Checkout createCheckout(Checkout checkout) {

        takeProductOutOfStock(checkout);
        return checkoutRepository.save(checkout);
    }

    public void deleteCheckout(Long id) {
        checkoutRepository.delete(getCheckoutById(id));
    }

    public Checkout updateCheckout(Checkout checkout, Long id) {
        Checkout oldCheckout = getCheckoutById(id);

        oldCheckout.setCustomer(checkout.getCustomer());
        oldCheckout.setProducts(checkout.getProducts());
        oldCheckout.setPaymentMethod(checkout.getPaymentMethod());

        return checkoutRepository.save(oldCheckout);
    }

    public Checkout updateCheckoutFields(Checkout newCheckout, Long id) {
        Checkout oldCheckout = getCheckoutById(id);
        Customer customer = oldCheckout.getCustomer();

        if (newCheckout.getCustomer() != null) {
            oldCheckout.setCustomer(newCheckout.getCustomer());
        }
        if (newCheckout.getProducts() != null) {
            oldCheckout.setProducts(newCheckout.getProducts());
        }
        if (newCheckout.getPaymentMethod() != null) {
            oldCheckout.setPaymentMethod(newCheckout.getPaymentMethod());
        }
        if (newCheckout.getZipCode() != null) {
            if (customer.getZipCodes().contains(newCheckout.getZipCode())) {
                oldCheckout.setZipCode(newCheckout.getZipCode());
            } else {
                return null;
            }

        }

        return checkoutRepository.save(oldCheckout);
    }

    public void addProductToCheckout(Product product, Integer quantity, Long id) {
        Checkout checkout = getCheckoutById(id);
        checkout.addProduct(product, quantity);
        checkoutRepository.save(checkout);
    }

    public void removeProductFromCheckout(Product product, Long id) {
        Checkout checkout = getCheckoutById(id);
        checkout.removeProduct(product);
    }

    public void updateProductQuantityInCheckout(Checkout checkout, Integer quantity) {
        for (CheckoutProduct checkoutProduct : checkout.getProducts()) {
            Product product = checkoutProduct.getProduct();
            product.setQuantity(quantity);
        }
    }

    public void takeProductOutOfStock(Checkout checkout) {
        for (CheckoutProduct checkoutProduct : checkout.getProducts()) {
            Product product = productService.getProductById(checkoutProduct.getProduct().getId());
            product.debitQuantity(product.getQuantity());
        }
    }

    public void resetProductInStock() {

    }

}
