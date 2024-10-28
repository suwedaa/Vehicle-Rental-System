package com.bptn.Vehicle_Rental_System.Project;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

public class MainVehicleSystem {
	private Rental rental;

	public MainVehicleSystem() {
		rental = new Rental(); // rental object is needed to handle all rental related issues (rent car,
								// return, bookings)
	}

	public static void main(String[] args) {
		MainVehicleSystem main = new MainVehicleSystem();
		main.displayMenu();
	}

	public void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;
		try {

			do {
				System.out.println("\n****Welcome to Suweda's Car Rental System!****\n");
				System.out.println("1. View Our Available Cars");
				System.out.println("2. Set Your Profile");
				System.out.println("3. Rent One of Our Cars");
				System.out.println("4. Return One of Our Cars");
				System.out.println("5. Manage Your Booking");
				System.out.println("0. Exit");
				System.out.print("Enter your choice: ");

				choice = scanner.nextInt();
				scanner.nextLine();

				switch (choice) {

				case 0:
					System.out.println("Thank you for using Suweda's Car Rental System!");
					break;

				case 1:
					System.out.println("\nHere are the available cars: ");
					rental.viewAvailableCars();
					break;

				case 2: // CREATE PROFILE: User wants to make a new profile
					System.out.println("\nPlease Enter Your Customer ID: ");
					String customerId = scanner.nextLine();
					System.out.println("\nPlease Enter Your Name: ");
					String customerName = scanner.nextLine();
					if (customerId.isEmpty() || customerName.isEmpty()) {
						// customer name and id cannot be empty
						System.out.println("Customer Name AND Customer ID cannot be empty. Please Try Again");
						break;
					}
					Customer customer = new Customer(customerId, customerName);
					// new customer is made and added to list of customers.
					rental.addCustomer(customer);

					break;

				case 3: // RENT CAR : User wants to rent a car
					try {
						System.out.println("\nEnter Customer ID: ");
						String customerID = scanner.nextLine();

						Customer c1 = rental.findCustomerId(customerID); // find customer by inputed ID

						if (c1 == null) {
							System.out.println("\nCustomer is not found, please create a profile first");
							break;
						}

						rental.viewAvailableCars(); // show available cars
						System.out.println("\nWhich Car Do You Want To Rent: ");
						int carNum = scanner.nextInt();

						Car selectedCar = rental.findCar(carNum - 1);
						// invalid car choice (-1 because indexing starts at 0)
						if (selectedCar == null) {
							System.out.println("\nInvalid Car Selection");
							break;
						}

						System.out.println("\nEnter the number of days for rental: ");
						int days = scanner.nextInt();
						scanner.nextLine();

						rental.rentCar(c1, selectedCar, days);
					} catch (InputMismatchException e) {
						System.out.println("\nInvalid Input. Please enter a number.");
						scanner.nextLine();
						break;
					}
					break;

				case 4: // RETURN CAR : User wants to return a car
					System.out.println("\nEnter Customer ID: ");
					String customerReturn = scanner.nextLine();

					Customer c2 = rental.findCustomerId(customerReturn);

					if (c2 == null) { // customer is not found, therefore they need to make a profile
						System.out.println("\nCustomer is not found, please create a profile first");
						break;
					}
					try {
						List<BookingReceipt> customerBookings = c2.getBooking();
						if (customerBookings == null || customerBookings.isEmpty()) {
							// checking if the user has any bookings (cars rented)
							System.out.println("\nYou Do Not Have Any Active Bookings! Please Rent a Car.");
							break;
						}

						rental.activeBookings(customerBookings); // showing active bookings
						System.out.println("\nWhich Car Would You Like To Return: ");
						int bookingNum = scanner.nextInt();
						scanner.nextLine();

						if (bookingNum < 1 || bookingNum > customerBookings.size()) {
							// checking is the booking number is within the range of the bookings list
							System.out.println("Invalid Booking Selection");
							break;
						}
						BookingReceipt carToReturn = customerBookings.get(bookingNum - 1);
						// get the booking here, using -1 since indexing starts at 0
						rental.returnCar(carToReturn);
					} catch (InputMismatchException e) {
						System.out.println("\nInvalid input. Please enter a number.");
						scanner.nextLine();
					}
					break;

				case 5: // MANAGE BOOKINGS : User wants to manage their booking: (either view or cancel
						// it)

					System.out.println("\nEnter Customer ID: ");
					String manageCustomer = scanner.nextLine();

					Customer c3 = rental.findCustomerId(manageCustomer);
					// get your customer from the user input

					if (c3 == null) {
						System.out.println("\nCustomer is not found, please create a profile first");
						break;
					}
					List<BookingReceipt> customerBookings2 = c3.getBooking();
					if (customerBookings2 == null || customerBookings2.isEmpty()) {
						// check if customer has any bookings
						System.out.println("\nYou Do Not Have Any Active Bookings! Please Rent a Car.");
						break;
					}
					System.out.println("\nWould You Like To Cancel Your Booking?(y/n) ");
					String cancelChoice = scanner.nextLine();

					if (!cancelChoice.equals("y") && !cancelChoice.equals("n")) {
						// making sure that the user is selecting the correct input (y or n)
						System.out.println("Please enter 'y' or 'n'.");
						continue;
					}

					if (cancelChoice.equals("y")) { // user wants to cancel a booking
						try {
							System.out.println("\nWhich Booking Would You Like to Cancel: ");
							rental.activeBookings(customerBookings2); // shows me active bookings
							int bookingNum2 = scanner.nextInt();
							scanner.nextLine(); // clearing extra line

							if (bookingNum2 < 1 || bookingNum2 > customerBookings2.size()) {
								// checking is the booking number is within the range of the bookings list
								System.out.println("Invalid Booking Selection");
								break;
							}
							BookingReceipt carToCancel = customerBookings2.get(bookingNum2 - 1);
							// get the booking here, using -1 since indexing starts at 0

							rental.cancelBooking(carToCancel); // cancel it!
							System.out.println("Your booking has been cancelled.");

						} catch (InputMismatchException e) { // wrong type (not a number in this case)
							System.out.println("\nInvalid input. Please enter a number for booking selection.");
							scanner.nextLine(); // clear invalid input
						}
					}
					if (cancelChoice.equals("n")) { // User chooses to not cancel, so they can see their active bookings
						System.out.println("\nYour Active Bookings: ");

						rental.activeBookings(customerBookings2);
					}
					break;

				default: // Invalid Choice! (0-5)
					System.out.println("Invalid Choice.");
					break;
				}

			} while (choice != 0);// Keep looping until the user chooses to exit

		} catch (InputMismatchException e) {
			System.out.println("\nInvalid choice. Please try again.");
			scanner.nextLine(); // Clearing the invalid input
		}
		scanner.close(); // Close the scanner when we're done

	}
}
