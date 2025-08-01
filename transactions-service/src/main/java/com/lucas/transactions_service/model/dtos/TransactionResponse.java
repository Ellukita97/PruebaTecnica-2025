package com.lucas.transactions_service.model.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TransactionResponse {

    private Long id;

    private LocalDate date;
    private String clientName;
    private Long accountNumber;
    private String type;
    private Integer initialbalance;
    private Boolean state;
    private Integer transactionAmount;
    private Integer availableBalance;
}
