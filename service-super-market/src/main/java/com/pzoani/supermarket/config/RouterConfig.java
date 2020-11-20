package com.pzoani.supermarket.config;

import com.pzoani.supermarket.handler.CategoryHandler;
import com.pzoani.supermarket.handler.ProductHandler;
import com.pzoani.supermarket.handler.VendorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Profile("handlers")
@Configuration
public class RouterConfig {

    @Bean
    public RouterFunction<ServerResponse> routerFunction(VendorHandler vHandler,
        CategoryHandler cHandler, ProductHandler productHandler
    ) {
        return RouterFunctions

            .route(GET(vPath("")), vHandler::findAll)
            .andRoute(POST(vPath("")), vHandler::save)
            .andRoute(GET(vPath("/{id}")), vHandler::findById)
            .andRoute(PUT(vPath("/{id}")), vHandler::update)
            .andRoute(DELETE(vPath("/{id}")), vHandler::delete)

            .andRoute(GET(cPath("")), cHandler::findAll)
            .andRoute(POST(cPath("")), cHandler::save)
            .andRoute(GET(cPath("/{id}")), cHandler::findById)
            .andRoute(PUT(cPath("/{id}")), cHandler::update)
            .andRoute(DELETE(cPath("/{id}")), cHandler::delete)

            .andRoute(GET(pPath("")), productHandler::findAll)
            .andRoute(POST(pPath("")), productHandler::save)
            .andRoute(GET(pPath("/{id}")), productHandler::findById)
            .andRoute(PUT(pPath("/{id}")), productHandler::update)
            .andRoute(DELETE(pPath("/{id}")), productHandler::delete)

//            .andRoute(GET("/api/v1/bootstrap"), null)
            ;
    }

    private String vPath(String append) {
        return Urls.VENDORS_BASE_URL + append;
    }

    private String cPath(String append) {
        return Urls.CATEGORIES_BASE_URL + append;
    }

    private String pPath(String append) {
        return Urls.PRODUCTS_BASE_URL + append;
    }
}
