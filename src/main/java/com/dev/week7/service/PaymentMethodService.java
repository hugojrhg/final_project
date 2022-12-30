package com.dev.week7.service;

import com.dev.week7.exceptions.PaymentMethodNotFoundException;
import com.dev.week7.model.payment.PaymentMethod;
import com.dev.week7.repository.PaymentMethodRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PaymentMethodService {

    @Autowired
    PaymentMethodRepository paymentMethodRepository;

    public List<PaymentMethod> getAllPaymentMethods() {
        return paymentMethodRepository.findAll();
    }

    public PaymentMethod getPaymentMethodById(Long id) {
        return paymentMethodRepository.findById(id).orElseThrow(PaymentMethodNotFoundException::new);
    }

    public PaymentMethod createPaymentMethod(PaymentMethod paymentMethod) {
        return paymentMethodRepository.save(paymentMethod);
    }

    public void deletePaymentMethod(Long id) {
        paymentMethodRepository.delete(getPaymentMethodById(id));
    }

    public PaymentMethod updatePaymentMethod(PaymentMethod paymentMethod, Long id) {
        PaymentMethod oldPaymentMethod = getPaymentMethodById(id);

        oldPaymentMethod.setName(paymentMethod.getName());
        oldPaymentMethod.setFunds(paymentMethod.getFunds());
        oldPaymentMethod.setReservedFunds(paymentMethod.getReservedFunds());

        return paymentMethodRepository.save(oldPaymentMethod);
    }

    public PaymentMethod updatePaymentMethodFields(PaymentMethod newPaymentMethod, Long id) {
        PaymentMethod oldPaymentMethod = getPaymentMethodById(id);

        if (newPaymentMethod.getName() != null) {
            oldPaymentMethod.setName(newPaymentMethod.getName());
        }
        if (newPaymentMethod.getFunds() != null) {
            oldPaymentMethod.setFunds(newPaymentMethod.getFunds());
        }
        if (newPaymentMethod.getReservedFunds() != null) {
            oldPaymentMethod.setReservedFunds(newPaymentMethod.getReservedFunds());
        }

        return paymentMethodRepository.save(oldPaymentMethod);
    }


}
