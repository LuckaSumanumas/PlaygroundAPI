package com.lucka.playground;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

/**
 * @author Mindaugas Lucka
 */
@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class PlaygroundApplication {

	private static final Logger LOGGER=LoggerFactory.getLogger(PlaygroundApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(PlaygroundApplication.class, args);

		LOGGER.info("Starting application");
	}

}
