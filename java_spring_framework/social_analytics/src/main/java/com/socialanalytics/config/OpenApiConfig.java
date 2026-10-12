package com.socialanalytics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;

// Swagger UI: /swagger-ui.html, JSON spec: /v3/api-docs
@Configuration
public class OpenApiConfig {

	@Bean
	public OpenAPI socialAnalyticsOpenApi() {
		return new OpenAPI().info(new Info()
				.title("Social Analytics API")
				.description("Theo dõi lượng tương tác (likes, shares, followers) từ Facebook & Twitter")
				.version("v1"));
	}
}
