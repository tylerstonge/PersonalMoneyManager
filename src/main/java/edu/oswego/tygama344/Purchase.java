package edu.oswego.tygama344;

public class Purchase {

    private int id;
    private String name;
	private double amount;

    public Purchase(int id, String name, double amount) {
        //TODO add date to purchase object
        //TODO store amounts in cents instead of dollar amounts
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
