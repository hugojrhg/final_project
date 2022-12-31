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
import java.util.stream.Collectors;

@Service
public class CheckoutService {

    @Autowired
    CheckoutRepository checkoutRepository;
    @Autowired
    ProductService productService;
    @Autowired
    PaymentMethodService paymentMethodService;

    public List<Checkout> getAllCheckouts() {
        return checkoutRepository.findAll();
    }

    public Checkout getCheckoutById(Long id) {
        return checkoutRepository.findById(id).orElseThrow(CheckoutNotFoundException::new);
    }

    public Checkout createCheckout(Checkout checkout) {
        checkout.getProducts().stream().forEach(CheckoutProduct::validProductQuantityInStock);
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
        if (!newCheckout.getProducts().isEmpty()) {
            oldCheckout.setProducts(newCheckout.getProducts());
        }
        if (newCheckout.getPaymentMethod() != null) {
            if (customer.getPaymentMethods().contains(paymentMethodService.getPaymentMethodById(newCheckout.getPaymentMethod().getId()))) {
                oldCheckout.setPaymentMethod(paymentMethodService.getPaymentMethodById(newCheckout.getPaymentMethod().getId()));
            } else {
                return null;
            }
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
        checkout.addProduct(productService.getProductById(product.getId()), quantity);
        productService.updateQuantity(product.getId(), productService.getProductById(product.getId()).getQuantity() - quantity);
        checkoutRepository.save(checkout);
    }

    public void removeProductFromCheckout(Product product, Long id) {
        Checkout checkout = getCheckoutById(id);
        Integer quantityInCheckout = 0;

        for (CheckoutProduct checkoutProduct : checkout.getProducts()) {
            if (checkoutProduct.getProduct().equals(product)) {
                quantityInCheckout = checkoutProduct.getQuantity();
            }
        }

        checkout.removeProduct(product);

        if (checkout.getProducts().isEmpty()) {
            checkoutRepository.delete(checkout);
        } else {
            checkoutRepository.save(checkout);
        }

        productService.updateQuantity(product.getId(), quantityInCheckout + product.getQuantity());

    }

    public void updateProductQuantityInCheckout(Long checkoutId, Long productId, Integer newQuantityInCheckout) {
        Checkout checkout = getCheckoutById(checkoutId);
        Product product = productService.getProductById(productId);


        for (CheckoutProduct checkoutProduct : checkout.getProducts()) {
            Integer quantityInCheckout = checkoutProduct.getQuantity();
            Integer quantityInStock = checkoutProduct.getProduct().getQuantity();
            Integer newQuantityInStock = quantityInStock + (quantityInCheckout - newQuantityInCheckout);
            if (checkoutProduct.getProduct().equals(product)) {
                productService.updateQuantity(checkoutProduct.getProduct().getId(), newQuantityInStock);
                checkoutProduct.setQuantity(newQuantityInCheckout);
            }
        }

        checkoutRepository.save(checkout);
    }

    public void takeProductOutOfStock(Product product, Integer quantity) {
        product.debitQuantity(quantity);
        productService.updateProduct(product.getId(), product);
    }

    public Double sumOfProductsValues(Checkout checkout){
        return checkout.getProducts().stream().mapToDouble(checkoutProduct -> {return checkoutProduct.getTotalValue();}).sum();
    }

}
