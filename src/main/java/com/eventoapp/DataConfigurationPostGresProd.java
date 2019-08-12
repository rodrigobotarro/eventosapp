package com.eventoapp;

import java.net.URI;
import java.net.URISyntaxException;

import javax.sql.DataSource;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.vendor.Database;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;

@Configuration
@Profile("prod")
public class DataConfigurationPostGresProd {

	@Bean
	public DataSource dataSource() throws URISyntaxException {
		DriverManagerDataSource dataSource = new DriverManagerDataSource();
		
		URI dbUri = new URI(System.getenv("DATABASE_URL"));
		
		String username = dbUri.getUserInfo().split(":")[0];
        String password = dbUri.getUserInfo().split(":")[1];
        String dbUrl = "jdbc:postgresql://" + dbUri.getHost() + ':' + dbUri.getPort() + dbUri.getPath();
		
		dataSource.setDriverClassName("org.postgresql.Driver");
		dataSource.setUrl(dbUrl);
		dataSource.setUsername(username);
		dataSource.setPassword(password);
		return dataSource;
	}

	@Bean
	public JpaVendorAdapter jpaVendorAdapter() {
		HibernateJpaVendorAdapter adapter = new HibernateJpaVendorAdapter();
		adapter.setDatabase(Database.POSTGRESQL);
		adapter.setShowSql(true);
		adapter.setGenerateDdl(true);
		adapter.setDatabasePlatform("org.hibernate.dialect.PostgreSQLDialect");
		adapter.setPrepareConnection(true);
		return adapter;
	}

}