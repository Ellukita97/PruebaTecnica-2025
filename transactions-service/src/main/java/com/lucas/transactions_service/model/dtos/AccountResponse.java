package com.lucas.transactions_service.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AccountResponse {
    private Long accountNumber;

    private String accountType;
    private Double initialBalance;
    private Boolean status;

    private ClientResponse clientResponse;
}
