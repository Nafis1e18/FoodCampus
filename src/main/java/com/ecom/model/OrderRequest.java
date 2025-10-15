package com.ecom.model;

import lombok.Data;
import lombok.ToString;

@ToString
@Data
public class OrderRequest {

	private String firstName;

	private String lastName;

	private String email;

	private String mobileNo;

	private String address;

	private String city;

	private String state;

	private String pincode;
	
	private String paymentType;

    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getEmail() { return email; }
    public String getMobileNo() { return mobileNo; }
    public String getAddress() { return address; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPincode() { return pincode; }
    public String getPaymentType() { return paymentType; }
}
