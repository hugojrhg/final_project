package com.dev.week7.controller;

import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.model.payment.PaymentMethodDTO;
import com.dev.week7.service.PaymentMethodService;
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
@RequestMapping("/payments")
public class PaymentMethodController {


    @Autowired
    PaymentMethodService paymentMethodService;

    @Autowired
    ModelMapper mapper;

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {
        List<PaymentMethodDTO> paymentMethodsDTO =
                paymentMethodService.getAllPaymentMethods()
                        .stream()
                        .map(paymentMethod -> mapper.map(paymentMethod, PaymentMethodDTO.class))
                        .toList();

        return new ResponseEntity<>(paymentMethodsDTO, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodById(@PathVariable Long id) {
        PaymentMethodDTO paymentMethodDTO = mapper.map(paymentMethodService.getPaymentMethodById(id), PaymentMethodDTO.class);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PaymentMethodDTO> createPaymentMethod(@Valid @RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = mapper.map(paymentMethodDTO, PaymentMethod.class);
        paymentMethodService.createPaymentMethod(paymentMethod);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.CREATED);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deletePaymentMethod(@PathVariable Long id) {
        paymentMethodService.deletePaymentMethod(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethod(@Valid @RequestBody PaymentMethodDTO paymentMethodDTO, @PathVariable Long id) {
        PaymentMethod paymentMethod = mapper.map(paymentMethodDTO, PaymentMethod.class);
        paymentMethodService.updatePaymentMethod(paymentMethod, id);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.OK);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> updatePaymentMethodFirstName(@PathVariable Long id, @Valid @RequestBody PaymentMethodDTO paymentMethodDTO) {
        PaymentMethod paymentMethod = mapper.map(paymentMethodDTO, PaymentMethod.class);
        paymentMethodService.updatePaymentMethodFields(paymentMethod, id);
        return new ResponseEntity<>(paymentMethodDTO, HttpStatus.OK);
    }

}
