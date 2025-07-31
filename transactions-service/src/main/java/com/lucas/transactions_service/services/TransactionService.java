package com.lucas.transactions_service.services;

import com.lucas.transactions_service.account.AccountTransaction;
import com.lucas.transactions_service.model.dtos.AccountResponse;
import com.lucas.transactions_service.model.dtos.TransactionRequest;
import com.lucas.transactions_service.model.dtos.TransactionResponse;
import com.lucas.transactions_service.model.entities.Transaction;
import com.lucas.transactions_service.repositories.TransactionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountTransaction accountTransaction;

    public void addTransaction(TransactionRequest request) {
        validateAccountExists(request.getAccountNumber());

        int availableBalance = request.getInitialbalance() + request.getTransactionAmount();

        if (availableBalance < 0) {
            throw new IllegalStateException("The account balance cannot be negative.");
        }

        Transaction transaction = Transaction.builder()
                .date(request.getDate())
                .accountNumber(request.getAccountNumber())
                .type(request.getType())
                .initialbalance(request.getInitialbalance())
                .state(request.getState())
                .transactionAmount(request.getTransactionAmount())
                .availableBalance(availableBalance)
                .build();

        transactionRepository.save(transaction);
        log.info("Transaction added: {}", transaction);
    }

    public void removeTransaction(Long id) {
        if (!transactionRepository.existsById(id)) {
            throw new NoSuchElementException("Transaction not found with id: " + id);
        }
        transactionRepository.deleteById(id);
    }

    public Transaction updateTransaction(Long id, TransactionRequest updatedRequest) {
        validateAccountExists(updatedRequest.getAccountNumber());

        return transactionRepository.findById(id)
                .map(transaction -> updateTransactionFields(transaction, updatedRequest))
                .map(transactionRepository::save)
                .orElseThrow(() -> new NoSuchElementException("Transaction not found with id: " + id));
    }

    public List<TransactionResponse> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .map(this::mapToClientResponse)
                .toList();
    }

    private Transaction updateTransactionFields(Transaction transaction, TransactionRequest request) {
        int availableBalance = request.getInitialbalance() + request.getTransactionAmount();

        transaction.setDate(request.getDate());
        transaction.setAccountNumber(request.getAccountNumber());
        transaction.setType(request.getType());
        transaction.setInitialbalance(request.getInitialbalance());
        transaction.setState(request.getState());
        transaction.setTransactionAmount(request.getTransactionAmount());
        transaction.setAvailableBalance(availableBalance);
        return transaction;
    }

    private TransactionResponse mapToClientResponse(Transaction transaction) {
        String clientName = getAccountById(transaction.getAccountNumber())
                .getClientResponse()
                .getName();

        return TransactionResponse.builder()
                .id(transaction.getId())
                .date(transaction.getDate())
                .clientName(clientName)
                .accountNumber(transaction.getAccountNumber())
                .type(transaction.getType())
                .initialbalance(transaction.getInitialbalance())
                .state(transaction.getState())
                .transactionAmount(transaction.getTransactionAmount())
                .availableBalance(transaction.getAvailableBalance())
                .build();
    }

    private void validateAccountExists(Long accountId) {
        getAccountById(accountId); // Throws if not found
    }

    private AccountResponse getAccountById(Long id) {
        try {
            AccountResponse response = accountTransaction.getAccountById(id);
            if (response == null) {
                throw new NoSuchElementException("Account does not exist with id: " + id);
            }
            return response;
        } catch (FeignException.NotFound e) {
            throw new NoSuchElementException("Account not found with id: " + id, e);
        }
    }
}
