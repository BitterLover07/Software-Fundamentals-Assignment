package Community_Pantry_Management_System;

// A pantry item with a unique ID, a name and the current stock level
public class Item {
	private final String id;
    private final String name;
    private int stock;

    public Item (String id, String name, int stock) {
        this.id = id;
        this.name = name;
        this.stock = stock;
    }

    public String getId ()   {return id;}
    public String getName () {return name;}
    public int getStock ()   {return stock;}

    // True only if the whole quantity can be issued (no partial fulfillment)
    public boolean hasEnough (int quantity) {
        return stock >= quantity;
    }

    // Deducts stock. Refuses to go below zero so stock can never become negative
    public void reduceStock (int quantity) {
        if (quantity > stock) {
            throw new IllegalStateException("Cannot reduce stock below zero.");
        }
        
        stock -= quantity;
    }
}