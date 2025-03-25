package tn.esprit.usergra;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

@OpenAPIDefinition(
        info = @Info(title = "UserGRA API", version = "1.0", description = "Documentation de l'API UserGRA")
)
@SpringBootApplication
@EnableDiscoveryClient
@EnableFeignClients
public class UserGraApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserGraApplication.class, args);
    }
}
