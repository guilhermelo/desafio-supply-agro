package melo.rodrigues.guilherme.desafiosupplyagro;

import com.totvs.tjf.api.jpa.repository.impl.ApiJpaRepositoryImpl;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@EnableTransactionManagement
@EntityScan(basePackages = "melo.rodrigues.guilherme.desafiosupplyagro")
@SpringBootApplication
@EnableJpaRepositories(repositoryBaseClass = ApiJpaRepositoryImpl.class, basePackages = "melo.rodrigues.guilherme.desafiosupplyagro")
public class ApplicationTests {
    public static void main(String[] args) {
        SpringApplication.run(ApplicationTests.class, args);
    }
}
