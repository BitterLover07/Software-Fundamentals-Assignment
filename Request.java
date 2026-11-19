package Community_Pantry_Management_System;

// A request for a quantity of one item. The arrival number is assigned by the program
public class Request {
	private final String id;
    private final String itemId;
    private final int quantity;
    private final int arrivalNumber;
    private Status status;

    public Request (String id, String itemId, int quantity, int arrivalNumber) {
        this.id = id;
        this.itemId = itemId;
        this.quantity = quantity;
        this.arrivalNumber = arrivalNumber;
        this.status = Status.PENDING;
    }

    public String getId ()          {return id;}
    public String getItemId ()      {return itemId;}
    public int getQuantity ()       {return quantity;}
    public int getArrivalNumber ()  {return arrivalNumber;}
    public Status getStatus ()      {return status;}

    public void setStatus (Status status) {
        this.status = status;
    }
}