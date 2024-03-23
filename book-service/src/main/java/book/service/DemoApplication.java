package book.service;

import org.flywaydb.core.Flyway;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		Flyway flyway =
				Flyway.configure()
						.locations("classpath:db/migration")
						.dataSource("jdbc:postgresql://localhost:5438/books_app", "postgres", "4569")
						.load();
		flyway.migrate();
		SpringApplication.run(DemoApplication.class, args);
	}

}
