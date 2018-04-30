package pl.grondek.workclock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
public class WorkclockApplication {

    public static void main(String[] args) {
        SpringApplication.run(WorkclockApplication.class, args);
    }
}
