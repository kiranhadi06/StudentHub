import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Student extends Person{

private double bill; // tracks the student’s total bill. It starts at 0. 
private StudySession session; //stores where the student studied and for how long (area, minutes)
private ArrayList<Billable> orders; //stores all items the student ordered of type Billable.
private LocalDateTime addTime;



/*
 * this method initializes the student’s name using the Person superclass. 
 * creates a new StudySession using the provided area name and duration. 
 * sets bill to 0 and initializes orders to an empty ArrayList.
 */

public Student(String name, String area, int minutes) {
	super(name); //inheritance! initializes name in Person
	this.bill = 0; //bill starts off as 0
	
	//creates and stores student's study session based on constructor params
	this.session = new StudySession(area, minutes);
	
	//initializes orders list so its ready to add Coffee & Dessert items
	this.orders = new ArrayList<>();
	
}


//returns the student’s name. (accessor) 
public String getName() {
	return name;
}


//implementing abstract method from Person.java
//returns the String literal "Student" 
public String getRole() {
	return "Student";
	
}


//assigns the provided list of orders to orders 
//calls calcBill() to compute the total cost 
public void addOrders (ArrayList<Billable> orderList){
	this.orders = orderList; 
	calcBill();
}


private void calcBill() {
	double total = 0.0;
	for(Billable item : orders) { //loop over orders and name each element 'items'
		total += item.getCost(); //get the cost of each item and store in 'total'
	} 
	//getCost is from cafeOrder which implements it from Billable
	//getCost uses getPrice from enumerations 
	this.bill = total; //add the total to the bill
}


//accessors 
public StudySession getSession() {
	return this.session;
}

public ArrayList<Billable> getBillable() {
	return this.orders;
}

public double getBill() {
	return this.bill;
}

//string for student info
@Override
public String toString() {
	String result = "Student: " + getName() + " " + session.toString() + "\n";
	result += "Student Ordered: \n"; //header 
	
	for(Billable order : orders) { //add each order
		result += order.toString() + "\n";
	}
	
	result += "Total Bill = " + String.format("%.2f", bill) + "\n";
	
	return result;
			
}

//nested static class 
//student can have a study session so define it as a static class
static class StudySession{
	private final String area; // the study area (Lounge, Cafe, or Study Room)
	private final int minutes;  //time spent in that area.
	
	//constructor initializes area and minute fields
	public StudySession(String area, int minutes) {
		this.area = area;
		this.minutes = minutes;
	}
	
	//returns area (accessor)
	public String getArea() {
		return this.area;
	}
	
	//returns time spent in area (accessor)
	public int getMinutes() {
		return this.minutes;
	}
	
	public String toString() {
		return "Studied at: " + area + " for: " + minutes + " mins";
	}
}

//mark added to hub time and format

//sets time stamp to right now
public void markAddTime() {
	this.addTime = LocalDateTime.now();
}

// a date and time without worrying about time zones
public LocalDateTime getAddedToHubAt() {
	return addTime;
}

//format into readable string
public String getAddTimeFormatted() {
	if (addTime == null) return "N/A";
	DateTimeFormatter fmt = DateTimeFormatter.ofPattern("yyy-MM-dd HH:mm:ss");
	return addTime.format(fmt);
}
}
