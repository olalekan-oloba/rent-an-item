package com.omegalambdang.rentanitem;

import com.omegalambdang.rentanitem.util.TransactionUtils;
import com.omegalambdang.rentanitem.util.extension.DBCleanerExtension;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.transaction.PlatformTransactionManager;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.lifecycle.Startables;
import org.testcontainers.utility.DockerImageName;

import java.io.File;
import java.time.Duration;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
@RequiredArgsConstructor
@ExtendWith(DBCleanerExtension.class)
public abstract class AbstractSpringIntegrationTest extends AbstractTest {

   static MySQLContainer DATABASE;
       static {
        DATABASE =
                new MySQLContainer<>(DockerImageName.parse("mysql:latest"))
                        .withDatabaseName("test-db")
                        .withUsername("root")
                        .withExposedPorts(3306)
                        .withConnectTimeoutSeconds(180)
                        .withStartupTimeout(Duration.ofSeconds(120));
    }

   /*
    static PostgreSQLContainer<?>  DATABASE;
    static {
        DATABASE =
                new PostgreSQLContainer<>(DockerImageName.parse("postgres:12-bullseye"))
                        .withDatabaseName("mydb")
                        .withUsername("user")
                        .withPassword("password");
        DATABASE.start();
    }*/

    @DynamicPropertySource
    static void databaseProperties(DynamicPropertyRegistry registry) {
        Startables.deepStart(DATABASE).join();
        registry.add("spring.datasource.url", DATABASE::getJdbcUrl);
        registry.add("spring.datasource.username", DATABASE::getUsername);
        registry.add("spring.datasource.password", DATABASE::getPassword);
        registry.add("spring.datasource.driver-class-name", DATABASE::getDriverClassName);
    }

    @Autowired
    protected PlatformTransactionManager transactionManager;
    @Autowired
    protected TransactionUtils transactionUtils;
    protected HttpHeaders headers;

    @BeforeEach
    public void runBeforeAllTestMethods() {
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
    }

    @Value("${local.server.port}")
    protected String serverPort;
    @Autowired
    Environment environment;

    @TempDir
    File tempDir;
    @Autowired
    protected TestRestTemplate restTemplate;
    @LocalServerPort
    private int port;
    @Value("${file.working-dir}")
    protected String workingDir;

}