//this is the enumeration of the list of coffees students can order
public enum CoffeeType {
	ESPRESSO(2.00),
	LATTE(3.50),
	MOCHA(4.00),
	AMERICANO (2.50);

	final double price;
	
	CoffeeType(double price) {
		this.price = price;
	}
	
	//to access the price
	public double getPrice() {
		return price;
	}
}
