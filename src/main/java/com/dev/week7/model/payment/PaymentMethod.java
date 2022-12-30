package com.dev.week7.model.payment;

import com.dev.week7.exceptions.InsufficientFundsException;
import com.dev.week7.model.customer.Customer;
import com.dev.week7.model.interfaces.IPaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
public class PaymentMethod implements IPaymentMethod {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Long id;
    private String name;
    private Double funds;
    private Double reservedFunds;
    @ManyToOne
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private Customer customer;

    protected PaymentMethod(Double funds, String name) {
        this.funds = funds;
        this.name = name;
    }

    @Override
    public boolean payDebt() {
        this.reservedFunds = 0.0;
        return true;
    }

    @Override
    public boolean reserveFunds(Double price) throws InsufficientFundsException {
        if (funds >= price) {
            funds -= price;
            reservedFunds += price;
            return true;
        }
        throw new InsufficientFundsException();
    }

}
