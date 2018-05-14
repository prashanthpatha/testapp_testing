package ac.integration;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"ac.integration"})
public class IntegrationServiceApp {
	public static void main(String[] args) {
		SpringApplication.run(IntegrationServiceApp.class,args);
	}
}
