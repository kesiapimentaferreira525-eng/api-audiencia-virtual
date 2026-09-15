package com.audiencia_virtual_service.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI audienciaVirtualOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Audiência Virtual")
                        .description("API para gerenciamento de usuários, partes, agendas e audiências virtuais.")
                        .version("v1"));
    }
}
