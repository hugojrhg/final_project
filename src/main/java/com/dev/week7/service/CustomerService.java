package com.dev.week7.service;

import com.dev.week7.exceptions.CustomerNotFoundException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    @Autowired
    CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElseThrow(CustomerNotFoundException::new);
    }

    public Customer createCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public void deleteCustomer(Long id) {
        customerRepository.delete(getCustomerById(id));
    }

    public Customer updateCustomer(Customer customer, Long id) {
        Customer oldCustomer = getCustomerById(id);

        oldCustomer.setFirstName(customer.getFirstName());
        oldCustomer.setLastName(customer.getLastName());
        oldCustomer.setZipCodes(customer.getZipCodes());

        return customerRepository.save(oldCustomer);
    }

    public Customer updateCustomerFields(Customer newCustomer, Long id) {
        Customer oldCustomer = getCustomerById(id);

        if (newCustomer.getFirstName() != null){
            oldCustomer.setFirstName(newCustomer.getFirstName());
        }
        if (newCustomer.getLastName() != null){
            oldCustomer.setLastName(newCustomer.getLastName());
        }
        if (newCustomer.getZipCodes() != null){
            oldCustomer.setZipCodes(newCustomer.getZipCodes());
        }

        return customerRepository.save(oldCustomer);
    }

}
