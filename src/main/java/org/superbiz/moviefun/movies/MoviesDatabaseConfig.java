package org.superbiz.moviefun.movies;

import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.superbiz.moviefun.DatabaseServiceCredentials;

import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;

@Configuration
public class MoviesDatabaseConfig {
    @Bean
    DataSource moviesDataSource(DatabaseServiceCredentials serviceCredentials) {
        MysqlDataSource dataSource = new MysqlDataSource();
        dataSource.setURL(serviceCredentials.jdbcUrl("movies-mysql"));
        HikariConfig config = new HikariConfig();
        config.setDataSource(dataSource);
        return new HikariDataSource(config);
    }

    @Bean
    LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory(
            DataSource moviesDataSource, HibernateJpaVendorAdapter hibernateJpaVendorAdapter) {
        LocalContainerEntityManagerFactoryBean moviesEntityManagerFactory
                = new LocalContainerEntityManagerFactoryBean();
        moviesEntityManagerFactory.setDataSource(moviesDataSource);
        moviesEntityManagerFactory.setJpaVendorAdapter(hibernateJpaVendorAdapter);
        moviesEntityManagerFactory.setPackagesToScan("org.superbiz.moviefun.movies");
        moviesEntityManagerFactory.setPersistenceUnitName("movies");

        return moviesEntityManagerFactory;
    }

    @Bean
    PlatformTransactionManager moviesPlatformTransactionManager(EntityManagerFactory moviesEntityManagerFactory) {
        JpaTransactionManager moviesJpaTransactionManager = new JpaTransactionManager();
        moviesJpaTransactionManager.setEntityManagerFactory(moviesEntityManagerFactory);
        return moviesJpaTransactionManager;
    }
}
