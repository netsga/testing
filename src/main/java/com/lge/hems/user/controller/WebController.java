package com.lge.hems.user.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by netsga on 2016. 5. 24..
 */

@Controller
public class WebController {
	@RequestMapping("/")
	public String home(Model model){
		model.addAttribute("name","hello springBoot");
		return "index";
	}
	
	@RequestMapping("/index")
	public String indexPage(){
		return "index";
	}
	
	@RequestMapping("/success")
	public String successPage(){
		return "success";
	}
	
	@RequestMapping("/logout")
	public String logoutPage(){
		return "logout";
	}
	
	@RequestMapping("/join")
	public String joinPage(){
		return "join";
	}
}