package com.lucas.transactions_service.repositories;

import com.lucas.transactions_service.model.entities.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, Long> {
}
