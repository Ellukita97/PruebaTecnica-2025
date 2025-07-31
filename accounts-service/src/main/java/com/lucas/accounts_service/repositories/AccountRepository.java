package com.lucas.accounts_service.repositories;

import com.lucas.accounts_service.model.entities.Account;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AccountRepository extends JpaRepository<Account, Long> {
}
