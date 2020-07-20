package com.airasia.booking.entities;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;

@Builder
@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class Customer {

    @Column(name = "customer_full_name")
    private String customerFullName;

    @Column(name = "customer_email")
    private String customerEmail;

    @Column(name = "customer_phone_number")
    private String customerPhoneNumber;
}
