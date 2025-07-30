package com.lucas.customers_service.repositories;

import com.lucas.customers_service.model.entities.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
