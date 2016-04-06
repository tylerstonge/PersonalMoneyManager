package edu.oswego.tygama344;

import java.text.NumberFormat;

public class Purchase {

    private int id;
    private String name;
	private int amount;

    public Purchase(int id, String name, int amount) {
        //TODO add date to purchase object
        //TODO store amounts in cents instead of dollar amounts
        this.id = id;
        this.name = name;
		this.amount = amount;
	}

    public Purchase(String name, int amount) {
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

	public int getAmount() {
		return amount;
	}

    public String getDollars() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(amount/100f);

    }
}
