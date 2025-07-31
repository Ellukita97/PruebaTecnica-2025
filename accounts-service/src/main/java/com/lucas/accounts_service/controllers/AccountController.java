package com.lucas.accounts_service.controllers;

import com.lucas.accounts_service.model.dtos.AccountRequest;
import com.lucas.accounts_service.model.dtos.AccountResponse;
import com.lucas.accounts_service.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    public ResponseEntity<Void> addAccount(@RequestBody AccountRequest accountRequest) {
        accountService.addAccount(accountRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<AccountResponse>> getAllAccount() {
        List<AccountResponse> accounts = accountService.getAllAccount();
        return ResponseEntity.ok(accounts);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AccountResponse> getAccountById(@PathVariable Long id) {
        AccountResponse account = accountService.getAccountById(id);
        return ResponseEntity.ok(account);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccount(@PathVariable Long id) {
        accountService.removeAccount(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest) {
        accountService.updateAccount(id, accountRequest);
        return ResponseEntity.ok().build();
    }
}
