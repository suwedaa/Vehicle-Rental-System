package com.bptn.Vehicle_Rental_System.Project;

import java.util.ArrayList;
import java.util.List;

public class Rental {

	private List<Car> availableCars; // list stores all cars that are AVAILABLE (AVAILABILITY = TRUE)

	private List<Customer> customers; // list stores all customers

	private List<BookingReceipt> bookings; // list stores all bookings

	private List<Car> cars; // list stores all cars (available and rented)

	private FileManager file; // this allows us to read and write to files

	public Rental() {
		// Initializing our array lists
		cars = new ArrayList<>();
		availableCars = new ArrayList<>();
		customers = new ArrayList<>();
		bookings = new ArrayList<>();

		file = new FileManager();
		file.readCars(availableCars, cars);
		file.readCustomer(customers);
		file.readBooking(bookings, customers, cars);
	}

	// Getters to access the lists for testing purposes
	public List<Car> getAvailableCars() {
		return availableCars;
	}

	public List<Car> getAllCars() {
		return cars;
	}

	public List<BookingReceipt> getBookings() {
		return bookings;
	}

	public List<Customer> getCustomers() {
		return customers;
	}

	// This method displays cars to the user
	public void viewAvailableCars() {
		if (availableCars.isEmpty()) { // no cars available (this will never happen since the list of cars is hard
										// coded)
			System.out.println("No cars available at Suweda's Car Rentals, Sorry for the inconvenience.");
		} else {
			for (int i = 0; i < availableCars.size(); i++) { // going through each car
				Car car = availableCars.get(i);
				if (car.getAvailability() == true) { // if the car is actually available
					System.out.println("Car Number: " + (i + 1)); // show the car number (starting from i+1) b/c of
																	// indexing starting at 0
					car.displayCarDetails(); // show car details
					System.out.println("......................."); // just a separator for viewing
				}
			}
		}
	}

	// Method finds a customer by their ID
	public Customer findCustomerId(String customerId) {
		for (Customer customer : customers) { // go through each customer
			if (customer.getCustomerId().equals(customerId)) { // if we find the customer, return the customer
				return customer;
			}

		}
		return null; // else, return null (we couldnt find the customer)
	}

	// This method is for finding a car by its number in the list
	public Car findCar(int carNum) {
		// once again I want to preface, it would be much more optimal to use a carId
		// instead of the display number
		if (carNum < 0 || carNum > availableCars.size()) {
			return null;
			// if the car number is not in the range of the list size, return null
		}
		return availableCars.get(carNum); // else, return the car
	}

	// This method adds a new customer to the system
	public void addCustomer(Customer customer) {
		boolean customerExists = false; // keeping track of if the customer already exists
		// this boolean has to start off false because it switches WHEN the customer is
		// found to be already in the system
		for (Customer c : customers) { // go through customers list and check if the customer is already in it
			if (c.getCustomerId().equals(customer.getCustomerId())) {
				customerExists = true;
				break; // if they already exist, set boolean to true and leave
			}
			if (!c.getCustomerName().matches("[a-zA-Z]+")) { // make sure that the customer name is a string with only
																// letters
				System.out.println("Name should only contain letters. Please Try Again");
				break;
			}
		}
		if (customerExists) {
			System.out.println("\nYour Profile Already Exists\n");
		} else {
			customers.add(customer); // if the customer doesn't exist, then add them
			System.out.println("\nProfile Created Successfully!\n");
			file.writeCustomer(customers); // save the updated customer list to the file
		}

	}

	// this method rents a car to a customer
	public void rentCar(Customer customer, Car car, int days) {
		if (customer == null) { // check if customer is null (they need a profile first)
			System.out.println("\nPlease set your profile first.");
			return;
		}
		if (car.getAvailability() == false) { // the car is already rented
			System.out.println("\nCar is not available.");
			return;
		}

		double cost = car.getRentalPrice() * days; // calculating the rental cost
		BookingReceipt booking = new BookingReceipt(customer, car, days, cost); // create a new booking
		bookings.add(booking); // add the booking to the list of bookings
		customer.addBooking(booking); // add the booking to the customer's bookings too

		file.writeBooking(bookings); // save the updated bookings to the file
		car.setAvilability(false); // mark the car as unavailable (it has been rented)
		file.writeCars(car, false); // update the car's availability in the file

		System.out.println("\nCar rented successfully!");

		booking.bookingDetails(); // show the booking details to the user
		availableCars.remove(car); // remove the car from the list of available cars

	}

	// this method handles returning a car
	public void returnCar(BookingReceipt booking) {
		if (booking == null) { // invalid booking (null)
			System.out.println("\nInvalid Booking.");
			return;
		}
		double totalCost = booking.getCost(); // get total cost of the booking
		// in this case i didnt add any late fees, but in a different version I will add
		// it next time
		System.out.println("Car returned successfully!");
		System.out.println("Total Cost: $" + totalCost); // show cost to the user

		Customer customer = booking.getCustomer(); // find the customer who made the booking
		customer.removeBooking(booking); // remove the booking from the customers list

		bookings.remove(booking); // remove the booking from the bookings list
		file.writeBooking(bookings); // save the updated bookings to the file

		Car returnedCar = booking.getCar(); // get the car that was returned
		returnedCar.setAvilability(true); // mark the car as available again!

		file.writeCars(returnedCar, true); // update the returned cars' availability
		availableCars.add(returnedCar); // add the car back to the list of available cars

	}

	// method shows the active bookings for a customer
	public void activeBookings(List<BookingReceipt> bookings) {
		System.out.println("\nActive Bookings: ");
		for (int i = 0; i < bookings.size(); i++) { // go through each booking and check if the status is "Active"
			if (bookings.get(i).getStatus() == "Active") {
				// display the booking
				System.out.println((i + 1) + ". " + bookings.get(i).getCar().getBrand() + " "
						+ bookings.get(i).getCar().getModel() + " Start Date: " + bookings.get(i).getStartDate());
			}
		}
	}

	// here we can cancel a booking
	public void cancelBooking(BookingReceipt booking) {
		Customer customer = booking.getCustomer(); // find the customer that made the booking
		Car returnedCar = booking.getCar(); // get the car that was booked
		returnedCar.setAvilability(true); // mark the car as available again

		booking.setStatus("Cancelled"); // update booking status to "Cancelled"
		customer.removeBooking(booking); // remove the booking from the customer's bookings
		availableCars.add(returnedCar); // add car back to available cars

		bookings.remove(booking); // remove booking from list of all bookings

		file.writeCars(returnedCar, true); // update car's availability in file
		file.writeBooking(bookings); // save updated bookings to file
	}

}
