package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.paths.Endpoints;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Profile("controllers")
@RestController
@RequestMapping(Endpoints.VENDORS_BASE_URL)
public class VendorController {

    private final VendorRepository vendorRepository;

    private final Inspector<Vendor> inspector;

    @Autowired
    public VendorController(VendorRepository vendorRepository,
        Inspector<Vendor> inspector
    ) {
        this.inspector = inspector;
        this.vendorRepository = vendorRepository;
    }

    @GetMapping
    public Flux<Vendor> findAll() {
        return vendorRepository.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Vendor> findById(@PathVariable("id") String id) {
        return vendorRepository.findById(id);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Mono<Vendor> save(@RequestBody Mono<Vendor> vendorMono) {
        return vendorRepository.saveAll(vendorMono).next();
    }

    @PutMapping("/{id}")
    public Mono<Vendor> update(@PathVariable String id,
        @RequestBody Mono<Vendor> vendorMono) {
        return vendorRepository.saveAll(vendorMono.map(v -> {
            v.setId(id);
            return v;
        })).next();
    }
}
