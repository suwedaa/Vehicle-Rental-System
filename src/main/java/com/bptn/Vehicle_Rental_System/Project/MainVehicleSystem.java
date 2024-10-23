package com.bptn.Vehicle_Rental_System.Project;

import java.util.List;
import java.util.Scanner;

public class MainVehicleSystem {
	private Rental rental;

	public MainVehicleSystem() {
		rental = new Rental();
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		MainVehicleSystem main = new MainVehicleSystem();
		main.displayMenu();
	}

	public void displayMenu() {
		Scanner scanner = new Scanner(System.in);
		int choice;

		do {
			System.out.println("\n****Welcome to Suweda's Car Rental System!****");
			System.out.println("\n");
			System.out.println("1. View Our Available Cars");
			System.out.println("2. Set Your Profile"); // login
			System.out.println("3. Rent One of Our Cars"); // rent car
			System.out.println("4. Return One of Our Cars"); // return car
			System.out.println("5. Manage Your Booking"); // cancel or view booking
			System.out.println("0. Back to Main Menu");
			System.out.print("Enter your choice: ");

			choice = scanner.nextInt();
			scanner.nextLine();

			switch (choice) {

			case 1:
				System.out.println("\nAvailable Cars: ");
				rental.viewAvailableCars();
				break;

			case 2:
				System.out.println("\nPlease Enter Your Customer ID: ");
				String customerId = scanner.nextLine();
				if (customerId.isEmpty()) {
					System.out.println("Customer ID cannot be empty. Please Try Again");
				}
				System.out.println("\nPlease Enter Your Name: ");
				String customerName = scanner.nextLine();
				if (customerName.isEmpty()) {
					System.out.println("Customer Name cannot be empty. Please Try Again");
				}
				Customer customer = new Customer(customerId, customerName);
				rental.addCustomer(customer);

				break;

			case 3: // rent car

				System.out.println("\nEnter Customer ID: ");
				String customerID = scanner.nextLine();

				Customer c1 = rental.findCustomerId(customerID);

				if (c1 == null) {
					System.out.println("\nCustomer is not found, please create a profile first");
					break;
				}

				rental.addCustomer(c1);

				rental.viewAvailableCars();
				System.out.println("\nWhich Number Car Do You Want: ");
				int carNum = scanner.nextInt();
				scanner.nextLine();

				Car selectedCar = rental.findCar(carNum - 1);
				if (selectedCar == null) {
					System.out.println("\nInvalid Car Selection");
					break;
				}

				System.out.println("\nEnter the number of days for rental: ");
				int days = scanner.nextInt();
				scanner.nextLine();

				rental.rentCar(c1, selectedCar, days);
				break;

			case 4: // return car
				System.out.println("\nEnter Customer ID: ");
				String customerReturn = scanner.nextLine();

				Customer c2 = rental.findCustomerId(customerReturn);

				if (c2 == null) {
					System.out.println("\nCustomer is not found, please create a profile first");
					break;
				}
				List<BookingReceipt> customerBookings = c2.getBooking();
				if (customerBookings == null) {
					System.out.println("\nYou Do Not Have Any Active Bookings! Please Rent a Car.");
					break;
				}

				System.out.println("\nYour Active Bookings: ");

				rental.activeBookings(customerBookings);
				System.out.println("\nWhich Car Would You Like To Return: ");
				int bookingNum = scanner.nextInt();
				scanner.nextLine();

				if (bookingNum < 1 || bookingNum > customerBookings.size()) {
					System.out.println("Invalid Booking Selection");
					break;
				}
				BookingReceipt carToReturn = customerBookings.get(bookingNum - 1);
				rental.returnCar(carToReturn);
				break;

			case 5: // MANAGE BOOKINGS

				System.out.println("\nEnter Customer ID: ");
				String manageCustomer = scanner.nextLine();

				Customer c3 = rental.findCustomerId(manageCustomer);

				if (c3 == null) {
					System.out.println("\nCustomer is not found, please create a profile first");
					break;
				}
				List<BookingReceipt> customerBookings2 = c3.getBooking();
				if (customerBookings2 == null || customerBookings2.isEmpty()) {
					System.out.println("\nYou Do Not Have Any Active Bookings! Please Rent a Car.");
					break;
				}
				System.out.println("\nWould You Like To Cancel Your Booking?(y/n) ");
				String cancelChoice = scanner.nextLine();

				if (!cancelChoice.equals("y") && !cancelChoice.equals("n")) {
					System.out.println("Please enter 'y' or 'n'.");
					continue;
				}

				if (cancelChoice.equals("y")) {
					System.out.println("\nWhich Booking Would You Like to Cancel: ");
					rental.activeBookings(customerBookings2);
					int bookingNum2 = scanner.nextInt();
					scanner.nextLine();

					if (bookingNum2 < 1 || bookingNum2 > customerBookings2.size()) {
						System.out.println("Invalid Booking Selection");
						break;
					}
					BookingReceipt carToCancel = customerBookings2.get(bookingNum2 - 1);

					rental.cancelBooking(carToCancel);

				}
				if (cancelChoice.equals("n")) {
					System.out.println("\nYour Active Bookings: ");

					rental.activeBookings(customerBookings2);
				}
				break;

			default:
				System.out.println("Invalid Choice. Please try again.");
			}
		} while (choice != 0);
		scanner.close();
	}

}
