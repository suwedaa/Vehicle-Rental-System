package com.bptn.Vehicle_Rental_System.Project;

import java.time.LocalDate;

public class BookingReceipt {

	private Customer customer;
	private Car car;
	private LocalDate startDate;
	private LocalDate endDate;
	private double cost;
	private String status;
	private int days;

	public BookingReceipt(Customer customer, Car car, int days, double cost) {

		this.customer = customer;
		this.car = car;
		this.startDate = LocalDate.now();
		this.days = days;
		this.cost = cost;
		this.status = "Active";
		this.endDate = this.startDate.plusDays(days);

	}

	public Customer getCustomer() {
		return customer;
	}

	public int getDyas() {
		return days;
	}

	public Car getCar() {
		return car;
	}

	public double getCost() {
		return cost;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public void bookingDetails() {
		System.out.println("\nBooking Details:");
		System.out.println("  Customer: " + customer.getCustomerName());
		System.out.println("  Car: " + car.getBrand() + " " + car.getModel());
		System.out.println("  Start Date: " + startDate);
		System.out.println("  End Date: " + endDate);
		System.out.println("  Rental Cost: $" + cost);
		System.out.println("  Status: " + status);

	}

	public String bookingFileString() {
		return customer.getCustomerId() + "," + customer.getCustomerName() + "," + car.getBrand() + "," + car.getModel()
				+ "," + days + "," + cost + "," + status;
	}

}
