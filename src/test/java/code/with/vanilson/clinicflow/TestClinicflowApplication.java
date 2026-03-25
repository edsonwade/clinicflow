package code.with.vanilson.clinicflow;

import org.springframework.boot.SpringApplication;

public class TestClinicflowApplication {

	public static void main(String[] args) {
		SpringApplication.from(ClinicflowApplication::main).with(TestcontainersConfiguration.class).run(args);
	}

}
