package com.lucas.accounts_service.client;

import com.lucas.accounts_service.model.dtos.ClientResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.lang.module.ResolutionException;

@FeignClient(name = "customers-service", url = "http://customers-service:8082")
public interface ClientAccount {

    @GetMapping("/api/clientes/{id}")
    ClientResponse getClientById(@PathVariable("id") Long id);
}

