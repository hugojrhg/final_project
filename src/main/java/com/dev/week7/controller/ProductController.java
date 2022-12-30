package com.dev.week7.controller;

import com.dev.week7.model.product.Product;
import com.dev.week7.model.product.ProductDTO;
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

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    ProductService productService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products =
                productService.getAllProducts()
                        .stream()
                        .map(product -> mapper.map(product, ProductDTO.class))
                        .toList();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        ProductDTO productDTO = mapper.map(productService.getProductById(id), ProductDTO.class);

        return new ResponseEntity<>(productDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ProductDTO> createProduct(@Valid @RequestBody ProductDTO productDTO) {
        Product product = mapper.map(productDTO, Product.class);

        productService.createProduct(product);

        return new ResponseEntity<>(productDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ProductDTO> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO){
        Product product = mapper.map(productDTO, Product.class);
        productService.updateProduct(id, product);
        ProductDTO updatedProductDTO = mapper.map(product, ProductDTO.class);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }


    @PatchMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProductFields(@PathVariable Long id, @Valid @RequestBody ProductDTO productDTO){
        Product product = mapper.map(productDTO, Product.class);
        productService.updateProductFields(product, id);
        ProductDTO updatedProductDTO = mapper.map(product, ProductDTO.class);
        return new ResponseEntity<>(updatedProductDTO, HttpStatus.OK);
    }
}
