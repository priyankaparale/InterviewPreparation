package com.dipdeveloper;

// Database Configuration for Development
@Configuration
@Profile("dev")
public class DevDatabaseConfig {

    @Bean
    public DataSource devDataSource() {
        System.out.println("Loading DEV Database Configuration");
        return new DriverManagerDataSource(
                "jdbc:mysql://localhost:3306/myapp_dev",
                "devuser",
                "devpass"
        );
    }
}

// Database Configuration for Production
@Configuration
@Profile("prod")
public class ProdDatabaseConfig {

    @Bean
    public DataSource prodDataSource() {
        System.out.println("Loading PROD Database Configuration");
        return new DriverManagerDataSource(
                "jdbc:mysql://prod-server:3306/myapp_prod",
                "produser",
                "prodpass"
        );
    }
}

// You can also use @Profile on specific beans
@Component
@Profile("test")
public class MockEmailService implements EmailService {

    @Override
    public void sendEmail(String to, String message) {
        System.out.println("MOCK EMAIL - To: " + to + ", Message: " + message);
    }
}

// In application.properties or application.yml
// spring.profiles.active=dev
// Pro tip: Spring Boot vs Spring Framework - Spring Boot uses @Profile the same way,
// but it's easier to activate profiles in Boot using application-{profile}.properties files.