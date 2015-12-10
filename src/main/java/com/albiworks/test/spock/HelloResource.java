package com.albiworks.test.spock;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping(path="/hello", produces=MediaType.APPLICATION_JSON_VALUE)
public class HelloResource {

	@RequestMapping(method=RequestMethod.GET)
	public @ResponseBody Greeting sayHello(){
		return new Greeting();
	}
	
	@RequestMapping(method=RequestMethod.GET, path="/{name}")
	public @ResponseBody Greeting sayHelloTo(@PathVariable(value="name") String name){
		return new Greeting(name);
	}
	
}
