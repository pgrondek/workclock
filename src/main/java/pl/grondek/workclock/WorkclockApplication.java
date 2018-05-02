package pl.grondek.workclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class WorkclockApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkclockApplication.class, args);
    }
}
