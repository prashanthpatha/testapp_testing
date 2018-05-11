package rally.integration.slack;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"rally.integration.slack"})
public class SpringBootRestApiApp {
	public static void main(String[] args) {
		SpringApplication.run(SpringBootRestApiApp.class,args);
	}
}
