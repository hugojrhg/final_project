package com.dev.week7.controller;

import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.customer.CustomerDTO;
import com.dev.week7.model.customer.CustomerPatchDTO;
import com.dev.week7.service.CustomerService;
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
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    CustomerService customerService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customersDTO =
                customerService.getAllCustomers()
                        .stream()
                        .map(customer -> mapper.map(customer, CustomerDTO.class))
                        .toList();

        return new ResponseEntity<>(customersDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable Long id) {
        CustomerDTO customerDTO = mapper.map(customerService.getCustomerById(id), CustomerDTO.class);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@Valid @RequestBody CustomerDTO customerDTO) {
        Customer customer = mapper.map(customerDTO, Customer.class);
        customerService.createCustomer(customer);
        return new ResponseEntity<>(customerDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<CustomerDTO> deleteCustomer(@PathVariable Long id) {
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(@Valid @RequestBody CustomerDTO customerDTO, @PathVariable Long id) {
        Customer customer = mapper.map(customerDTO, Customer.class);
        customerService.updateCustomer(customer, id);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<CustomerPatchDTO> updateCustomerFirstName(@PathVariable Long id,@Valid @RequestBody CustomerPatchDTO customerDTO) {
        Customer customer = mapper.map(customerDTO, Customer.class);
        customerService.updateCustomerFields(customer, id);
        return new ResponseEntity<>(customerDTO, HttpStatus.OK);
    }
}
