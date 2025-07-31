package com.lucas.transactions_service.account;

import com.lucas.transactions_service.model.dtos.AccountResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "account", url = "http://localhost:8083")
public interface AccountTransaction {

    @GetMapping("/api/cuentas/{id}")
    AccountResponse getAccountById(@PathVariable("id") Long id);
}
