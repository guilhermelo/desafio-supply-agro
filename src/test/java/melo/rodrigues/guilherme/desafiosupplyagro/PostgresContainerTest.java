package melo.rodrigues.guilherme.desafiosupplyagro;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.testcontainers.containers.PostgreSQLContainer;

import javax.sql.DataSource;

@Component
public class PostgresContainerTest {

    private PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13-alpine");

    @Bean
    public DataSource datasource() {
        postgreSQLContainer.start();
        String jdbcURL = String.format("jdbc:postgresql://%s:%s/%s", postgreSQLContainer.getContainerIpAddress(),
                                                                     postgreSQLContainer.getFirstMappedPort(),
                                                                     postgreSQLContainer.getDatabaseName());

        return DataSourceBuilder.create()
                                .password(postgreSQLContainer.getPassword())
                                .url(jdbcURL)
                                .username(postgreSQLContainer.getUsername())
                                .driverClassName(postgreSQLContainer.getDriverClassName())
                                .build();
    }
}
