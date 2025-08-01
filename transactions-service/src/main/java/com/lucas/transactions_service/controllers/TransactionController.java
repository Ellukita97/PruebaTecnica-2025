package com.lucas.transactions_service.controllers;

import com.lucas.transactions_service.model.dtos.ReportRequest;
import com.lucas.transactions_service.model.dtos.TransactionRequest;
import com.lucas.transactions_service.model.dtos.TransactionResponse;
import com.lucas.transactions_service.model.entities.Transaction;
import com.lucas.transactions_service.services.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/movimientos")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionService transactionService;

    @PostMapping
    public ResponseEntity<Void> addTransaction(@RequestBody TransactionRequest transactionRequest) {
        transactionService.addTransaction(transactionRequest);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<TransactionResponse>> getAllTransactions() {
        List<TransactionResponse> transactions = transactionService.getAllTransactions();
        return ResponseEntity.ok(transactions);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Long id) {
        transactionService.removeTransaction(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateTransaction(@PathVariable Long id, @RequestBody TransactionRequest transactionRequest) {
        transactionService.updateTransaction(id, transactionRequest);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reportes")
    public ResponseEntity<List<TransactionResponse>> getReportsBetweenDates(@ModelAttribute ReportRequest dates) {

        List<TransactionResponse> transactions = transactionService.getReportsBetweenDates(dates);

        return ResponseEntity.ok(transactions);
    }

}
