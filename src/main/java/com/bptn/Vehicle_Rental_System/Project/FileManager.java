package com.bptn.Vehicle_Rental_System.Project;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class FileManager {

	// write cars
	public void readCars(List<Car> availableCars, List<Car> cars) {

		// this method reads the car data from the cars.txt
		try (BufferedReader reader = new BufferedReader(new FileReader("cars.txt"))) { // open file for reading
			String line;
			while ((line = reader.readLine()) != null) {// read each line until the end of the file
				String[] carDetails = line.split(",");// split the line into parts using the comma
				if (carDetails.length == 7) { // making sure we have all car details
					// I believe there is a way to load each object but this seemed like the more
					// intuitive approach for me
					Car car = new Car(carDetails[0], Double.parseDouble(carDetails[1]), // Price
							Boolean.parseBoolean(carDetails[2]), // Availability
							carDetails[3], // Brand
							carDetails[4], // Model
							Integer.parseInt(carDetails[5]), carDetails[6]); // Seat Number and Fuel Type
					cars.add(car);
					if (car.getAvailability()) {
						availableCars.add(car); // add the car to the list of all cars
					}
				}
			}

		} catch (IOException e) { // error loading file
			System.out.println("Error loading cars from file: " + e.getMessage());
		}

	}

	// this method updates the availability of a car in the cars.txt file
	public void writeCars(Car car, boolean isAvailable) {
		File input = new File("cars.txt");
		File temp = new File("temp_cars.txt");
		// I used two files for cars because it is the only file where I would need to
		// change the contents of the file. the other files dont require this
		// I do believe there are more optimal ways of doing this (for example
		// overwriting) or through logic in the Rental class, but I feared tweaking with
		// the logic would cause my program to crash
		try (BufferedReader reader = new BufferedReader(new FileReader(input)); // original is used for reading but...
				BufferedWriter writer = new BufferedWriter(new FileWriter(temp))) { // temp file is used for writing
			String line;

			while ((line = reader.readLine()) != null) { // read each line from the original file
				String[] carDetails = line.split(","); // split the line into parts
				// Check if this is the car we want to update
				if (carDetails.length == 7 && carDetails[3].equals(car.getBrand())
						&& carDetails[4].equals(car.getModel())) {
					carDetails[2] = Boolean.toString(isAvailable); // Update the availability
					line = String.join(",", carDetails);// Put the line back together

				}
				writer.write(line); // write the line (updated or not) to the temp file
				writer.newLine();
			}
		} catch (IOException e) { // issue with the file
			System.out.println("Error updating car: " + e.getMessage());
		}
		// replace the original file with the updated temporary file
		if (!input.delete()) { // delete the original file
			System.out.println("Could not delete original file");
		}
		if (!temp.renameTo(input)) { // rename the temporary file to the original file name
			System.out.println("Could not rename temporary file");
		}

	}

	// this method reads booking data from the bookings.txt file
	public void readBooking(List<BookingReceipt> bookings, List<Customer> customers, List<Car> cars) {
		try (BufferedReader reader = new BufferedReader(new FileReader("bookings.txt"))) { // open the file for reading
			String line;
			while ((line = reader.readLine()) != null) { // read each line until the end of the file
				String[] bookingDetails = line.split(","); // split the line parts using comma
				if (bookingDetails.length == 7) { // 7 because 7 parameters (making sure we have all booking details)
					// Find existing customer who made the booking
					Customer customer = null;
					for (Customer c : customers) {
						if (c.getCustomerId().equals(bookingDetails[0])) { // we find the customer
							customer = c;
							break; // then, no need to keep looking
						}
					}
					if (customer == null) { // customer not found
						System.err.println("Customer not found for booking: " + line);
						continue; // skip to next booking if customer not found
					}
					Car car = null; // find the car that was booked
					for (Car c : cars) { // go through each car
						if (c.getBrand().equals(bookingDetails[2]) && c.getModel().equals(bookingDetails[3])) {
							// we find the car!
							// Its important to note here that i only use the brand and model matching as a
							// way to check because there are
							// no two cars with the same model and brand, but in the future, I would
							// implement a carId so that everything is
							// unique and use that instead, but for now i used model and brand to check if
							// the car is found.
							car = c;
							break; // no need to keep looking so we break
						}
					}
					if (car == null) { // there is no car found
						System.err.println("Car not found for booking: " + line);
						continue; // continue is crucial here, it allows us to skip to the next booking
					}

					// create a new bookingReceipt object with details from the file
					BookingReceipt booking = new BookingReceipt(customer, car, Integer.parseInt(bookingDetails[4]),
							Double.parseDouble(bookingDetails[5]));
					bookings.add(booking); // add the booking to the bookings list
					customer.addBooking(booking); // add booking to the customer list of booking as well
				}
			}
		} catch (IOException e) {
			System.out.println("Error reading car.");
		}
	}

	// this method is writing the booking data into bookings.txt
	public void writeBooking(List<BookingReceipt> bookings) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("bookings.txt"))) { // open file for writing
			for (BookingReceipt booking : bookings) { // go through each booking in the list
				writer.write(booking.bookingFileString()); // write the booking info to the file
				writer.newLine();// add a new line after each booking
			}
		} catch (IOException e) {
			System.out.println("Error writing bookings: " + e.getMessage());
		}
	}

	// this method is responsible for reading the customer data from customers.txt
	public void readCustomer(List<Customer> customers) {
		try (BufferedReader reader = new BufferedReader(new FileReader("customers.txt"))) {// open the file to read it
			String line;
			while ((line = reader.readLine()) != null) { // read each line until end of file
				String[] customerDetails = line.split(","); // split the line parts into commas
				if (customerDetails.length == 2) { // make sure that the length is two (since our customer object is
													// only two parameters
					Customer customer = new Customer(customerDetails[0], customerDetails[1]); // create the new customer
																								// object
					customers.add(customer); // finally, add the customer to the customers list
				}
			}
		} catch (IOException e) { // here we catch the error with loading to customers.txt
			System.out.println("Error loading customers from file: " + e.getMessage());
		}
	}

	// This Method writes the customer data to the "customers.txt" file
	public void writeCustomer(List<Customer> customers) {
		try (BufferedWriter writer = new BufferedWriter(new FileWriter("customers.txt"))) {
			for (Customer customer : customers) { // Go through each customer object in list of customers
				writer.write(customer.toString()); // write customers info into the file
				writer.newLine(); // add a new line after each customer is made
			}
		} catch (IOException e) { // here we catch an error if there is a difficulty with writing to the
									// customers.txt file
			System.out.println("Error writing file: " + e.getMessage());
		}

	}

}
