//this is an abstract class that will be implemented by Student.java
public abstract class Person {
	//a String field, name  
	protected String name;
	
	//the constructor initializes name 
	public Person(String name) {
		this.name = name;
	}
	
	//this is an abstract getter method 
	public abstract String getRole();

}
