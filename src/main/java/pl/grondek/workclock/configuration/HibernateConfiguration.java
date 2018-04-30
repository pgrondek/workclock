package pl.grondek.workclock.configuration;

import org.apache.tomcat.dbcp.dbcp2.BasicDataSource;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.autoconfigure.orm.jpa.JpaProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@Configuration
@EnableTransactionManagement
@EnableConfigurationProperties(JpaProperties.class)
public class HibernateConfiguration {

    private final DataSourceProperties dataSourceProperties;
    private final JpaProperties jpaProperties;
    private final JpaProperties.Hibernate hibernateProperties;

    public HibernateConfiguration(DataSourceProperties dataSourceProperties, JpaProperties jpaProperties) {
        this.dataSourceProperties = dataSourceProperties;
        this.jpaProperties = jpaProperties;
        this.hibernateProperties = jpaProperties.getHibernate();
    }

    @Bean
    public DataSource dataSource() {
        BasicDataSource dataSource = new BasicDataSource();

        dataSource.setDriverClassName(dataSourceProperties.getDriverClassName());
        dataSource.setUrl(dataSourceProperties.getUrl());
        dataSource.setUsername(dataSource.getUsername());
        dataSource.setPassword(dataSource.getPassword());

        return dataSource;
    }

    @Bean
    public LocalSessionFactoryBean sessionFactoryBean() {
        LocalSessionFactoryBean factoryBean = new LocalSessionFactoryBean();
        Properties hibernateProperties = new Properties();


        hibernateProperties.setProperty("hibernate.hbm2ddl.auto", this.hibernateProperties.getDdlAuto());

        factoryBean.setDataSource(dataSource());
        factoryBean.setHibernateProperties(hibernateProperties);
        factoryBean.setPackagesToScan("pl.grondek.workclock.entity");

        return factoryBean;
    }

    @Bean
    public HibernateTransactionManager transactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(sessionFactoryBean().getObject());
        return transactionManager;
    }
}
