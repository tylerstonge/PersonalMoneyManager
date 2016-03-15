package edu.oswego.tygama344;

public class Purchase {
	
	private String name;
	private double amount;

	public Purchase(String name, double amount) {
		this.name = name;
		this.amount = amount;
	}

	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}
}
