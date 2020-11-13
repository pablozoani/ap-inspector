package com.pzoani.supermarket.endpoint;

import com.pzoani.supermarket.handler.CategoryHandler;
import com.pzoani.supermarket.paths.Endpoints;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Profile("handlers")
@Configuration
public class CategoryEndpoint {

    @Bean
    public RouterFunction<ServerResponse> categoryRouter(CategoryHandler handler) {
        return route(GET(basePath()), handler::findAll)
            .andRoute(POST(basePath()), handler::save)
            .andRoute(GET(basePath("/{id}")), handler::findById)
            .andRoute(PUT(basePath("/{id}")), handler::update)
            .andRoute(DELETE(basePath("/{id}")), handler::delete);
    }

    private String basePath(String append) {
        return Endpoints.CATEGORIES_BASE_URL + append;
    }

    private String basePath() {
        return basePath("");
    }
}
