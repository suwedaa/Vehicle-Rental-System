package com.bptn.Vehicle_Rental_System.Project;

public class Car {

	private String colour;
	private double rentalPrice;
	private boolean inStock;
	private String brand;
	private String model;
	private int seatNum;
	public String fuelType;

	public Car(String colour, double rentalPrice, boolean inStock, String brand, String model, int seatNum,
			String fuelType) {

		this.colour = colour;
		this.rentalPrice = rentalPrice;
		this.inStock = inStock;
		this.brand = brand;
		this.model = model;
		this.seatNum = seatNum;
		this.fuelType = fuelType;

	}

	public String getColour() {
		return colour;
	}

	public double getRentalPrice() {
		return rentalPrice;
	}

	public boolean getAvailability() {
		return inStock;
	}

	public String getBrand() {
		return brand;
	}

	public String getModel() {
		return model;
	}

	public int getSeatNum() {
		return seatNum;
	}

	public String getFuelType() {
		return fuelType;
	}

	public void setAvilability(boolean inStock) {
		this.inStock = inStock;
	}

	public void displayCarDetails() {
		System.out.println("\nBrand: " + brand);
		System.out.println("Model: " + model);
		System.out.println("Color: " + colour);
		System.out.println("Seats: " + seatNum);
		System.out.println("Fuel Type: " + fuelType);
		System.out.println("Rental Price: $" + rentalPrice);
		System.out.println("Availability: " + inStock);

	}

}
