package simple_blog.LeeJerry;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class LeeJerryApplication {

	public static void main(String[] args) {
		SpringApplication.run(LeeJerryApplication.class, args);
	}

}
