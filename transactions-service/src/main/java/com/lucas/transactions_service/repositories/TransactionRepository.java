package com.lucas.transactions_service.repositories;

import com.lucas.transactions_service.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    List<Transaction> findByDateBetween(LocalDate startDate, LocalDate endDate);
}
