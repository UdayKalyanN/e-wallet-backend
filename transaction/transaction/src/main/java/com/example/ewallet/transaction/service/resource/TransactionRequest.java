package com.example.ewallet.transaction.service.resource;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionRequest {

    private Long ReceiverId;
    private Double amount;
    private String description;
    private String transactionType;
}
