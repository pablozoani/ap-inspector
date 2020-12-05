package com.pzoani.supermarket.controller;

import com.pzoani.inspector.Inspector;
import com.pzoani.supermarket.config.Urls;
import com.pzoani.supermarket.domain.Vendor;
import com.pzoani.supermarket.repository.VendorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@Profile("controllers")
@RestController
@RequestMapping(Urls.VENDORS_BASE_URL)
@CrossOrigin("*")
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
        return vendorRepository.findById(id)
            .switchIfEmpty(Mono.error(new ResponseStatusException(NOT_FOUND,
                "Vendor " + id + " not found."
            )));
    }

    @ResponseStatus(CREATED)
    @PostMapping(consumes = "application/json")
    public Mono<Vendor> save(@RequestBody Vendor vendor) {
        return vendorRepository.save(inspector.inspect(vendor));
    }

    @PutMapping(path = "/{id}", consumes = "application/json")
    public Mono<Vendor> update(@PathVariable String id,
        @RequestBody Vendor vendor
    ) {
        return vendorRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    inspector.inspect(vendor);
                    vendor.setId(id);
                    return vendorRepository.save(vendor);
                } else {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Vendor " + id + " not found."
                    );
                }
            });
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteById(@PathVariable("id") String id) {
        return vendorRepository.existsById(id)
            .flatMap(bool -> {
                if (bool) {
                    return vendorRepository.deleteById(id);
                } else {
                    throw new ResponseStatusException(NOT_FOUND,
                        "Vendor " + id + " not found."
                    );
                }
            });
    }
}
