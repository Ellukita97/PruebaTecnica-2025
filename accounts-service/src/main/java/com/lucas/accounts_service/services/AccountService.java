package com.lucas.accounts_service.services;

import com.lucas.accounts_service.client.ClientAccount;
import com.lucas.accounts_service.exeptions.ClientNotFoundException;
import com.lucas.accounts_service.exeptions.ResourceNotFoundException;
import com.lucas.accounts_service.model.dtos.AccountRequest;
import com.lucas.accounts_service.model.dtos.AccountResponse;
import com.lucas.accounts_service.model.dtos.ClientResponse;
import com.lucas.accounts_service.model.entities.Account;
import com.lucas.accounts_service.repositories.AccountRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class AccountService {

    private final AccountRepository accountRepository;
    private final ClientAccount clientAccount;

    public void addAccount(AccountRequest accountRequest) {
        searchClientById(accountRequest.getClientId());

        var account = Account.builder()
                .accountType(accountRequest.getAccountType())
                .initialBalance(accountRequest.getInitialBalance())
                .status(true)
                .clientId(accountRequest.getClientId())
                .build();

        accountRepository.save(account);
    }

    public void removeAccount(Long id){
        boolean exists = accountRepository.existsById(id);
        if (!exists) {
            throw new ResourceNotFoundException("Account not found with id: " + id);
        }
        accountRepository.deleteById(id);
    }

    public Account updateAccount(Long id, AccountRequest accountRequestUpdated) {
        searchClientById(accountRequestUpdated.getClientId());
        return accountRepository.findById(id).map(account -> {
            account.setAccountType(accountRequestUpdated.getAccountType());
            account.setInitialBalance(accountRequestUpdated.getInitialBalance());
            account.setStatus(true);
            account.setClientId(accountRequestUpdated.getClientId());

            return accountRepository.save(account);
        }).orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));
    }

    public List<AccountResponse> getAllAccount(){
        var account = accountRepository.findAll();

        return account.stream().map(this::mapToAccountResponse).toList();
    }

    public AccountResponse getAccountById(Long id){
        var account = accountRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + id));

        return mapToAccountResponse(account);
    }

    private AccountResponse mapToAccountResponse(Account account){
        return AccountResponse.builder()
                .accountNumber(account.getAccountNumber())
                .accountType(account.getAccountType())
                .initialBalance(account.getInitialBalance())
                .status(account.getStatus())
                .clientResponse(searchClientById(account.getClientId()))
                .build();
    }

    private ClientResponse searchClientById(Long clientId) {
        try {
            return clientAccount.getClientById(clientId);
        } catch (FeignException.NotFound e) {
            throw new ClientNotFoundException("Client not found", e);
        }
    }

}



