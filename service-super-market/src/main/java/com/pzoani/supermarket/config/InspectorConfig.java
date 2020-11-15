package com.pzoani.supermarket.config;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Vendor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Configuration
public class InspectorConfig {

    @Bean
    public Inspector<Vendor> makeVendorInspector() {
        // TODO
        return new Inspector<Vendor>(
            new ResponseStatusException(BAD_REQUEST, "Invalid Payload")
        ) {
            @Override
            public Vendor inspect(Vendor vendor) {
                return begin()
                    .isNotBlank(vendor.getFirstName())
                    .haveLengthRange(2, 64, vendor.getFirstName(),
                        vendor.getLastName()
                    ).end(vendor);
            }
        };
    }
}