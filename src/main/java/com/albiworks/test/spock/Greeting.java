package com.albiworks.test.spock;

/**
 * Simple greeting class to display a hello message
 * @author Alexandro Blanco <alex@albiworks.com>
 *
 */
public class Greeting {

	private String greeting;

	public Greeting(){
		this("World");
	}
	
	public Greeting(String name) {
		super();
		this.greeting = ("Hello " + name);
	}

	public String getGreeting() {
		return greeting;
	}
	
}
