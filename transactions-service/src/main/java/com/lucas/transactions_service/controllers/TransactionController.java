package com.lucas.transactions_service.controllers;

import com.lucas.transactions_service.model.dtos.TransactionRequest;
import com.lucas.transactions_service.model.dtos.TransactionResponse;
import com.lucas.transactions_service.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void addTransaction(@RequestBody TransactionRequest transactionRequest){
        this.transactionService.addTransaction(transactionRequest);
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<TransactionResponse> getAllTransaction(){
        return this.transactionService.getAllTransactions();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTransaction(@PathVariable Long id){
        this.transactionService.removeTransaction(id);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public void updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest){
        this.transactionService.updateTransaction(id, transactionRequest);
    }

}
