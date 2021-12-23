package top.moflowerlkh.shareparkingdemo.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@Configuration
@EnableJpaRepositories("top.moflowerlkh.shareparkingdemo.dao")
public class JpaRepositoryConfig {
}
