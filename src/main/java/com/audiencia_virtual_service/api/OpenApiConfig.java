package com.audiencia_virtual_service.api;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.servers.Server;
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
                                                .description("""
                                                                API para gerenciamento de usuários, partes, agendas e audiências virtuais.

                                                                A audiência virtual possui e-mail, data e horário do agendamento
                                                                e plataforma de reunião, como Microsoft Teams.
                                                                """)
                                                .version("v1"))
                                .servers(java.util.List.of(
                                                new Server()
                                                                .url("http://localhost:8082")
                                                                .description("Ambiente local")));
        }
}
