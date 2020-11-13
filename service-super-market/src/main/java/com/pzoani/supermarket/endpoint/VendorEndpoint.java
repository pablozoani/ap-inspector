package com.pzoani.supermarket.endpoint;

import com.pzoani.supermarket.handler.VendorHandler;
import com.pzoani.supermarket.paths.Endpoints;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@Profile("handlers")
@Configuration
public class VendorEndpoint {

    public RouterFunction<ServerResponse> vendorRouter(VendorHandler vendorHandler) {
        return RouterFunctions.route(GET(basePath()), vendorHandler::findAll)
            .andRoute(POST(basePath()), vendorHandler::save)
            .andRoute(GET(basePath("/{id}")), vendorHandler::findById)
            .andRoute(PUT(basePath("/{id}")), vendorHandler::update)
            .andRoute(DELETE(basePath("/{id}")), vendorHandler::delete);
    }

    private String basePath(String append) {
        return Endpoints.VENDORS_BASE_URL + append;
    }

    private String basePath() {
        return basePath("");
    }
}
