package edu.oswego.tygama344;

public class Purchase {

    private int id;
    private String name;
	private double amount;

    public Purchase(int id, String name, double amount) {
        this.id = id;
        this.name = name;
		this.amount = amount;
	}

    public Purchase(String name, double amount) {
        this.id = -1;
        this.name = name;
        this.amount = amount;
    }

    public int getId() {
        return id;
    }

	public String getName() {
		return name;
	}

	public double getAmount() {
		return amount;
	}
}
