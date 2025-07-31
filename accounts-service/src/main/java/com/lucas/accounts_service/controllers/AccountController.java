package com.lucas.accounts_service.controllers;

import com.lucas.accounts_service.model.dtos.AccountRequest;
import com.lucas.accounts_service.model.dtos.AccountResponse;
import com.lucas.accounts_service.services.AccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cuentas")
@RequiredArgsConstructor
public class AccountController {

    private final AccountService accountService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addAccount(@RequestBody AccountRequest accountRequest){
        this.accountService.addAccount(accountRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<AccountResponse> getAllAccount(){
        return this.accountService.getAllAccount();
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public AccountResponse getAccountById(@PathVariable Long id){
        return this.accountService.getAccountById(id);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteAccount(@PathVariable Long id){
        this.accountService.removeAccount(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateAccount(@PathVariable Long id, @RequestBody AccountRequest accountRequest){
        this.accountService.updateAccount(id, accountRequest);
    }
}
