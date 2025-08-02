package com.lucas.transactions_service.services;

import com.lucas.transactions_service.account.AccountTransaction;
import com.lucas.transactions_service.exeptions.InsufficientBalanceException;
import com.lucas.transactions_service.exeptions.ResourceNotFoundException;
import com.lucas.transactions_service.model.dtos.AccountResponse;
import com.lucas.transactions_service.model.dtos.ReportRequest;
import com.lucas.transactions_service.model.dtos.TransactionRequest;
import com.lucas.transactions_service.model.dtos.TransactionResponse;
import com.lucas.transactions_service.model.entities.Transaction;
import com.lucas.transactions_service.repositories.TransactionRepository;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class TransactionService {

    private final TransactionRepository transactionRepository;
    private final AccountTransaction accountTransaction;

    public void addTransaction(TransactionRequest transactionRequest){
        int availableBalance =
                transactionRequest.getInitialbalance() +
                        transactionRequest.getTransactionAmount();

        if(availableBalance < 0){
            throw new InsufficientBalanceException("Saldo no disponible");
        }


        getAccountById(transactionRequest.getAccountNumber());

        var transaction = Transaction.builder()
                .date(transactionRequest.getDate())
                .accountNumber(transactionRequest.getAccountNumber())
                .type(transactionRequest.getType())
                .initialbalance(transactionRequest.getInitialbalance())
                .state(transactionRequest.getState())
                .transactionAmount(transactionRequest.getTransactionAmount())
                .availableBalance(availableBalance)
                .build();

        transactionRepository.save(transaction);
    }

    public void removeTransaction(Long id){
        boolean exists = transactionRepository.existsById(id);;
        if (!exists) {
            throw new ResourceNotFoundException("Transaction not found with id: " + id);
        }

        transactionRepository.deleteById(id);
    }

    public Transaction updateTransaction(Long id, TransactionRequest transactionRequestUpdated) {
        getAccountById(transactionRequestUpdated.getAccountNumber());
        return transactionRepository.findById(id).map(transaction -> {
            transaction.setDate(transactionRequestUpdated.getDate());
            transaction.setAccountNumber(transactionRequestUpdated.getAccountNumber());
            transaction.setType(transactionRequestUpdated.getType());
            transaction.setInitialbalance(transactionRequestUpdated.getInitialbalance());
            transaction.setState(transactionRequestUpdated.getState());
            transaction.setTransactionAmount(transactionRequestUpdated.getTransactionAmount());
            transaction.setAvailableBalance(transactionRequestUpdated.getAvailableBalance());

            return transactionRepository.save(transaction);
        }).orElseThrow(() -> new ResourceNotFoundException("transaction not found with id: " + id));
    }

    public List<TransactionResponse> getAllTransactions(){
        var transactions = transactionRepository.findAll();

        return transactions.stream().map(this::mapToClientResponse).toList();
    }

    public List<TransactionResponse> getReportsBetweenDates(ReportRequest reportRequest){

        if (reportRequest.getStartDate().isAfter(reportRequest.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date");
        }

        var transactions = transactionRepository.findByDateBetween(reportRequest.getStartDate(),reportRequest.getEndDate());

        return transactions.stream().map(this::mapToClientResponse).toList();
    }

    private TransactionResponse mapToClientResponse(Transaction transaction){
        return TransactionResponse.builder()
                .id(transaction.getId())
                .date(transaction.getDate())
                .clientName(getAccountById(transaction.getAccountNumber()).getClientResponse().getName())
                .accountNumber(transaction.getAccountNumber())
                .type(transaction.getType())
                .initialbalance(transaction.getInitialbalance())
                .state(transaction.getState())
                .transactionAmount(transaction.getTransactionAmount())
                .availableBalance(transaction.getAvailableBalance())
                .build();
    }

    private AccountResponse getAccountById(Long id){
        try {
            return accountTransaction.getAccountById(id);
        } catch (FeignException.NotFound e) {
            throw new ResourceNotFoundException("Account not found");

        } catch (FeignException e) {
            log.error("Feign error - Status: {}, Body: {}, Message: {}", e.status(), e.contentUTF8(), e.getMessage());
            throw new RuntimeException("Error calling the account service", e);

        } catch (Exception e) {
            log.error("Unexpected error while searching for account", e);
            throw new RuntimeException("Unexpected error", e);
        }
    }

}
