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
    public RouterFunction<ServerResponse> routerFunction(VendorHandler vendorHandler,
        CategoryHandler categoryHandler, ProductHandler productHandler
    ) {
        return RouterFunctions

            .route(GET(vendorHandlerPath("")), vendorHandler::findAll)
            .andRoute(POST(vendorHandlerPath("")), vendorHandler::save)
            .andRoute(GET(vendorHandlerPath("/{id}")), vendorHandler::findById)
            .andRoute(PUT(vendorHandlerPath("/{id}")), vendorHandler::update)
            .andRoute(DELETE(vendorHandlerPath("/{id}")), vendorHandler::delete)

            .andRoute(GET(categoryHandlerPath("")), categoryHandler::findAll)
            .andRoute(POST(categoryHandlerPath("")), categoryHandler::save)
            .andRoute(GET(categoryHandlerPath("/{id}")), categoryHandler::findById)
            .andRoute(PUT(categoryHandlerPath("/{id}")), categoryHandler::update)
            .andRoute(DELETE(categoryHandlerPath("/{id}")), categoryHandler::delete)

            .andRoute(GET(productHandlerPath("")), productHandler::findAll)
            .andRoute(POST(productHandlerPath("")), productHandler::save)
            .andRoute(GET(productHandlerPath("/{id}")), productHandler::findById)
            .andRoute(PUT(productHandlerPath("/{id}")), productHandler::update)
            .andRoute(DELETE(productHandlerPath("/{id}")), productHandler::delete)

//            .andRoute(GET("/api/v1/bootstrap"), null)
            ;
    }

    private String vendorHandlerPath(String append) {
        return Urls.VENDORS_BASE_URL + append;
    }

    private String categoryHandlerPath(String append) {
        return Urls.CATEGORIES_BASE_URL + append;
    }

    private String productHandlerPath(String append) {
        return Urls.PRODUCTS_BASE_URL + append;
    }
}
