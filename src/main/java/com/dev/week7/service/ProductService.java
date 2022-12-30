package com.dev.week7.service;

import com.dev.week7.exceptions.ProductNotFoundException;
import com.dev.week7.model.product.Product;
import com.dev.week7.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public List<Product> getAllProducts(){
        return productRepository.findAll();
    }

    public Product getProductById(Long id){
        return productRepository.findById(id).orElseThrow(ProductNotFoundException::new);
    }

    public Product createProduct(Product product){
        return productRepository.save(product);
    }

    public void deleteProduct(Long id){
        productRepository.deleteById(id);
    }

    public Product updateProduct(Long id, Product newProduct){
        Product oldProduct = getProductById(id);

        oldProduct.setName(newProduct.getName());
        oldProduct.setPrice(newProduct.getPrice());
        oldProduct.setQuantity(newProduct.getQuantity());

        return productRepository.save(oldProduct);
    }

    public Product updateProductFields(Product newProduct, Long id) {
        Product oldProduct = getProductById(id);

        if (newProduct.getName() != null){
            oldProduct.setName(newProduct.getName());
        }
        if (newProduct.getPrice() != null){
            oldProduct.setPrice(newProduct.getPrice());
        }
        if (newProduct.getQuantity() != null){
            oldProduct.setQuantity(newProduct.getQuantity());
        }

        return productRepository.save(oldProduct);
    }

}
