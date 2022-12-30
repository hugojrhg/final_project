package com.dev.week7.model.customer;

import com.dev.week7.model.payment.PaymentMethod;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.JoinColumn;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CustomerDTO {

    @Email
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String email;
    @Length(max = 50, message = "The first name length can't be more than 50 chars")
    @NotNull(message = "First Name can't be null")
    private String firstName;
    @Length(max = 50, message = "The last name length can't be more than 50 chars")
    @NotNull(message = "Last Name can't be null")
    private String lastName;
    @NotNull(message = "The list of Zip Codes can't be null")
    private List<Integer> zipCodes;
    private List<PaymentMethod> paymentMethods;

}
