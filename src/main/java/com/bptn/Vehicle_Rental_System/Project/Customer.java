package com.bptn.Vehicle_Rental_System.Project;

import java.util.ArrayList;
import java.util.List;

class Customer {
	private String customerId;
	private String customerName;
	private List<BookingReceipt> bookings;

	public Customer(String customerId, String customerName) {

		this.customerId = customerId;
		this.customerName = customerName;
		this.bookings = new ArrayList<>();

	}

	public List<BookingReceipt> getBooking() {
		return bookings;
	}

	public void addBooking(BookingReceipt booking) {
		bookings.add(booking);
	}

	public void removeBooking(BookingReceipt booking) {
		if (bookings != null) {
			bookings.remove(booking);
		}
	}

	public String getCustomerId() {
		return this.customerId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void checkout() {
		System.out.println("Processing order for: " + customerName);
	}

	@Override
	public String toString() {
		return "Customer ID: " + customerId + ", Name: " + customerName;
	}

}