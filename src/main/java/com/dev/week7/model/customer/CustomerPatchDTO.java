package com.dev.week7.model.customer;

import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
public class CustomerPatchDTO {
    @Length(max = 50, message = "The first name length can't be more than 50 chars")
    private String firstName;
    @Length(max = 50, message = "The last name length can't be more than 50 chars")
    private String lastName;
    private List<Integer> zipCodes;
}
