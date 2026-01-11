import java.util.Queue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.nio.file.*;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.List;

/*
 * manages the Student Hub (a waiting queue of students, plus a 2D structure
 * that groups students by an area (Cafe, Lounge, Study Room))
 */

public class StudentHub {
	
/**keeps track of students waiting to be placed into the hub */
private final Queue<Student> waitingQueue; 

/**
 * 2D list that stores students by area 
 * row 0 = cafe, row 1= lounge, row 2 = study rom
 */
private ArrayList<ArrayList<Student>> hubData; 


/**
 * Creates an empty StudentHub with a fresh queue and empty hubData
 */
public StudentHub() {
	 waitingQueue = new LinkedList<Student>();
	 hubData = new ArrayList<ArrayList<Student>>();
}


/**
 * Adds a student to the waiting queue and timestamps when they were added
 * @param s the student to add
 * 
 */
public void buildQueue(Student s) {
	s.markAddTime(); //timestamp it
	waitingQueue.add(s);  //add to queue
} 


/**
 * Returns the current waiting queue
 * @return the queue of students waiting to enter the hub
 */
public Queue<Student> getWaitingQueue(){
	return waitingQueue;
}


/**
 * Moves students out of the waiting queue and groups them into hubData
 * based on their session area (cafe, lounge, study room)
 * 
 * After this runs the waiting queue will be empty
 */
public void buildHubData() {
	hubData.clear(); //make sure it starts off empty
	
	//create the 3 area lists to store where people are 
	ArrayList<Student> cafeList = new ArrayList<>();
	ArrayList<Student> loungeList = new ArrayList<>();
	ArrayList<Student> studyList = new ArrayList<>();
	
	//while there are students in the queue
	while(!waitingQueue.isEmpty()) {
		
		Student currStudent = waitingQueue.poll(); //get the first student in waiting queue
		String area = currStudent.getSession().getArea().toLowerCase(); //student's area
		
		if(area.equals("cafe")) { //if they're at the cafe
			cafeList.add(currStudent); //add them to the cafe list
		}
		
		else if(area.equals("lounge")) { //if they're at the lounge
			loungeList.add(currStudent); //add them to the lounge list
		}
		
		else if(area.equals("study room")) { //if they're in the study room
			studyList.add(currStudent); //add them to the study list
		}
		
	}
	
	//add the 3 sublists into hubData in order once loops are done
			hubData.add(cafeList);    //row 0
			hubData.add(loungeList); //row 1
			hubData.add(studyList); // row 2
	
 }


/**
 * Returns the current 2DD hub data (grouped by area)
 * @return the hubData structure
 */
public ArrayList<ArrayList<Student>> getHubData(){
	return hubData;
}




/*
 *  sorts each row of hubData alphabetically by student name
 *  Uses Collections.sort wit lambda operator
 */
public void sortHubDatabyStudentName() {
	for(ArrayList<Student> row : hubData) {
		Collections.sort(row, (s1, s2) -> s1.getName().compareTo(s2.getName())); 
	}
	
}



/*
 *  sorts each row of hubData alphabetically by student bill
 * Uses Double.compare because bills are primitive dobules
 */
public void sortHubDatabyStudentBill(){
	for(ArrayList<Student> row : hubData) {
		Collections.sort(row, (s1, s2) -> Double.compare(s1.getBill(), s2.getBill())); 
	} 
}




/**
 * Searches hubData for a student by name and returns their bill if found
 * 
 * @param name the student name to search for 
 * @return the student's total bill
 * @throws UnknownStudentException if no matching student is found
 * 
 */
public double getStudentBill(String name) throws UnKnownStudentException{
	String search = name.toLowerCase(); 
	
	for(ArrayList<Student> row : hubData) { //loop thru rows in hubData
		for(Student s : row) { 				//loop through each element in each row
			String sName = s.getName().toLowerCase(); //students name lower-cased
			if (sName.compareTo(search)==0) { //compare parameter and s name
				return s.getBill();
			}
		
		}
	} 
		throw new UnKnownStudentException("Student not found.");
}


/**
 * Calculates the total profit of the hub by summing every student's bill
 * A wrapper that starts recursive method with (0,0)
 */
public double calculateTotalHubProfit() {
	return calculateTotalHubProfitRecursive(0, 0);
	//starts recursion method at i = 0 (row 0), j =0 (column 0)
}



/**
 * Recursively walks through hubDat and adds up every student's bill
 * 
 * @param i current row index
 * @param j current column index within the row
 * @return total bill from (i,j) onward
 */
private double calculateTotalHubProfitRecursive(int i, int j) { 
	 if(i == hubData.size()) { //base case: finishes all rows
	        return 0.0; 
	  }

	  if (j == hubData.get(i).size()) { //end of this row, move to next row
	        return calculateTotalHubProfitRecursive(i  + 1, 0);
	  }
	    
	  Student s = hubData.get(i).get(j); 
	  return s.getBill() + calculateTotalHubProfitRecursive(i, j + 1);	

}


/**
 * Flattens hubData into a single list of all students
 * 
 * @return a list containing every student current in hubData
 */
private ArrayList<Student> getAllStudentsFromHubData(){
	ArrayList<Student> allStudents = new ArrayList<>();
	for(ArrayList<Student> row : hubData) { 
		allStudents.addAll(row);
	}
	
	return allStudents;
}


/**
 * Saves hub data to a text file 
 * Each line is stored like this:
 * @code name | area | minutes | order1;order2;order3
 * 
 * @param filePath path to the file to write
 * @throws IOException if the file can't be written
 */
public void saveHubDataToFile(String filePath) throws IOException {
	ArrayList<Student> all = getAllStudentsFromHubData();
	ArrayList<String> lines = new ArrayList<>();

	for(Student s : all) {
		String name = s.getName();
		String area = s.getSession().getArea();
		int minutes = s.getSession().getMinutes();
	
		ArrayList<String> orderNames = new ArrayList<>();

		//build a semicolon seperated list of order descriptions
		for (Billable b : s.getBillable()) { 
		    orderNames.add(b.getDescription());
		}
		
		String ordersString = String.join(";", orderNames);  
        String line = name + " | " + area + " | " + minutes + " | " + ordersString;
        
        lines.add(line);
    }
	
	Files.write(
		    Paths.get(filePath),   
		    lines,                
		    StandardCharsets.UTF_8 
		);
	 
	}



/**
 * Loads hub data from a text file created by saveHubDataToFile
 * 
 * Clears existing data, rebuilds each Student, adds them back into the queue,
 * and then rebuilds hubData from that queue
 * 
 * @param filePath path to the file to read
 * @throws IOException if the file can't be read
 */

public void loadHubDataFromFile(String filePath) throws IOException{
	List<String> lines = Files.readAllLines(Paths.get(filePath), StandardCharsets.UTF_8);
	
	//reset existing data
	waitingQueue.clear();
	hubData.clear();
	
	for(String line : lines) {
		if (line.isBlank()) continue; 
		
		//split into 4 parts...name, area, minutes, orders
		String[] parts = line.split("\\|", 4);
		
		String name = parts[0].trim();
		String area = parts[1].trim();
		int minutes = Integer.parseInt(parts[2].trim()); //parseInt converts text --> number
		
		//rebuild same Student 
		Student s = new Student(name, area, minutes);
		
		//orders are optional
		if(parts.length == 4 && !parts[3].isBlank()) {
			String[] items = parts[3].split(";"); // ["LATTE";COOKIE;MOCHA] --> ["LATTE", "COOKIE", "MOCHA"]
			ArrayList<Billable> orderList = new ArrayList<>(); //will hold reconstructed orders
			
			for(String item : items) {
				String token = item.trim().toUpperCase();
				if(token.isEmpty()) continue;
				
				//try DessertType first, then CoffeeType; unknown tokens get ignored
				try {
					orderList.add(new CafeOrder<DessertType>(DessertType.valueOf(token)));
				}
				catch(IllegalArgumentException ex1) {
					//ignore
				}
				
				try {
                    orderList.add(new CafeOrder<CoffeeType>(CoffeeType.valueOf(token)));
                } 
				
				catch (IllegalArgumentException ex2) {
                    //ignore
                }
			}
			
			//attach orders to the student (updates their bill too)
			s.addOrders(orderList);
		}
		//put student back into queue (also timestamps the add time)
		buildQueue(s);
	}
	//rebuild hubData from the queue
	buildHubData();
}

}


