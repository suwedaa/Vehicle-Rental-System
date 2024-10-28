package com.bptn.Vehicle_Rental_System.Project;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;

public class RentalTest {

	@Test
	public void testAddCustomer_successful() {
		Rental rental = new Rental(); // create a new rental object to test with
		Customer customer = new Customer("100", "SuwedaAfrah"); // create a new customer to test with

		rental.addCustomer(customer); // add the customer to the rental system

		List<Customer> customers = rental.getCustomers(); // get the list of all customers
		boolean customerFound = customers.stream()
				.anyMatch(c -> c.getCustomerId().equals("100") && c.getCustomerName().equals("SuwedaAfrah"));

		// here I used a lamda expression in combination with a stream in order to find
		// if the customerId and customerName matches
		// I used the anyMatch method in Streams to do so .
		assertTrue(customerFound, "Customer with ID 100 and name Suweda Afrah not found in the list.");
		// assert that the customer is found in the list
	}

	@Test
	public void testRentCar_setsAvailabilityToFalse() {
		Rental rental = new Rental(); // create a new rental object to test with
		List<Car> availableCars = rental.getAvailableCars(); // get list of available cars

		// if there are no cars in the list (maybe the file is empty) add a car for
		// testing
		// I only added this check as a "good practice" since I hard coded the cars it
		// is kind of redundant
		if (availableCars.isEmpty()) {
			Car car = new Car("Red", 99.50, true, "Toyota", "Corolla", 5, "Gasoline");
			availableCars.add(car);
		}

		Car carToRent = availableCars.get(0); // get first available car
		Customer customer = new Customer("200", "Selena Gomez"); // create a new customer
		rental.addCustomer(customer); // add the customer to the system

		rental.rentCar(customer, carToRent, 5); // make that customer rent out the car for 5 days

		assertFalse(carToRent.getAvailability()); // make sure that the availability is set to false (car is rented)
	}

}