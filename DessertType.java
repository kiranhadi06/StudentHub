//this is the enumeration of the list of desserts students can order
public enum DessertType {
	CAKE(4.50),
	COOKIE(1.25),
	BROWNIE(2.75),
	PASTRY(3.00);
	
	final double price;
	
	DessertType(double price){
		this.price = price;
	}
	
	public double getPrice() {
		return price;
	}
}
