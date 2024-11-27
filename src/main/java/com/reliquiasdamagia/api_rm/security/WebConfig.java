package com.reliquiasdamagia.api_rm.security;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        // Liberando o CORS para qualquer IP acessando a porta 4200
        registry.addMapping("/**")  // Mapeia todas as rotas
                .allowedOrigins("http://localhost:4200")  // Permite requisições apenas de http://localhost:4200
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*")  // Permite todos os headers
                .allowCredentials(true);  // Permite credenciais como cookies
    }
}
