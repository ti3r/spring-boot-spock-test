package com.albiworks.test.spock

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*

import java.util.concurrent.Callable
import java.util.concurrent.Executors
import java.util.concurrent.Future
import java.util.concurrent.TimeUnit

import org.springframework.boot.SpringApplication
import org.springframework.context.ConfigurableApplicationContext
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.util.NestedServletException

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

class SpringBootSpockTestApplicationTests extends Specification {

	@Shared
    @AutoCleanup
    ConfigurableApplicationContext context

	@Shared
	MockMvc mockMvc;
	
    void setupSpec() {
		//Create an application context based on the SpringBootSpockTestApplication
		//class containing the app configuration. It is created in a Future object
		//In order to wait only 60 seconds for the context to come up or fail
        Future future = Executors
                .newSingleThreadExecutor().submit(
                new Callable() {
                    @Override
                    public ConfigurableApplicationContext call() throws Exception {
                        return (ConfigurableApplicationContext) SpringApplication
                                .run(SpringBootSpockTestApplication.class)
                    }
                })
        context = future.get(60, TimeUnit.SECONDS)
		
		mockMvc = MockMvcBuilders.webAppContextSetup(context).build();	
    }

	/**
	 * This methods perform a simple check with the Greeting object
	 * defined in the application. The default greeting message
	 * should be Hello World
	 */
	void "Test greeting with default value"(){
		when:
			Greeting g = new Greeting();
		then:
			g.getGreeting().contains("World")
	}
	
	void "Test greeting with a parameter in the constructor"(){
		when:
			Greeting g = new Greeting("Alex");
		then:
			g.getGreeting() == "Hello Alex"
	}
	/**
	 * This method creates a Mock object of the Greeting class
	 * that will return "Hola Mundo" (Hello World in spanish)
	 * when the getGreeting method is called. 
	 * Then it checks a difference in the default return value
	 */
	void "Test greeting with a mock object"(){
		when:
			def Greeting gmock = Mock(Greeting)
			gmock.getGreeting() >> "Hola Mundo"
		then:
		
			def hello = gmock.getGreeting()
			hello != "Hello World"
	}
	
	/**
	 * These following methods display how to check calls to a Restful
	 * resource using the MockMvc object prepared in the setup method
	 * and check the result of the call using jsonPath expressions.
	 * the result of the resource should be something like this
	 * {'greeting':'<message>'}
	 */
	void "Test api call to hello"(){
		when:
			ResultActions result = mockMvc.perform(get("/hello"))
		then:
			result.andExpect(status().isOk())
			.andExpect(jsonPath("\$.greeting").exists())
			.andExpect(jsonPath("\$.greeting").value("Hello World"))
	}
	
	void "Test api call to hello with name"(){
		when:
			ResultActions result = mockMvc.perform(get("/hello/alex"))
		then:
			result.andExpect(status().isOk())
			.andExpect(jsonPath("\$.greeting").value("Hello alex"))
	}
	
	/**
	 * This test creates a new mock object of the HelloResource to 
	 * override the sayHello method in the resource to throw an exception
	 * then it builds a stand alone MockMvc object with that resource
	 * in order to be called when the get("/hello") request is invoked.
	 * Then is demonstrates how an expected exception is caught and checked
	 * in the then clause.
	 */
	void "Test a runtime exception in the hello controller"(){
		setup:
			def HelloResource mockHello = Mock(HelloResource)
			mockHello.sayHello() >> {throw new RuntimeException("Test Exception")}
			def mvcWithMock = MockMvcBuilders.standaloneSetup(mockHello).build()
		when:
			ResultActions r = mvcWithMock.perform(get("/hello"))
		then:
			NestedServletException ex = thrown() 
			ex.getCause().getMessage() == "Test Exception"
	}
	
}
