package com.lge.hems;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class GetEvent {
	@RequestMapping(value = "/eventTest", method = RequestMethod.POST)
	public void getEvent(@RequestBody String request) {
		System.out.println(request);
	}
}
