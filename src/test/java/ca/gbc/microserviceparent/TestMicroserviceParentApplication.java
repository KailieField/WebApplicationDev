package ca.gbc.microserviceparent;

import org.springframework.boot.SpringApplication;

public class TestMicroserviceParentApplication {

	public static void main(String[] args) {
		SpringApplication.from(MicroserviceParentApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
