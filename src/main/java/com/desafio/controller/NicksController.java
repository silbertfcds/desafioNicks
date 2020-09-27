package com.desafio.controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.desafio.service.DriverService;

@RestController
@RequestMapping("/api")
public class NicksController {

	@Autowired
	private DriverService driverService;
	
	@RequestMapping("/executar")
	public String executar() throws FileNotFoundException  {
		return driverService.executar();
	}
	
}
