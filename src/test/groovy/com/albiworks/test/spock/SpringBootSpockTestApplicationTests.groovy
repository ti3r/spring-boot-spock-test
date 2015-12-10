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

import spock.lang.AutoCleanup
import spock.lang.Shared
import spock.lang.Specification

//@RunWith(SpringJUnit4ClassRunner)
//@SpringApplicationConfiguration(classes = SpringBootSpockTestApplication)
//@WebAppConfiguration
class SpringBootSpockTestApplicationTests extends Specification {

	@Shared
    @AutoCleanup
    ConfigurableApplicationContext context

	@Shared
	MockMvc mockMvc;
	
    void setupSpec() {
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

	void "Test greeting with default value"(){
		when:
			Greeting g = new Greeting();
		then:
			g.getGreeting().contains("World")
	}
	
	
	
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
	
}
