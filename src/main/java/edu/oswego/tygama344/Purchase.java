package edu.oswego.tygama344;

import java.sql.Date;
import java.text.NumberFormat;

public class Purchase {

    private int id;
    private String name;
	private int amount;
    Date purchaseDate;
    String category;

    public Purchase(int id, String name, int amount, Date date, String category) {
        this.id = id;
        this.name = name;
		this.amount = amount;
        this.purchaseDate = date;
        this.category = category;
    }

    public Purchase(String name, int amount, String category) {
        this.name = name;
        this.amount = amount;
        this.category = category;
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

    public Date getDate() {return purchaseDate;}

    public String getDollars() {
        NumberFormat nf = NumberFormat.getCurrencyInstance();
        return nf.format(amount/100f);
    }

    public String getCategory() {return category;}
}
