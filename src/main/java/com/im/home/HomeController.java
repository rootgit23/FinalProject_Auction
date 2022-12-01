package com.im.home;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@CrossOrigin(origins = "*", methods = RequestMethod.GET) 
public class HomeController {
	@GetMapping("/")
	public String home() throws Exception{
		return "index";
	}
}
