package com.bptn.Vehicle_Rental_System.Project;

import java.util.ArrayList;
import java.util.List;

public class Rental {

	private List<Car> availableCars;

	private List<Customer> customers;

	private List<BookingReceipt> bookings;

	public Rental() {
		availableCars = new ArrayList<>();
		customers = new ArrayList<>();
		bookings = new ArrayList<>();

		Car car1 = new Car("Black", 75.50, true, "Toyota", "Camry", 5, "Gasoline");
		Car car2 = new Car("Silver", 100.50, true, "Tesla", "Model 3", 5, "Electric");
		Car car3 = new Car("Black", 80.00, true, "Nissan", "Rogue", 5, "Gasoline");
		Car car4 = new Car("White", 70.00, true, "Tesla", "Model X", 7, "Electric");
		Car car5 = new Car("Grey", 50.00, true, "Dodge", "Durango", 7, "Gasoline");

		availableCars.add(car1);
		availableCars.add(car2);
		availableCars.add(car3);
		availableCars.add(car4);
		availableCars.add(car5);

		FileManager file = new FileManager();
		file.writeCars(availableCars);

		if (customers == null) {
			customers = new ArrayList<>();
		}

	}

	public List<Customer> getCustomers() {
		return customers;
	}

	public void viewAvailableCars() {
		if (availableCars.isEmpty()) {
			System.out.println("No cars available at Suweda's Car Rentals, Sorry for the inconvenience.");
		} else {
			for (int i = 0; i < availableCars.size(); i++) {
				Car car = availableCars.get(i);
				System.out.println("Car Number: " + (i + 1));
				if (car.getAvailability() == true) {
					car.displayCarDetails();
					System.out.println(".........................");
				}
			}
		}
	}

	public Customer findCustomerId(String customerId) {

		for (Customer customer : customers) {
			if (customer.getCustomerId().equals(customerId)) {
				return customer;
			}

		}
		return null;
	}

	public Car findCar(int carNum) {
		if (carNum < 0 || carNum > availableCars.size()) {
			return null;
		}
		Car car = availableCars.get(carNum);
		if (car.getAvailability() == true) {
			return car;
		}

		return null;
	}

	public void rentCar(Customer customer, Car car, int days) {
		if (customer == null) {
			System.out.println("\nPlease set your profile first.");
			return;
		}
		if (car.getAvailability() == false) {
			System.out.println("\nCar is not available.");
			return;
		}

		double cost = car.getRentalPrice() * days;
		BookingReceipt booking = new BookingReceipt(customer, car, days, cost);
		bookings.add(booking);
		customer.addBooking(booking);
		System.out.println("\nCar rented successfully!");
		booking.bookingDetails();

		availableCars.remove(car);

	}

	public void addCustomer(Customer customer) {
		boolean customerExists = false;

		for (Customer c : customers) {
			if (c.getCustomerId().equals(customer.getCustomerId())) {
				customerExists = true;
				break;
			}
		}
		if (customerExists) {
			System.out.println("\nYour Profile Already Exists\n");
		} else {
			customers.add(customer);
			System.out.println("\nProfile Created Successfully!\n");
		}
	}

	public void returnCar(BookingReceipt booking) {
		if (booking == null) {
			System.out.println("\nInvalid Booking.");
			return;
		}

		Car returnedCar = booking.getCar();
		returnedCar.setAvilability(true);
		availableCars.add(returnedCar);

		double totalCost = booking.getCost();
		System.out.println("Car returned successfully!");
		System.out.println("Total Cost: $" + totalCost);

		Customer customer = booking.getCustomer();
		customer.removeBooking(booking);

	}

	public void activeBookings(List<BookingReceipt> bookings) {
		System.out.println("\nActive Bookings: ");
		for (int i = 0; i < bookings.size(); i++) {
			System.out.println((i + 1) + ". " + bookings.get(i).getCar().getBrand() + " "
					+ bookings.get(i).getCar().getModel() + " Start Date: " + bookings.get(i).getStartDate());

		}
	}

	public void cancelBooking(BookingReceipt booking) {
		Customer customer = booking.getCustomer();
		customer.removeBooking(booking);
		Car returnedCar = booking.getCar();
		availableCars.add(returnedCar);
	}

}
