package pl.grondek.workclock.test

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.orm.hibernate5.HibernateTransactionManager
import org.springframework.orm.hibernate5.LocalSessionFactoryBean
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.EnableTransactionManagement

import javax.sql.DataSource

@TestConfiguration
@EnableTransactionManagement
class HibernateTestConfiguration {

    @Bean
    LocalSessionFactoryBean sessionFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(dataSource());
        sessionFactory.setPackagesToScan("pl.grondek.workclock.entity", "pl.grondek.workclock.test")
        sessionFactory.setHibernateProperties(hibernateProperties())

        return sessionFactory;
    }

    @Bean
    DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource()
        dataSource.setDriverClassName("org.h2.Driver")
        dataSource.setUrl("jdbc:h2:mem:db;DB_CLOSE_DELAY=-1")
        dataSource.setUsername("sa")
        dataSource.setPassword("sa")

        return dataSource
    }

    @Bean
    PlatformTransactionManager hibernateTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager()
        transactionManager.setSessionFactory(sessionFactory().getObject())
        return transactionManager
    }

    private Properties hibernateProperties() {
        Properties hibernateProperties = new Properties()
        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", "create-drop")
        hibernateProperties.setProperty("hibernate.dialect", "org.hibernate.dialect.H2Dialect")

        return hibernateProperties
    }
}
