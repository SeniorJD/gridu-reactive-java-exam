package com.syarm.gridu.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.tools.agent.ReactorDebugAgent;

@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		if ("true".equalsIgnoreCase(System.getProperty("reactorDebug"))) {
			ReactorDebugAgent.init();
			ReactorDebugAgent.processExistingClasses();
		}
		SpringApplication.run(Application.class, args);
	}

}
