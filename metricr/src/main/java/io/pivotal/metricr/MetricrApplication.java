package io.pivotal.metricr;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import io.pivotal.metricr.domain.EventTimeStampRange;
import lombok.extern.log4j.Log4j2;

@SpringBootApplication
@Log4j2
public class MetricrApplication {
		
	public static void main(String[] args) {
		SpringApplication.run(MetricrApplication.class, args);
	}
	
	@Bean
	EventTimeStampRange eventTimeStampRange() {
		return new EventTimeStampRange();
	}
}
