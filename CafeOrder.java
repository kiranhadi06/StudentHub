//type of order is exclusive to enumerations type and implements abstract fields in Billable.java

public class CafeOrder <T extends Enum<T>> implements Billable{

	private final T order; //a generic type variable order
	
	//the constructor initializes order 
	public CafeOrder(T order) {
		this.order = order;
	}
	
	
	/*
	 *  This method checks to see if order is CoffeeType or DessertType and then returns the price. 
	 *  returns 0 otherwise. 
	 *  -uses instanceof to check the type 
	 *  -casts the order to the correct type
	 *  -uses the getPrice() method to return the correct price
	 */
	@Override
	public double getCost() {
		//if the order is a Coffee type
		if (order instanceof CoffeeType) {
			CoffeeType coffee = (CoffeeType) order; //cast order to a CoffeeType/instantiate it
			return coffee.getPrice(); //use the getPrice() method on order from CoffeeType class 
		}
		
		//if the order is a Dessert type
		else if (order instanceof DessertType) { 
			DessertType dessert = (DessertType) order; //cast order to a DessertType/instantiate it
			return dessert.getPrice(); //use the getPrice() method on order from DessertType class 
		}
		
		//if its not coffeetype or dessert type return 0
		return 0;
	}

	 
	//returns name of the order
	@Override
	public String getDescription() {
		return order.name(); //use it to get names of enumeration 
	}
	
	
	//returns a string which contains the order name and the price
	@Override
	public String toString() {
		return "Item: " + getDescription() + " Price: " + getCost();
	}
	

}
