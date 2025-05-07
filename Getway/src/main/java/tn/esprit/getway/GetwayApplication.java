package tn.esprit.getway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@EnableDiscoveryClient  // Plus flexible qu'@EnableEurekaClient (facultatif en Spring Boot 2.4+)
public class GetwayApplication {

	public static void main(String[] args) {
		SpringApplication.run(GetwayApplication.class, args);
	}

	@Bean
	public RouteLocator gatewayRoutes(RouteLocatorBuilder builder) {
		return builder.routes()
				.route("usergra", r -> r.path("/usergra/**")
						.uri("lb://UserGRA"))  // Utilisation de Eureka pour la découverte
				.route("client", r -> r.path("/client/**")
						.uri("lb://CLIENT"))

				.build();
	}
}
