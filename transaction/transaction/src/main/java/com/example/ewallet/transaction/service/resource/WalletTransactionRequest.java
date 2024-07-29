package com.example.ewallet.transaction.service.resource;

import lombok.*;

@Getter
@AllArgsConstructor
@Setter
@NoArgsConstructor
@Builder
public class WalletTransactionRequest {

    private Long senderId;
    private Long receiverId;
    private Double amount;
    private String transactionType;
    private String description;

}