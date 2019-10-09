package com.im.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.im.service.UserService;

@Controller
@RequestMapping("/index")
public class InitController {
	
	@Autowired
	private UserService userService;
	
	@RequestMapping("/init")
	public String initIndex() {
		return "index";
	}
}
